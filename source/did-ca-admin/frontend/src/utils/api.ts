type ApiResponse = { url: string; data: any };

type ApiErrorResponse = {
  url: string;
  status: number;
  code: string;
  message: string;
};

const requestApi = async (
  baseUrl: string,
  endpoint: string,
  method: "GET" | "POST" | "PUT" | "DELETE",
  body?: any
): Promise<ApiResponse> => {
  const fullUrl = `${baseUrl}/${endpoint}`;
  const isFormData = body instanceof FormData;

  const options: RequestInit = {
    method,
    headers: isFormData ? undefined : { "Content-Type": "application/json" },
    body: isFormData ? body : body ? JSON.stringify(body) : undefined,
  };

  try {
    const response = await fetch(fullUrl, options);
    const text = await response.text();
    let data: Record<string, any> = {};

    if (text) {
      try {
        data = JSON.parse(text);
      } catch (error) {
        console.warn("Failed to parse JSON response:", error);
      }
    }

    if (!response.ok) {
      throw {
        url: fullUrl,
        status: response.status,
        code: typeof data.code === "string" ? data.code : "UNKNOWN_ERROR",
        message: typeof data.description === "string" ? data.description : `HTTP error! Status: ${response.status}`,
      } as ApiErrorResponse;
    }

    return { url: fullUrl, data };
  } catch (error) {
    throw {
      url: fullUrl,
      status: (error as ApiErrorResponse).status || 500,
      code: (error as ApiErrorResponse).code || "NETWORK_ERROR",
      message: (error as ApiErrorResponse).message || "Failed to connect to the network.",
    };
  }
};

export const getData = (baseUrl: string, endpoint: string): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, "GET");

export const postData = (baseUrl: string, endpoint: string, body: any): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, "POST", body);

export const putData = (baseUrl: string, endpoint: string, body: any): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, "PUT", body);

export const deleteData = (baseUrl: string, endpoint: string): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, "DELETE");

export const uploadData = (baseUrl: string, endpoint: string, formData: FormData): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, "POST", formData);