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

  const validAccountId =
    Number.isInteger(accountId) && accountId > 0;

  const isClient =
    keycloak.realmAccess?.roles.includes("CLIENT") ?? false;

  useEffect(() => {
    if (!validAccountId) {
      setError("Invalid account ID.");
      setLoading(false);
      setTransactionsLoading(false);
      return;
    }

    loadAccount();
    loadTransactions();
  }, [accountId, validAccountId]);

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

      const data = isClient
        ? await transactionApi.getMyTransactions(accountId)
        : await transactionApi.getByBankAccountId(accountId);

      setTransactions(data);
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
        <strong>ID:</strong> {account.id}
      </p>

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

      <div>
        <Link
          href={`/accounts/${account.id}/transactions/create?type=DEPOSIT`}
          className="btn"
        >
          Deposit
        </Link>

        {" "}

        <Link
          href={`/accounts/${account.id}/transactions/create?type=WITHDRAWAL`}
          className="btn"
        >
          Withdraw
        </Link>

        {" "}

        <Link
          href={`/accounts/${account.id}/transfer`}
          className="btn"
        >
          Transfer
        </Link>
      </div>

      <br />

      <h2>Transaction History</h2>

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
        <ul>
          {transactions.map((transaction) => (
            <li key={transaction.id}>
              <p>
                <strong>Transaction ID:</strong>{" "}
                {transaction.id}
              </p>

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
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}