
"use client";

import { useEffect, useState } from "react";

import Link from "next/link";

import { useParams, useRouter } from "next/navigation";

import {
  customerApi,
  CustomerResponse,
} from "@/api/customerApi";

import keycloak from "@/auth/keycloak";

export default function CustomerDetailsPage() {
  const params = useParams();
  const router = useRouter();

  const id = Number(params.id);

  const [customer, setCustomer] =
    useState<CustomerResponse | null>(null);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [deleting, setDeleting] = useState(false);

  const isClient =
    keycloak.realmAccess?.roles.includes("CLIENT") ?? false;

  const isAdmin =
    keycloak.realmAccess?.roles.includes("ADMIN") ?? false;

  useEffect(() => {
    loadCustomer();
  }, [id]);

  async function loadCustomer() {
    try {
      setLoading(true);
      setError("");

      const data = isClient
        ? await customerApi.getMe()
        : await customerApi.getById(id);

      setCustomer(data);
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to load customer"
      );
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete() {
    if (!customer) {
      return;
    }

    const confirmed = window.confirm(
      `Are you sure you want to delete customer "${customer.name}"?`
    );

    if (!confirmed) {
      return;
    }

    try {
      setDeleting(true);
      setError("");

      await customerApi.delete(customer.id);

      router.push("/customers");
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to delete customer"
      );
      setDeleting(false);
    }
  }

  if (loading) {
    return <p>Loading customer...</p>;
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>

        <button onClick={loadCustomer}>
          Retry
        </button>
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
        <strong>Name:</strong> {customer.name}
      </p>

      <p>
        <strong>Email:</strong> {customer.email}
      </p>

      <p>
        <strong>Phone:</strong> {customer.phoneNumber}
      </p>

      <br />

      <Link
        href={`/customers/${customer.id}/beneficiaries`}
      >
        View Beneficiaries
      </Link>

      {isAdmin && (
        <>
          <br />
          <br />

          <button
            onClick={handleDelete}
            disabled={deleting}
          >
            {deleting ? "Deleting..." : "Delete Customer"}
          </button>
        </>
      )}
    </main>
  );
}
