import React, { createContext, useContext, useState, ReactNode, useCallback, useEffect } from 'react';
import { PasswordPolicy } from '../constants/password-policy';
import { getAdminPasswordPolicy } from '../apis/admin-api';

interface PasswordPolicyContextType {
  policy: PasswordPolicy | null;
  isLoading: boolean;
  error: string | null;
  loadPolicy: () => Promise<void>;
  updatePolicy: (newPolicy: PasswordPolicy) => void;
  resetError: () => void;
}

const PasswordPolicyContext = createContext<PasswordPolicyContextType>({
  policy: null,
  isLoading: false,
  error: null,
  loadPolicy: async () => {},
  updatePolicy: () => {},
  resetError: () => {},
});

export const PasswordPolicyProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [policy, setPolicy] = useState<PasswordPolicy | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [hasTriedLoading, setHasTriedLoading] = useState<boolean>(false);

  // Manual load policy function (for external calls)
  const loadPolicy = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    setHasTriedLoading(true);

    try {
      const response = await getAdminPasswordPolicy();

      if (response && response.data) {
        setPolicy(response.data);
      }
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Unknown error occurred';
      setError(errorMessage);
      console.warn(`Failed to load password policy: ${errorMessage}`);
    } finally {
      setIsLoading(false);
    }
  }, []);

  // Auto-load policy on mount (only once)
  useEffect(() => {
    let mounted = true;

    const autoLoad = async () => {
      if (hasTriedLoading || isLoading) {
        return;
      }

      setIsLoading(true);
      setError(null);
      setHasTriedLoading(true);

      try {
        const response = await getAdminPasswordPolicy();

        if (mounted && response && response.data) {
          setPolicy(response.data);
        }
      } catch (err) {
        if (mounted) {
          const errorMessage = err instanceof Error ? err.message : 'Unknown error occurred';
          setError(errorMessage);
          console.warn(`Failed to load password policy: ${errorMessage}`);
        }
      } finally {
        if (mounted) {
          setIsLoading(false);
        }
      }
    };

    autoLoad();

    return () => {
      mounted = false;
    };
  }, []);

  // Update policy in context (for real-time updates after policy changes)
  const updatePolicy = useCallback((newPolicy: PasswordPolicy) => {
    setPolicy(newPolicy);
    setError(null);
  }, []);

  // Reset error state and allow retry
  const resetError = useCallback(() => {
    setError(null);
    setHasTriedLoading(false);
  }, []);

  const contextValue: PasswordPolicyContextType = {
    policy,
    isLoading,
    error,
    loadPolicy,
    updatePolicy,
    resetError,
  };

  return (
    <PasswordPolicyContext.Provider value={contextValue}>
      {children}
    </PasswordPolicyContext.Provider>
  );
};

export const usePasswordPolicy = () => {
  const context = useContext(PasswordPolicyContext);

  if (!context) {
    throw new Error('usePasswordPolicy must be used within a PasswordPolicyProvider');
  }

  return context;
};
