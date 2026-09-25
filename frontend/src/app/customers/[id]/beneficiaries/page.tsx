
"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";
import {
  beneficiaryApi,
  BeneficiaryResponse,
} from "@/api/beneficiaryApi";

export default function BeneficiariesPage() {
  const params = useParams();
  const customerId = Number(params.id);

  const [beneficiaries, setBeneficiaries] = useState<
    BeneficiaryResponse[]
  >([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadBeneficiaries();
  }, []);

  async function loadBeneficiaries() {
    try {
      setLoading(true);
      setError("");

      const data =
        await beneficiaryApi.getByCustomerId(customerId);

      setBeneficiaries(data);
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to load beneficiaries"
      );
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(beneficiaryId: number) {
    try {
      setError("");

      await beneficiaryApi.delete(beneficiaryId);

      setBeneficiaries((previous) =>
        previous.filter(
          (beneficiary) => beneficiary.id !== beneficiaryId
        )
      );
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to delete beneficiary"
      );
    }
  }

  if (loading) {
    return <p>Loading beneficiaries...</p>;
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>
        <button onClick={loadBeneficiaries}>
          Retry
        </button>
      </main>
    );
  }

  return (
    <main>
      <h1>Beneficiaries</h1>

      <Link href={`/customers/${customerId}`}>
        Back to Customer
      </Link>

      <br />
      <br />

      <Link
        href={`/customers/${customerId}/beneficiaries/create`}
      >
        Add Beneficiary
      </Link>

      <br />
      <br />

      {beneficiaries.length === 0 ? (
        <p>No beneficiaries found.</p>
      ) : (
        <ul>
          {beneficiaries.map((beneficiary) => (
            <li key={beneficiary.id}>
              <strong>{beneficiary.name}</strong>
              <br />

              Account Number: {beneficiary.accountNumber}
              <br />

              Bank Code: {beneficiary.bankCode}
              <br />

              <button
                type="button"
                onClick={() =>
                  handleDelete(beneficiary.id)
                }
              >
                Delete
              </button>
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}