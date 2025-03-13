import { useDialogs } from '@toolpad/core';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router';
import { Box, Button, Popover, TextField, Typography, useTheme } from '@mui/material';
import CustomDialog from '../../components/dialog/CustomDialog';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import { getUserInfo } from '../../apis/user-api';
import { formatErrorMessage } from '../../utils/error-handler';

type Props = {}

type UserFormData = {
    userId: string;
    pii: string;
    createdAt?: string;
    updatedAt?: string;
};

const UserDetailPage = (props: Props) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const dialogs = useDialogs();
    const theme = useTheme();

    const numericId = id ? parseInt(id, 10) : null;
    const [isLoading, setIsLoading] = useState<boolean>(true); 
    const [formData, serFormData] = useState<UserFormData>({
        userId: '',
        pii: '',
        createdAt: '',
        updatedAt: '',
    });

    useEffect(() => {
        const fetchData = async () => {
            if (numericId === null || isNaN(numericId)) {
                await dialogs.open(CustomDialog, { 
                    title: 'Notification', 
                    message: 'Invalid Path.', 
                    isModal: true 
                },{
                    onClose: async () => navigate('/list-settings/allowed-ca', { replace: true }),
                });
                return;
            }

            setIsLoading(true);

            try {
                const { data } = await getUserInfo(numericId);
                serFormData({
                    userId: data.userId,
                    pii: data.pii,
                    createdAt: data.createdAt,
                    updatedAt: data.updatedAt,
                });
                setIsLoading(false);
            } catch (err) {
                  console.error('Failed to fetch User information:', err);
                  setIsLoading(false);
                  navigate('/error', { state: { message: formatErrorMessage(err, "Failed to fetch User information") } });
            }
        };
        fetchData();
    }, [numericId]);

    return (
        <>
            <FullscreenLoader open={isLoading} />
            <Box sx={{ p: 3 }}>
                <Typography variant="h4">User Detail Information</Typography>
                <Box sx={{ maxWidth: 500, margin: 'auto', mt: 2, p: 3, border: '1px solid #ccc', borderRadius: 2 }}>
                    <TextField 
                        fullWidth
                        label="ID" 
                        variant="standard"
                        margin="normal" 
                        value={formData.userId || ''} 
                        slotProps={{ input: { readOnly: true } }} 
                    />

                    <TextField 
                        fullWidth
                        label="PII" 
                        variant="standard"
                        margin="normal" 
                        value={formData.pii || ''} 
                        slotProps={{ input: { readOnly: true } }} 
                    />

                    <TextField 
                        fullWidth 
                        label="Registered At" 
                        variant="standard" 
                        margin="normal" 
                        value={formData.createdAt || ''} 
                        slotProps={{ input: { readOnly: true } }} 
                    />

                    {formData.updatedAt && (
                        <TextField 
                            fullWidth 
                            label="Updated At" 
                            variant="standard" 
                            margin="normal" 
                            value={formData.updatedAt} 
                            slotProps={{ input: { readOnly: true } }} 
                        />
                    )}

                     <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 3 }}>
                        <Button variant="contained" color="secondary" onClick={() => navigate('/user-management')}>
                            Back
                        </Button>
                    </Box>
                </Box>
            </Box>
        </>
    )
}

export default UserDetailPage