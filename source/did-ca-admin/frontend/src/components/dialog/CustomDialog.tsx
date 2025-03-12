import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import { DialogProps } from '@toolpad/core/useDialogs';
import React from 'react';

const CustomDialog: React.FC<DialogProps<{ message: string; title?: string; isModal?: boolean }, void>> = ({ 
  payload, 
  open, 
  onClose 
}) => {
  const handleClose = (event: unknown, reason?: string) => {
    if (payload?.isModal && reason === 'backdropClick') {
      return;
    }
    onClose();
  };

  return (
    <Dialog 
      open={open} 
      onClose={handleClose} 
      disableEscapeKeyDown={payload?.isModal ?? false} 
    >
      {payload?.title && <DialogTitle>{payload.title}</DialogTitle>}
      <DialogContent>{payload?.message}</DialogContent>
      <DialogActions>
        <Button variant="contained" onClick={() => onClose()} autoFocus>OK</Button>
      </DialogActions>
    </Dialog>
  );
};

export default CustomDialog;
