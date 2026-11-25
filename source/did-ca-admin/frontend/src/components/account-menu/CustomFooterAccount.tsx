import { Stack, Typography, Avatar, Divider } from '@mui/material';
import { SignOutButton } from '@toolpad/core/Account';
import { useSession } from '../../context/SessionContext';

export default function CustomFooterAccount() {
  const { session } = useSession();
  const user = session?.user;
  const displayName = user?.id || user?.email || user?.name || 'Admin';

  return (
    <Stack spacing={1} sx={{ p: 2 }}>
      <Stack direction="row" spacing={1} alignItems="center">
        <Avatar src={user?.image || ''} alt={displayName} sx={{ width: 32, height: 32 }}>
          {displayName[0].toUpperCase()}
        </Avatar>
        <Typography variant="body2">{displayName}</Typography>
      </Stack>
      <Divider />
      <SignOutButton />
    </Stack>
  );
}
