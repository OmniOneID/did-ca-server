import type { ExtendedSession } from '../context/SessionContext';
import { tokenStore } from './authTokens';

export async function restoreSessionFromToken(baseUrl: string): Promise<ExtendedSession | null> {
  const refreshToken = tokenStore.getRefresh();
  if (!refreshToken) return null;
  const logoutMarker = localStorage.getItem('adminLogoutAt');
  try {
    const refreshed = await fetch(`${baseUrl}/refresh-token`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken }),
      signal: AbortSignal.timeout(3000),
    });
    if (!refreshed.ok) return null;
    const { accessToken } = await refreshed.json();
    if (typeof accessToken !== 'string') return null;
    const encodedClaims = accessToken.split('.')[1]?.replace(/-/g, '+').replace(/_/g, '/');
    if (!encodedClaims) return null;
    const claims = JSON.parse(atob(encodedClaims));
    if (!/^\d+$/.test(String(claims.sub))) return null;
    const adminResponse = await fetch(`${baseUrl}/admins?id=${encodeURIComponent(claims.sub)}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
      signal: AbortSignal.timeout(3000),
    });
    if (!adminResponse.ok) return null;
    const admin = await adminResponse.json();
    if (!admin.loginId || !admin.role || admin.requirePasswordReset
        || localStorage.getItem('adminLogoutAt') !== logoutMarker) return null;
    const session: ExtendedSession = { user: { id: admin.loginId, role: admin.role } };
    tokenStore.save(accessToken, refreshToken, false);
    sessionStorage.setItem('session', JSON.stringify(session));
    localStorage.setItem('session', JSON.stringify(session));
    sessionStorage.setItem('activeSession', 'true');
    return session;
  } catch {
    return null;
  }
}
