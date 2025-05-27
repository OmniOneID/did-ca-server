import { Box, Button, Typography } from '@mui/material';
import React from 'react';

const StepComplete: React.FC = () => {
  const handleGoHome = () => {
    window.location.href = '/';
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      textAlign="center"
      mt={8}
      px={2}
    >
      <Typography variant="h4" gutterBottom>
        Completed
      </Typography>
      <Typography variant="body1" color="textSecondary" gutterBottom>
        TA registration is complete.
      </Typography>
      <Button variant="contained" color="primary" sx={{ mt: 4 }} onClick={handleGoHome}>
        Go to Home
      </Button>
    </Box>
  );
};

export default StepComplete;
