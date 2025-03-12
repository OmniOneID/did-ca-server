import { postData, getData, deleteData } from "../utils/api";

const API_BASE_URL = "/cas/admin/v1";

export const requestLogin = async (data: any) => {
    return postData(API_BASE_URL, `login`, data);
};

export const requestPasswordReset = async (data: any) => {
    return postData(API_BASE_URL, `admins/reset-password`, data);
}

export const fetchAdminList = async (page: number, size: number, searchKey: string|null, searchValue: string|null) => {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
    });

    if (searchKey && searchValue) {
        params.append("searchKey", searchKey);
        params.append("searchValue", searchValue);
    }

    return getData(API_BASE_URL, `admins/list?${params.toString()}`);
}

export const getAdminInfo = async (id: number) => {
    return getData(API_BASE_URL, `admins?id=${id}`);
}

export const verifyAdminIdUnique = async (loginId: string) => {
    return getData(API_BASE_URL, `admins/check-admin-id?loginId=${loginId}`);
}

export const registerAdmin = async (data: any) => {
    return postData(API_BASE_URL, 'admins', data);
}


export const deleteAdmin = async (id: number) => {  
    return deleteData(API_BASE_URL, `admins?id=${id}`);
}

export const requestPasswordResetByRoot = async (data: any) => {
    return postData(API_BASE_URL, `admins/root/reset-password`, data);
}