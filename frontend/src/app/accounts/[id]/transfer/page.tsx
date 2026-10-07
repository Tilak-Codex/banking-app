
"use client";

import { FormEvent, useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { transactionApi } from "@/api/transactionApi";
import {
  bankAccountApi,
  BankAccountResponse,
} from "@/api/bankAccountApi";

export default function TransferPage() {
  const params = useParams();
  const router = useRouter();

  const accountId = Number(params.id);

  const [account, setAccount] =
    useState<BankAccountResponse | null>(null);

  const [destinationAccountNumber, setDestinationAccountNumber] =
    useState("");

  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [accountLoading, setAccountLoading] = useState(true);

  useEffect(() => {
    loadAccount();
  }, [accountId]);

  async function loadAccount() {
    try {
      setAccountLoading(true);
      setError("");

      const data = await bankAccountApi.getById(accountId);
      setAccount(data);
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to load source account."
      );
    } finally {
      setAccountLoading(false);
    }
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      await transactionApi.transfer({
        sourceAccountId: accountId,
        destinationAccountNumber,
        amount: Number(amount),
        description,
      });

      router.push(`/accounts/${accountId}`);
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to transfer money."
      );
    } finally {
      setLoading(false);
    }
  }

  if (accountLoading) {
    return <p>Loading source account...</p>;
  }

  if (!account) {
    return (
      <main>
        <h1>Transfer Money</h1>
        <p>{error || "Source account not found."}</p>

        <button
          type="button"
          onClick={() => router.push(`/accounts/${accountId}`)}
        >
          Back to Account
        </button>
      </main>
    );
  }

  return (
    <main>
      <h1>Transfer Money</h1>

      <p>
        <strong>Source Account Number:</strong>{" "}
        {account.accountNumber}
      </p>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="destinationAccountNumber">
            Destination Account Number
          </label>

          <input
            id="destinationAccountNumber"
            type="text"
            value={destinationAccountNumber}
            onChange={(event) =>
              setDestinationAccountNumber(event.target.value)
            }
            placeholder="Enter account number"
            required
          />
        </div>

        <div>
          <label htmlFor="amount">
            Amount
          </label>

          <input
            id="amount"
            type="number"
            value={amount}
            onChange={(event) =>
              setAmount(event.target.value)
            }
            required
            min="0.01"
            step="0.01"
          />
        </div>

        <div>
          <label htmlFor="description">
            Description
          </label>

          <input
            id="description"
            type="text"
            value={description}
            onChange={(event) =>
              setDescription(event.target.value)
            }
          />
        </div>

        {error && <p>{error}</p>}

        <button
          type="submit"
          disabled={loading}
        >
          {loading ? "Transferring..." : "Transfer Money"}
        </button>
      </form>

      <button
        type="button"
        onClick={() =>
          router.push(`/accounts/${accountId}`)
        }
      >
        Cancel
      </button>
    </main>
  );
}
