import { Backdrop, CircularProgress, Typography } from '@mui/material';
import { useServerStatus } from '../../context/ServerStatusContext';

const LoadingOverlay = () => {
  const { isLoading, isLoadingMessage } = useServerStatus();

  return (
    <Backdrop open={isLoading} sx={{ color: '#fff', zIndex: 9999 }}>
      <div style={{ textAlign: 'center' }}>
        <CircularProgress color="inherit" />
        <Typography variant="h6" sx={{ mt: 2 }}>
          {isLoadingMessage || '처리 중입니다.'}
        </Typography>
      </div>
    </Backdrop>
  );
};

export default LoadingOverlay;
