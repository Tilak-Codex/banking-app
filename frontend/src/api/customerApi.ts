import { apiClient } from "./apiClient";

export interface CustomerRequest {
  name: string;
  email: string;
  phoneNumber: string;
}

export interface CustomerResponse {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
}
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export const customerApi = {
   getAll(
  pageNo = 0,
  pageSize = 5,
  sort = "id,asc",
  name?: string
): Promise<PageResponse<CustomerResponse>> {

  const params = new URLSearchParams({
    pageNo: pageNo.toString(),
    pageSize: pageSize.toString(),
    sort,
  });

  if (name) {
    params.append("name", name);
  }

  return apiClient.get<PageResponse<CustomerResponse>>(
    `/customers?${params.toString()}`
  );
},

  getMe(): Promise<CustomerResponse> {
    return apiClient.get<CustomerResponse>("/customers/me");
  },

  getById(id: number): Promise<CustomerResponse> {
    return apiClient.get<CustomerResponse>(`/customers/${id}`);
  },

  create(data: CustomerRequest): Promise<CustomerResponse> {
    return apiClient.post<CustomerResponse>("/customers", data);
  },

  update(
    id: number,
    data: CustomerRequest
  ): Promise<CustomerResponse> {
    return apiClient.put<CustomerResponse>(
      `/customers/${id}`,
      data
    );
  },

  delete(id: number): Promise<void> {
    return apiClient.delete<void>(`/customers/${id}`);
  },
};