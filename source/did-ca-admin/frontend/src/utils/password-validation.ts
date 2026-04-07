import { PasswordPolicy, ValidationResult, RequirementCheck, ValidationRuleResult } from '../constants/password-policy';

/**
 * Password policy-based validation utilities
 */

// Define special characters (must match server-side)
const SPECIAL_CHARACTERS = /[!@#$%^&*(),.?":{}|<>]/;

/**
 * Individual requirement check functions
 */
const checkMinLength = (password: string, minLength: number): RequirementCheck => {
  const passed = password.length >= minLength;
  return {
    name: 'minLength',
    passed,
    message: passed
        ? `Password meets minimum length requirement (${password.length}/${minLength})`
        : `Password must be at least ${minLength} characters long (current: ${password.length})`
  };
};

const checkUppercase = (password: string, required: boolean): RequirementCheck => {
  if (!required) {
    return {
      name: 'uppercase',
      passed: true,
      message: ''
    };
  }

  const passed = /[A-Z]/.test(password);
  return {
    name: 'uppercase',
    passed,
    message: passed
        ? 'Contains uppercase letter'
        : 'Must contain at least one uppercase letter (A-Z)'
  };
};

const checkLowercase = (password: string, required: boolean): RequirementCheck => {
  if (!required) {
    return {
      name: 'lowercase',
      passed: true,
      message: ''
    };
  }

  const passed = /[a-z]/.test(password);
  return {
    name: 'lowercase',
    passed,
    message: passed
        ? 'Contains lowercase letter'
        : 'Must contain at least one lowercase letter (a-z)'
  };
};

const checkNumber = (password: string, required: boolean): RequirementCheck => {
  if (!required) {
    return {
      name: 'number',
      passed: true,
      message: ''
    };
  }

  const passed = /[0-9]/.test(password);
  return {
    name: 'number',
    passed,
    message: passed
        ? 'Contains number'
        : 'Must contain at least one number (0-9)'
  };
};

const checkSpecialCharacter = (password: string, required: boolean): RequirementCheck => {
  if (!required) {
    return {
      name: 'specialChar',
      passed: true,
      message: ''
    };
  }

  const passed = SPECIAL_CHARACTERS.test(password);
  return {
    name: 'specialChar',
    passed,
    message: passed
        ? 'Contains special character'
        : 'Must contain at least one special character (!@#$%^&*)'
  };
};

/**
 * Simple validation function for boolean result
 * @param password Password to validate
 * @param policy Password policy to apply
 * @returns Boolean indicating if password meets all requirements
 */
export const validatePassword = (password: string, policy: PasswordPolicy): boolean => {
  if (!password) return false;

  // Validate each requirement
  const checks: RequirementCheck[] = [
    checkMinLength(password, policy.minLength),
    checkUppercase(password, policy.requireUppercase),
    checkLowercase(password, policy.requireLowercase),
    checkNumber(password, policy.requireNumber),
    checkSpecialCharacter(password, policy.requireSpecial)
  ];

  // All checks must pass
  return checks.every(check => check.passed);
};

/**
 * Detailed validation function for UI display
 * @param password Password to validate
 * @param policy Password policy to apply
 * @returns Detailed validation results
 */
export const getPasswordValidationResults = (password: string, policy: PasswordPolicy): ValidationRuleResult[] => {
  const checks: RequirementCheck[] = [
    checkMinLength(password, policy.minLength),
    checkUppercase(password, policy.requireUppercase),
    checkLowercase(password, policy.requireLowercase),
    checkNumber(password, policy.requireNumber),
    checkSpecialCharacter(password, policy.requireSpecial)
  ];

  // Filter out empty messages and convert to ValidationRuleResult
  return checks
    .filter(check => check.message) // Only show rules with messages
    .map(check => ({
      rule: check.name,
      isValid: check.passed,
      message: check.message
    }));
};

/**
 * Returns validation summary for UI display
 * @param password Password to validate
 * @param policy Password policy to apply
 * @returns Summary of validation results
 */
export const getPasswordValidationSummary = (password: string, policy: PasswordPolicy) => {
  if (!password) {
    return {
      isValid: false,
      passedCount: 0,
      totalCount: 0,
      failedRules: ['Password is required']
    };
  }

  const results = getPasswordValidationResults(password, policy);
  const passedResults = results.filter(result => result.isValid);
  const failedResults = results.filter(result => !result.isValid);

  return {
    isValid: failedResults.length === 0,
    passedCount: passedResults.length,
    totalCount: results.length,
    failedRules: failedResults.map(result => result.message)
  };
};

/**
 * Returns a simple error message (compatible with existing components)
 * @param password Password to validate
 * @param policy Password policy to apply
 * @returns The first error message or undefined
 */
export const getPasswordError = (password: string, policy: PasswordPolicy): string | undefined => {
  const summary = getPasswordValidationSummary(password, policy);
  return summary.isValid ? undefined : summary.failedRules[0];
};

/**
 * Calculates password strength (additional feature)
 * @param password Password to validate
 * @param policy Password policy to apply
 * @returns Strength score between 0 and 100
 */
export const calculatePasswordStrength = (password: string, policy: PasswordPolicy): number => {
  if (!password) return 0;

  let score = 0;

  // Length score (40 points)
  const lengthScore = Math.min((password.length / policy.minLength) * 40, 40);
  score += lengthScore;

  // Uppercase score (20 points)
  if (/[A-Z]/.test(password)) score += 20;

  // Lowercase score (10 points)
  if (/[a-z]/.test(password)) score += 10;

  // Number score (15 points)
  if (/[0-9]/.test(password)) score += 15;

  // Special character score (15 points)
  if (SPECIAL_CHARACTERS.test(password)) score += 15;

  return Math.min(score, 100);
};

/**
 * Converts password policy requirements to a string array (for UI display)
 * @param policy Password policy
 * @returns Array of requirement descriptions
 */
export const getPolicyRequirements = (policy: PasswordPolicy): string[] => {
  const requirements: string[] = [];

  requirements.push(`At least ${policy.minLength} characters long`);

  if (policy.requireUppercase) {
    requirements.push('At least one uppercase letter (A-Z)');
  }

  if (policy.requireLowercase) {
    requirements.push('At least one lowercase letter (a-z)');
  }

  if (policy.requireNumber) {
    requirements.push('At least one number (0-9)');
  }

  if (policy.requireSpecial) {
    requirements.push('At least one special character (!@#$%^&*)');
  }

  return requirements;
};
