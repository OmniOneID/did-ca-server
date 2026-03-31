
import { Box, Link, styled, Typography } from '@mui/material';
import { GridPaginationModel } from '@mui/x-data-grid';
import { useDialogs } from '@toolpad/core';
import React, { useCallback, useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import CustomDataGrid from '../../components/data-grid/CustomDataGrid';
import CustomConfirmDialog from '../../components/dialog/CustomConfirmDialog';
import CustomDialog from '../../components/dialog/CustomDialog';
import { fetchAdminList, deleteAdmin, requestPasswordResetByRoot } from '../../apis/admin-api';
import PasswordResetDialog from '../auth/PasswordResetDialog';
import { useSession } from '../../context/SessionContext';
import { formatErrorMessage } from '../../utils/error-handler';
import { sha256Hash } from '../../utils/sha256-hash';

type Props = {}

type AdminRow = {
  id: string | number;
  loginId: string;
  role: string;
  createdAt: string;
  updatedAt: string;
};

const AdminManagementPage = (props: Props) => {
    const navigate = useNavigate();
    const dialogs = useDialogs();
    const [loading, setLoading] = useState<boolean>(false);
    const [totalRows, setTotalRows] = useState<number>(0);
    const [selectedRow, setSelectedRow] = useState<string | number | null>(null);
    const [rows, setRows] = useState<AdminRow[]>([]);
    const [requirePasswordReset, setRequirePasswordReset] = useState(false);
    const [searchText, setSearchText] = useState<string>('');
    const [selectedSearch, setSelectedSearch] = useState<string>('loginId');
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
            const response = await fetchAdminList(
                paginationModel.page,
                paginationModel.pageSize,
                selectedSearch && searchText.trim() ? selectedSearch : null,
                selectedSearch && searchText.trim() ? searchText.trim() : null
            );
            setRows(response.data.content);
            setTotalRows(response.data.totalElements);
        } catch (err) {
            console.error("Failed to retrieve Admin List. ", err);
            navigate('/error', { state: { message: formatErrorMessage(err, "Failed to fetch Admin List") } });
        } finally {
            setLoading(false);
        }
    }, [paginationModel.page, paginationModel.pageSize, selectedSearch, searchText, navigate]);

    const getData = useCallback(async () => {
        setLoading(true);
        try {
            const response = await fetchAdminList(
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
                message: formatErrorMessage(err, 'Failed to retrieve Admin List'),
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

    const handleDelete = async () => {
      const id = selectedRowData?.id as number;
      if (id) {

        if (selectedRowData?.role === 'ROOT') {
          dialogs.open(CustomDialog, {
            title: 'Notification',
            message: 'Root Admin cannot be deleted.',
            isModal: true,
          });
          return;
        }

        const result = await dialogs.open(CustomConfirmDialog, {
          title: 'Confirmation',
          message: 'Are you sure you want to delete Admin?',
          isModal: true,
        });

        if (result) {
          setLoading(true);
          deleteAdmin(id)
            .then(() => {
              dialogs.open(CustomDialog, {
                title: 'Notification',
                message: 'Admin delete completed.',
                isModal: true,
              }, {
                onClose: async () => {
                  getData();
                },
              });
            })
            .catch((err) => {
              console.error("Failed to delete Admin. ", err);
              navigate('/error', { state: { message: formatErrorMessage(err, "Failed to delete Admin") } });
            })
            .finally(() => setLoading(false));
        }
      }
    };

    const handlePasswordReset = async (newPassword: string) => {
      if (!selectedRowData) return;

      try {

        const result = await dialogs.open(CustomConfirmDialog, {
          title: 'Confirmation',
          message: 'Are you sure you want to Change Admin password?',
          isModal: true,
        });

        if (result) {
          const hashedPassword = await sha256Hash(newPassword);
          await requestPasswordResetByRoot({
                  loginId: selectedRowData.loginId,
                  newPassword: hashedPassword,
          });

          dialogs.open(CustomDialog, {
              title: 'Notification',
              message: 'Admin password reset completed.',
              isModal: true,
          });
        }
      } catch (error) {
        console.error('Failed to reset password:', error);
      } finally {
        setRequirePasswordReset(false);
      }
    };

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
          <StyledSubTitle>Admin Management</StyledSubTitle>
          <CustomDataGrid
              rows={rows}
              columns={[
                  {
                  field: 'loginId',
                  headerName: "ID",
                  width: 250,
                  renderCell: (params) => (
                      <Link
                      component="button"
                      variant='body2'
                      onClick={() => navigate(`/admin-management/${params.row.id}`)}
                      sx={{ cursor: 'pointer', color: 'primary.main', textAlign: 'left' }}
                      >
                      {params.value}
                      </Link>),
                  },
                  { field: 'role', headerName: "Role", width: 150},
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
                  { value: 'loginId', label: 'ID' },
                  { value: 'role', label: 'Role' },
              ]}
              selectableFields={[
                  {
                      field: 'role',
                      options: [
                          { value: 'ROOT', label: 'Root' },
                          { value: 'NORMAL', label: 'Normal' },
                      ],
                  },
              ]}
              onSearch={handleSearch}
              onRefresh={getData}
              // onEdit={() => {
              //     if (selectedRowData) {
              //     navigate(`/list-settings/allowed-ca/allowed-ca-edit/${selectedRowData.id}`);
              //     }
              // }}
              onRegister={session?.user?.role === 'ROOT' ? () => navigate('/admin-management/admin-registration') : undefined}
              onDelete={session?.user?.role === 'ROOT' ? handleDelete : undefined}
              additionalButtons={
                session?.user?.role === 'ROOT'
                  ? [
                      {
                        label: 'Change Password',
                        onClick: () => setRequirePasswordReset(true),
                        color: 'secondary',
                        disabled: selectedRow === null,
                      },
                    ]
                  : []
              }
              paginationMode="server"
              totalRows={totalRows}
              paginationModel={paginationModel}
              setPaginationModel={setPaginationModel}
          />
          <PasswordResetDialog
            open={requirePasswordReset}
            onClose={() => setRequirePasswordReset(false)}
            onSubmit={handlePasswordReset}
          />
      </StyledContainer>
      </>
  )
}

export default AdminManagementPage
