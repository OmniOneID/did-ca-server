import { getData } from "../utils/api";

const API_BASE_URL = "/cas/admin/v1";

export const getCaInfo = async () => {
    return getData(API_BASE_URL, "ca/info");
}