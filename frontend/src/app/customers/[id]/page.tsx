"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { useParams } from "next/navigation";
import { customerApi, CustomerResponse } from "@/api/customerApi";

export default function CustomerDetailsPage() {
  const params = useParams();

  const id = Number(params.id);

  const [customer, setCustomer] = useState<CustomerResponse | null>(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadCustomer();
  }, []);

  async function loadCustomer() {
    try {
      setLoading(true);
      setError("");

      const data = await customerApi.getById(id);

      setCustomer(data);
    } catch (error) {
      setError(
        error instanceof Error ? error.message : "Failed to load customer",
      );
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return <p>Loading customer...</p>;
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>

        <button onClick={loadCustomer}>Retry</button>
      </main>
    );
  }

  if (!customer) {
    return <p>Customer not found.</p>;
  }

  return (
    <main>
      <h1>Customer Details</h1>

      <p>
        <strong>ID:</strong> {customer.id}
      </p>

      <p>
        <Link href={`/customers/${customer.id}`}>
          <strong>{customer.name}</strong>
        </Link>
      </p>

      <p>
        <strong>Email:</strong> {customer.email}
      </p>

      <p>
        <strong>Phone:</strong> {customer.phoneNumber}
      </p>

      <br />
      <Link href={`/customers/${customer.id}/beneficiaries`}>
        View Beneficiaries
      </Link>
    </main>
  );
}
