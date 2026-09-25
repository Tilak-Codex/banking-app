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

export const customerApi = {
  getAll(): Promise<CustomerResponse[]> {
    return apiClient.get<CustomerResponse[]>("/customers");
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