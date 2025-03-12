import { Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from "@mui/material";
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
      tempErrors.oldPassword = "Please enter a current password.";
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
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Reset Password</DialogTitle>
      <DialogContent>
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
      <DialogActions>
        <Button variant="contained" onClick={onClose} color="secondary">
          Cancel
        </Button>
        <Button variant="contained" onClick={handleConfirm} color="primary" disabled={isButtonDisabled}>
          Update
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default PasswordChangeDialog;
