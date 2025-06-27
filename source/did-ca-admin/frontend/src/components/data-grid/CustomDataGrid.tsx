import { DataGrid, GridColDef, GridPaginationModel, GridRowSelectionModel } from "@mui/x-data-grid";
import * as React from "react";
import CustomToolbar from "../tool-bar/CustomToolbar";

interface CustomDataGridProps {
  rows: Array<{ id: string | number } & Record<string, any>>;
  columns: GridColDef[];
  enableSearch?: boolean;
  onSearch?: (searchField: string, searchText: string) => void;
  onRegister?: () => void;
  onEdit?: () => void;
  onDelete?: () => void;
  selectedRow: string | number | null;
  setSelectedRow: (id: string | number | null) => void;
  searchOptions?: Array<{ value: string; label: string }>;
  additionalButtons?: Array<{
    label: string;
    onClick: () => void;
    color?: "primary" | "secondary" | "error" | "info" | "success" | "warning";
    disabled?: boolean;
  }>;
  paginationMode: "server" | "client"; 
  totalRows?: number; 
  paginationModel?: GridPaginationModel; 
  setPaginationModel?: (model: GridPaginationModel) => void; 
  loading?: boolean; 
}

export default function CustomDataGrid({
  rows,
  columns,
  enableSearch = false,
  onSearch,
  onRegister,
  onEdit,
  onDelete,
  selectedRow,
  setSelectedRow,
  searchOptions,
  additionalButtons = [],
  paginationMode,
  totalRows = 0,
  paginationModel,
  setPaginationModel,
  loading = false,
}: CustomDataGridProps) {
  const [searchText, setSearchText] = React.useState("");
  const [selectedSearch, setSelectedSearch] = React.useState(
    searchOptions?.[0]?.value || ""
  );

  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
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
        rowCount={paginationMode === "server" ? totalRows : undefined} 
        paginationModel={paginationMode === "server" ? paginationModel : undefined} 
        onPaginationModelChange={(model) => {
          if (setPaginationModel) {
            setPaginationModel(model);
          }
        }}
        loading={loading} 
        slots={{
          toolbar: () => (
            <CustomToolbar
              enableSearch={enableSearch}
              searchText={searchText}
              setSearchText={setSearchText}
              selectedSearch={selectedSearch}
              setSelectedSearch={setSelectedSearch}
              onSearch={onSearch}
              onRegister={onRegister}
              onEdit={onEdit}
              onDelete={onDelete}
              disableEdit={!selectedRow}
              disableDelete={!selectedRow}
              searchOptions={searchOptions}
              additionalButtons={additionalButtons}
            />
          ),
        }}
        onRowSelectionModelChange={(selectedIds: GridRowSelectionModel) => {
          const selectedId = selectedIds.length > 0 ? selectedIds[0] : null;
          setSelectedRow(selectedId as string | number | null);
        }}
        getRowHeight={() => 45}
      />
    </div>
  );
}
