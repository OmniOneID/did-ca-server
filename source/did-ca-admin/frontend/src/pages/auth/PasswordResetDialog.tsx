import { Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField } from "@mui/material";
import React, { useEffect, useState } from "react";

interface PasswordResetDialogProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (newPassword: string) => void;
}

interface ErrorState {
  newPassword?: string;
  confirmPassword?: string;
}

const PasswordResetDialog: React.FC<PasswordResetDialogProps> = ({ open, onClose, onSubmit }) => {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState<ErrorState>({});
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);

  const handleConfirm = () => {
    if (!validate()) return;
    onSubmit(newPassword);
    onClose();
  };

  const handleChange = (setter: React.Dispatch<React.SetStateAction<string>>) => 
    (event: React.ChangeEvent<HTMLInputElement>) => setter(event.target.value);

  const validate = () => {
    let tempErrors: ErrorState = {};

    if (!newPassword.trim()) {
      tempErrors.newPassword = "Please enter a new password.";
    } else if (newPassword.length < 8 || newPassword.length > 64) {
      tempErrors.newPassword = "Password must be between 8 and 64 characters.";
    }

    if (confirmPassword !== newPassword) {
      tempErrors.confirmPassword = "Passwords do not match.";
    }

    setErrors(tempErrors);
    return Object.values(tempErrors).every((error) => !error);
  };

  useEffect(() => {
    setIsButtonDisabled(!newPassword.trim() || !confirmPassword.trim());
  }, [newPassword, confirmPassword]);

  useEffect(() => {
    if (open) {
      setNewPassword("");
      setConfirmPassword("");
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
          label="New Password"
          type="password"
          variant="outlined"
          margin="normal"
          value={newPassword}
          onChange={handleChange(setNewPassword)}
          error={!!errors.newPassword}
          helperText={errors.newPassword}
        />
        <TextField
          fullWidth
          label="Confirm Password"
          type="password"
          variant="outlined"
          margin="normal"
          value={confirmPassword}
          onChange={handleChange(setConfirmPassword)}
          error={!!errors.confirmPassword}
          helperText={errors.confirmPassword}
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

export default PasswordResetDialog;
