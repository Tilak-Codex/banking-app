
"use client";

import { FormEvent, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  beneficiaryApi,
  BeneficiaryRequest,
} from "@/api/beneficiaryApi";

export default function CreateBeneficiaryPage() {
  const params = useParams();
  const router = useRouter();

  const customerId = Number(params.id);

  const [formData, setFormData] =
    useState<BeneficiaryRequest>({
      name: "",
      accountNumber: "",
      bankCode: "",
    });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [validationErrors, setValidationErrors] =
    useState<Record<string, string>>({});

  function handleChange(
    event: React.ChangeEvent<HTMLInputElement>
  ) {
    const { name, value } = event.target;

    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }));

    setValidationErrors((previous) => ({
      ...previous,
      [name]: "",
    }));
  }

  function validateForm(): boolean {
    const errors: Record<string, string> = {};

    if (!formData.name.trim()) {
      errors.name = "Beneficiary name is required";
    }

    if (!formData.accountNumber.trim()) {
      errors.accountNumber =
        "Beneficiary account number is required";
    }

    if (!formData.bankCode.trim()) {
      errors.bankCode = "Bank code is required";
    }

    setValidationErrors(errors);

    return Object.keys(errors).length === 0;
  }

  async function handleSubmit(
    event: FormEvent<HTMLFormElement>
  ) {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);
      setError("");

      await beneficiaryApi.create(
        customerId,
        formData
      );

      router.push(
        `/customers/${customerId}/beneficiaries`
      );
    } catch (error) {
      if (error instanceof Error) {
        const apiError = error as Error & {
          validationErrors?: Record<string, string>;
        };

        if (apiError.validationErrors) {
          setValidationErrors(
            apiError.validationErrors
          );
        }

        setError(apiError.message);
      } else {
        setError("Failed to create beneficiary");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <main>
      <h1>Add Beneficiary</h1>

      {error && <p>{error}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="name">Name</label>
          <br />

          <input
            id="name"
            name="name"
            type="text"
            value={formData.name}
            onChange={handleChange}
          />

          {validationErrors.name && (
            <p>{validationErrors.name}</p>
          )}
        </div>

        <br />

        <div>
          <label htmlFor="accountNumber">
            Account Number
          </label>
          <br />

          <input
            id="accountNumber"
            name="accountNumber"
            type="text"
            value={formData.accountNumber}
            onChange={handleChange}
          />

          {validationErrors.accountNumber && (
            <p>{validationErrors.accountNumber}</p>
          )}
        </div>

        <br />

        <div>
          <label htmlFor="bankCode">
            Bank Code
          </label>
          <br />

          <input
            id="bankCode"
            name="bankCode"
            type="text"
            value={formData.bankCode}
            onChange={handleChange}
          />

          {validationErrors.bankCode && (
            <p>{validationErrors.bankCode}</p>
          )}
        </div>

        <br />

        <button type="submit" disabled={loading}>
          {loading
            ? "Adding..."
            : "Add Beneficiary"}
        </button>
      </form>
    </main>
  );
}
