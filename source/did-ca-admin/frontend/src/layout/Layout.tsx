import { Stack, useTheme } from '@mui/material';
import { DashboardLayout } from '@toolpad/core/DashboardLayout';
import { PageContainer } from '@toolpad/core/PageContainer';
import { Navigate, Outlet, useNavigate } from 'react-router';
import logo from '../assets/logo.svg';
import CustomAccount from '../components/account-menu/CustomAccount';
import { useSession } from '../context/SessionContext';
import { useEffect } from 'react';
import CustomFooterAccount from '../components/account-menu/CustomFooterAccount';

export default function Layout() {
  const { session } = useSession();
  const navigate = useNavigate();
  const theme = useTheme(); 

  if (!session) {
    return <Navigate to="/sign-in" replace />;
  }

  const CustomAppTitle = () => {
    return (
      <Stack 
        direction="row" 
        alignItems="center"
        spacing={2}
        sx={{ cursor: 'pointer'}}
        onClick={() => navigate('/')}
      >
         <img src={logo} alt="OpenDID Logo" style={{ height: '20px', width: '140px' }} />
      </Stack>
    );
  };

  return (
    <DashboardLayout
      disableCollapsibleSidebar
      sx={{
        '& main': { 
          marginLeft: 0, 
          marginRight: 'auto', 
          maxWidth: '100%', 
          paddingLeft: '16px',
        },
        '& .MuiAppBar-root': {
          width: {
            xs: '0',
            sm: '0',
            md: '320px',
          },
          display: {
            xs: 'none',
            sm: 'none',
            md: 'block',
          },
          position: 'absolute',
          left: 0,
          top: 0,   
          borderColor: theme.palette.mode === 'light' ? 'rgba(0, 0, 0, 0.12)' : undefined,
          backgroundColor: theme.palette.mode === 'light' ? '#202B45' : undefined,
          borderTopLeftRadius: '8px',
          borderTopRightRadius: '8px',
          paddingTop: '10px',
        },
        '& .MuiToolbar-root': {
          width: {
            xs: '0',
            sm: '0',
            md: '305px',
          },
          minHeight: '30px !important',
          marginLeft: '5px',  
          marginTop: '15px',        
        },
        '& .MuiBox-root > .MuiToolbar-root': {
          display: 'none',
        },
      }}
      slots={{
        appTitle: CustomAppTitle,
        toolbarAccount: () => <CustomAccount />,
        sidebarFooter: () => <CustomFooterAccount />,
      }}
    >
      <PageContainer breadcrumbs={[]} >
        <Outlet />
      </PageContainer>
    </DashboardLayout>
  );  
}
