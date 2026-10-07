
import { apiClient } from "./apiClient";

export interface BankAccountResponse {
  id: number;
  accountNumber: string;
  balance: number;
  accountType: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export const bankAccountApi = {
  getAll(
    pageNo = 0,
    pageSize = 5,
    sort = "id,asc",
    accountNumber?: string
  ): Promise<PageResponse<BankAccountResponse>> {
    const params = new URLSearchParams({
      pageNo: pageNo.toString(),
      pageSize: pageSize.toString(),
      sort,
    });

    if (accountNumber) {
      params.append("accountNumber", accountNumber);
    }

    return apiClient.get<PageResponse<BankAccountResponse>>(
      `/accounts?${params.toString()}`
    );
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
};