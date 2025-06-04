import { AuthProvider, AuthResponse, SignInPage } from '@toolpad/core/SignInPage';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { requestLogin, requestPasswordReset } from '../../apis/admin-api';
import { useSession } from '../../context/SessionContext';
import { sha256Hash } from '../../utils/sha256-hash';
import PasswordResetDialog from './PasswordResetDialog';

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

      const session = { user: { id: loginData.email } };
      setSession(session);
      
      navigate('/ta-management', { replace: true });
    } catch (error) {
      console.error('Failed to reset password:', error);
    } finally {
      setRequirePasswordReset(false);
      setLoginData(null);
    }
  };

  const Title = () => <p style={{ fontWeight: 700, fontSize: '32px', lineHeight: '150%', margin: 0 }}>CA Admin Login</p>;
  const SubTitle = () => <p style={{ fontSize: '14px', marginBottom: 16, marginTop: 8 }}>Welcome, please sign in to continue</p>;

  return (
    <>
      <SignInPage
        providers={[{ id: 'credentials', name: 'Credentials' }]}
        signIn={handleSignIn}
        slots={{ title: Title, subtitle: SubTitle }}
        slotProps={{
          emailField: {
            defaultValue: rememberMe ? localStorage.getItem('email') ?? '' : '',
            sx: { '& .MuiOutlinedInput-root': { height: 48 } },
          },
          passwordField: { sx: { '& .MuiOutlinedInput-root': { height: 48 } } },
          rememberMe: { 
            checked: rememberMe, 
            onChange: (_e, checked) => setRememberMe(checked),
            sx: { '& .MuiFormControlLabel-label': { color: '#000000' } },
          },
          submitButton: {
            sx: {
              height: '60px',
              backgroundColor: '#FF8400',
              borderRadius: '8px',
              color: 'white',
              marginTop: '16px',
              padding: '10px 32px',
              '&:hover': { backgroundColor: '#E67300' },
            },
          },
        }}
        sx={{
          '& .MuiContainer-root': { maxWidth: 'none', width: 540, height: 500 },
          '& .MuiContainer-root > .MuiBox-root:first-of-type': {
            height: 410,
            borderRadius: '4px',
            boxShadow: 'none',
            backgroundColor: 'white',
            border: '#FFFFFF',
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
