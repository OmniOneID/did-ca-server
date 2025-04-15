import { postData } from "../utils/api";

const API_BASE_URL = "/cas/admin/v1";

export const verifyServerUrl = async (body: any) => {
    return postData(API_BASE_URL, `servers/ping`, body);
}