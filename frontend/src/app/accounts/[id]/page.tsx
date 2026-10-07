
"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Link from "next/link";

import {
  bankAccountApi,
  BankAccountResponse,
} from "@/api/bankAccountApi";

import {
  transactionApi,
  TransactionResponse,
} from "@/api/transactionApi";

import keycloak from "@/auth/keycloak";

export default function AccountDetailsPage() {
  const params = useParams();
  const router = useRouter();

  const accountId = Number(params.id);

  const [account, setAccount] =
    useState<BankAccountResponse | null>(null);

  const [transactions, setTransactions] =
    useState<TransactionResponse[]>([]);

  const [transactionsLoading, setTransactionsLoading] =
    useState(true);

  const [transactionsError, setTransactionsError] =
    useState("");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Transaction pagination
  const [pageNo, setPageNo] = useState(0);
  const [pageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Transaction search
  const [search, setSearch] = useState("");
  const [searchText, setSearchText] = useState("");

  // Transaction filter
  const [transactionType, setTransactionType] =
    useState("");

  // Transaction sorting
  const [sort, setSort] =
    useState("transactionDate,desc");

  const validAccountId =
    Number.isInteger(accountId) && accountId > 0;

  const isClient =
    keycloak.realmAccess?.roles.includes("CLIENT") ?? false;

  const isMaker =
    keycloak.realmAccess?.roles.includes("MAKER") ?? false;

  useEffect(() => {
    if (!validAccountId) {
      setError("Invalid account ID.");
      setLoading(false);
      setTransactionsLoading(false);
      return;
    }

    loadAccount();
  }, [accountId, validAccountId]);

  useEffect(() => {
    if (!validAccountId) {
      return;
    }

    loadTransactions();
  }, [
    accountId,
    validAccountId,
    pageNo,
    transactionType,
    searchText,
    sort,
  ]);

  async function loadAccount() {
    try {
      setLoading(true);
      setError("");

      const data = isClient
        ? await bankAccountApi.getMyById(accountId)
        : await bankAccountApi.getById(accountId);

      setAccount(data);
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to load account"
      );
    } finally {
      setLoading(false);
    }
  }

  async function loadTransactions() {
    try {
      setTransactionsLoading(true);
      setTransactionsError("");

      const filters = {
        page: pageNo,
        size: pageSize,
        sort,
        transactionType:
          transactionType || undefined,
        search:
          searchText.trim() || undefined,
      };

      const data = isClient
        ? await transactionApi.getMyTransactions(
            accountId,
            filters
          )
        : await transactionApi.getByBankAccountId(
            accountId,
            filters
          );

      setTransactions(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (error) {
      setTransactionsError(
        error instanceof Error
          ? error.message
          : "Failed to load transactions"
      );
    } finally {
      setTransactionsLoading(false);
    }
  }

  function handleSearch(event: React.FormEvent) {
    event.preventDefault();

    setPageNo(0);
    setSearchText(search);
  }

  function handleClearSearch() {
    setSearch("");
    setSearchText("");
    setPageNo(0);
  }

  function handleTransactionTypeChange(
    event: React.ChangeEvent<HTMLSelectElement>
  ) {
    setTransactionType(event.target.value);
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

  if (!validAccountId) {
    return (
      <main>
        <h1>Invalid Account</h1>

        <p>
          The account ID is missing or invalid.
        </p>

        <button
          type="button"
          onClick={() => router.push("/accounts")}
        >
          Back to Accounts
        </button>
      </main>
    );
  }

  if (loading) {
    return <p>Loading account...</p>;
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>

        <button onClick={loadAccount}>
          Retry
        </button>
      </main>
    );
  }

  if (!account) {
    return <p>Account not found.</p>;
  }

  return (
    <main>
      <h1>Account Details</h1>

      <Link href="/accounts">
        Back to Accounts
      </Link>

      <br />
      <br />

      <p>
        <strong>Account Number:</strong>{" "}
        {account.accountNumber}
      </p>

      <p>
        <strong>Balance:</strong>{" "}
        {account.balance}
      </p>

      <p>
        <strong>Account Type:</strong>{" "}
        {account.accountType}
      </p>

      <br />

      {isMaker && (
        <div>
          <Link
            href={`/accounts/${account.id}/transactions/create?type=DEPOSIT`}
            className="btn"
          >
            Deposit
          </Link>{" "}

          <Link
            href={`/accounts/${account.id}/transactions/create?type=WITHDRAWAL`}
            className="btn"
          >
            Withdraw
          </Link>{" "}

          <Link
            href={`/accounts/${account.id}/transfer`}
            className="btn"
          >
            Transfer
          </Link>
        </div>
      )}

      <br />

      <h2>Transaction History</h2>

      {/* Search and filters */}
      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Search description"
          value={search}
          onChange={(event) =>
            setSearch(event.target.value)
          }
        />

        <button type="submit">
          Search
        </button>

        <button
          type="button"
          onClick={handleClearSearch}
        >
          Clear
        </button>
      </form>

      <br />

      <label>
        Transaction Type:{" "}

        <select
          value={transactionType}
          onChange={handleTransactionTypeChange}
        >
          <option value="">
            All
          </option>

          <option value="DEPOSIT">
            Deposit
          </option>

          <option value="WITHDRAWAL">
            Withdrawal
          </option>
        </select>
      </label>

      <br />
      <br />

      <label>
        Sort By:{" "}

        <select
          value={sort}
          onChange={handleSortChange}
        >
          <option value="transactionDate,desc">
            Newest First
          </option>

          <option value="transactionDate,asc">
            Oldest First
          </option>

          <option value="amount,desc">
            Amount: High to Low
          </option>

          <option value="amount,asc">
            Amount: Low to High
          </option>
        </select>
      </label>

      <br />
      <br />

      {transactionsLoading ? (
        <p>Loading transactions...</p>
      ) : transactionsError ? (
        <div>
          <p>{transactionsError}</p>

          <button onClick={loadTransactions}>
            Retry
          </button>
        </div>
      ) : transactions.length === 0 ? (
        <p>No transactions found.</p>
      ) : (
        <>
          <p>
            Showing {transactions.length} of{" "}
            {totalElements} transactions
          </p>

          <ul>
            {transactions.map((transaction) => (
              <li key={transaction.id}>
                <p>
                  <strong>Amount:</strong>{" "}
                  {transaction.amount}
                </p>

                <p>
                  <strong>Type:</strong>{" "}
                  {transaction.transactionType}
                </p>

                <p>
                  <strong>Balance After:</strong>{" "}
                  {transaction.balanceAfter}
                </p>

                <p>
                  <strong>Description:</strong>{" "}
                  {transaction.description}
                </p>

                <p>
                  <strong>Date:</strong>{" "}
                  {new Date(
                    transaction.transactionDate
                  ).toLocaleString()}
                </p>

                <p>
                  <strong>Reversed:</strong>{" "}
                  {transaction.reversed
                    ? "Yes"
                    : "No"}
                </p>

                <hr />
              </li>
            ))}
          </ul>

          <div>
            <button
              type="button"
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
              type="button"
              onClick={handleNext}
              disabled={
                pageNo >= totalPages - 1
              }
            >
              Next
            </button>
          </div>
        </>
      )}
    </main>
  );
}
