import { Box, Button } from '@mui/material';
import { GridToolbarContainer } from '@mui/x-data-grid';
import CustomSearchBar from '../search-bar/CustomSearchBar';

interface CustomToolbarProps {
  enableSearch?: boolean;
  searchText: string;
  setSearchText: (text: string) => void;
  selectedSearch: string;
  setSelectedSearch: (value: string) => void;
  onSearch?: (searchField: string, searchText: string) => void;
  onRegister?: () => void;
  onEdit?: () => void;
  onDelete?: () => void;
  disableEdit: boolean;
  disableDelete: boolean;
  searchOptions?: Array<{ value: string; label: string }>;
  additionalButtons?: Array<{ 
    label: string; 
    onClick: () => void; 
    color?: 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning';
    disabled?: boolean;
  }>;
}

export default function CustomToolbar({
  enableSearch = false,
  searchText,
  setSearchText,
  selectedSearch,
  setSelectedSearch,
  onSearch,
  onRegister,
  onEdit,
  onDelete,
  disableEdit,
  disableDelete,
  searchOptions,
  additionalButtons = [],
}: CustomToolbarProps) {
  return (
    <GridToolbarContainer sx={{ display: 'flex', alignItems: 'center', padding: '8px' }}>
      {enableSearch && (
        <Box sx={{ flex: 1 }}>
          <CustomSearchBar
            searchText={searchText}
            setSearchText={setSearchText}
            selectedSearch={selectedSearch}
            setSelectedSearch={setSelectedSearch}
            onSearch={onSearch}
            searchOptions={searchOptions}
          />
        </Box>
      )}

      <Box sx={{ display: 'flex', gap: '4px', justifyContent: 'flex-end', flex: enableSearch ? 1 : 'auto', width: '100%' }}>
        {onRegister && (
          <Button variant="contained" color="primary" onClick={onRegister} size='small'>
            Register
          </Button>
        )}
        {onEdit && (
          <Button variant="contained" color="primary" onClick={onEdit} disabled={disableEdit} size='small'>
            Update
          </Button>
        )}
        {onDelete && (
          <Button variant="contained" color="error" onClick={onDelete} disabled={disableDelete} size='small'>
            Delete
          </Button>
        )}
        {additionalButtons.map((btn, index) => (
          <Button
            key={index}
            variant="contained"
            color={btn.color || 'primary'}
            onClick={btn.onClick}
            disabled={btn.disabled}
            size='small'
          >
            {btn.label}
          </Button>
        ))}
      </Box>
    </GridToolbarContainer>
  );
}
