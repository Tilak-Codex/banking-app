
"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

import {
  bankAccountApi,
  BankAccountResponse,
} from "@/api/bankAccountApi";

import keycloak from "@/auth/keycloak";

export default function AccountsPage() {
  const [accounts, setAccounts] = useState<BankAccountResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [searchTerm, setSearchTerm] = useState("");
  const [searchAccountNumber, setSearchAccountNumber] = useState("");

  const [pageNo, setPageNo] = useState(0);
  const [pageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [sort, setSort] = useState("id,asc");

  const isClient =
    keycloak.realmAccess?.roles.includes("CLIENT") ?? false;

  useEffect(() => {
    loadAccounts();
  }, [pageNo, searchAccountNumber, sort]);

  async function loadAccounts() {
    try {
      setLoading(true);
      setError("");

      if (isClient) {
        const data = await bankAccountApi.getMe();

        const filteredAccounts = searchAccountNumber
          ? data.filter((account) =>
              account.accountNumber
                .toLowerCase()
                .includes(searchAccountNumber.toLowerCase())
            )
          : data;

        setAccounts(filteredAccounts);
        setTotalElements(filteredAccounts.length);
        setTotalPages(1);
      } else {
        const data = await bankAccountApi.getAll(
          pageNo,
          pageSize,
          sort,
          searchAccountNumber
        );

        setAccounts(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
      }
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to load accounts"
      );
    } finally {
      setLoading(false);
    }
  }

  function handleSearch(event: React.FormEvent) {
    event.preventDefault();

    setPageNo(0);
    setSearchAccountNumber(searchTerm.trim());
  }

  function handleClear() {
    setSearchTerm("");
    setSearchAccountNumber("");
    setPageNo(0);
  }

  function handleSortChange(
    event: React.ChangeEvent<HTMLSelectElement>
  ) {
    setSort(event.target.value);
    setPageNo(0);
  }

  function handlePrevious() {
    if (pageNo > 0) {
      setPageNo((currentPage) => currentPage - 1);
    }
  }

  function handleNext() {
    if (pageNo < totalPages - 1) {
      setPageNo((currentPage) => currentPage + 1);
    }
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>

        <button onClick={loadAccounts}>
          Retry
        </button>
      </main>
    );
  }

  return (
    <main>
      <h1>Bank Accounts</h1>

      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Enter account number"
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.target.value)}
        />

        <button type="submit">
          Search
        </button>

        <button
          type="button"
          onClick={handleClear}
        >
          Clear
        </button>
      </form>

      <br />

      {!isClient && (
        <>
          <label>
            Sort by:{" "}
            <select
              value={sort}
              onChange={handleSortChange}
            >
              <option value="id,asc">
                ID - Ascending
              </option>

              <option value="id,desc">
                ID - Descending
              </option>

              <option value="accountNumber,asc">
                Account Number - A to Z
              </option>

              <option value="accountNumber,desc">
                Account Number - Z to A
              </option>

              <option value="balance,asc">
                Balance - Low to High
              </option>

              <option value="balance,desc">
                Balance - High to Low
              </option>
            </select>
          </label>

          <br />
          <br />
        </>
      )}

      {loading ? (
        <p>Loading accounts...</p>
      ) : accounts.length === 0 ? (
        <p>No accounts found.</p>
      ) : (
        <>
          <p>
            Showing {totalElements} account
            {totalElements !== 1 ? "s" : ""}.
          </p>

          <ul>
            {accounts.map((account) => (
              <li key={account.id}>
                <Link href={`/accounts/${account.id}`}>
                  <strong>{account.accountNumber}</strong>
                </Link>

                <br />

                Balance: {account.balance}

                <br />

                Type: {account.accountType}
              </li>
            ))}
          </ul>

          {!isClient && totalPages > 0 && (
            <div>
              <button
                onClick={handlePrevious}
                disabled={pageNo === 0}
              >
                Previous
              </button>

              <span>
                {" "}
                Page {pageNo + 1} of {totalPages}{" "}
              </span>

              <button
                onClick={handleNext}
                disabled={pageNo >= totalPages - 1}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </main>
  );
}
