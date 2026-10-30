import { Box, Button, Chip, Dialog, DialogActions, DialogContent, DialogTitle, Link, MenuItem, Select, styled, Typography } from '@mui/material';
import { GridPaginationModel } from '@mui/x-data-grid';
import { useDialogs } from '@toolpad/core/useDialogs';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import CustomDataGrid from '../../components/data-grid/CustomDataGrid';
import CustomDialog from '../../components/dialog/CustomDialog';
import { formatErrorMessage } from '../../utils/error-handler';


import { fetchAuditApiLogs, fetchAuditApiLogDetail } from '../../apis/log-api';


export type ApiLogRow = {
  id: string | number;
  createdAt: string; // ISO string
  actionType: string;
  targetType: string;
  apiName?: string;
  targetId?: string;
  requesterId?: string;
  status: number;
  result: 'SUCCESS' | 'FAIL';
};

export type ApiLogDetail = ApiLogRow & {
  headers?: Record<string, string> | null;
  queryParams?: Record<string, string> | null;
  requestBody?: unknown;
  responseBody?: unknown;
  durationMs?: number | null;
  clientIp?: string | null;
  apiDescription?: string | null;
  userAgent?: string | null;
  message?: string | null;
};

const AuditApiLogManagementPage = () => {
  const navigate = useNavigate();
  const dialogs = useDialogs();

  const [loading, setLoading] = useState(false);
  const [rows, setRows] = useState<ApiLogRow[]>([]);
  const [totalRows, setTotalRows] = useState(0);
  const [selectedRow, setSelectedRow] = useState<string | number | null>(null);

  const [searchText, setSearchText] = useState('');
  const [selectedSearch, setSelectedSearch] = useState<string>('actionType');

  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({ page: 0, pageSize: 10 });

  // Detail dialog
  const [detailOpen, setDetailOpen] = useState(false);
  const [detail, setDetail] = useState<ApiLogDetail | null>(null);

  const selectedRowData = useMemo(
    () => (Array.isArray(rows) ? rows.find(r => r.id === selectedRow) ?? null : null),
    [rows, selectedRow]
  );

  const StyledContainer = useMemo(
    () =>
      styled(Box)(({ theme }) => ({
        margin: 'auto',
        marginTop: theme.spacing(1),
        padding: theme.spacing(3),
        border: 'none',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: '#ffffff',
        boxShadow: '0px 4px 8px 0px #0000001A',
      })),
    []
  );

  const StyledSubTitle = useMemo(
    () =>
      styled(Typography)({
        textAlign: 'left',
        fontSize: '24px',
        fontWeight: 700,
      }),
    []
  );

  const resultChipColor = (result: ApiLogRow['result']) => (result === 'SUCCESS' ? 'success' : 'error') as any;

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const response = await fetchAuditApiLogs(
        paginationModel.page,
        paginationModel.pageSize,
        selectedSearch && searchText.trim() ? selectedSearch : null,
        selectedSearch && searchText.trim() ? searchText.trim() : null
      );
      setRows(response.data.content);
      setTotalRows(response.data.totalElements);
    } catch (err) {
      console.error('Failed to retrieve API logs', err);
      navigate('/error', { state: { message: formatErrorMessage(err, 'Failed to retrieve API logs.') } });
    } finally {
      setLoading(false);
    }
  }, [paginationModel.page, paginationModel.pageSize, selectedSearch, searchText, navigate]);

  const getData = useCallback(async () => {
    setLoading(true);
    try {
      const response = await fetchAuditApiLogs(
        0,
        paginationModel.pageSize,
        selectedSearch && searchText.trim() ? selectedSearch : null,
        selectedSearch && searchText.trim() ? searchText.trim() : null
      );
      setRows(response.data.content);
      setTotalRows(response.data.totalElements);
      setPaginationModel(prev => ({ ...prev, page: 0 }));
    } catch (err) {
      console.error('Failed to retrieve API logs', err);
      await dialogs.open(CustomDialog, {
        title: 'Notification',
        message: formatErrorMessage(err, 'Failed to retrieve API logs.'),
        isModal: true,
      });
    } finally {
      setLoading(false);
    }
  }, [paginationModel.pageSize, selectedSearch, searchText, dialogs]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const onClickTimestamp = async (rowId: string | number) => {
    try {
      setLoading(true);
      const res = await fetchAuditApiLogDetail(rowId);
      setDetail(res.data);
      setDetailOpen(true);
    } catch (err) {
      await dialogs.open(CustomDialog, {
        title: 'Notification',
        message: formatErrorMessage(err, 'Failed to load log detail.'),
        isModal: true,
      });
    } finally {
      setLoading(false);
    }
  };

  const asTextOrJson = (v: unknown) =>
    typeof v === 'string' ? v : JSON.stringify(v ?? {}, null, 2);

  const handleSearch = useCallback(async (field: string, text: string) => {
    const trimmed = text.trim();
    if (!trimmed) return;
    setSelectedSearch(field as any);
    setSearchText(trimmed);
    setPaginationModel(prev => ({ ...prev, page: 0 }));
  }, []);

  return (
    <>
      <FullscreenLoader open={loading} />
      <StyledContainer>
        <StyledSubTitle>Audit API Log</StyledSubTitle>
        <CustomDataGrid
          rows={rows}
          columns={[
            {
              field: 'createdAt',
              headerName: 'Called At',
              width: 200,
              renderCell: (params: any) => (
                <Link
                  component="button"
                  variant="body2"
                  onClick={() => onClickTimestamp(params.row.id)}
                  sx={{ cursor: 'pointer', color: 'primary.main' }}
                >
                  {params.value}
                </Link>
              ),
            },
            { field: 'requesterId', headerName: 'Requester ID', width: 100 },
            { field: 'actionType', headerName: 'Action Type', width: 100 },
            { field: 'targetType', headerName: 'Target Type', width: 200 },
            { field: 'targetId', headerName: 'Target ID', width: 200 },
            {
              field: 'result',
              headerName: 'Result',
              width: 120,
              renderCell: (params: any) => (
                <Chip size="small" label={params.value} color={resultChipColor(params.value)} />
              ),
            },
          ]}
          selectedRow={selectedRow}
          setSelectedRow={setSelectedRow}
          onEdit={undefined} // no row edit on this page
          additionalButtons={[]}
          paginationMode="server"
          totalRows={totalRows}
          paginationModel={paginationModel}
          setPaginationModel={setPaginationModel}
          enableSearch
          searchText={searchText}
          setSearchText={setSearchText}
          selectedSearch={selectedSearch}
          setSelectedSearch={setSelectedSearch}
          searchOptions={[
            { value: 'actionType', label: 'Action Type' },
            { value: 'targetType', label: 'Target Type' },
            { value: 'result', label: 'Result' },
            { value: 'requesterId', label: 'Requester ID' },
          ]}
          selectableFields={[
                  {
                      field: 'actionType',
                      options: [
                          { value: 'NONE', label: 'NONE' },
                          { value: 'CREATE', label: 'CREATE' },
                          { value: 'UPDATE', label: 'UPDATE' },
                          { value: 'DELETE', label: 'DELETE' },
                          { value: 'LOGIN', label: 'LOGIN' },
                          { value: 'LOGOUT', label: 'LOGOUT' },
                      ]
                  },
                  {
                      field: 'result',
                      options: [
                          { value: 'SUCCESS', label: 'SUCCESS' },
                          { value: 'FAIL', label: 'FAIL' },
                      ]
                  },
              ]}
          onSearch={handleSearch}
          onRefresh={getData}
        />
      </StyledContainer>

      {/* Detail Dialog */}
      <Dialog open={detailOpen} onClose={() => setDetailOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>Audit API Log Detail</DialogTitle>
        <DialogContent dividers sx={{ bgcolor: '#fafafa' }}>
          {detail ? (
            <Box sx={{ display: 'grid', gridTemplateColumns: '160px 1fr', rowGap: 1, columnGap: 2 }}>
              <Typography variant="body2" sx={{ fontWeight: 600 }}>Called At</Typography>
              <Typography variant="body2">{detail.createdAt}</Typography>

              <Typography variant="body2" sx={{ fontWeight: 600 }}>Requester ID</Typography>
              <Typography variant="body2">{detail.requesterId}</Typography>

              <Typography variant="body2" sx={{ fontWeight: 600 }}>Action Type</Typography>
              <Typography variant="body2">{detail.actionType}</Typography>

              <Typography variant="body2" sx={{ fontWeight: 600 }}>Target Type</Typography>
              <Typography variant="body2">{detail.targetType}</Typography>

              <Typography variant="body2" sx={{ fontWeight: 600 }}>Target ID</Typography>
              <Typography variant="body2">{detail.targetId}</Typography>

              <Typography variant="body2" sx={{ fontWeight: 600 }}>Result</Typography>
              <Box>
                <Chip size="small" label={detail.result} color={resultChipColor(detail.result)} />
              </Box>

              {detail.clientIp && (
                <>
                  <Typography variant="body2" sx={{ fontWeight: 600 }}>Client IP</Typography>
                  <Typography variant="body2">{detail.clientIp}</Typography>
                </>
              )}

              <Typography variant="body2" sx={{ fontWeight: 600, mt: 2, gridColumn: '1 / -1' }}>Description</Typography>
              <Box
                component="pre"
                sx={{
                  gridColumn: '1 / -1',
                  m: 0, mt: 1, p: 1,
                  bgcolor: '#fff',
                  borderRadius: 1,
                  overflow: 'auto',
                  maxHeight: 200,
                  fontFamily: 'ui-monospace, SFMono-Regular, Menlo, monospace',
                  fontSize: 12,
                  whiteSpace: 'pre-wrap',
                  overflowWrap: 'anywhere',
                  wordBreak: 'break-word',
                }}
              >
                {asTextOrJson(detail.apiDescription)}
              </Box>
              {detail.userAgent && (
                <>
                  <Typography variant="body2" sx={{ fontWeight: 600, mt: 2, gridColumn: '1 / -1' }}>User Agent</Typography>
                  <Box
                    component="pre"
                    sx={{
                      gridColumn: '1 / -1',
                      m: 0, mt: 1, p: 1,
                      bgcolor: '#fff',
                      borderRadius: 1,
                      overflow: 'auto',
                      maxHeight: 160,
                      fontFamily: 'ui-monospace, SFMono-Regular, Menlo, monospace',
                      fontSize: 12,
                      whiteSpace: 'pre-wrap',
                      overflowWrap: 'anywhere',
                      wordBreak: 'break-word',
                    }}
                  >
                    {asTextOrJson(detail.userAgent)}
                  </Box>
                </>
              )}
              {detail.message && (
                <>
                  <Typography variant="body2" sx={{ fontWeight: 600, mt: 2, gridColumn: '1 / -1' }}>Message</Typography>
                  <Box
                    component="pre"
                    sx={{
                      gridColumn: '1 / -1',
                      m: 0, mt: 1, p: 1,
                      bgcolor: '#fff',
                      borderRadius: 1,
                      overflow: 'auto',
                      maxHeight: 160,
                      fontFamily: 'ui-monospace, SFMono-Regular, Menlo, monospace',
                      fontSize: 12,
                      whiteSpace: 'pre-wrap',
                      overflowWrap: 'anywhere',
                      wordBreak: 'break-word',
                    }}
                  >
                    {asTextOrJson(detail.message)}
                  </Box>
                </>
              )}


            </Box>
          ) : (
            <Typography variant="body2">No data</Typography>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDetailOpen(false)} variant="contained">Close</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default AuditApiLogManagementPage;
