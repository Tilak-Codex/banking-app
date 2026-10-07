
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

export interface TransactionPage {
  content: TransactionResponse[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface TransactionFilter {
  page?: number;
  size?: number;
  sort?: string;
  transactionType?: string;
  search?: string;
}

export interface TransferRequest {
  sourceAccountId: number;
  destinationAccountNumber: string;
  amount: number;
  description: string;
}

function buildQueryParams(
  filters: TransactionFilter = {}
): string {
  const params = new URLSearchParams();

  if (filters.page !== undefined) {
    params.append("page", String(filters.page));
  }

  if (filters.size !== undefined) {
    params.append("size", String(filters.size));
  }

  if (filters.sort) {
    params.append("sort", filters.sort);
  }

  if (filters.transactionType) {
    params.append(
      "transactionType",
      filters.transactionType
    );
  }

  if (filters.search) {
    params.append("search", filters.search);
  }

  const queryString = params.toString();

  return queryString ? `?${queryString}` : "";
}

export const transactionApi = {
  getByBankAccountId(
    accountId: number,
    filters: TransactionFilter = {}
  ): Promise<TransactionPage> {
    return apiClient.get(
      `/transactions/account/${accountId}${buildQueryParams(filters)}`
    );
  },

  getMyTransactions(
    accountId: number,
    filters: TransactionFilter = {}
  ): Promise<TransactionPage> {
    return apiClient.get(
      `/transactions/me/${accountId}${buildQueryParams(filters)}`
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
