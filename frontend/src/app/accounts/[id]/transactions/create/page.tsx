"use client";

import { FormEvent, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { transactionApi } from "@/api/transactionApi";

export default function CreateTransactionPage() {
  const params = useParams();
  const router = useRouter();

  const accountId = Number(params.id);

  const [amount, setAmount] = useState("");
  const [transactionType, setTransactionType] = useState("DEPOSIT");
  const [description, setDescription] = useState("");

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      await transactionApi.create({
        amount: Number(amount),
        transactionType,
        description,
        bankAccountId: accountId,
      });

      router.push(`/accounts/${accountId}`);
    } catch (error) {
      const apiError = error as Error & {
        status?: number;
      };

      setError(
        apiError.message || "Failed to create transaction."
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <main>
      <h1>Create Transaction</h1>

      <p>Account ID: {accountId}</p>

      {error && <p>{error}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="amount">Amount</label>

          <input
            id="amount"
            type="number"
            step="0.01"
            min="0.01"
            value={amount}
            onChange={(event) => setAmount(event.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="transactionType">
            Transaction Type
          </label>

          <select
            id="transactionType"
            value={transactionType}
            onChange={(event) =>
              setTransactionType(event.target.value)
            }
          >
            <option value="DEPOSIT">DEPOSIT</option>
            <option value="WITHDRAWAL">WITHDRAWAL</option>
          </select>
        </div>

        <div>
          <label htmlFor="description">
            Description
          </label>

          <input
            id="description"
            value={description}
            onChange={(event) =>
              setDescription(event.target.value)
            }
            required
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Creating..." : "Create Transaction"}
        </button>
      </form>

      <button
        type="button"
        onClick={() => router.push(`/accounts/${accountId}`)}
      >
        Back to Account
      </button>
    </main>
  );
}