import CryptoJS from 'crypto-js';

export const sha256Hash = async (input: string): Promise<string> => {
  console.log('input', input);
  const hash = CryptoJS.SHA256(input).toString();
  console.log('bbb', input);
  return hash;
};
