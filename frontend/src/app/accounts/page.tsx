"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import {
  bankAccountApi,
  BankAccountResponse,
} from "@/api/bankAccountApi";

export default function AccountsPage() {
  const [accounts, setAccounts] = useState<BankAccountResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadAccounts();
  }, []);

  async function loadAccounts() {
    try {
      setLoading(true);
      setError("");

      const data = await bankAccountApi.getAll();

      setAccounts(data);
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

  if (loading) {
    return <p>Loading accounts...</p>;
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

      {accounts.length === 0 ? (
        <p>No accounts found.</p>
      ) : (
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
      )}
    </main>
  );
}