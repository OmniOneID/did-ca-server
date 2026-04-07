/**
 * Type definitions and constants related to password policy
 */

// Policy interface used for validation (extended from DTO)
export interface PasswordPolicy {
  minLength: number;
  maxLength: number;
  requireUppercase: boolean;
  requireLowercase: boolean;
  requireNumber: boolean;
  requireSpecial: boolean;
  passwordExpiryDays: number;
  preventReuse: number;
  maxAttempts: number;
  lockoutDuration: number;
}

// Validation result of individual requirements (used for real-time validation UI)
export interface ValidationRuleResult {
  rule: string;
  isValid: boolean;
  message: string;
}

// Password validation result
export interface ValidationResult {
  isValid: boolean;
  errors: string[];
  failedRequirements: string[];
  passedRequirements: string[];
}

// Validation result of individual requirements
export interface RequirementCheck {
  name: string;
  passed: boolean;
  message: string;
}
