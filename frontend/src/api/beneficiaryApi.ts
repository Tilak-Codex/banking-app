import { apiClient } from "./apiClient";

export interface BeneficiaryRequest {
  name: string;
  accountNumber: string;
  bankCode: string;
}

export interface BeneficiaryResponse {
  id: number;
  name: string;
  accountNumber: string;
  bankCode: string;
}

export const beneficiaryApi = {
  getByCustomerId(
    customerId: number
  ): Promise<BeneficiaryResponse[]> {
    return apiClient.get<BeneficiaryResponse[]>(
      `/beneficiaries/customers/${customerId}`
    );
  },
delete(id: number): Promise<void> {
  return apiClient.delete<void>(`/beneficiaries/${id}`);
},
  create(
    customerId: number,
    data: BeneficiaryRequest
  ): Promise<BeneficiaryResponse> {
    return apiClient.post<BeneficiaryResponse>(
      `/beneficiaries/customers/${customerId}`,
      data
    );
  },
};