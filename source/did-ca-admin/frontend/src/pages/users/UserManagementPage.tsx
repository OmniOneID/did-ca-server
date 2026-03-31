
import { Box, Link, styled, Typography } from '@mui/material';
import { GridPaginationModel } from '@mui/x-data-grid';
import { useDialogs } from '@toolpad/core';
import React, { useCallback, useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import CustomDataGrid from '../../components/data-grid/CustomDataGrid';
import CustomDialog from '../../components/dialog/CustomDialog';
import { useSession } from '../../context/SessionContext';
import { formatErrorMessage } from '../../utils/error-handler';
import { fetchUserList } from '../../apis/user-api';

type Props = {}

type UserRow = {
  id: string | number;
  userId: string;
  pii: string;
  createdAt: string;
  updatedAt: string;
};

const UserManagementPage = (props: Props) => {
  const navigate = useNavigate();
  const dialogs = useDialogs();
  const [loading, setLoading] = useState<boolean>(false);
  const [totalRows, setTotalRows] = useState<number>(0);
  const [selectedRow, setSelectedRow] = useState<string | number | null>(null);
  const [rows, setRows] = useState<UserRow[]>([]);
  const [searchText, setSearchText] = useState<string>('');
  const [selectedSearch, setSelectedSearch] = useState<string>('userId');
  const { session } = useSession();

  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({
      page: 0,
      pageSize: 10,
  });

  const selectedRowData = useMemo(() => {
      return rows.find(row => row.id === selectedRow) || null;
  }, [rows, selectedRow]);

  const fetchData = useCallback(async () => {
      setLoading(true);
      try {
          const response = await fetchUserList(
              paginationModel.page,
              paginationModel.pageSize,
              selectedSearch && searchText.trim() ? selectedSearch : null,
              selectedSearch && searchText.trim() ? searchText.trim() : null
          );
          setRows(response.data.content);
          setTotalRows(response.data.totalElements);
      } catch (err) {
          console.error("Failed to retrieve User List. ", err);
          navigate('/error', { state: { message: formatErrorMessage(err, "Failed to fetch User List") } });
      } finally {
          setLoading(false);
      }
  }, [paginationModel.page, paginationModel.pageSize, selectedSearch, searchText, navigate]);

  const getData = useCallback(async () => {
      setLoading(true);
      try {
          const response = await fetchUserList(
              0,
              paginationModel.pageSize,
              selectedSearch && searchText.trim() ? selectedSearch : null,
              selectedSearch && searchText.trim() ? searchText.trim() : null
          );
          setRows(response.data.content);
          setTotalRows(response.data.totalElements);
          setPaginationModel((prev) => ({ ...prev, page: 0 }));
      } catch (err) {
          await dialogs.open(CustomDialog, {
              title: 'Notification',
              message: formatErrorMessage(err, 'Failed to retrieve User List'),
              isModal: true,
          });
      } finally {
          setLoading(false);
      }
  }, [paginationModel.pageSize, selectedSearch, searchText, dialogs]);

  const handleSearch = useCallback(async (field: string, text: string) => {
      const trimmed = text.trim();
      if (!trimmed) return;
      setSelectedSearch(field);
      setSearchText(trimmed);
      setPaginationModel((prev) => ({ ...prev, page: 0 }));
  }, []);

  useEffect(() => {
      fetchData();
  }, [fetchData]);

  const StyledContainer = useMemo(() => styled(Box)(({ theme }) => ({
    margin: 'auto',
    marginTop: theme.spacing(1),
    padding: theme.spacing(3),
    border: 'none',
    borderRadius: theme.shape.borderRadius,
    backgroundColor: '#ffffff',
    boxShadow: '0px 4px 8px 0px #0000001A',
  })), []);

  const StyledSubTitle = useMemo(() => styled(Typography)({
      textAlign: 'left',
      fontSize: '24px',
      fontWeight: 700,
  }), []);

  return (
    <>
      <FullscreenLoader open={loading} />
      <StyledContainer>
        <StyledSubTitle>User Management</StyledSubTitle>
        <CustomDataGrid
              rows={rows}
              columns={[
                  {
                  field: 'userId',
                  headerName: "ID",
                  width: 250,
                  renderCell: (params) => (
                      <Link
                      component="button"
                      variant='body2'
                      onClick={() => navigate(`/user-management/${params.row.id}`)}
                      sx={{ cursor: 'pointer', color: 'primary.main', textAlign: 'left' }}
                      >
                      {params.value}
                      </Link>),
                  },
                  { field: 'pii', headerName: "PII", width: 250},
                  { field: 'createdAt', headerName: "Registered At", width: 150},
                  { field: 'updatedAt', headerName: "Updated At", width: 150},
              ]}
              selectedRow={selectedRow}
              setSelectedRow={setSelectedRow}
              enableSearch={true}
              searchText={searchText}
              setSearchText={setSearchText}
              selectedSearch={selectedSearch}
              setSelectedSearch={setSelectedSearch}
              searchOptions={[
                  { value: 'userId', label: 'ID' },
                  { value: 'pii', label: 'PII' },
              ]}
              onSearch={handleSearch}
              onRefresh={getData}
              paginationMode="server"
              totalRows={totalRows}
              paginationModel={paginationModel}
              setPaginationModel={setPaginationModel}
          />
        </StyledContainer>
    </>
  )
}

export default UserManagementPage
