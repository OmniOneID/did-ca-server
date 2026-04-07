import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    TextField,
    Box,
    Typography,
    IconButton,
    Alert,
    Collapse,
    List,
    ListItem,
    ListItemIcon,
    ListItemText
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { usePasswordPolicy } from "../../hooks/usePasswordPolicy";
import { ValidationRuleResult } from "../../constants/password-policy";
import { Visibility, VisibilityOff, Shield, Check, Close, ExpandMore, ExpandLess, Info } from "@mui/icons-material";
import { verifyAdminIdUnique } from "../../apis/admin-api";
import {emailRegex} from "../../utils/regex";

interface ChangeIdAndPasswordDialogProps {
    open: boolean;
    onClose: () => void;
    onSubmit: (oldLoginId: string, newLoginId: string, oldPassword: string, newPassword: string) => void;
    oldLoginId?: string;
}

interface ErrorState {
    oldLoginId?: string;
    newLoginId?: string;
    oldPassword?: string;
    newPassword?: string;
    confirmPassword?: string;
}

const ChangeIdAndPasswordDialog: React.FC<ChangeIdAndPasswordDialogProps> = ({
                                                                                 open,
                                                                                 onClose,
                                                                                 onSubmit,
                                                                                 oldLoginId: initialOldLoginId = ""
                                                                             }) => {
    const [oldLoginId, setOldLoginId] = useState(initialOldLoginId);
    const [newLoginId, setNewLoginId] = useState("");
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errors, setErrors] = useState<ErrorState>({});
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [isCheckingId, setIsCheckingId] = useState(false);
    const [idCheckMessage, setIdCheckMessage] = useState<string | null>(null);
    const [isIdUnique, setIsIdUnique] = useState(false);
    const [idCheckPerformed, setIdCheckPerformed] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitError, setSubmitError] = useState<string | null>(null);

    // Password visibility states
    const [showOldPassword, setShowOldPassword] = useState(false);
    const [showNewPassword, setShowNewPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    // Touch states for validation
    const [oldLoginIdTouched, setOldLoginIdTouched] = useState(false);
    const [newLoginIdTouched, setNewLoginIdTouched] = useState(false);
    const [oldPasswordTouched, setOldPasswordTouched] = useState(false);
    const [newPasswordTouched, setNewPasswordTouched] = useState(false);
    const [confirmPasswordTouched, setConfirmPasswordTouched] = useState(false);

    // Password policy hook
    const {
        policy,
        isLoading: isPolicyLoading,
        validatePassword,
        getValidationResults,
        getValidationSummary
    } = usePasswordPolicy();

    // Real-time password validation results
    const [validationResults, setValidationResults] = useState<ValidationRuleResult[]>([]);
    const [validationSummary, setValidationSummary] = useState({
        isValid: false,
        passedCount: 0,
        totalCount: 0,
        failedRules: [] as string[]
    });

    // Requirements visibility
    const [showNewPasswordRequirements, setShowNewPasswordRequirements] = useState(false);

    // Check ID uniqueness
    const checkIdUniqueness = async () => {
        if (!newLoginId.trim()) {
            setIdCheckMessage("Please enter a new ID");
            setIsIdUnique(false);
            setIdCheckPerformed(false);
            return;
        }

        // Check email format first before API call
        if (!emailRegex.test(newLoginId.trim())) {
            setIdCheckMessage(null);
            setIsIdUnique(false);
            setIdCheckPerformed(false);
            return;
        }

        setIsCheckingId(true);
        try {
            const response = await verifyAdminIdUnique(newLoginId);
            if (response.data.unique) {
                setIdCheckMessage("ID is available");
                setIsIdUnique(true);
                setIdCheckPerformed(true);
            } else {
                setIdCheckMessage("ID is already in use");
                setIsIdUnique(false);
                setIdCheckPerformed(true);
            }
        } catch (error) {
            setIdCheckMessage("Error checking ID availability");
            setIsIdUnique(false);
            setIdCheckPerformed(true);
        } finally {
            setIsCheckingId(false);
        }
    };

    const handleNewLoginIdChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        setNewLoginId(value);
        // Reset uniqueness check when user modifies the input
        setIsIdUnique(false);
        setIdCheckMessage(null);
        setIdCheckPerformed(false);
    };

    const handleNewPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const password = event.target.value;
        setNewPassword(password);

        // Update real-time validation when policy is available
        if (policy && password) {
            const results = getValidationResults(password);
            const summary = getValidationSummary(password);
            setValidationResults(results);
            setValidationSummary(summary);

            // Auto-show requirements when user starts typing
            if (!showNewPasswordRequirements) {
                setShowNewPasswordRequirements(true);
            }

            // Auto-hide requirements when password becomes valid and is not focused
            if (summary.isValid && document.activeElement?.id !== 'new-password-change') {
                setTimeout(() => {
                    setShowNewPasswordRequirements(false);
                }, 1500);
            }
        } else {
            setValidationResults([]);
            setValidationSummary({
                isValid: false,
                passedCount: 0,
                totalCount: 0,
                failedRules: []
            });
        }
    };

    const handleConfirm = async () => {
        if (!validate(true)) return;
        setIsSubmitting(true);
        setSubmitError(null);
        try {
            await onSubmit(oldLoginId, newLoginId, oldPassword, newPassword);
        } catch (error: any) {
            const errorMessage = error?.response?.data?.description || error?.message || "An error occurred while updating your information.";
            setSubmitError(errorMessage);
            setIsSubmitting(false);
        }
    };

    const validate = (showErrors = false) => {
        let tempErrors: ErrorState = {};

        // Validate old login ID - only show error if touched or showErrors is true
        if (showErrors && !oldLoginId.trim()) {
            tempErrors.oldLoginId = "Current ID cannot be empty";
        }

        // Validate new login ID
        if ((showErrors || newLoginIdTouched) && !newLoginId.trim()) {
            tempErrors.newLoginId = "New ID cannot be empty";
        } else if (idCheckMessage && !isIdUnique && newLoginId.trim()) {
            tempErrors.newLoginId = "Please check availability";
        } else if (!idCheckPerformed && newLoginId.trim() && !emailRegex.test(newLoginId)) {
            // Validate email format only if check has NOT been performed yet
            // Once checked, rely on the check result instead
            tempErrors.newLoginId = 'Please enter a valid email address.';
        }

        // Validate old password
        if ((showErrors || oldPasswordTouched) && !oldPassword.trim()) {
            tempErrors.oldPassword = "Current password cannot be empty";
        }

        // Validate new password
        if ((showErrors || newPasswordTouched) && !newPassword.trim()) {
            tempErrors.newPassword = "New password cannot be empty";
        } else if (policy && newPasswordTouched && newPassword.trim() && !validatePassword(newPassword)) {
            tempErrors.newPassword = "Password does not meet policy requirements";
        } else if (!policy && newPassword.trim() && (newPassword.length < 8 || newPassword.length > 64)) {
            tempErrors.newPassword = "Password must be between 8 and 64 characters";
        }

        // Validate password confirmation
        if ((showErrors || confirmPasswordTouched) && !confirmPassword.trim()) {
            tempErrors.confirmPassword = "Please confirm your new password";
        } else if (confirmPasswordTouched && confirmPassword !== newPassword && confirmPassword.trim()) {
            tempErrors.confirmPassword = "Passwords do not match";
        }

        setErrors(tempErrors);
        return Object.values(tempErrors).every((error) => !error);
    };

    // Update button state based on validation
    useEffect(() => {
        const hasAllInputs = oldLoginId.trim() && newLoginId.trim() && oldPassword.trim() && newPassword.trim() && confirmPassword.trim();
        const isPasswordValid = policy ? validatePassword(newPassword) : newPassword.length >= 8;
        const passwordsMatch = newPassword === confirmPassword;

        setIsButtonDisabled(!hasAllInputs || !isPasswordValid || !passwordsMatch || !isIdUnique || isCheckingId);
    }, [oldLoginId, newLoginId, oldPassword, newPassword, confirmPassword, policy, validatePassword, isIdUnique, isCheckingId]);

    // Reset form when dialog opens
    useEffect(() => {
        if (open) {
            setOldLoginId(initialOldLoginId);
            setNewLoginId("");
            setOldPassword("");
            setNewPassword("");
            setConfirmPassword("");
            setErrors({});
            setIsButtonDisabled(true);
            setIdCheckMessage(null);
            setIsIdUnique(false);
            setIdCheckPerformed(false);
            setShowOldPassword(false);
            setShowNewPassword(false);
            setShowConfirmPassword(false);
            setOldLoginIdTouched(false);
            setNewLoginIdTouched(false);
            setOldPasswordTouched(false);
            setNewPasswordTouched(false);
            setConfirmPasswordTouched(false);
            setSubmitError(null);
            setValidationResults([]);
            setValidationSummary({
                isValid: false,
                passedCount: 0,
                totalCount: 0,
                failedRules: []
            });
            setShowNewPasswordRequirements(false);
        }
    }, [open, initialOldLoginId]);

    // Validate form on changes - only validate fields that have been touched
    useEffect(() => {
        validate(false);
    }, [oldLoginId, newLoginId, oldPassword, newPassword, confirmPassword, oldLoginIdTouched, newLoginIdTouched, oldPasswordTouched, newPasswordTouched, confirmPasswordTouched]);

    if (isPolicyLoading) {
        return (
            <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm" sx={{ maxWidth: 500, margin: "0 auto" }}>
                <DialogContent sx={{ textAlign: 'center', py: 4 }}>
                    <Typography>Loading password policy...</Typography>
                </DialogContent>
            </Dialog>
        );
    }

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm" sx={{ maxWidth: 500, margin: "0 auto" }} disableEscapeKeyDown={isSubmitting}>
            <Box sx={{ px: 2 }}>
                <DialogTitle sx={{ p: 0, pt: 2, fontWeight: 700 }}>
                    Change ID and Password
                </DialogTitle>
                <Box sx={{ height: "1px", backgroundColor: "var(--G40, #BFBFBF)", width: "100%", mt: 1 }} />
            </Box>

            <DialogContent sx={{ px: 2, pt: 3, position: 'relative' }}>
                {isSubmitting && (
                    <Box
                        sx={{
                            position: 'absolute',
                            top: 0,
                            left: 0,
                            right: 0,
                            bottom: 0,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            justifyContent: 'center',
                            backgroundColor: 'rgba(255, 255, 255, 0.9)',
                            zIndex: 10,
                            borderRadius: '4px'
                        }}
                    >
                        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                            <Box
                                sx={{
                                    width: 48,
                                    height: 48,
                                    border: '4px solid #f3f3f3',
                                    borderTop: '4px solid #FF8400',
                                    borderRadius: '50%',
                                    animation: 'spin 1s linear infinite',
                                    '@keyframes spin': {
                                        '0%': { transform: 'rotate(0deg)' },
                                        '100%': { transform: 'rotate(360deg)' },
                                    },
                                }}
                            />
                            <Typography sx={{ fontSize: '16px', fontWeight: 500, color: '#333' }}>
                                Updating your information...
                            </Typography>
                        </Box>
                    </Box>
                )}

                {/* Error Alert */}
                {submitError && (
                    <Alert severity="error" sx={{ mb: 2 }} onClose={() => setSubmitError(null)}>
                        {submitError}
                    </Alert>
                )}

                {/* Message */}
                <Alert
                    severity="info"
                    sx={{ mb: 2 }}
                    variant="outlined"
                    icon={<Shield sx={{ color: 'info.main' }} />}
                >
                    Welcome! For security reasons, please change your initial ID and password.
                </Alert>

                {/* Current Login ID */}
                <TextField
                    fullWidth
                    label="Current ID *"
                    variant="outlined"
                    margin="normal"
                    value={oldLoginId}
                    error={!!errors.oldLoginId}
                    helperText={errors.oldLoginId}
                    disabled
                    slotProps={{
                        input: {
                            sx: { backgroundColor: '#f5f5f5' }
                        }
                    }}
                />

                {/* New Login ID with Check Availability Button */}
                <Box sx={{ display: "flex", gap: 1, alignItems: "flex-start", mt: 2 }}>
                    <TextField
                        fullWidth
                        label="New ID *"
                        variant="outlined"
                        value={newLoginId}
                        onChange={handleNewLoginIdChange}
                        onBlur={() => setNewLoginIdTouched(true)}
                        error={!!errors.newLoginId}
                        helperText={errors.newLoginId}
                        disabled={isSubmitting}
                        sx={{ mt: 0 }}
                    />
                    <Button
                        variant="outlined"
                        onClick={checkIdUniqueness}
                        disabled={isCheckingId || !newLoginId.trim() || isSubmitting}
                        sx={{
                            height: "56px",
                            whiteSpace: "nowrap",
                            flexShrink: 0,
                            px: 2,
                            mt: 0
                        }}
                    >
                        {isCheckingId ? "Checking..." : "Check Availability"}
                    </Button>
                </Box>

                {/* ID Check Message */}
                {idCheckMessage && (
                    <Typography
                        variant="caption"
                        sx={{
                            display: "block",
                            mt: 0.5,
                            mb: 1,
                            color: isIdUnique ? "success.main" : "error.main",
                            fontWeight: 500
                        }}
                    >
                        {idCheckMessage}
                    </Typography>
                )}

                {/* Current Password */}
                <TextField
                    fullWidth
                    label="Current Password *"
                    type={showOldPassword ? "text" : "password"}
                    variant="outlined"
                    margin="normal"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                    onBlur={() => setOldPasswordTouched(true)}
                    error={!!errors.oldPassword}
                    helperText={errors.oldPassword}
                    disabled={isSubmitting}
                    InputProps={{
                        endAdornment: (
                            <IconButton
                                aria-label="toggle password visibility"
                                onClick={() => setShowOldPassword(!showOldPassword)}
                                edge="end"
                                disabled={isSubmitting}
                            >
                                {showOldPassword ? <VisibilityOff /> : <Visibility />}
                            </IconButton>
                        ),
                    }}
                />

                {/* New Password */}
                <TextField
                    id="new-password-change"
                    fullWidth
                    label="New Password *"
                    type={showNewPassword ? "text" : "password"}
                    variant="outlined"
                    margin="normal"
                    value={newPassword}
                    onChange={handleNewPasswordChange}
                    onBlur={() => setNewPasswordTouched(true)}
                    error={!!errors.newPassword}
                    helperText={errors.newPassword}
                    disabled={isSubmitting}
                    InputProps={{
                        endAdornment: (
                            <IconButton
                                aria-label="toggle password visibility"
                                onClick={() => setShowNewPassword(!showNewPassword)}
                                edge="end"
                                disabled={isSubmitting}
                            >
                                {showNewPassword ? <VisibilityOff /> : <Visibility />}
                            </IconButton>
                        ),
                    }}
                />

                {/* Password Requirements Toggle */}
                {newPassword && policy && (
                    <Box sx={{ mt: 1 }}>
                        <Button
                            size="small"
                            variant="text"
                            color="inherit"
                            startIcon={<Info fontSize="small" />}
                            endIcon={showNewPasswordRequirements ? <ExpandLess /> : <ExpandMore />}
                            onClick={() => setShowNewPasswordRequirements(!showNewPasswordRequirements)}
                            disabled={isSubmitting}
                            sx={{
                                textTransform: 'none',
                                fontSize: '0.875rem',
                                color: 'text.secondary',
                                p: 0.5,
                                minWidth: 'auto'
                            }}
                        >
                            Requirements ({validationSummary.passedCount}/{validationSummary.totalCount})
                        </Button>

                        {/* Password Requirements List */}
                        <Collapse in={showNewPasswordRequirements}>
                            <Box sx={{
                                mt: 1,
                                backgroundColor: 'rgba(0,0,0,0.02)',
                                borderRadius: 1,
                                border: '1px solid rgba(0,0,0,0.1)'
                            }}>
                                <List dense sx={{ py: 1 }}>
                                    {validationResults.map((result, index) => (
                                        <ListItem key={index} sx={{ py: 0.25, px: 2 }}>
                                            <ListItemIcon sx={{ minWidth: 32 }}>
                                                {result.isValid ? (
                                                    <Check
                                                        fontSize="small"
                                                        sx={{ color: 'success.main' }}
                                                    />
                                                ) : (
                                                    <Close
                                                        fontSize="small"
                                                        sx={{ color: 'text.disabled' }}
                                                    />
                                                )}
                                            </ListItemIcon>
                                            <ListItemText
                                                primary={result.message}
                                                slotProps={{
                                                    primary: {
                                                        variant: 'body2',
                                                        sx: {
                                                            color: result.isValid ? 'success.main' : 'text.secondary',
                                                            fontSize: '0.875rem'
                                                        }
                                                    }
                                                }}
                                            />
                                        </ListItem>
                                    ))}
                                </List>
                            </Box>
                        </Collapse>
                    </Box>
                )}

                {/* Confirm Password */}
                <TextField
                    fullWidth
                    label="Confirm Password *"
                    type={showConfirmPassword ? "text" : "password"}
                    variant="outlined"
                    margin="normal"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    onBlur={() => setConfirmPasswordTouched(true)}
                    error={!!errors.confirmPassword}
                    helperText={errors.confirmPassword}
                    disabled={isSubmitting}
                    InputProps={{
                        endAdornment: (
                            <IconButton
                                aria-label="toggle password visibility"
                                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                edge="end"
                                disabled={isSubmitting}
                            >
                                {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                            </IconButton>
                        ),
                    }}
                />
            </DialogContent>

            <DialogActions sx={{ px: 2, pt: 0, display: "flex", gap: 2, mt: 0 }}>
                <Button
                    variant="outlined"
                    onClick={onClose}
                    color="primary"
                    sx={{ flexGrow: 1, height: "48px" }}
                    disabled={isSubmitting}
                >
                    Cancel
                </Button>
                <Button
                    variant="contained"
                    onClick={handleConfirm}
                    color="primary"
                    disabled={isButtonDisabled || isSubmitting}
                    sx={{ flexGrow: 1, height: "48px" }}
                >
                    {isSubmitting ? "Updating..." : "Change ID and Password"}
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default ChangeIdAndPasswordDialog;
