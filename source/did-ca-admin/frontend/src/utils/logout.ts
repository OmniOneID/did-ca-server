import { tokenStore } from './authTokens';

export const ADMIN_LOGOUT_EVENT = 'adminLogoutAt';

export function clearAdminSession() {
  tokenStore.clear();
  sessionStorage.removeItem('session');
  localStorage.removeItem('session');
  sessionStorage.removeItem('activeSession');
}

export async function logoutAdmin(baseUrl: string) {
  if (tokenStore.isPendingLogout()) {
    sessionStorage.removeItem('session');
    sessionStorage.removeItem('activeSession');
    return;
  }
  const refreshToken = tokenStore.getRefresh();
  try {
    if (refreshToken) {
      await fetch(`${baseUrl}/logout`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken }),
        signal: AbortSignal.timeout(3000),
      });
    }
  } catch {
    // Clear the browser session even if the server cannot be reached.
  } finally {
    clearAdminSession();
    localStorage.setItem(ADMIN_LOGOUT_EVENT, `${Date.now()}-${Math.random()}`);
  }
}
