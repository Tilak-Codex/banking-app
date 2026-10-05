
"use client";

import { FormEvent, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { transactionApi } from "@/api/transactionApi";

export default function TransferPage() {
  const params = useParams();
  const router = useRouter();

  const accountId = Number(params.id);

  const [destinationAccountNumber, setDestinationAccountNumber] =
    useState("");

  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

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

  return (
    <main>
      <h1>Transfer Money</h1>

      <p>
        Source Account ID: <strong>{accountId}</strong>
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

