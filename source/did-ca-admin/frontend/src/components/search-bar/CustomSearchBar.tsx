import * as React from 'react';
import { TextField, InputAdornment, Button, Box, Select, MenuItem, FormControl, SelectChangeEvent } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

interface CustomSearchBarProps {
  searchText: string;
  setSearchText: (text: string) => void;
  selectedSearch: string;  // ✅ 부모에서 받아옴
  setSelectedSearch: (value: string) => void;  // ✅ 부모에서 받아옴
  onSearch?: (searchField: string, searchText: string) => void;
  searchOptions?: Array<{ value: string; label: string }>;
}

export default function CustomSearchBar({
  searchText,
  setSearchText,
  selectedSearch,
  setSelectedSearch,
  onSearch,
  searchOptions = [],
}: CustomSearchBarProps) {
  const inputRef = React.useRef<HTMLInputElement | null>(null);

  const handleSearchTextChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchText(e.target.value);
  };

  const handleSearchOptionChange = (e: SelectChangeEvent<string>) => {
    setSelectedSearch(e.target.value);
  };

  return (
    <Box sx={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
      <FormControl size="small">
        <Select value={selectedSearch} onChange={handleSearchOptionChange}>
          {searchOptions.map((option) => (
            <MenuItem key={option.value} value={option.value}>
              {option.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <TextField
        inputRef={inputRef}
        autoFocus
        size="small"
        variant="outlined"
        placeholder="검색어 입력"
        value={searchText}
        onChange={handleSearchTextChange}
        slotProps={{
          input: {
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon color="action" />
              </InputAdornment>
            )
          }
        }}
      />

      <Button variant="contained" color="primary" onClick={() => onSearch?.(selectedSearch, searchText)} disabled={!onSearch}>
        검색
      </Button>
    </Box>
  );
}
