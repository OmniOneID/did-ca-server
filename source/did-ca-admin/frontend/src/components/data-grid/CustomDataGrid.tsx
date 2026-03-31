import { DataGrid, GridColDef, GridPaginationModel, GridRowSelectionModel, GridToolbarProps } from '@mui/x-data-grid';
import * as React from 'react';
import CustomToolbar from '../tool-bar/CustomToolbar';

interface CustomDataGridProps {
  rows: Array<{ id: string | number } & Record<string, any>>;
  columns: GridColDef[];
  enableSearch?: boolean;
  onSearch?: (searchField: string, searchText: string) => void;
  onRegister?: () => void;
  onEdit?: () => void;
  onDelete?: () => void;
  onRefresh?: () => void;
  selectedRow: string | number | null;
  setSelectedRow: (id: string | number | null) => void;
  searchOptions?: Array<{ value: string; label: string }>;
  selectableFields?: Array<{ field: string; options: Array<{ value: string; label: string }> }>;
  additionalButtons?: Array<{
    label: string;
    onClick: () => void;
    color?: 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning';
    disabled?: boolean;
  }>;
  paginationMode: 'server' | 'client';
  totalRows?: number;
  paginationModel?: GridPaginationModel;
  setPaginationModel?: (model: GridPaginationModel) => void;
  loading?: boolean;
  searchText: string;
  setSearchText: (text: string) => void;
  selectedSearch: string;
  setSelectedSearch: (value: string) => void;
}

export default function CustomDataGrid({
                                         rows,
                                         columns,
                                         enableSearch = false,
                                         onSearch,
                                         onRegister,
                                         onEdit,
                                         onDelete,
                                         onRefresh,
                                         selectedRow,
                                         setSelectedRow,
                                         searchOptions,
                                         selectableFields,
                                         additionalButtons = [],
                                         paginationMode,
                                         totalRows = 0,
                                         paginationModel,
                                         setPaginationModel,
                                         loading = false,
                                         searchText,
                                         setSearchText,
                                         selectedSearch,
                                         setSelectedSearch,
                                       }: CustomDataGridProps) {

  const CustomToolbarWrapper = React.memo((props: GridToolbarProps) => {
    return (
        <CustomToolbar
            {...props}
            enableSearch={enableSearch}
            searchText={searchText}
            setSearchText={setSearchText}
            selectedSearch={selectedSearch}
            setSelectedSearch={setSelectedSearch}
            onSearch={onSearch}
            onRegister={onRegister}
            onEdit={onEdit}
            onDelete={onDelete}
            onRefresh={onRefresh}
            disableEdit={!selectedRow}
            disableDelete={!selectedRow}
            searchOptions={searchOptions}
            selectableFields={selectableFields}
            additionalButtons={additionalButtons}
        />
    );
  });

  CustomToolbarWrapper.displayName = 'CustomToolbarWrapper';

  const handleRowSelectionModelChange = React.useCallback(
      (selectedIds: GridRowSelectionModel) => {
        const selectedId = selectedIds.length > 0 ? selectedIds[0] : null;
        setSelectedRow(selectedId as string | number | null);
      },
      [setSelectedRow]
  );

  const handlePaginationModelChange = React.useCallback(
      (model: GridPaginationModel) => {
        if (setPaginationModel) {
          setPaginationModel(model);
        }
      },
      [setPaginationModel]
  );

  const toolbarProps = React.useMemo(() => ({
    enableSearch,
    searchText,
    setSearchText,
    selectedSearch,
    setSelectedSearch,
    onSearch,
    onRegister,
    onEdit,
    onDelete,
    onRefresh,
    disableEdit: !selectedRow,
    disableDelete: !selectedRow,
    searchOptions,
    selectableFields,
    additionalButtons,
  }), [
    enableSearch, searchText, setSearchText, selectedSearch, setSelectedSearch,
    onSearch, onRegister, onEdit, onDelete, selectedRow, searchOptions, selectableFields, additionalButtons
  ]);

  return (
      <div style={{ display: 'flex', flexDirection: 'column' }}>
        <DataGrid
            checkboxSelection={!!onEdit || !!onDelete}
            disableMultipleRowSelection
            disableRowSelectionOnClick
            rows={rows}
            columns={columns}
            pageSizeOptions={[10, 20, 50]}
            disableColumnResize
            density="compact"
            paginationMode={paginationMode}
            rowCount={paginationMode === 'server' ? totalRows : undefined}
            paginationModel={paginationModel}
            onPaginationModelChange={handlePaginationModelChange}
            loading={loading}
            slots={{
              toolbar: CustomToolbarWrapper,
            }}
            slotProps={{
              toolbar: toolbarProps,
            }}
            onRowSelectionModelChange={handleRowSelectionModelChange}
            getRowHeight={() => 45}
        />
      </div>
  );
}
