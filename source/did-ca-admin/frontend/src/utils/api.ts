import { tokenStore } from './authTokens';

type ApiResponse = { url: string; data: any };
type ApiErrorResponse = { url: string; status: number; code: string; message: string };

async function refreshAccessToken(baseUrl: string): Promise<void> {
  const refreshToken = tokenStore.getRefresh();
  if (!refreshToken) throw new Error('NO_REFRESH_TOKEN');
  const response = await fetch(`${baseUrl}/refresh-token`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken }),
  });
  if (!response.ok) throw new Error('REFRESH_FAILED');
  const data = await response.json();
  if (!data.accessToken) throw new Error('NO_ACCESS_TOKEN');
  tokenStore.writeAccess(data.accessToken);
}

const requestApi = async (
  baseUrl: string,
  endpoint: string,
  method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE',
  body?: any,
  retry = false,
): Promise<ApiResponse> => {
  const fullUrl = `${baseUrl}/${endpoint}`;
  const isFormData = body instanceof FormData;
  const isAdmin = baseUrl.includes('/admin/');
  const isLogin = endpoint === 'login';
  if (isAdmin && !isLogin && tokenStore.isPendingLogout()) {
    sessionStorage.removeItem('session');
    window.location.replace('/sign-in');
    throw new Error('SESSION_EXPIRED');
  }
  const accessToken = isAdmin && !isLogin ? tokenStore.getAccess() : null;
  const headers: Record<string, string> = {};
  if (!isFormData) headers['Content-Type'] = 'application/json';
  if (accessToken) headers.Authorization = `Bearer ${accessToken}`;
  const response = await fetch(fullUrl, {
    method,
    headers,
    body: isFormData ? body : body ? JSON.stringify(body) : undefined,
  });
  if (response.status === 403 && isAdmin && !accessToken) {
    tokenStore.clear();
    sessionStorage.removeItem('session');
    localStorage.removeItem('session');
    window.location.replace('/sign-in');
    throw new Error('SESSION_EXPIRED');
  }
  if (response.status === 401 && isAdmin && !isLogin && !retry) {
    try {
      await refreshAccessToken(baseUrl);
      return requestApi(baseUrl, endpoint, method, body, true);
    } catch {
      tokenStore.clear();
      sessionStorage.removeItem('session');
      localStorage.removeItem('session');
      window.location.replace('/sign-in');
      throw new Error('SESSION_EXPIRED');
    }
  }
  if (response.status === 401 && isAdmin && !isLogin && retry) {
    tokenStore.clear();
    sessionStorage.removeItem('session');
    localStorage.removeItem('session');
    window.location.replace('/sign-in');
    throw new Error('SESSION_EXPIRED');
  }
  const text = await response.text();
  let data: any = {};
  if (text) {
    try { data = JSON.parse(text); } catch { /* non-JSON response */ }
  }
  if (!response.ok) {
    throw {
      url: fullUrl,
      status: response.status,
      code: typeof data.code === 'string' ? data.code : 'UNKNOWN_ERROR',
      message: typeof data.description === 'string' ? data.description : `HTTP error! Status: ${response.status}`,
    } as ApiErrorResponse;
  }
  if (isAdmin && isLogin && data.accessToken && data.refreshToken) {
    tokenStore.save(data.accessToken, data.refreshToken, localStorage.getItem('rememberMe') === 'true');
  }
  return { url: fullUrl, data };
};

export const getData = (baseUrl: string, endpoint: string): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, 'GET');
export const postData = (baseUrl: string, endpoint: string, body: any): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, 'POST', body);
export const putData = (baseUrl: string, endpoint: string, body: any): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, 'PUT', body);
export const deleteData = (baseUrl: string, endpoint: string): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, 'DELETE');
export const uploadData = (baseUrl: string, endpoint: string, formData: FormData): Promise<ApiResponse> =>
  requestApi(baseUrl, endpoint, 'POST', formData);
