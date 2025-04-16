import { Box, Button, Card, CardContent, Typography, styled } from '@mui/material';
import React, { useEffect, useMemo, useState } from 'react';
import { requestEnrollEntity } from '../../../apis/ca-api';
import CustomDialog from '../../../components/dialog/CustomDialog';
import { formatErrorMessage } from '../../../utils/error-handler';
import { useDialogs } from '@toolpad/core';

interface Props {
  step: number;
  onRegister: (step: number, validate: () => boolean, afterValidate?: () => Promise<void>) => void;
  setIsLoading: (loading: boolean) => void;
}

const Step3Certificate: React.FC<Props> = ({ step, onRegister, setIsLoading }) => {
  const [isRequested, setIsRequested] = useState(false);
  const dialogs = useDialogs();

  const handleRequestCertificateVc = async () => {
    setIsLoading(true);
    try {
      const {data} = await requestEnrollEntity();
      setIsRequested(true);
      dialogs.open(CustomDialog, {
        title: 'Notification',
        message: 'Certificate VC has been successfully issued.',
        isModal: true,
      });
    } catch (error) {
      dialogs.open(CustomDialog, {
        title: 'Notification',
        message: formatErrorMessage(error, `Failed to request Certificate VC`),
        isModal: true,
      });
      setIsRequested(false);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const validate = () => isRequested;

  const afterValidate = async () => {
    // 후속 처리 없음
  };

  useEffect(() => {
    onRegister(step, validate, afterValidate);
  }, [isRequested]);

  const StyledDescription = useMemo(
    () =>
      styled(Box)(({ theme }) => ({
        maxWidth: 700,
        marginTop: theme.spacing(1),
        padding: theme.spacing(0),
      })),
    []
  );

  return (
    <Box>
      <Typography variant="h6" gutterBottom>
        Step 3 – Register Entity and Issue Certificate VC
      </Typography>
      <StyledDescription>
        <Typography variant="body1">
          In this step, you will register your CA as an Entity in the OpenDID network via the Trust Agent.
        </Typography>
        <Typography variant="body1" sx={{ mt: 1 }}>
          Once registered, you will receive a <strong>Certificate VC</strong>, which serves as a trust credential for verifying relationships among OpenDID components.
        </Typography>
      </StyledDescription>

      <Card variant="outlined" sx={{ mt: 3 }}>
        <CardContent>
          <Typography variant="subtitle1" gutterBottom>
            Request Entity Registration
          </Typography>
          <Button
            variant="contained"
            onClick={handleRequestCertificateVc}
            disabled={isRequested}
          >
            Request
          </Button>
          {isRequested && (
            <Typography variant="body2" color="success.main" sx={{ mt: 1 }}>
             ✅ Entity registration completed and Certificate VC issued.
            </Typography>
          )}
        </CardContent>
      </Card>
    </Box>
  );
};

export default Step3Certificate;
