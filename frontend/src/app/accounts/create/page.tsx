"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { apiClient } from "@/api/apiClient";

interface AccountForm {
  accountNumber: string;
  balance: string;
  accountType: string;
}

export default function CreateAccountPage() {
  const router = useRouter();

  const [formData, setFormData] = useState<AccountForm>({
    accountNumber: "",
    balance: "",
    accountType: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(
    event: React.ChangeEvent<HTMLInputElement>
  ) {
    const { name, value } = event.target;

    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }));
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      await apiClient.post("/accounts", {
        accountNumber: formData.accountNumber,
        balance: Number(formData.balance),
        accountType: formData.accountType,
      });

      router.push("/accounts");
    } catch (error) {
      const apiError = error as Error & {
        status?: number;
      };

      if (apiError.status === 403) {
        setError("You are not authorized to create an account.");
      } else {
        setError(apiError.message || "Failed to create account.");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <main>
      <h1>Create Bank Account</h1>

      {error && (
        <p>
          {error}
        </p>
      )}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="accountNumber">
            Account Number
          </label>

          <input
            id="accountNumber"
            name="accountNumber"
            value={formData.accountNumber}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label htmlFor="balance">
            Balance
          </label>

          <input
            id="balance"
            name="balance"
            type="number"
            step="0.01"
            value={formData.balance}
            onChange={handleChange}
            required
          />
        </div>

        <div>
          <label htmlFor="accountType">
            Account Type
          </label>

          <input
            id="accountType"
            name="accountType"
            value={formData.accountType}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Creating..." : "Create Account"}
        </button>
      </form>

      <button
        type="button"
        onClick={() => router.push("/accounts")}
      >
        Back to Accounts
      </button>
    </main>
  );
}