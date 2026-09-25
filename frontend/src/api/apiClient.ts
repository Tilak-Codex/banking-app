import keycloak from "@/auth/keycloak";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

if (!API_BASE_URL) {
  throw new Error("NEXT_PUBLIC_API_BASE_URL is not configured");
}

async function request<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  if (keycloak.authenticated) {
    try {
      await keycloak.updateToken(30);
    } catch (error) {
      console.error("Failed to refresh Keycloak token:", error);
      throw new Error("Authentication session expired");
    }
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    headers: {
      "Content-Type": "application/json",

      ...(keycloak.token
        ? {
            Authorization: `Bearer ${keycloak.token}`,
          }
        : {}),

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

  if (response.status === 401) {
    error.message = "Authentication required";
  }

  if (response.status === 403) {
    error.message = "You are not authorized to perform this action";
  }

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