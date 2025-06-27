import { Box, Button, Step, StepLabel, Stepper, Typography, styled } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { CasStatus } from '../../apis/constants/CasStatus';
import { CasInfoResDto } from '../../apis/models/CasInfoResDto';
import { getCaInfo, requestEntityStatus } from '../../apis/ca-api';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import { useServerStatus } from '../../context/ServerStatusContext';
import Step1CaInfo from './stepper/Step1CaInfo';
import Step2DIDDocument from './stepper/Step2DIDDocument';
import Step3Certificate from './stepper/Step3Certificate';
import StepComplete from './stepper/StepComplete';
import { Navigate } from 'react-router';

const steps = ['Enter CA Info', 'Register DID Document', 'Enroll Entity'];

const StyledContainer = styled(Box)(({ theme }) => ({
  width: 800,
  margin: 'auto',
  marginTop: theme.spacing(1),
  padding: theme.spacing(3),
  border: 'none',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: '#ffffff',
  boxShadow: '0px 4px 8px 0px #0000001A',
}));

const StyledTitle = styled(Typography)({
  textAlign: 'left',
  fontSize: '24px',
  fontWeight: 700,
});

const StyledStepperWrapper = styled(Box)({
  width: '100%',
  maxWidth: 800,
  marginLeft: 'auto',
  marginRight: 'auto',
  marginTop: 10,
});

const StyledStepper = styled(Stepper)({
  width: '100%',
});

const StyledStep = styled(Step)({});

const StyledStepLabel = styled(StepLabel)({});

const StyledContentWrapper = styled(Box)(({ theme }) => ({
  marginTop: theme.spacing(4),
}));

const StyledActionWrapper = styled(Box)({
  display: 'flex',
  justifyContent: 'center',
  marginTop: '24px',
  gap: "12px",
});

type Props = {}

const CaServiceRegistrationPage = (props: Props) => {
  const [isLoading, setIsLoading] = useState(false);
  const [activeStep, setActiveStep] = useState<number>(0);
  const [isInitialized, setIsInitialized] = useState(false); // 초기화 상태 추적
  const [validateFns, setValidateFns] = useState<Record<number, () => boolean>>({});
  const [afterValidateFns, setAfterValidateFns] = useState<Record<number, () => Promise<void>>>({});
  const { serverStatus } = useServerStatus();
  
  const registerStepFns = (step: number, validate: () => boolean, afterValidate?: () => Promise<void>) => {
    setValidateFns(prev => ({ ...prev, [step]: validate }));
    if (afterValidate) {
      setAfterValidateFns(prev => ({ ...prev, [step]: afterValidate }));
    }
  };

  const handleNext = async () => {
    const validate = validateFns[activeStep];
    const afterValidate = afterValidateFns[activeStep];

    if (validate && !validate()) return;

    try {
      if (afterValidate) {
        await afterValidate();
      }
  
      const { data } = await getCaInfo();
  
      setIsLoading(true);
      const nextStep = getNextStepByTaStatus(data);
      setActiveStep(nextStep);
      setIsLoading(false);
  
    } catch (error) {
      console.error('Step transition failed:', error);
      setIsLoading(false);
    }
  };

  const getNextStepByTaStatus = (caInfo: CasInfoResDto): number => {
    return activeStep + 1;
  };

  const handleBack = () => setActiveStep((prev) => prev - 1);

  const getStepContent = (step: number) => {
    switch (step) {
      case 0: return <Step1CaInfo step={0} onRegister={registerStepFns} setIsLoading={setIsLoading}/>;
      case 1: return <Step2DIDDocument step={0} onRegister={registerStepFns} setIsLoading={setIsLoading}/>;
      case 2: return <Step3Certificate step={0} onRegister={registerStepFns} setIsLoading={setIsLoading}/>;
      case 3: return <StepComplete />;
      default: return 'Unknown step';
    }
  };
  
  useEffect(() => {
    requestEntityStatus()
      .catch(err => console.error('Error requesting entity status:', err))
      .finally(() => {
        const fetchCaInfo = () => {
          getCaInfo()
            .then(({ data }) => {
              if (!data.name) {
                setActiveStep(0);
              } else if (data.status === CasStatus.DID_DOCUMENT_REQUIRED) {
                setActiveStep(1);
              } else if (data.status === CasStatus.DID_DOCUMENT_REQUESTED) {
                setActiveStep(1);
              } else if (data.status === CasStatus.CERTIFICATE_VC_REQUIRED) {
                setActiveStep(2);
              }
              setIsInitialized(true);
              setIsLoading(false);
            })
            .catch((err) => {
              console.error('Error fetching CA info:', err);
              setIsInitialized(true);
              setIsLoading(false);
            });
        };

        fetchCaInfo();
      });
  }, []);
  
  if (!isInitialized) {
    return <FullscreenLoader open={true} />;
  }

  if (serverStatus === 'ACTIVATE') {
    return <Navigate to="/ca-management" replace />;
  }

  return (
    <>
      <FullscreenLoader open={isLoading} />
      <StyledContainer>
        <StyledTitle>CA Registration</StyledTitle>
  
        <StyledStepperWrapper>
          {activeStep < steps.length && (
            <StyledStepper activeStep={activeStep}>
              {steps.map((label) => (
                <StyledStep key={label}>
                  <StyledStepLabel>{label}</StyledStepLabel>
                </StyledStep>
              ))}
            </StyledStepper>
          )}
  
          <StyledContentWrapper>
            {getStepContent(activeStep)}
  
            {activeStep < steps.length && (
              <StyledActionWrapper>
                <Button variant='outlined' disabled={activeStep === 0} onClick={handleBack}>
                  Back
                </Button>
                <Button variant="contained" onClick={handleNext}>
                  {activeStep === steps.length - 1 ? 'Finish' : 'Next'}
                </Button>
              </StyledActionWrapper>
            )}
          </StyledContentWrapper>
        </StyledStepperWrapper>
      </StyledContainer>
    </>
  );
}

export default CaServiceRegistrationPage