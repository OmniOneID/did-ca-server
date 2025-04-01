import React, { createContext, useContext, useState, ReactNode, useCallback } from 'react';
import { CasInfoResDto } from '../apis/models/CasInfoResDto';

export type ServerStatus = 'ACTIVATE' | 'DEACTIVATE' | 'REQUIRED_ENROLL_ENTITY';

interface ServerStatusContextType {
  serverStatus: ServerStatus | null;
  setServerStatus: (status: ServerStatus | null) => void;
  isLoading: boolean;
  setIsLoading: (loading: boolean, message?: string) => void;
  isLoadingMessage: string;
  casInfo: CasInfoResDto | null;
  setCasInfo: (info: CasInfoResDto | null) => void;
}

export const ServerStatusContext = createContext<ServerStatusContextType>({
  serverStatus: null,
  setServerStatus: () => {},
  isLoading: false,
  setIsLoading: () => {},
  isLoadingMessage: '',
  casInfo: null,
  setCasInfo: () => {},
});

export const ServerStatusProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [serverStatus, setServerStatus] = useState<ServerStatus | null>(null);
  const [isLoading, setIsLoadingState] = useState<boolean>(false);
  const [isLoadingMessage, setIsLoadingMessage] = useState<string>('');
  const [casInfo, setCasInfo] = useState<CasInfoResDto | null>(null);

  const setIsLoading = useCallback((loading: boolean, message?: string) => {
    setIsLoadingState(loading);
    setIsLoadingMessage(message ?? '처리 중입니다...');
  }, []);

  return (
    <ServerStatusContext.Provider 
    value={{ 
        serverStatus, 
        setServerStatus, 
        isLoading, 
        setIsLoading, 
        isLoadingMessage,
        casInfo,
        setCasInfo,
      }}
    >
      {children}
    </ServerStatusContext.Provider>
  );
};

export const useServerStatus = () => useContext(ServerStatusContext);
