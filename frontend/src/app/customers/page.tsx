"use client";
import Link from "next/link";
import { useEffect, useState } from "react";
import { customerApi, CustomerResponse } from "@/api/customerApi";

export default function CustomersPage() {
  const [customers, setCustomers] = useState<CustomerResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadCustomers();
  }, []);

  async function loadCustomers() {
    try {
      setLoading(true);
      setError("");

      const data = await customerApi.getAll();

      setCustomers(data);
    } catch (error) {
      setError(
        error instanceof Error ? error.message : "Failed to load customers",
      );
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return <p>Loading customers...</p>;
  }

  if (error) {
    return (
      <div>
        <p>{error}</p>
        <button onClick={loadCustomers}>Retry</button>
      </div>
    );
  }

  return (
    <main>
      <h1>Customers</h1>
      <Link href="/customers/create">Create Customer</Link>
      {customers.length === 0 ? (
        <p>No customers found.</p>
      ) : (
        <ul>
          {customers.map((customer) => (
            <li key={customer.id}>
              <Link href={`/customers/${customer.id}`}>
                <strong>{customer.name}</strong>
              </Link>
              <br />
              Email: {customer.email}
              <br />
              Phone: {customer.phoneNumber}
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}
