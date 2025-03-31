import { useDialogs } from '@toolpad/core';
import React, { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import { Box, Button, FormControl, FormHelperText, IconButton, InputLabel, Paper, Select, SelectChangeEvent, styled, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from '@mui/material';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import DeleteIcon from '@mui/icons-material/Delete';
import CustomConfirmDialog from '../../components/dialog/CustomConfirmDialog';
import CustomDialog from '../../components/dialog/CustomDialog';
import { verifyAdminIdUnique, registerAdmin } from '../../apis/admin-api';
import { emailRegex } from '../../utils/regex';
import { sha256Hash } from '../../utils/sha256-hash';

type Props = {}

type AdminFormData = {
    loginId: string;
    role: string;
    loginPassword: string;
    confirmPassword: string;
};

interface ErrorState {
    loginId?: string;
    role?: string[];
    loginPassword?: string;
    confirmPassword?: string;
}

const AdminRegisterPage = (props: Props) => {
    const navigate = useNavigate();
    const dialogs = useDialogs();

    const [formData, setFormData] = useState<AdminFormData>({
        loginId: '',
        role: 'NORMAL',
        loginPassword: '',
        confirmPassword: '',
    });

    const [errors, setErrors] = useState<ErrorState>({});
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [isLoading, setIsLoading] = useState(false);
    const [isLoginIdIsValid, setIsLoginIdIsValid] = useState(false);

    const handleChange = (field: keyof AdminFormData) => 
        (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>) => {
            const newValue = event.target.value;
            setFormData((prev) => ({ ...prev, [field]: newValue }));

            if (field === 'loginId') {
                setIsLoginIdIsValid(false);
                setErrors((prev) => ({ ...prev, loginId: undefined }));
            }
    };

    const handleReset = () => {
        setErrors({});
        setIsButtonDisabled(true);
        setFormData({ loginId: '', role: 'NORMAL', loginPassword: '', confirmPassword: '' });
        setIsLoginIdIsValid(false);
    };

    const validate = () => {
        let tempErrors: ErrorState = {};
        tempErrors.loginId = validateLoginId(formData.loginId);
        tempErrors.loginPassword = validateLoginPassword(formData.loginPassword);
        tempErrors.confirmPassword = validateConfirmPassword(formData.confirmPassword);

        setErrors(tempErrors);
        return Object.values(tempErrors).every((error) => !error);
    };

    const validateLoginId = (loginId?: string): string | undefined => {
        if (!loginId) return 'Please enter a Login ID.';
        if (loginId.length < 3 || loginId.length > 30) return 'Login ID must be between 3 and 30 characters.';
        if (!emailRegex.test(loginId)) return 'Please enter a valid email address.';
        if (!isLoginIdIsValid) return 'Please check for duplicate Login ID.';
        return undefined;
    };

    const validateLoginPassword = (loginPassword?: string): string | undefined => {
        if (!loginPassword) return 'Please enter a Login Password.';
        if (loginPassword.length < 8 || loginPassword.length > 30) return 'Login ID must be between 8 and 30 characters.';
        return undefined;
    };

    const validateConfirmPassword = (confirmPassword?: string): string | undefined => {
        if (!confirmPassword) return 'Please re-enter the Login Password.';
        if (confirmPassword !== formData.loginPassword) return 'Passwords do not match.';
        return undefined;
    };

    const handleSubmit = async () => {
        if (!validate()) return;

        const result = await dialogs.open(CustomConfirmDialog, {
            title: 'Confirmation',
            message: 'Are you sure you want to register Admin?',
            isModal: true,
        });

        if (result) {
            setIsLoading(true);

            const hashedPassword = await sha256Hash(formData.loginPassword);
            let requestObject = {
                loginId: formData.loginId,
                loginPassword: hashedPassword,
                role: formData.role,
            }

            await registerAdmin(requestObject).then((response) => {
                setIsLoading(false);
                dialogs.open(CustomDialog, {
                    title: 'Notification',
                    message: 'Admin registration completed.',
                    isModal: true,
                },{
                    onClose: async (result) =>  navigate('/admin-management'),
                });
    
            }).catch((error) => {
                setIsLoading(false);
                dialogs.open(CustomDialog, {
                    title: 'Notification',
                    message: `Failed to register Admin: ${error}`,
                    isModal: true,
                });
            });
        }
    };

    const validateOnlyLoginId = () => {
        let tempErrors: ErrorState = {};
     
        if (!formData.loginId)  tempErrors.loginId = 'Please enter a Login ID.';
        if (formData.loginId.length < 3 || formData.loginId.length > 30) tempErrors.loginId = 'Login ID must be between 3 and 30 characters.';
        if (!emailRegex.test(formData.loginId)) tempErrors.loginId = 'Please enter a valid email address.';

        setErrors(tempErrors);
        return Object.values(tempErrors).every((error) => !error);
    };

    const handleCheckDuplicateLoginId = () => {
        if (!validateOnlyLoginId()) return;
        verifyAdminIdUnique(formData.loginId as string)
        .then((response) => {
            if (response.data.unique === false) {
                setErrors((prev) => ({ ...prev, loginId: 'Login ID already exists.' }));
                setIsLoginIdIsValid(false);
            } else {        
                setIsLoginIdIsValid(true);
                setErrors((prev) => ({ ...prev, loginId: undefined }));
            }
        });
    };

    const handleCancel = async () => {
        const result = await dialogs.open(CustomConfirmDialog, {
            title: 'Confirmation',
            message: 'Are you sure you want to cancel admin registration?',
            isModal: true,
        });

        if (result) {
            navigate('/admin-management');
        }
    };

    useEffect(() => {
        const isModified = Object.entries(formData).some(([key, value]) => {
            if (key === 'role') return false;
            if (Array.isArray(value)) return value.length > 0;
            return value !== '' && value !== undefined;
        });
        setIsButtonDisabled(!isModified);
    }, [formData]);

    const StyledContainer = useMemo(() => styled(Box)(({ theme }) => ({
        width: 500,
        margin: 'auto',
        marginTop: theme.spacing(3),
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

    const StyledDescription = useMemo(() => styled(Box)(({ theme }) => ({
        maxWidth: 500, 
        marginTop: theme.spacing(1),
        padding: theme.spacing(0),
    })), []);

    const StyledInputArea = useMemo(() => styled(Box)(({ theme }) => ({
        marginTop: theme.spacing(2),
    })), []);

    return (
        <>
            <FullscreenLoader open={isLoading} />
            <Typography variant="h4">Admin Management</Typography>
            <StyledContainer>
                <StyledSubTitle>Admin Registration</StyledSubTitle>
                <StyledInputArea>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        <TextField 
                            fullWidth
                            label="ID *" 
                            variant="outlined"
                            margin="normal" 
                            size="medium"
                            type='email'
                            value={formData.loginId} 
                            onChange={handleChange('loginId')} 
                            error={!!errors.loginId} 
                            helperText={errors.loginId} 
                            sx={{minWidth: 250}}
                        />
                        <Button 
                            variant="outlined" 
                            onClick={handleCheckDuplicateLoginId}
                            disabled={!formData.loginId}
                            sx={{ 
                                minWidth: 150,  
                                whiteSpace: 'nowrap', 
                                textTransform: 'none' 
                            }}
                        >
                            Check Availability
                        </Button>                        
                    </Box>

                    <FormControl fullWidth margin="normal">
                        <InputLabel>Role *</InputLabel>
                        <Select 
                            value={formData.role} 
                            onChange={() => {}}
                            label="Role"
                            slotProps={{ input: { readOnly: true } }} 
                        >
                            <option value="NORMAL">Normal Admin</option>
                        </Select>
                    </FormControl>

                    <TextField 
                        fullWidth
                        label="Password *"
                        type="password" 
                        variant="outlined"
                        margin="normal" 
                        size="medium"
                        value={formData.loginPassword} 
                        onChange={handleChange('loginPassword')} 
                        error={!!errors.loginPassword} 
                        helperText={errors.loginPassword} 
                    />

                    <TextField 
                        fullWidth
                        label="Re-enter Password *"
                        type="password"
                        variant="outlined"
                        margin="normal" 
                        size="medium"
                        value={formData.confirmPassword} 
                        onChange={handleChange('confirmPassword')} 
                        error={!!errors.confirmPassword} 
                        helperText={errors.confirmPassword} 
                    />

                    <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 3 }}>
                        <Button variant="contained" color="primary" onClick={handleSubmit} disabled={isButtonDisabled}>Register</Button>
                        <Button variant="contained" color="secondary" onClick={handleReset}>Reset</Button>
                        <Button variant="outlined" color="primary" onClick={handleCancel}>Cancel</Button>
                    </Box>
                </StyledInputArea>
            </StyledContainer>
        </>
    )
}

export default AdminRegisterPage