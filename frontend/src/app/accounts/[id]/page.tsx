"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";
import { bankAccountApi, BankAccountResponse } from "@/api/bankAccountApi";
import { transactionApi, TransactionResponse } from "@/api/transactionApi";

export default function AccountDetailsPage() {
  const params = useParams();
  const id = Number(params.id);

  const [account, setAccount] = useState<BankAccountResponse | null>(null);
  const [transactions, setTransactions] = useState<TransactionResponse[]>([]);
  const [transactionsLoading, setTransactionsLoading] = useState(true);

  const [transactionsError, setTransactionsError] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadAccount();
    loadTransactions();
  }, []);

  async function loadAccount() {
    try {
      setLoading(true);
      setError("");

      const data = await bankAccountApi.getById(id);

      setAccount(data);
    } catch (error) {
      setError(
        error instanceof Error ? error.message : "Failed to load account",
      );
    } finally {
      setLoading(false);
    }
  }
  async function loadTransactions() {
    try {
      setTransactionsLoading(true);
      setTransactionsError("");

      const data = await transactionApi.getByBankAccountId(id);

      setTransactions(data);
    } catch (error) {
      setTransactionsError(
        error instanceof Error ? error.message : "Failed to load transactions",
      );
    } finally {
      setTransactionsLoading(false);
    }
  }
  if (loading) {
    return <p>Loading account...</p>;
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>

        <button onClick={loadAccount}>Retry</button>
      </main>
    );
  }

  if (!account) {
    return <p>Account not found.</p>;
  }

  return (
    <main> <h1>Account Details</h1> <Link href="/accounts"> Back to Accounts </Link> <br /> <br /> <p> <strong>ID:</strong> {account.id} </p> <p> <strong>Account Number:</strong> {account.accountNumber} </p> <p> <strong>Balance:</strong> {account.balance} </p> <p> <strong>Account Type:</strong> {account.accountType} </p> {/* Transaction History section remains here */} </main>
  );
}
