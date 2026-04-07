import { useMemo } from 'react';
import { usePasswordPolicy as usePasswordPolicyContext } from '../context/PasswordPolicyContext';
import { validatePassword, getPasswordValidationResults, getPasswordValidationSummary } from '../utils/password-validation';
import { PasswordPolicy, ValidationRuleResult } from '../constants/password-policy';

export interface PasswordValidationHook {
  // Policy state
  policy: PasswordPolicy | null;
  isLoading: boolean;
  error: string | null;

  // Policy management
  loadPolicy: () => Promise<void>;
  updatePolicy: (newPolicy: PasswordPolicy) => void;
  resetError: () => void;

  // Validation functions
  validatePassword: (password: string) => boolean;
  getValidationResults: (password: string) => ValidationRuleResult[];
  getValidationSummary: (password: string) => {
    isValid: boolean;
    passedCount: number;
    totalCount: number;
    failedRules: string[];
  };
}

/**
 * Custom hook for password policy management and validation
 * Provides comprehensive password validation functionality based on current policy
 */
export const usePasswordPolicy = (): PasswordValidationHook => {
  const {
    policy,
    isLoading,
    error,
    loadPolicy,
    updatePolicy,
    resetError,
  } = usePasswordPolicyContext();

  // Memoized validation function
  const validatePasswordMemo = useMemo(() => {
    return (password: string): boolean => {
      if (!policy) return false;
      return validatePassword(password, policy);
    };
  }, [policy]);

  // Memoized detailed validation results function
  const getValidationResultsMemo = useMemo(() => {
    return (password: string): ValidationRuleResult[] => {
      if (!policy) return [];
      return getPasswordValidationResults(password, policy);
    };
  }, [policy]);

  // Memoized validation summary function
  const getValidationSummaryMemo = useMemo(() => {
    return (password: string) => {
      if (!policy) {
        return {
          isValid: false,
          passedCount: 0,
          totalCount: 0,
          failedRules: ['Policy not loaded'],
        };
      }

      return getPasswordValidationSummary(password, policy);
    };
  }, [policy]);

  return {
    // Policy state
    policy,
    isLoading,
    error,

    // Policy management
    loadPolicy,
    updatePolicy,
    resetError,

    // Validation functions
    validatePassword: validatePasswordMemo,
    getValidationResults: getValidationResultsMemo,
    getValidationSummary: getValidationSummaryMemo,
  };
};

export default usePasswordPolicy;
