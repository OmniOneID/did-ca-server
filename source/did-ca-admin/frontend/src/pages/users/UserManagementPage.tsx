
import { Link } from '@mui/material';
import { GridPaginationModel } from '@mui/x-data-grid';
import { useDialogs } from '@toolpad/core';
import React, { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import CustomDataGrid from '../../components/data-grid/CustomDataGrid';
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
  const { session } = useSession(); 

  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({
      page: 0,
      pageSize: 10,
  });

  const selectedRowData = useMemo(() => {
      return rows.find(row => row.id === selectedRow) || null;
  }, [rows, selectedRow]);

  useEffect(() => {
      setLoading(true);
      fetchUserList(paginationModel.page, paginationModel.pageSize, null, null)
        .then((response) => {
          setRows(response.data.content);
          setTotalRows(response.data.totalElements);
        })
        .catch((err) => {
          console.error("Failed to retrieve User List. ", err);
          navigate('/error', { state: { message: formatErrorMessage(err, "Failed to fetch User List") } });
        })
        .finally(() => setLoading(false));
  }, [paginationModel]);

  return (
    <>
      <FullscreenLoader open={loading} />
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
            paginationMode="server" 
            totalRows={totalRows} 
            paginationModel={paginationModel} 
            setPaginationModel={setPaginationModel} 
        />
    </>
  )
}

export default UserManagementPage