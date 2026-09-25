const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;   // loaded our bankend url from .env.local file and stored in API_BASE_URL variable. This variable will be used to make API requests to the backend server.

// to avoid boilerplate code for making API requests, we created this file to make API requests to the backend server. This file will be used in the frontend code to make API requests to the backend server.
if (!API_BASE_URL) {
  throw new Error("NEXT_PUBLIC_API_BASE_URL is not configured");
}

async function request<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    let errorData: {
      message?: string;
      validationErrors?: Record<string, string>;
    } = {};

    try {
      errorData = await response.json();
    } catch {
      // Response does not contain JSON
    }

    const error = new Error(
      errorData.message || "Request failed"
    ) as Error & {
      status?: number;
      validationErrors?: Record<string, string>;
    };

    error.status = response.status;
    error.validationErrors = errorData.validationErrors;

    throw error;
  }

  if (response.status === 204) {
    return null as T;
  }

  return response.json();
}

export const apiClient = {
  get<T>(endpoint: string) {
    return request<T>(endpoint);
  },

  post<T>(endpoint: string, data: unknown) {
    return request<T>(endpoint, {
      method: "POST",
      body: JSON.stringify(data),
    });
  },

  put<T>(endpoint: string, data: unknown) {
    return request<T>(endpoint, {
      method: "PUT",
      body: JSON.stringify(data),
    });
  },

  delete<T>(endpoint: string) {
    return request<T>(endpoint, {
      method: "DELETE",
    });
  },
};