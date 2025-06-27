import { Box, Button, Card, CardContent, TextField, Typography, styled } from '@mui/material';
import React, { useEffect, useMemo, useState } from 'react'
import { englishRegex, ipRegex, urlRegex } from '../../../utils/regex';
import CustomDialog from '../../../components/dialog/CustomDialog';
import { formatErrorMessage } from '../../../utils/error-handler';
import { useDialogs } from '@toolpad/core';
import { getCaInfo, generateCaDidDocument, registerCaDidDocument, requestEntityStatus } from '../../../apis/ca-api';
import { useNavigate } from 'react-router';

interface Props {
    step: number;
    onRegister: (step: number, validate: () => boolean, afterValidate?: () => Promise<void>) => void;
    setIsLoading: (loading: boolean) => void;
}

const Step2DIDDocument: React.FC<Props> = ({ step, onRegister, setIsLoading }) => {
    const [didDocument, setDidDocument] = useState<string>('');
    const [isDidGenerated, setIsDidGenerated] = useState<boolean>(false);
    const [isDidRequestSubmitted, setIsDidRequestSubmitted] = useState<boolean>(false); 
    const [isDidApproved, setIsDidApproved] = useState<boolean>(false); 
    const dialogs = useDialogs();
    const navigate = useNavigate();

    const handleGenerateDid = async () => {
        setIsLoading(true);
        setIsDidGenerated(false);
        setDidDocument('');

        await generateCaDidDocument()
        .then((response) => {
          setDidDocument(JSON.stringify(response.data, null, 2));
          setIsDidGenerated(true);
          setIsLoading(false);
        }).catch((error) => {
          setIsLoading(false);
          dialogs.open(CustomDialog, {
            title: 'Notification',
            message: formatErrorMessage(error, `Failed to generate DID Document`),
            isModal: true,
          });
          throw error;
        });
    };

    const handleSubmitDidRequest = async () => {
        setIsLoading(true);
        
        const requestBody = {
            didDocument: didDocument
        };

        await registerCaDidDocument(requestBody).then((response) => {
            setIsDidRequestSubmitted(true);
            setIsLoading(false);
          }).catch((error) => {
              setIsLoading(false);
              setIsDidRequestSubmitted(false);
              dialogs.open(CustomDialog, {
                  title: 'Notification',
                  message: formatErrorMessage(error, `Failed to send CA DID Document registration request to TA`),
                  isModal: true,
              });
              throw error;
          });
    };

    const validate = () => {
        return isDidApproved;
    };

    const afterValidate = async () => {
        // 후속 처리
    };

    const handleCheckApproval = async () => {
        setIsLoading(true);
        await requestEntityStatus()
        .then((response) => {
            const { data } = response;
            if (data.status === 'DID_DOCUMENT_REQUIRED') {
                setIsDidApproved(false);
                dialogs.open(CustomDialog, {
                    title: 'Notification',
                    message: `The DID Document registration request is still pending approval.`,
                    isModal: true,
                });
            } else if (data.status === 'CERTIFICATE_VC_REQUIRED') {
                setIsDidApproved(true);
                dialogs.open(CustomDialog, {
                    title: 'Notification',
                    message: `The DID Document registration request has been approved.`,
                    isModal: true,
                });
            } else if (data.status === 'NOT_REGISTERED') {
                setIsDidApproved(false);
                dialogs.open(CustomDialog, {
                    title: 'Notification',
                    message: `The DID Document registration request has been rejected.`,
                    isModal: true,
                }, {
                  onClose: async () => navigate('/', { replace: true }),
                });
            }
            setIsLoading(false);
        }).catch((error) => {
            setIsLoading(false);
            setIsDidApproved(false);
            dialogs.open(CustomDialog, {
                title: 'Notification',
                message: formatErrorMessage(error, `Failed to check approval status`),
                isModal: true,
            });
            throw error;
        });  
    };

    useEffect(() => {
        const fetchCaInfo = () => {
        setIsLoading(true);
        getCaInfo()
            .then(({ data }) => {
                if (data.didDocument) {
                    setDidDocument(JSON.stringify(data.didDocument, null, 2));
                    setIsDidGenerated(true);
                }

                if (data.status === 'DID_DOCUMENT_REQUESTED') {
                    setIsDidGenerated(true);
                    setIsDidRequestSubmitted(true);
                } else if (data.status === 'CERTIFICATE_VC_REQUIRED') {
                    setIsDidGenerated(true);
                    setIsDidRequestSubmitted(true);
                    setIsDidApproved(true)
                }
                setIsLoading(false);
        })
        .catch((err) => {
            console.error('Error fetching CA info:', err);
            setIsLoading(false);
        });
        };

        fetchCaInfo();
    }, []);

    useEffect(() => {
        onRegister(step, validate, afterValidate);
    }, [isDidRequestSubmitted]);

    const StyledDescription = useMemo(() => styled(Box)(({ theme }) => ({
        maxWidth: 700, 
        marginTop: theme.spacing(1),
        padding: theme.spacing(0),
    })), []);

    return (
        <Box>
          <Typography variant="h6" gutterBottom>
            Step 2 – Register DID Document
          </Typography>
          <StyledDescription>
            <Typography variant="body1">
                In this step, you will create the CA's DID Document and request registration to the Trust Agent.
            </Typography>
            <Typography variant="body1" sx={{ mt: 1 }}>
                <strong>Note:</strong> Once the Trust Agent administrator approves the request and the DID Document is registered, you can proceed to the next step.
            </Typography>
          </StyledDescription>

          {/* Step 1. Generate DID Document */}
          <Card variant="outlined" sx={{ mb: 4, mt: 1 }}>
            <CardContent>
              <Typography variant="subtitle1" gutterBottom>
                Step 1. Generate DID Document
              </Typography>
              <Button 
                variant="contained" 
                onClick={handleGenerateDid} 
                sx={{ mt: 1 }}
                disabled={isDidRequestSubmitted}
              >
                Generate
              </Button>

              {isDidGenerated && (
                <>
                <Box
                  sx={{
                    maxHeight: 300,
                    overflow: 'auto',
                    backgroundColor: '#f5f5f5',
                    border: '1px solid #ccc',
                    borderRadius: 1,
                    padding: 2,
                    fontFamily: 'monospace',
                    whiteSpace: 'pre-wrap',
                    marginTop: 2,
                    fontSize: 14,
                  }}
                >
                  {didDocument}
                </Box>
                  <Typography variant="body2" color="success.main" sx={{ mt: 1 }}>
                    ✅ DID Document has been successfully created.
                  </Typography>
                </>
              )}
            </CardContent>
          </Card>

          {/* Step 2. Submit Registration Request */}
          {isDidGenerated && (
            <Card variant="outlined" sx={{ mb: 4, mt: 1 }}>
              <CardContent>
                <Typography variant="subtitle1" gutterBottom>
                  Step 2. Submit Registration Request
                </Typography>
                <Button 
                  variant="contained" 
                  onClick={handleSubmitDidRequest} 
                  sx={{ mt: 1 }}
                  disabled={isDidRequestSubmitted}
                >
                  Request
                </Button>
                {isDidRequestSubmitted && (
                  <Typography variant="body2" color="success.main" sx={{ mt: 1 }}>
                    ✅ Registration request has been submitted.
                  </Typography>
                )}
              </CardContent>
            </Card>
          )}

          {/* Step 3. Check Approval */}
          {isDidRequestSubmitted && (
            <Card variant="outlined">
              <CardContent>
                <Typography variant="subtitle1" gutterBottom>
                  Step 3. Check Approval Status
                </Typography>
                <Typography variant="body2" sx={{ mb: 2 }}>
                  Check if the Trust Agent administrator has approved your DID Document.
                </Typography>
                <Button
                  variant="contained"
                  onClick={handleCheckApproval}
                  disabled={isDidApproved}
                >
                  Check
                </Button>
                {isDidApproved && (
                  <Typography variant="body2" color="success.main" sx={{ mt: 1 }}>
                    ✅ Approval confirmed. You can proceed.
                  </Typography>
                )}
              </CardContent>
            </Card>
          )}
        </Box>
    );
}

export default Step2DIDDocument;
