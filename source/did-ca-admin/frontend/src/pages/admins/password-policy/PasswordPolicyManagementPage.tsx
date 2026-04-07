import React, {useState, useEffect, useMemo} from 'react';
import { registerAdminPasswordPolicy } from '../../../apis/admin-api';
import {Box, Button, SelectChangeEvent, styled, TextField, Typography, FormControl, InputLabel, OutlinedInput, FormHelperText, Switch} from "@mui/material";
import {useNavigate} from 'react-router';
import {useDialogs} from '@toolpad/core/useDialogs';
import FullscreenLoader from "../../../components/loading/FullscreenLoader";
import {formatErrorMessage} from "../../../utils/error-handler";
import CustomConfirmDialog from "../../../components/dialog/CustomConfirmDialog";
import CustomDialog from "../../../components/dialog/CustomDialog";
import { usePasswordPolicy } from '../../../hooks/usePasswordPolicy';

type Props = {}

interface FormData {
    minLength?: number;
    requireUppercase?: boolean;
    requireNumber?: boolean;
    requireSpecial?: boolean;
    passwordExpiryDays?: number;
}

interface ErrorState {
    minLength?: string;
    requireUppercase?: string;
    requireNumber?: string;
    requireSpecial?: string;
    passwordExpiryDays?: string;
}

const PasswordPolicyManagementPage = (props: Props) => {
    const navigate = useNavigate();
    const dialogs = useDialogs();
    const [formData, setFormData] = useState<FormData>({
        minLength: undefined,
        requireUppercase: false,
        requireNumber: false,
        requireSpecial: false,
        passwordExpiryDays: undefined,
    });
    const [initialData, setInitialData] = useState<FormData>({
        minLength: undefined,
        requireUppercase: false,
        requireNumber: false,
        requireSpecial: false,
        passwordExpiryDays: undefined,
    });
    const [errors, setErrors] = useState<ErrorState>({});
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [isEditMode, setIsEditMode] = useState(false);

    // Password policy context for global state and data
    const { policy, isLoading, error, updatePolicy } = usePasswordPolicy();

    const handleChange = (field: keyof FormData) =>
        (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => {
            const target = event.target as HTMLInputElement;
            let newValue: any;

            if (target.type === 'checkbox') {
                newValue = target.checked;
            } else if (target.type === 'number') {
                newValue = target.value === '' ? undefined : Number(target.value);
            } else {
                newValue = target.value;
            }

            setFormData((prev) => ({ ...prev, [field]: newValue }));
        };

    const handleSwitchChange = (field: keyof FormData) =>
        (event: React.ChangeEvent<HTMLInputElement>) => {
            setFormData((prev) => ({ ...prev, [field]: event.target.checked }));
        };

    const handleReset = () => {
        setFormData(initialData);
        setIsButtonDisabled(true);
        setErrors({});
    };

    const validate = () => {
        let tempErrors: ErrorState = {};

        tempErrors.minLength = validateMinLength(formData.minLength);
        tempErrors.passwordExpiryDays = validatePasswordExpiryDays(formData.passwordExpiryDays);
        tempErrors.requireUppercase = validateRequireUppercase(formData.requireUppercase);
        tempErrors.requireNumber = validateRequireNumber(formData.requireNumber);
        tempErrors.requireSpecial = validateRequireSpecial(formData.requireSpecial);

        setErrors(tempErrors);
        return Object.values(tempErrors).every((error) => !error);
    };

    const validateMinLength = (minLength?: number): string | undefined => {
        if (!minLength) return 'Please enter minimum length.';
        if (minLength < 6 || minLength > 20) return 'Minimum length must be between 6 and 20 characters.';
        return undefined;
    };

    const validatePasswordExpiryDays = (passwordExpiryDays?: number): string | undefined => {
        if (!passwordExpiryDays) return 'Please enter password expiration period.';
        if (passwordExpiryDays < 30 || passwordExpiryDays > 365) return 'Password expiration period must be between 30 and 365 days.';
        return undefined;
    };

    const validateRequireUppercase = (requireUppercase?: boolean): string | undefined => {
        if (requireUppercase === undefined) return 'Please select a value.';
        return undefined;
    };

    const validateRequireNumber = (requireNumber?: boolean): string | undefined => {
        if (requireNumber === undefined) return 'Please select a value.';
        return undefined;
    };

    const validateRequireSpecial = (requireSpecial?: boolean): string | undefined => {
        if (requireSpecial === undefined) return 'Please select a value.';
        return undefined;
    };

    const handleSubmit = async () => {
        if (!validate()) return;

        const result = await dialogs.open(CustomConfirmDialog, {
            title: 'Confirmation',
            message: 'Are you sure you want to register Password Policy Settings?',
            isModal: true,
        });

        if (result) {
            try {
                const response = await registerAdminPasswordPolicy(formData);

                if (response.data) {
                    setInitialData(formData);

                    // Update global password policy state
                    // Convert FormData to PasswordPolicy format
                    const updatedPolicy = {
                        minLength: formData.minLength || 8,
                        maxLength: 64, // Use a reasonable default
                        requireUppercase: formData.requireUppercase || false,
                        requireLowercase: false, // This field doesn't exist in the form, use default
                        requireNumber: formData.requireNumber || false,
                        requireSpecial: formData.requireSpecial || false,
                        passwordExpiryDays: formData.passwordExpiryDays || 90,
                        preventReuse: 0, // This field doesn't exist in the form, use default
                        maxAttempts: 5, // This field doesn't exist in the form, use default
                        lockoutDuration: 30, // This field doesn't exist in the form, use default
                    };

                    updatePolicy(updatedPolicy);
                }

                await dialogs.open(CustomDialog, {
                    title: 'Notification',
                    message: 'Completed password policy registration.',
                    isModal: true,
                });

            } catch (error) {
                console.error('Password policy registration failed:', error);
                await dialogs.open(CustomDialog, {
                    title: 'Notification',
                    message: `Failed to register Password Policy: ${formatErrorMessage(error, 'Registration failed')}`,
                    isModal: true,
                });
            }
        }
    };

    // Use context data when available
    useEffect(() => {
        if (policy && !isLoading && !error) {
            const contextData = {
                minLength: policy.minLength,
                requireUppercase: policy.requireUppercase,
                requireNumber: policy.requireNumber,
                requireSpecial: policy.requireSpecial,
                passwordExpiryDays: policy.passwordExpiryDays,
            };

            setFormData(contextData);
            setInitialData(contextData);
            setIsEditMode(true);
        } else if (error && !isLoading) {
            // Handle error from context (e.g., server down, API error)
            console.warn('Password policy context error:', error);
            // Still allow form to be used for registration
        }
    }, [policy, isLoading, error]);

    useEffect(() => {
        const isModified = Object.keys(formData).some(
            (key) => formData[key as keyof FormData] !== initialData[key as keyof FormData]
        );
        setIsButtonDisabled(!isModified);
    }, [formData, initialData]);

    const StyledContainer = useMemo(() => styled(Box)(({theme}) => ({
        width: 600,
        margin: 'auto',
        marginTop: theme.spacing(1),
        padding: theme.spacing(3),
        border: 'none',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: '#ffffff',
        boxShadow: '0px 4px 8px 0px #0000001A',
    })), []);

    const StyledSubTitle = useMemo(() => styled(Typography)({
        textAlign: 'left',
        fontSize: '24px',
        fontWeight: 700,
    }), []);

    const StyledSectionTitle = useMemo(() => styled(Typography)(({theme}) => ({
        textAlign: 'left',
        fontSize: '18px',
        fontWeight: 600,
        color: '#1976d2',
        marginTop: theme.spacing(3),
        marginBottom: theme.spacing(1),
        paddingLeft: theme.spacing(1),
        borderLeft: `4px solid #1976d2`,
    })), []);

    const StyledDescription = useMemo(() => styled(Box)(({theme}) => ({
        maxWidth: 600,
        marginTop: theme.spacing(1),
        padding: theme.spacing(0),
    })), []);

    const StyledInputArea = useMemo(() => styled(Box)(({theme}) => ({
        marginTop: theme.spacing(2),
    })), []);

    return <>
        <FullscreenLoader open={isLoading}/>
        <StyledContainer>
            <StyledSubTitle>Password Policy Settings</StyledSubTitle>

            <StyledInputArea>
                {/* Basic Settings Section */}
                <StyledSectionTitle>Basic Settings</StyledSectionTitle>

                <TextField
                    fullWidth
                    label="Minimum Length"
                    variant="outlined"
                    margin="normal"
                    type="number"
                    value={formData.minLength || ''}
                    onChange={handleChange('minLength')}
                    error={!!errors.minLength}
                    helperText={errors.minLength || "Password minimum length must be set (6-20 characters)"}
                    inputProps={{ min: 6, max: 20 }}
                />

                <TextField
                    fullWidth
                    label="Password Expiration Period"
                    variant="outlined"
                    margin="normal"
                    type="number"
                    value={formData.passwordExpiryDays || ''}
                    onChange={handleChange('passwordExpiryDays')}
                    error={!!errors.passwordExpiryDays}
                    helperText={errors.passwordExpiryDays || "Password expiration period must be set in days (30-365 days)"}
                    inputProps={{ min: 30, max: 365 }}
                />

                {/* Password Composition Rules Section */}
                <StyledSectionTitle>Password Composition Rules</StyledSectionTitle>

                <FormControl fullWidth variant="outlined" sx={{ mt: 2 }} error={!!errors.requireUppercase}>
                    <InputLabel shrink>Uppercase Letters (A-Z)</InputLabel>
                    <OutlinedInput
                        notched
                        label="Uppercase Letters (A-Z)"
                        startAdornment={
                            <Switch
                                checked={formData.requireUppercase ?? false}
                                onChange={handleSwitchChange("requireUppercase")}
                                color="primary"
                                sx={{ transform: "scale(1.2)", mr: 1 }}
                            />
                        }
                    />
                    {errors.requireUppercase && <FormHelperText>{errors.requireUppercase}</FormHelperText>}
                </FormControl>

                <FormControl fullWidth variant="outlined" sx={{ mt: 2 }} error={!!errors.requireNumber}>
                    <InputLabel shrink>Numbers (0-9)</InputLabel>
                    <OutlinedInput
                        notched
                        label="Numbers (0-9)"
                        startAdornment={
                            <Switch
                                checked={formData.requireNumber ?? false}
                                onChange={handleSwitchChange("requireNumber")}
                                color="primary"
                                sx={{ transform: "scale(1.2)", mr: 1 }}
                            />
                        }
                    />
                    {errors.requireNumber && <FormHelperText>{errors.requireNumber}</FormHelperText>}
                </FormControl>

                <FormControl fullWidth variant="outlined" sx={{ mt: 2 }} error={!!errors.requireSpecial}>
                    <InputLabel shrink>Special Characters (!@#$%^&*)</InputLabel>
                    <OutlinedInput
                        notched
                        label="Special Characters (!@#$%^&*)"
                        startAdornment={
                            <Switch
                                checked={formData.requireSpecial ?? false}
                                onChange={handleSwitchChange("requireSpecial")}
                                color="primary"
                                sx={{ transform: "scale(1.2)", mr: 1 }}
                            />
                        }
                    />
                    {errors.requireSpecial && <FormHelperText>{errors.requireSpecial}</FormHelperText>}
                </FormControl>

                <Box sx={{display: 'flex', justifyContent: 'center', gap: 2, mt: 4}}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleSubmit}
                        disabled={isButtonDisabled}
                    >
                        {isEditMode ? 'Update' : 'Register'}
                    </Button>
                    <Button variant="contained" color="secondary" onClick={handleReset}>
                        Reset
                    </Button>
                </Box>

            </StyledInputArea>
        </StyledContainer>
    </>
}

export default PasswordPolicyManagementPage
