import { Box, Button, Popover, styled, TextField, Typography, useMediaQuery, useTheme } from '@mui/material';
import React, { useMemo, useState } from 'react';
import { Navigate, useNavigate } from 'react-router';
import { useServerStatus } from '../../context/ServerStatusContext';

export default function TrustAgentManagementPage() {
  const { casInfo } = useServerStatus();
  const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
  const { setServerStatus, setCasInfo, serverStatus } = useServerStatus();
  const navigate = useNavigate();
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

  const handlePopoverOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handlePopoverClose = () => {
    setAnchorEl(null);
  };

  const StyledContainer = useMemo(() => styled(Box)(({ theme }) => ({
    width: 400,
    margin: 'auto',
    marginTop: theme.spacing(1),
    padding: theme.spacing(3),
    border: 'none',
    borderRadius: theme.shape.borderRadius,
    backgroundColor: '#ffffff',
    boxShadow: '0px 4px 8px 0px #0000001A',
  })), []);
  
  const StyledTitle = useMemo(() => styled(Typography)({
    textAlign: 'left',
    fontSize: '24px',
    fontWeight: 700,
  }), []);

  const StyledInputArea = useMemo(() => styled(Box)(({ theme }) => ({
      marginTop: theme.spacing(1),
  })), []);

  if (serverStatus !== 'ACTIVATE') {
    return <Navigate to="/ca-registration" replace />;
  }

  return (
    <StyledContainer>
      <StyledTitle>CA Management</StyledTitle>
      <StyledInputArea>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <TextField 
            fullWidth 
            label="DID" 
            variant="standard" 
            margin="normal" 
            value={casInfo?.did || ''} 
            slotProps={{ input: { readOnly: true } }} 
          />
          <Button 
            variant="outlined" 
            size="small" 
            onClick={handlePopoverOpen} 
            sx={{
              height: '100%', 
              flexShrink: 0, 
              whiteSpace: 'nowrap', 
              minWidth: 'auto',
            }}
          >
            View DID Document
          </Button>
        </Box>

        <Popover
          open={Boolean(anchorEl)}
          anchorEl={anchorEl}
          onClose={handlePopoverClose}
          anchorOrigin={{ vertical: 'bottom', horizontal: 'left' }}
          slotProps={{
            paper: {
              sx: {
                p: 2,
                maxWidth: isSmallScreen ? '90vw' : 500,
                width: '100%',
              },
            },
          }}
        >
          <Box sx={{ p: 2, maxWidth: 500 }}>
            <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
              {JSON.stringify(casInfo?.didDocument, null, 2)}
            </Typography>
          </Box>
        </Popover>

        <TextField 
          fullWidth 
          label="Name" 
          variant="standard" 
          margin="normal" 
          value={casInfo?.name || ''} 
          slotProps={{ input: { readOnly: true } }} 
        />

        <TextField 
          fullWidth 
          label="Status" 
          variant="standard" 
          margin="normal" 
          value={casInfo?.status || ''} 
          slotProps={{ input: { readOnly: true } }} 
        />

        <TextField 
          fullWidth 
          label="URL" 
          variant="standard" 
          margin="normal" 
          value={casInfo?.serverUrl || ''} 
          slotProps={{ input: { readOnly: true } }} 
        />

        <TextField 
          fullWidth 
          label="Certificate URL" 
          variant="standard" 
          margin="normal" 
          value={casInfo?.certificateUrl || ''} 
          slotProps={{ input: { readOnly: true } }} 
        />

        <TextField 
          fullWidth 
          label="Registered At" 
          variant="standard" 
          margin="normal" 
          value={casInfo?.createdAt || ''} 
          slotProps={{ input: { readOnly: true } }} 
        />
      </StyledInputArea>
    </StyledContainer>
  );
}
