import * as React from 'react';
import {
    TextField,
    InputAdornment,
    Button,
    Box,
    Select,
    MenuItem,
    FormControl,
    SelectChangeEvent,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

interface CustomSearchBarProps {
    searchText: string;
    setSearchText: (text: string) => void;
    selectedSearch: string;
    setSelectedSearch: (value: string) => void;
    onSearch?: (searchField: string, searchText: string) => void;
    searchOptions?: Array<{ value: string; label: string }>;
    selectableFields?: Array<{ field: string; options: Array<{ value: string; label: string }> }>;
}

export default function CustomSearchBar({
                                            searchText: externalSearchText,
                                            setSearchText,
                                            selectedSearch,
                                            setSelectedSearch,
                                            onSearch,
                                            searchOptions = [],
                                            selectableFields = [],
                                        }: CustomSearchBarProps) {
    const inputRef = React.useRef<HTMLInputElement | null>(null);
    const defaultValueRef = React.useRef(externalSearchText);
    const lastSearchedValueRef = React.useRef(externalSearchText);
    const isFirstRender = React.useRef(true);

    // State to store temporary value for SelectBox
    const [tempSelectValue, setTempSelectValue] = React.useState<string>(externalSearchText);

    const isSelectableField = React.useMemo(() => {
        return selectableFields.some(field => field.field === selectedSearch);
    }, [selectableFields, selectedSearch]);

    const currentSelectableField = React.useMemo(() => {
        return selectableFields.find(field => field.field === selectedSearch);
    }, [selectableFields, selectedSearch]);

    React.useEffect(() => {
        if (isFirstRender.current) {
            isFirstRender.current = false;
            return;
        }

        if (externalSearchText === lastSearchedValueRef.current) {
            return;
        }

        // Synchronize tempSelectValue when externalSearchText changes
        setTempSelectValue(externalSearchText);

        if (inputRef.current && document.activeElement !== inputRef.current) {
            inputRef.current.value = externalSearchText;
        }
    }, [externalSearchText]);

    const executeSearch = React.useCallback(() => {
        if (!onSearch) return;

        const currentValue = isSelectableField ? tempSelectValue : (inputRef.current?.value || '');
        lastSearchedValueRef.current = currentValue;

        setSearchText(currentValue);
        onSearch(selectedSearch, currentValue);
    }, [onSearch, selectedSearch, setSearchText, tempSelectValue, isSelectableField]);

    const handleSelectableFieldChange = React.useCallback((e: SelectChangeEvent<string>) => {
        const newValue = e.target.value;
        // Store in temporary state only, actual search will be executed when Search button is clicked
        setTempSelectValue(newValue);
    }, []);

    const handleSearchClick = React.useCallback(() => {
        executeSearch();
    }, [executeSearch]);

    const handleSearchOptionChange = React.useCallback((e: SelectChangeEvent<string>) => {
        const newField = e.target.value;
        setSelectedSearch(newField);
        // Reset all search values when search field is changed
        setTempSelectValue('');
        setSearchText('');
        if (inputRef.current) {
            inputRef.current.value = '';
        }
    }, [setSelectedSearch, setSearchText]);

    const handleBlur = React.useCallback(() => {
        if (inputRef.current) {
            if (inputRef.current.value !== externalSearchText) {
                setSearchText(inputRef.current.value);
            }
        }
    }, [setSearchText, externalSearchText]);

    const handleKeyDown = React.useCallback((e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && onSearch) {
            e.preventDefault();
            executeSearch();
        }
    }, [executeSearch, onSearch]);

    return (
        <Box sx={{ display: 'flex', gap: '6px', alignItems: 'center' }}>
            <FormControl size="small" sx={{ minWidth: 100 }}>
                <Select
                    value={selectedSearch}
                    onChange={handleSearchOptionChange}
                    displayEmpty
                    size="small"
                >
                    {searchOptions.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

            {isSelectableField ? (
                <FormControl size="small" sx={{ width: 200 }}>
                    <Select
                        value={tempSelectValue}
                        onChange={handleSelectableFieldChange}
                        displayEmpty
                        size="small"
                        startAdornment={
                            <InputAdornment position="start">
                                <SearchIcon fontSize="small" />
                            </InputAdornment>
                        }
                    >
                        <MenuItem value="">
                            <em>Select {currentSelectableField?.field}</em>
                        </MenuItem>
                        {currentSelectableField?.options.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            ) : (
                <TextField
                    inputRef={inputRef}
                    size="small"
                    variant="outlined"
                    placeholder="Enter keyword"
                    defaultValue={defaultValueRef.current}
                    onBlur={handleBlur}
                    onKeyDown={handleKeyDown}
                    slotProps={{
                        input: {
                            startAdornment: (
                                <InputAdornment position="start">
                                    <SearchIcon fontSize="small" />
                                </InputAdornment>
                            ),
                        },
                    }}
                    sx={{ width: 200 }}
                />
            )}

            <Button
                variant="contained"
                color="primary"
                onClick={handleSearchClick}
                disabled={!onSearch || (isSelectableField && !tempSelectValue)}
                size="small"
                sx={{ minWidth: 80, height: 36 }}
            >
                Search
            </Button>
        </Box>
    );
}
