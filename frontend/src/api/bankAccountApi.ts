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

  getById(id: number): Promise<BankAccountResponse> {
    return apiClient.get<BankAccountResponse>(`/accounts/${id}`);
  },
};