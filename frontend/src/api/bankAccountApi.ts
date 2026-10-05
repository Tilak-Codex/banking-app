import { apiClient } from "./apiClient";

export interface BankAccountResponse {
  id: number;
  accountNumber: string;
  balance: number;
  accountType: string;
}

export const bankAccountApi = {
  getAll(): Promise<BankAccountResponse[]> {
    return apiClient.get<BankAccountResponse[]>("/accounts");
  },

  getMe(): Promise<BankAccountResponse[]> {
    return apiClient.get<BankAccountResponse[]>("/accounts/me");
  },

  getById(id: number): Promise<BankAccountResponse> {
    return apiClient.get<BankAccountResponse>(`/accounts/${id}`);
  },

  getMyById(id: number): Promise<BankAccountResponse> {
    return apiClient.get<BankAccountResponse>(`/accounts/me/${id}`);
  },

  search(accountNumber: string): Promise<BankAccountResponse[]> {
    return apiClient.get<BankAccountResponse[]>(
      `/accounts/search?accountNumber=${encodeURIComponent(accountNumber)}`
    );
  },
};