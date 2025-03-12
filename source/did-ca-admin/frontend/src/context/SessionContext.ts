import * as React from 'react';
import type { Session as ToolpadSession } from '@toolpad/core';

export interface ExtendedSession extends ToolpadSession {
  user?: {
    id?: string | null;
    name?: string | null;
    email?: string | null;
    image?: string | null;
    role?: string;  
  };
}

export interface SessionContextValue {
  session: ExtendedSession | null;
  setSession: (session: ExtendedSession | null) => void;
}

export const SessionContext = React.createContext<SessionContextValue>({
  session: null,
  setSession: () => {},
});

export function useSession() {
  return React.useContext(SessionContext);
}
