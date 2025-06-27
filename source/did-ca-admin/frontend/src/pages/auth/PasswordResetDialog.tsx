import { Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField, Box } from "@mui/material";
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
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm" sx={{ maxWidth: 500, margin: "0 auto" }}>
      <Box sx={{ px: 2 }}>
        <DialogTitle sx={{ p: 0, pt: 2, fontWeight: 700 }}>Reset Password</DialogTitle>
        <Box sx={{ height: "1px", backgroundColor: "var(--G40, #BFBFBF)", width: "100%", mt: 1 }} />
      </Box>

      <DialogContent sx={{ px: 2 }}>
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

export default PasswordResetDialog;
