export const urlRegex = /^(https?:\/\/)(localhost|[a-zA-Z0-9.-]+(?:\.[a-zA-Z]{2,})?)(:\d+)?(\/\S*)?$/;
export const ipRegex = /^(https?:\/\/)(\d{1,3}\.){3}\d{1,3}(:\d+)?(\/\S*)?$/;
export const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
export const phoneRegex = /^\d{3}-\d{3,4}-\d{4}$/;
export const englishRegex= /^[a-zA-Z]*$/;
export const hostRegex = /^(localhost|((?!-)[a-zA-Z0-9-]+(?:\.[a-zA-Z]{2,})+)|(25[0-5]|2[0-4][0-9]|1\d{2}|\d{1,2})\.(25[0-5]|2[0-4][0-9]|1\d{2}|\d{1,2})\.(25[0-5]|2[0-4][0-9]|1\d{2}|\d{1,2})\.(25[0-5]|2[0-4][0-9]|1\d{2}|\d{1,2}))$/;
export const portRegex = /^(0|[1-9]\d{0,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;

