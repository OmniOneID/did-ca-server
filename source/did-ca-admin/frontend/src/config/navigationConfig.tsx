import { SettingsApplications } from '@mui/icons-material';
import StorageIcon from '@mui/icons-material/Storage';
import SupervisorAccountIcon from '@mui/icons-material/SupervisorAccount';
import { type Navigation } from '@toolpad/core/AppProvider';
import PersonIcon from '@mui/icons-material/Person';


export const getNavigationByStatus = (serverStatus: string | null): Navigation=> {
  if (serverStatus !== 'ACTIVATE') {
    return [
      {kind: 'divider'},
      { segment: 'ca-registration', title: 'CA Registration',},
      {kind: 'divider'},
    ];
  } 
  return [
    {kind: 'divider'},
    { 
      segment: 'ca-management', 
      title: 'CA Management',
    },
    { 
      segment: 'user-management', 
      title: 'User Management',
    },
    {
      segment: 'admin-management',
      title: 'Admin Management',
    },
    {kind: 'divider'},
  ];
};
