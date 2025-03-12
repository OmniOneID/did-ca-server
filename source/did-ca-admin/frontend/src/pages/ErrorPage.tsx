import React from 'react';
import { Typography, Button, Box } from '@mui/material';
import { useLocation } from 'react-router';

const ErrorPage: React.FC = () => {
  const location = useLocation();

  const goBackAndRefresh = () => {
    if (window.history.length > 1) {
      window.history.back();
    } else {
      window.location.href = "/";
    }
  };

  const errorMessage = location.state?.message || "An unexpected error occurred. Please try again later.";
  const errorLines: string[] = errorMessage.split("\n").filter((line:string) => line.trim() !== "Error Occurred");

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      height="100vh"
      textAlign="center"
    >
      <Typography variant="h4" color="error" gutterBottom>
        System Error
      </Typography>

      <Box
        sx={{
          mt: 2,
          p: 2,
          backgroundColor: "#f5f5f5",
          borderRadius: "8px",
          maxWidth: "600px",
          wordBreak: "break-word",
          textAlign: "left"
        }}
      >
        {errorLines.map((line: string, index: number) => (
          <Typography key={index} variant="body2">{line}</Typography>
        ))}
      </Box>

      <Button variant="contained" color="primary" onClick={goBackAndRefresh} sx={{ mt: 2 }}>
        Go Back
      </Button>
    </Box>
  );
};

export default ErrorPage;
