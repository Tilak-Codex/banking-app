
import { apiClient } from "./apiClient";

export interface TransactionResponse {
  id: number;
  amount: number;
  transactionType: string;
  balanceAfter: number;
  description: string;
  transactionDate: string;
  reversed: boolean;
  bankAccountId: number;
}

export interface TransferRequest {
  sourceAccountId: number;
  destinationAccountNumber: string;
  amount: number;
  description: string;
}

export const transactionApi = {
  getByBankAccountId(
    accountId: number
  ): Promise<TransactionResponse[]> {
    return apiClient.get(
      `/accounts/${accountId}/transactions`
    );
  },

  getMyTransactions(
    accountId: number
  ): Promise<TransactionResponse[]> {
    return apiClient.get(
      `/transactions/me/${accountId}`
    );
  },

  create(data: {
    amount: number;
    transactionType: string;
    description: string;
    bankAccountId: number;
  }): Promise<TransactionResponse> {
    return apiClient.post(
      "/transactions",
      data
    );
  },

  transfer(
    data: TransferRequest
  ): Promise<TransactionResponse[]> {
    return apiClient.post(
      "/transactions/transfer",
      data
    );
  },
};
