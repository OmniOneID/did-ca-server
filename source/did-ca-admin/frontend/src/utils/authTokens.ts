const ACCESS_KEY = 'accessToken';
const REFRESH_KEY = 'refreshToken';
const LAST_ACTIVITY_KEY = 'adminLastActivityAt';
const PENDING_LOGOUT_KEY = 'adminPendingLogoutAt';
const IDLE_TIMEOUT_MS = 30 * 60 * 1000;

function hasActiveSession(): boolean {
  const lastActivity = Number(localStorage.getItem(LAST_ACTIVITY_KEY));
  return !lastActivity || Date.now() - lastActivity < IDLE_TIMEOUT_MS;
}

export const tokenStore = {
  save(access: string, refresh: string, _remember: boolean) {
    localStorage.setItem(ACCESS_KEY, access);
    localStorage.setItem(REFRESH_KEY, refresh);
    localStorage.setItem(LAST_ACTIVITY_KEY, String(Date.now()));
    sessionStorage.removeItem(PENDING_LOGOUT_KEY);
    sessionStorage.removeItem(ACCESS_KEY);
    sessionStorage.removeItem(REFRESH_KEY);
    window.dispatchEvent(new Event('admin-authenticated'));
  },

  isPendingLogout(): boolean {
    return !!sessionStorage.getItem(PENDING_LOGOUT_KEY);
  },

  markRemoteLogout(marker: string) {
    sessionStorage.setItem(PENDING_LOGOUT_KEY, marker);
    sessionStorage.removeItem(ACCESS_KEY);
    sessionStorage.removeItem(REFRESH_KEY);
  },

  getAccess(): string | null {
    if (this.isPendingLogout()) return null;
    return hasActiveSession()
      ? localStorage.getItem(ACCESS_KEY) ?? sessionStorage.getItem(ACCESS_KEY)
      : null;
  },

  getRefresh(): string | null {
    if (this.isPendingLogout() || !hasActiveSession()) return null;
    const shared = localStorage.getItem(REFRESH_KEY);
    if (shared) return shared;

    const tabToken = sessionStorage.getItem(REFRESH_KEY);
    const tabSession = sessionStorage.getItem('session');
    if (tabToken && tabSession) {
      const lastActivity = Number(sessionStorage.getItem(LAST_ACTIVITY_KEY)) || Date.now();
      if (Date.now() - lastActivity >= IDLE_TIMEOUT_MS) return null;
      localStorage.setItem(REFRESH_KEY, tabToken);
      const access = sessionStorage.getItem(ACCESS_KEY);
      if (access) localStorage.setItem(ACCESS_KEY, access);
      localStorage.setItem('session', tabSession);
      localStorage.setItem(LAST_ACTIVITY_KEY, String(lastActivity));
    }
    return tabToken;
  },

  clear() {
    [localStorage, sessionStorage].forEach((storage) => {
      storage.removeItem(ACCESS_KEY);
      storage.removeItem(REFRESH_KEY);
    });
    localStorage.removeItem(LAST_ACTIVITY_KEY);
  },

  writeAccess(newAccess: string) {
    if (localStorage.getItem(REFRESH_KEY)) {
      localStorage.setItem(ACCESS_KEY, newAccess);
    } else {
      sessionStorage.setItem(ACCESS_KEY, newAccess);
    }
  },
};
