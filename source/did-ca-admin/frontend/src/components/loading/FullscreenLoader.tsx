import React from 'react';
import { Backdrop, CircularProgress } from '@mui/material';

interface FullscreenLoaderProps {
  open: boolean;
}

const FullscreenLoader: React.FC<FullscreenLoaderProps> = ({ open }) => {
  return (
    <Backdrop open={open} sx={{ color: '#fff', zIndex: 9999 }}>
      <CircularProgress size={60} />
    </Backdrop>
  );
};

export default FullscreenLoader;
