import { Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField, Box } from "@mui/material";
import React, { useEffect, useState } from "react";

interface PasswordResetDialogProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (oldPassword: string, newPassword: string) => void;
}

interface ErrorState {
  oldPassword?: string;
  newPassword?: string;
}

const PasswordChangeDialog: React.FC<PasswordResetDialogProps> = ({ open, onClose, onSubmit }) => {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [errors, setErrors] = useState<ErrorState>({});
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);

  const handleConfirm = () => {
    if (!validate()) return;
    onSubmit(oldPassword, newPassword);
    onClose();
  };

  const handleChange = (setter: React.Dispatch<React.SetStateAction<string>>) => 
    (event: React.ChangeEvent<HTMLInputElement>) => setter(event.target.value);

  const validate = () => {
    let tempErrors: ErrorState = {};

    if (!oldPassword.trim()) {
      tempErrors.oldPassword = "Please enter your current password.";
    } 
    
    if (!newPassword.trim()) {
      tempErrors.newPassword = "Please enter a new password.";
    } else if (newPassword.length < 8 || newPassword.length > 64) {
      tempErrors.newPassword = "Password must be between 8 and 64 characters.";
    }

    setErrors(tempErrors);
    return Object.values(tempErrors).every((error) => !error);
  };

  useEffect(() => {
    setIsButtonDisabled(!oldPassword.trim() || !newPassword.trim());
  }, [oldPassword, newPassword]);

  useEffect(() => {
    if (open) {
      setOldPassword("");
      setNewPassword("");
      setErrors({});
      setIsButtonDisabled(true);
    }
  }, [open]);

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm" sx={{ maxWidth: 500, margin: "0 auto" }}>
      <Box sx={{ px: 2 }}>
        <DialogTitle sx={{ p: 0, pt: 2, fontWeight: 700 }}>Reset Password</DialogTitle>
        <Box sx={{ height: "1px", backgroundColor: "var(--G40, #BFBFBF)", width: "100%", mt: 1 }} />
      </Box>

      <DialogContent sx={{ px: 2 }}>
        <TextField
          fullWidth
          label="Current Password"
          type="password"
          variant="outlined"
          margin="normal"
          value={oldPassword}
          onChange={handleChange(setOldPassword)}
          error={!!errors.oldPassword}
          helperText={errors.oldPassword}
        />
        <TextField
          fullWidth
          label="New Password"
          type="password"
          variant="outlined"
          margin="normal"
          value={newPassword}
          onChange={handleChange(setNewPassword)}
          error={!!errors.newPassword}
          helperText={errors.newPassword}
        />
      </DialogContent>

      <DialogActions sx={{ px: 2, pt: 0, display: "flex", gap: 2, mt: 0 }}>
        <Button variant="outlined" onClick={onClose} color="primary" sx={{ flexGrow: 1, height: "48px" }}>
          Cancel
        </Button>
        <Button variant="contained" onClick={handleConfirm} color="primary" disabled={isButtonDisabled} sx={{ flexGrow: 1, height: "48px" }}>
          Update
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default PasswordChangeDialog;
