import { apiClient } from "./apiClient";

export interface TransactionResponse {
  id: number;
  amount: number;
  transactionType: string;
  description: string;
  transactionDate: string;
  reversed: boolean;
  bankAccountId: number;
}

export const transactionApi = {
  getByBankAccountId(
    accountId: number
  ): Promise<TransactionResponse[]> {
    return apiClient.get<TransactionResponse[]>(
      `/accounts/${accountId}/transactions`
    );
  },
  create(data: {
  amount: number;
  transactionType: string;
  description: string;
  bankAccountId: number;
}): Promise<TransactionResponse> {
  return apiClient.post<TransactionResponse>(
    "/transactions",
    data
  );
},
};