import { Box, Button, IconButton } from '@mui/material';
import { GridToolbarContainer } from '@mui/x-data-grid';
import * as React from 'react';
import CustomSearchBar from '../search-bar/CustomSearchBar';
import RefreshIcon from '@mui/icons-material/Refresh';


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
  onRefresh?: () => void;
  disableEdit: boolean;
  disableDelete: boolean;
  searchOptions?: Array<{ value: string; label: string }>;
  selectableFields?: Array<{ field: string; options: Array<{ value: string; label: string }> }>;
  additionalButtons?: Array<{
    label: string;
    onClick: () => void;
    color?: 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning';
    disabled?: boolean;
  }>;
}

const CustomToolbar = React.memo(({
                                    enableSearch = false,
                                    searchText,
                                    setSearchText,
                                    selectedSearch,
                                    setSelectedSearch,
                                    onSearch,
                                    onRegister,
                                    onEdit,
                                    onDelete,
                                    onRefresh,
                                    disableEdit,
                                    disableDelete,
                                    searchOptions,
                                    selectableFields,
                                    additionalButtons = [],
                                    ...props
                                  }: CustomToolbarProps) => {
  // Check if any buttons are available
  const hasButtons = Boolean(onRegister || onEdit || onDelete || additionalButtons.length > 0);
  // Use column layout only when both search and buttons are present
  const useColumnLayout = enableSearch && hasButtons;

  return (
      <GridToolbarContainer sx={{
        display: 'flex',
        flexDirection: useColumnLayout ? 'column' : 'row',
        alignItems: useColumnLayout ? 'stretch' : 'center',
        justifyContent: (!enableSearch && !hasButtons && onRefresh) ? 'flex-end' : 'flex-start',
        p: 0,
        pb: '8px',
        pt: '8px',
        gap: useColumnLayout ? '8px' : '8px',
        width: '100%'
      }}>
        {enableSearch && (
            <Box sx={{
              display: 'flex',
              justifyContent: 'flex-start',
              alignItems: 'center',
              flex: useColumnLayout ? 'none' : 1
            }}>
              <CustomSearchBar
                  searchText={searchText}
                  setSearchText={setSearchText}
                  selectedSearch={selectedSearch}
                  setSelectedSearch={setSelectedSearch}
                  onSearch={onSearch}
                  searchOptions={searchOptions}
                  selectableFields={selectableFields}
              />
            </Box>
        )}

        {(hasButtons || onRefresh) && (
            <Box sx={{
              display: 'flex',
              gap: '4px',
              justifyContent: hasButtons ? 'space-between' : 'flex-end',
              alignItems: 'center',
              width: useColumnLayout ? '100%' : (enableSearch ? 'auto' : '100%')
            }}>
              {hasButtons && (
                  <Box sx={{
                    display: 'flex',
                    gap: '4px',
                    alignItems: 'center'
                  }}>
                    {onRegister && (
                        <Button variant="contained" color="primary" onClick={onRegister} size="small">
                          Register
                        </Button>
                    )}
                    {onEdit && (
                        <Button variant="contained" color="primary" onClick={onEdit} disabled={disableEdit} size="small">
                          Update
                        </Button>
                    )}
                    {onDelete && (
                        <Button variant="contained" color="error" onClick={onDelete} disabled={disableDelete} size="small">
                          Delete
                        </Button>
                    )}
                    {additionalButtons.map((btn, index) => (
                        <Button
                            key={index}
                            variant="outlined"
                            color={btn.color || 'primary'}
                            onClick={btn.onClick}
                            disabled={btn.disabled}
                            size="small"
                        >
                          {btn.label}
                        </Button>
                    ))}
                  </Box>
              )}

              {onRefresh && (
                  <IconButton
                      onClick={onRefresh}
                      sx={{
                        backgroundColor: 'primary.main',
                        color: 'white',
                        '&:hover': {
                          backgroundColor: 'primary.dark',
                        },
                        width: 36,
                        height: 36,
                        borderRadius: '4px',
                      }}
                      size="small"
                  >
                    <RefreshIcon fontSize="small" />
                  </IconButton>
              )}
            </Box>
        )}
      </GridToolbarContainer>
  );
});

CustomToolbar.displayName = 'CustomToolbar';

export default CustomToolbar;
