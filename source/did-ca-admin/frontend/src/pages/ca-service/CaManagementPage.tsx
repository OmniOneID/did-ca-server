import { Box, Button, Popover, TextField, Typography, useMediaQuery, useTheme } from '@mui/material';
import React, { useState } from 'react';
import { Navigate, useNavigate } from 'react-router';
import { useServerStatus } from '../../context/ServerStatusContext';

export default function TrustAgentManagementPage() {
  const { casInfo } = useServerStatus();
  const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
  const { setServerStatus, setCasInfo, serverStatus } = useServerStatus();
  const navigate = useNavigate();
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

  if (!casInfo) {
    return (
      <Box sx={{ textAlign: 'center', mt: 5 }}>
        <Typography variant="h6">Failed to retrieve CAS information.</Typography>
      </Box>
    );
  }

  const handlePopoverOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handlePopoverClose = () => {
    setAnchorEl(null);
  };

  if (serverStatus !== 'ACTIVATE') {
    return <Navigate to="/ca-registration" replace />;
  }

  return (
    <Box sx={{ maxWidth: 400, margin: 'auto', mt: 1, p: 3, border: '1px solid #ccc', borderRadius: 2 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <TextField 
          fullWidth 
          label="DID" 
          variant="standard" 
          margin="normal" 
          value={casInfo.did} 
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
            {JSON.stringify(casInfo.didDocument, null, 2)}
          </Typography>
        </Box>
      </Popover>

      <TextField 
        fullWidth 
        label="Name" 
        variant="standard" 
        margin="normal" 
        value={casInfo.name} 
        slotProps={{ input: { readOnly: true } }} 
      />

      <TextField 
        fullWidth 
        label="Status" 
        variant="standard" 
        margin="normal" 
        value={casInfo.status} 
        slotProps={{ input: { readOnly: true } }} 
      />

      <TextField 
        fullWidth 
        label="URL" 
        variant="standard" 
        margin="normal" 
        value={casInfo.serverUrl} 
        slotProps={{ input: { readOnly: true } }} 
      />

      <TextField 
        fullWidth 
        label="Certificate URL" 
        variant="standard" 
        margin="normal" 
        value={casInfo.certificateUrl} 
        slotProps={{ input: { readOnly: true } }} 
      />

      <TextField 
        fullWidth 
        label="Registered At" 
        variant="standard" 
        margin="normal" 
        value={casInfo.createdAt} 
        slotProps={{ input: { readOnly: true } }} 
      />
    </Box>
  );
}
