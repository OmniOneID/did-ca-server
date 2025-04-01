import * as React from 'react';
import { Stack, Typography, Avatar, Button, Divider } from '@mui/material';
import { SessionContext } from '@toolpad/core/AppProvider';
import { SignOutButton } from '@toolpad/core/Account';
import { useContext, useEffect, useState } from 'react';

export default function CustomFooterAccount() {
  const session = useContext(SessionContext);
  const [user, setUser] = useState<{ id?: string; name?: string; image?: string } | null>(null);

  useEffect(() => {
    if (session?.user) {
        console.log(session.user);
      setUser({
        id: session.user.id || '',
        name: session.user.name || 'Guest',
        image: session.user.image || '',
      });
    }
  }, [session?.user]);

  return (
    <Stack spacing={1} sx={{ p: 2 }}>
        <Stack direction="row" spacing={1} alignItems="center">
            <Avatar
            src={user?.image || ''}
            alt={user?.id || 'User'}
            sx={{ width: 32, height: 32 }}
            >
            {user?.id ? user.id[0] : 'U'}
            </Avatar>
            <Typography variant="body2">{user?.id || 'Unknown User'}</Typography>
        </Stack>

        <Divider />
        <SignOutButton
            
        />
    </Stack>
  );
}
