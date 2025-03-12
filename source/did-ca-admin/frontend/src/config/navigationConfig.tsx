import { SettingsApplications } from '@mui/icons-material';
import StorageIcon from '@mui/icons-material/Storage';
import SupervisorAccountIcon from '@mui/icons-material/SupervisorAccount';
import { type Navigation } from '@toolpad/core/AppProvider';


export const getNavigationByStatus = (serverStatus: string | null): Navigation=> {
  if (serverStatus !== 'ACTIVATE') {
    return [{ segment: 'ca-registration', title: 'CAS Registration', icon: <StorageIcon /> }];
  } 
  return [
    { 
      segment: 'ca-management', 
      title: 'CAS Management', 
      icon: <StorageIcon />,
    },
    {
      segment: 'admin-management',
      title: 'Admin Management', 
      icon: <SupervisorAccountIcon />,
    },
  ];
};
