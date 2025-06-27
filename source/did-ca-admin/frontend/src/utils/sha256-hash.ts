import CryptoJS from 'crypto-js';

export const sha256Hash = async (input: string): Promise<string> => {
  const hash = CryptoJS.SHA256(input).toString();
  return hash;
};
