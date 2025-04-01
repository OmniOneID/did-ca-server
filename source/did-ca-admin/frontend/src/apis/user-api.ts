import { getData } from "../utils/api";

const API_BASE_URL = "/cas/admin/v1";

export const fetchUserList = async (page: number, size: number, searchKey: string|null, searchValue: string|null) => {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
    });

    if (searchKey && searchValue) {
        params.append("searchKey", searchKey);
        params.append("searchValue", searchValue);
    }

    return getData(API_BASE_URL, `users/list?${params.toString()}`);
}

export const getUserInfo = async (id: number) => {
    return getData(API_BASE_URL, `users?id=${id}`);
}
