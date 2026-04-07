import { postData, getData, putData } from "../utils/api";

const API_BASE_URL = "/cas/admin/v1";

export const verifyServerUrl = async (body: any) => {
    return postData(API_BASE_URL, `servers/ping`, body);
}

export interface ServerConfigDto {
    id: number;
    configKey: string;
    configValue: string;
    description?: string;
    createdAt: string;
    updatedAt?: string;
}

export interface UpdateServerConfigReqDto {
    configs: {
        configKey: string;
        configValue: string;
        description?: string;
    }[];
}

export interface UpdateServerConfigResDto {
    updatedCount: number;
    updatedConfigs: ServerConfigDto[];
    message: string;
}

export const getAllServerConfigs = async (): Promise<{ data: ServerConfigDto[] }> => {
    return getData(API_BASE_URL, `server-configs`);
};

export const getServerConfigByKey = async (configKey: string): Promise<{ data: ServerConfigDto }> => {
    return getData(API_BASE_URL, `server-configs/${configKey}`);
};

export const updateServerConfigs = async (body: UpdateServerConfigReqDto): Promise<{ data: UpdateServerConfigResDto }> => {
    return putData(API_BASE_URL, `server-configs`, body);
};