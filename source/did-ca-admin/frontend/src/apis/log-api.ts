import { getData } from "../utils/api";

const API_BASE_URL = "/cas/admin/v1";

export const fetchApiLogs = async (page: number, size: number, searchKey: string | null, searchValue: string | null) => {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
    });

    if (searchKey && searchValue) {
        params.append("searchKey", searchKey);
        params.append("searchValue", searchValue);
    }

    return getData(API_BASE_URL, `logs?${params.toString()}`);
}


export const fetchApiLogDetail = async (id: number | string) => {
    return getData(API_BASE_URL, `logs/${id}`);
}


export const fetchAuditApiLogs = async (page: number, size: number, searchKey: string | null, searchValue: string | null) => {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
    });

    if (searchKey && searchValue) {
        params.append("searchKey", searchKey);
        params.append("searchValue", searchValue);
    }

    return getData(API_BASE_URL, `logs/audit?${params.toString()}`);
}


export const fetchAuditApiLogDetail = async (id: number | string) => {
    return getData(API_BASE_URL, `logs/audit/${id}`);
}
