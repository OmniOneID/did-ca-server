import { useEffect } from 'react';

const IDLE_TIMEOUT_MS = 30 * 60 * 1000;
const LAST_ACTIVITY_KEY = 'adminLastActivityAt';
const PENDING_LOGOUT_KEY = 'adminPendingLogoutAt';

export function useIdleLogout(active: boolean, logout: () => void) {
  useEffect(() => {
    if (!active) return;

    const getLastActivity = () => {
      const storage = sessionStorage.getItem(PENDING_LOGOUT_KEY) ? sessionStorage : localStorage;
      return Number(storage.getItem(LAST_ACTIVITY_KEY)) || Date.now();
    };
    sessionStorage.setItem(LAST_ACTIVITY_KEY, String(getLastActivity()));
    if (Date.now() - getLastActivity() >= IDLE_TIMEOUT_MS) {
      logout();
      return;
    }

    let lastActivity = getLastActivity();
    let timer: ReturnType<typeof setTimeout>;
    const expire = () => {
      const remaining = IDLE_TIMEOUT_MS - (Date.now() - getLastActivity());
      if (remaining <= 0) logout();
      else timer = setTimeout(expire, remaining);
    };
    const onActivity = () => {
      const now = Date.now();
      if (now - lastActivity < 5000) return;
      lastActivity = now;
      sessionStorage.setItem(LAST_ACTIVITY_KEY, String(now));
      if (!sessionStorage.getItem(PENDING_LOGOUT_KEY)) {
        localStorage.setItem(LAST_ACTIVITY_KEY, String(now));
      }
      clearTimeout(timer);
      timer = setTimeout(expire, IDLE_TIMEOUT_MS);
    };
    const onRemoteLogout = (event: StorageEvent) => {
      if (event.key !== 'adminLogoutAt' || !event.newValue) return;
      lastActivity = Number(sessionStorage.getItem(LAST_ACTIVITY_KEY)) || Date.now();
      clearTimeout(timer);
      timer = setTimeout(expire, Math.max(0, IDLE_TIMEOUT_MS - (Date.now() - lastActivity)));
    };
    timer = setTimeout(expire, Math.max(0, IDLE_TIMEOUT_MS - (Date.now() - getLastActivity())));
    window.addEventListener('storage', onRemoteLogout);
    for (const event of ['mousemove', 'keydown', 'click', 'touchstart', 'scroll']) {
      window.addEventListener(event, onActivity, { passive: true });
    }
    return () => {
      clearTimeout(timer);
      window.removeEventListener('storage', onRemoteLogout);
      for (const event of ['mousemove', 'keydown', 'click', 'touchstart', 'scroll']) {
        window.removeEventListener(event, onActivity);
      }
    };
  }, [active, logout]);
}
