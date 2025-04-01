import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Box } from '@mui/material';
import { DialogProps } from '@toolpad/core/useDialogs';
import React from 'react';

const CustomConfirmDialog: React.FC<DialogProps<{ message: string; title?: string; isModal?: boolean }, boolean>> = ({
  payload,
  open,
  onClose,
}) => {
  const handleClose = (event: unknown, reason?: string) => {
    if (payload?.isModal && reason === 'backdropClick') {
      return; 
    }
    onClose(false);
  };

  return (
    <Dialog 
      open={open} 
      onClose={handleClose} 
      disableEscapeKeyDown={payload?.isModal ?? false} 
      fullWidth
      sx={{ maxWidth: 500, margin: '0 auto' }} 
    >
      {payload?.title && (
        <Box sx={{ px: 2 }}>
          <DialogTitle sx={{ p: 0, pt: 2, fontWeight: 700 }}>{payload.title}</DialogTitle>
          <Box sx={{ height: '1px', backgroundColor: 'var(--G40, #BFBFBF)', width: '100%', mt: 1 }} />
        </Box>
      )}

      <DialogContent sx={{ px: 2 }}>
        <DialogContentText sx={{ textAlign: 'left' }}>
          {payload?.message}
        </DialogContentText>
      </DialogContent>

      <DialogActions sx={{ px: 2, pt: 0, display: 'flex', gap: 2, mt: 0 }}>
        <Button variant="outlined" onClick={() => onClose(false)} color="primary" sx={{ flexGrow: 1, height: '48px' }}>
          Cancel
        </Button>
        <Button variant="contained" onClick={() => onClose(true)} color="primary" autoFocus sx={{ flexGrow: 1, height: '48px' }}>
          OK
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CustomConfirmDialog;
