import { AuthProvider, AuthResponse, SignInPage } from '@toolpad/core/SignInPage';
import * as React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { requestLogin, requestPasswordReset } from '../../apis/admin-api';
import { useSession } from '../../context/SessionContext';
import PasswordResetDialog from './PasswordResetDialog';
import { sha256Hash } from '../../utils/sha256-hash';

export default function SignIn() {
  const { setSession } = useSession();
  const navigate = useNavigate();
  const [requirePasswordReset, setRequirePasswordReset] = useState(false);
  const [loginData, setLoginData] = useState<{ email: string; hashedPassword: string } | null>(null);
  const [rememberMe, setRememberMe] = useState<boolean>(() => {
    return localStorage.getItem('rememberMe') === 'true';
  });

  const handleSignIn = async (
    provider: AuthProvider,
    formData?: FormData,
    callbackUrl?: string
  ): Promise<AuthResponse> => {
    try {
      const email = formData?.get('email') as string;
      const password = formData?.get('password') as string;
      const hashedPassword = await sha256Hash(password);

      const { data } = await requestLogin({
        loginId: email,
        loginPassword: hashedPassword,
      });

      if (data.requirePasswordReset) {
        setRequirePasswordReset(true);
        setLoginData({ email, hashedPassword });
        return {};
      }

      const session = {
        user: {
          id: data.loginId, 
          role: data.role,
        },
      };
      setSession(session);

      const storage = rememberMe ? localStorage : sessionStorage;
      storage.setItem('session', JSON.stringify(session));
      localStorage.setItem('rememberMe', rememberMe.toString());
      if (rememberMe) {
        localStorage.setItem('email', email);
      } else {
        localStorage.removeItem('email');
      }

      navigate(callbackUrl ?? '/ta-registration', { replace: true });
      return {};
    } catch (error) {
      return { error: 'Invalid username or password.' };
    }
  };

  const handlePasswordReset = async (newPassword: string) => {
    if (!loginData) return;

    try {
      const newHashedPassword = await sha256Hash(newPassword);
      await requestPasswordReset({
        loginId: loginData.email,
        oldPassword: loginData.hashedPassword,
        newPassword: newHashedPassword,
      });

      const session = { user: { name: loginData.email } };
      setSession(session);
      
      navigate('/ta-management', { replace: true });
    } catch (error) {
      console.error('Failed to reset password:', error);
    } finally {
      setRequirePasswordReset(false);
      setLoginData(null);
    }
  };

  function SubTitle() {
    return <p style={{ fontSize: '14px', marginBottom: 16 }}>Welcome to OpenDID CA Admin Console</p>;
  }

  return (
    <>
      <SignInPage
        providers={[{ id: 'credentials', name: 'Credentials' }]}
        signIn={handleSignIn}
        slots={{
          subtitle: SubTitle,
        }}
        slotProps={{
          emailField: {
            defaultValue: rememberMe ? localStorage.getItem('email') ?? '' : '',
          },
          rememberMe: {
            checked: rememberMe,
            onChange: (_event: React.SyntheticEvent, checked: boolean) => {
              setRememberMe(checked);
            },
          },
        }}
      />
      <PasswordResetDialog
        open={requirePasswordReset}
        onClose={() => setRequirePasswordReset(false)}
        onSubmit={handlePasswordReset}
      />
    </>
  );
}
