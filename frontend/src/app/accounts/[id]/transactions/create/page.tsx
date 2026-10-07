
"use client";

import { FormEvent, useEffect, useState } from "react";

import { useParams, useRouter, useSearchParams } from "next/navigation";

import { transactionApi } from "@/api/transactionApi";
import { useNotification } from "@/components/NotificationProvider";

export default function CreateTransactionPage() {
  const params = useParams();
  const router = useRouter();
  const searchParams = useSearchParams();

  const { showSuccess, showError } = useNotification();

  const accountId = Number(params.id);

  const [amount, setAmount] = useState("");
  const [transactionType, setTransactionType] = useState("DEPOSIT");
  const [description, setDescription] = useState("");

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const type = searchParams.get("type");

    if (type === "DEPOSIT" || type === "WITHDRAWAL") {
      setTransactionType(type);
    }
  }, [searchParams]);

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

      showSuccess(
        transactionType === "DEPOSIT"
          ? "Deposit successful"
          : "Withdrawal successful"
      );

      setTimeout(() => {
        router.push(`/accounts/${accountId}`);
      }, 1000);
    } catch (error) {
      const apiError = error as Error & {
        status?: number;
      };

      const message =
        apiError.message || "Failed to create transaction.";

      setError(message);
      showError(message);
    } finally {
      setLoading(false);
    }
  }

  const transactionTitle =
    transactionType === "DEPOSIT"
      ? "Deposit Money"
      : "Withdraw Money";

  return (
    <main>
      <h1>{transactionTitle}</h1>

      {/* <p>Account ID: {accountId}</p> */}

      {error && <p>{error}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="amount">
            Amount
          </label>

          <input
            id="amount"
            type="number"
            step="0.01"
            min="0.01"
            value={amount}
            onChange={(event) =>
              setAmount(event.target.value)
            }
            required
          />
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

        <button
          type="submit"
          disabled={loading}
        >
          {loading
            ? "Processing..."
            : transactionTitle}
        </button>
      </form>

      <button
        type="button"
        onClick={() =>
          router.push(`/accounts/${accountId}`)
        }
      >
        Back to Account
      </button>
    </main>
  );
}
