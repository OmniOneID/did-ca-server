import { getData, postData } from "../utils/api";

const API_BASE_URL = "/cas/admin/v1";

export const getCaInfo = async () => {
    return getData(API_BASE_URL, "ca/info");
}

export const registerCaInfo = async (data: any) => {
    return postData(API_BASE_URL, `ca/register-ca-info`, data);
};

export const generateCaDidDocument = async () => {
    return postData(API_BASE_URL, `ca/generate-did-auto`, undefined);
}

export const registerCaDidDocument = async (data: any) => {
    return postData(API_BASE_URL, `ca/register-did`, data);
};

export const requestEntityStatus = async () => {
    return getData(API_BASE_URL, "ca/request-status");
}

export const requestEnrollEntity = async () => {
    return postData(API_BASE_URL, "ca/request-enroll-entity", undefined);
}