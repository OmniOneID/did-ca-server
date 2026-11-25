import { AuthProvider, AuthResponse, SignInPage } from '@toolpad/core/SignInPage';
import { useState } from 'react';
import { Navigate, useNavigate } from 'react-router';
import { requestLogin, requestPasswordReset, changeAdminIdAndPassword } from '../../apis/admin-api';
import { useSession } from '../../context/SessionContext';
import { sha256Hash } from '../../utils/sha256-hash';
import { tokenStore } from '../../utils/authTokens';
import { emailRegex } from '../../utils/regex';
import PasswordResetDialog from './PasswordResetDialog';
import ChangeIdAndPasswordDialog from './ChangeIdAndPasswordDialog';

export default function SignIn() {
  const { session, setSession } = useSession();
  const navigate = useNavigate();
  const [requirePasswordReset, setRequirePasswordReset] = useState(false);
  const [loginData, setLoginData] = useState<{ email: string; hashedPassword: string } | null>(null);
  const [requireChangeIdAndPassword, setRequireChangeIdAndPassword] = useState(false);
  const [changeIdAndPasswordData, setChangeIdAndPasswordData] = useState<{ email: string; hashedPassword: string } | null>(null);
  const [rememberMe, setRememberMe] = useState<boolean>(() => {
    return localStorage.getItem('rememberMe') === 'true';
  });
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');

  const handleSignIn = async (
    provider: AuthProvider,
    formData?: FormData,
    callbackUrl?: string
  ): Promise<AuthResponse> => {
    const email = ((formData?.get('email') as string) ?? '').trim();
    const password = (formData?.get('password') as string) ?? '';

    const nextEmailError = !email
      ? 'Please enter your email.'
      : !emailRegex.test(email)
        ? 'Please enter a valid email address.'
        : '';
    const nextPasswordError = password ? '' : 'Please enter your password.';
    setEmailError(nextEmailError);
    setPasswordError(nextPasswordError);
    if (nextEmailError || nextPasswordError) {
      return {};
    }

    try {
      const hashedPassword = await sha256Hash(password);

      localStorage.setItem('rememberMe', rememberMe.toString());
      const { data } = await requestLogin({
        loginId: email,
        loginPassword: hashedPassword,
      });

      if (data.requirePasswordReset &&
          data.role === 'ROOT' &&
          data.passwordResetReason === 'FIRST_LOGIN') {
        setRequireChangeIdAndPassword(true);
        setChangeIdAndPasswordData({ email, hashedPassword });
        return {};
      }

      if (data.requirePasswordReset) {
        setRequirePasswordReset(true);
        setLoginData({ email, hashedPassword });
        return {};
      }

      const session = {
        user: {
          id: data.loginId || email,
          role: data.role,
        },
      };
      setSession(session);

      localStorage.setItem('rememberMe', rememberMe.toString());
      if (rememberMe) {
        localStorage.setItem('email', email);
      } else {
        localStorage.removeItem('email');
      }

      navigate(callbackUrl ?? '/ca-registration', { replace: true });
      return {};
    } catch (error) {
      return { error: 'Invalid username or password.' };
    }
  };

  const handleChangeIdAndPassword = async (
    oldLoginId: string,
    newLoginId: string,
    oldPassword: string,
    newPassword: string
  ) => {
    try {
      const newHashedPassword = await sha256Hash(newPassword);
      const oldHashedPassword = await sha256Hash(oldPassword);
      await changeAdminIdAndPassword({
        oldLoginId,
        newLoginId,
        oldPassword: oldHashedPassword,
        newPassword: newHashedPassword,
      });
      localStorage.setItem('rememberMe', rememberMe.toString());
      const { data } = await requestLogin({
        loginId: newLoginId,
        loginPassword: newHashedPassword,
      });
      const session = { user: { id: newLoginId, role: data.role } };
      setSession(session);
      localStorage.setItem('rememberMe', rememberMe.toString());
      if (rememberMe) localStorage.setItem('email', newLoginId);
      else localStorage.removeItem('email');
      setRequireChangeIdAndPassword(false);
      setChangeIdAndPasswordData(null);
      navigate('/ca-management', { replace: true });
    } catch (error: any) {
      throw error;
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

      localStorage.setItem('rememberMe', rememberMe.toString());
      const { data } = await requestLogin({
        loginId: loginData.email,
        loginPassword: newHashedPassword,
      });
      const session = { user: { id: data.loginId || loginData.email, role: data.role } };
      setSession(session);

      navigate('/ca-management', { replace: true });
    } catch (error) {
      console.error('Failed to reset password:', error);
    } finally {
      setRequirePasswordReset(false);
      setLoginData(null);
    }
  };

  const Title = () => <p style={{ fontWeight: 700, fontSize: '32px', lineHeight: '150%', margin: 0 }}>CA Admin Login</p>;
  const SubTitle = () => <p style={{ fontSize: '14px', marginBottom: 16, marginTop: 8 }}>Welcome, please sign in to continue</p>;

  if (session && !tokenStore.isPendingLogout()) return <Navigate to="/" replace />;

  return (
    <>
      <SignInPage
        providers={[{ id: 'credentials', name: 'Credentials' }]}
        signIn={handleSignIn}
        slots={{ title: Title, subtitle: SubTitle }}
        slotProps={{
          emailField: {
            defaultValue: rememberMe ? localStorage.getItem('email') ?? '' : '',
            type: 'text',
            error: !!emailError,
            helperText: emailError,
            onChange: () => emailError && setEmailError(''),
            slotProps: { htmlInput: { required: false, inputMode: 'email' } },
            sx: { '& .MuiOutlinedInput-root': { height: 48 } },
          },
          passwordField: {
            error: !!passwordError,
            helperText: passwordError,
            onChange: () => passwordError && setPasswordError(''),
            slotProps: { htmlInput: { required: false } },
            sx: { '& .MuiOutlinedInput-root': { height: 48 } },
          },
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
          '& .MuiContainer-root': { maxWidth: 'none', width: 540, minHeight: 500 },
          '& .MuiContainer-root > .MuiBox-root:first-of-type': {
            minHeight: 410,
            borderRadius: '4px',
            boxShadow: 'none',
            backgroundColor: 'white',
            border: '#FFFFFF',
          },
        }}
      />
      <ChangeIdAndPasswordDialog
        open={requireChangeIdAndPassword}
        onClose={() => { setRequireChangeIdAndPassword(false); setChangeIdAndPasswordData(null); }}
        onSubmit={handleChangeIdAndPassword}
        oldLoginId={changeIdAndPasswordData?.email}
      />
      <PasswordResetDialog
        open={requirePasswordReset}
        onClose={() => { setRequirePasswordReset(false); setLoginData(null); }}
        onSubmit={handlePasswordReset}
      />
    </>
  );
}
