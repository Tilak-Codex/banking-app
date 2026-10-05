
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

  const isClient = keycloak.realmAccess?.roles.includes("CLIENT");

  useEffect(() => {
    loadAccounts();
  }, []);

  async function loadAccounts() {
    try {
      setLoading(true);
      setError("");

      const data = isClient
        ? await bankAccountApi.getMe()
        : await bankAccountApi.getAll();

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

  async function searchAccounts() {
    try {
      setLoading(true);
      setError("");

      if (!searchTerm.trim()) {
        await loadAccounts();
        return;
      }

      if (isClient) {
        const myAccounts = await bankAccountApi.getMe();

        const filteredAccounts = myAccounts.filter(
          (account) =>
            account.accountNumber
              .toLowerCase()
              .includes(searchTerm.trim().toLowerCase())
        );

        setAccounts(filteredAccounts);
      } else {
        const data = await bankAccountApi.search(searchTerm);

        setAccounts(data);
      }
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to search accounts"
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

      <div>
        <input
          type="text"
          placeholder="Enter account number"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />

        <button onClick={searchAccounts}>
          Search
        </button>

        <button
          onClick={() => {
            setSearchTerm("");
            loadAccounts();
          }}
        >
          Clear
        </button>
      </div>

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
