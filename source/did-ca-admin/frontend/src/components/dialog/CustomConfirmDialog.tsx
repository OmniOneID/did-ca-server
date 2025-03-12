import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';
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
    <Dialog open={open} onClose={handleClose} disableEscapeKeyDown={payload?.isModal ?? false}>
      {payload?.title && <DialogTitle>{payload.title}</DialogTitle>}
      <DialogContent>
        <DialogContentText>{payload?.message}</DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button variant="contained" onClick={() => onClose(false)} color="secondary">
          Cancel
        </Button>
        <Button variant="contained" onClick={() => onClose(true)} color="primary" autoFocus>
          OK
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CustomConfirmDialog;
