import React, { useState, useEffect, useMemo } from 'react';
import {
  Box,
  Button,
  Typography,
  FormControl,
  InputLabel,
  OutlinedInput,
  FormHelperText,
  styled,
} from '@mui/material';
import { useDialogs } from '@toolpad/core';
import CustomDialog from '../../components/dialog/CustomDialog';
import CustomConfirmDialog from '../../components/dialog/CustomConfirmDialog';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import { getAllServerConfigs, updateServerConfigs, ServerConfigDto, UpdateServerConfigReqDto } from '../../apis/server-api';
import { formatErrorMessage } from '../../utils/error-handler';

interface FormData {
  API_KEY_LSS?: string;
}

interface ErrorState {
  API_KEY_LSS?: string;
}

const ServerConfigurationPage: React.FC = () => {
  const dialogs = useDialogs();
  const [formData, setFormData] = useState<FormData>({
    API_KEY_LSS: '',
  });
  const [initialData, setInitialData] = useState<FormData>({
    API_KEY_LSS: '',
  });
  const [errors, setErrors] = useState<ErrorState>({});
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  const [isLoading, setIsLoading] = useState(true);
  const [configDescriptions, setConfigDescriptions] = useState<Record<string, string>>({});

  useEffect(() => {
    loadServerConfigs();
  }, []);

  useEffect(() => {
    const isModified = Object.keys(formData).some(
      (key) => formData[key as keyof FormData] !== initialData[key as keyof FormData]
    );
    setIsButtonDisabled(!isModified);
  }, [formData, initialData]);

  const loadServerConfigs = async () => {
    setIsLoading(true);
    try {
      const response = await getAllServerConfigs();
      const configs = response.data;

      const formDataFromConfigs: FormData = {};
      const descriptions: Record<string, string> = {};

      configs.forEach((config: ServerConfigDto) => {
        if (config.configKey in formData) {
          formDataFromConfigs[config.configKey as keyof FormData] = config.configValue || '';
          descriptions[config.configKey] = config.description || '';
        }
      });

      setFormData(formDataFromConfigs);
      setInitialData(formDataFromConfigs);
      setConfigDescriptions(descriptions);
    } catch (error) {
      console.error('Failed to load server configuration:', error);
      await dialogs.open(CustomDialog, {
        title: 'Error',
        message: `Failed to load server configuration: ${formatErrorMessage(error, 'Load failed')}`,
        isModal: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (field: keyof FormData) =>
    (event: React.ChangeEvent<HTMLInputElement>) => {
      const value = event.target.value;
      setFormData((prev) => ({ ...prev, [field]: value }));
      if (errors[field]) {
        setErrors((prev) => ({ ...prev, [field]: undefined }));
      }
    };

  const handleReset = () => {
    setFormData(initialData);
    setIsButtonDisabled(true);
    setErrors({});
  };

  const validate = () => {
    let tempErrors: ErrorState = {};
    if (!formData.API_KEY_LSS || formData.API_KEY_LSS.trim().length === 0) {
      tempErrors.API_KEY_LSS = 'Invalid input value.';
    }
    setErrors(tempErrors);
    return Object.values(tempErrors).every((error) => !error);
  };

  const handleSubmit = async () => {
    if (!validate()) return;

    const result = await dialogs.open(CustomConfirmDialog, {
      title: 'Would you like to change the server configuration?',
      message: 'Server configuration has been updated successfully.',
      isModal: true,
    });

    if (result) {
      setIsLoading(true);
      try {
        const updateRequest: UpdateServerConfigReqDto = {
          configs: Object.entries(formData)
            .filter(([_, value]) => value !== undefined && value !== '')
            .map(([key, value]) => ({
              configKey: key,
              configValue: value as string,
              description: configDescriptions[key],
            }))
        };

        const response = await updateServerConfigs(updateRequest);

        if (response.data) {
          setInitialData(formData);
        }

        setIsLoading(false);
        await dialogs.open(CustomDialog, {
          title: 'Notification',
          message: 'Server configuration has been updated successfully.',
          isModal: true,
        });

      } catch (error) {
        console.error('Server configuration update failed:', error);
        setIsLoading(false);
        await dialogs.open(CustomDialog, {
          title: 'Notification',
          message: `Failed to update server configuration: ${formatErrorMessage(error, 'Update failed')}`,
          isModal: true,
        });
      } finally {
        setIsLoading(false);
      }
    }
  };

  const StyledContainer = useMemo(() => styled(Box)(({ theme }) => ({
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

  const StyledSectionTitle = useMemo(() => styled(Typography)(({ theme }) => ({
    textAlign: 'left',
    fontSize: '18px',
    fontWeight: 600,
    color: '#1976d2',
    marginTop: theme.spacing(3),
    marginBottom: theme.spacing(1),
    paddingLeft: theme.spacing(1),
    borderLeft: `4px solid #1976d2`,
  })), []);

  const StyledInputArea = useMemo(() => styled(Box)(({ theme }) => ({
    marginTop: theme.spacing(2),
  })), []);

  return (
    <>
      <FullscreenLoader open={isLoading} />
      <StyledContainer>
        <StyledSubTitle>Server Configuration</StyledSubTitle>

        <StyledInputArea>
          <StyledSectionTitle>API Key Settings</StyledSectionTitle>

          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Set an API Key to connect with external servers.
          </Typography>

          <FormControl fullWidth variant="outlined" sx={{ mt: 2 }} error={!!errors.API_KEY_LSS}>
            <InputLabel shrink>Ledger Service Server</InputLabel>
            <OutlinedInput
              notched
              label="Ledger Service Server"
              value={formData.API_KEY_LSS || ''}
              onChange={handleInputChange('API_KEY_LSS')}
              disabled={isLoading}
              placeholder="Enter LSS API Key"
              type="text"
            />
            {errors.API_KEY_LSS && (
              <FormHelperText error>
                {errors.API_KEY_LSS}
              </FormHelperText>
            )}
          </FormControl>

          <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 4 }}>
            <Button
              variant="contained"
              color="primary"
              onClick={handleSubmit}
              disabled={isButtonDisabled || isLoading}
            >
              UPDATE
            </Button>
            <Button
              variant="contained"
              color="secondary"
              onClick={handleReset}
              disabled={isLoading}
            >
              RESET
            </Button>
          </Box>
        </StyledInputArea>
      </StyledContainer>
    </>
  );
};

export default ServerConfigurationPage;
