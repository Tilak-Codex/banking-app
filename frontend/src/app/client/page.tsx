
"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

import { customerApi, CustomerResponse } from "@/api/customerApi";
import {
  bankAccountApi,
  BankAccountResponse,
} from "@/api/bankAccountApi";
import {
  beneficiaryApi,
  BeneficiaryResponse,
} from "@/api/beneficiaryApi";

import keycloak from "@/auth/keycloak";

export default function ClientDashboard() {
  const [customer, setCustomer] =
    useState<CustomerResponse | null>(null);

  const [accounts, setAccounts] =
    useState<BankAccountResponse[]>([]);

  const [beneficiaries, setBeneficiaries] =
    useState<BeneficiaryResponse[]>([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadDashboard();
  }, []);

  async function loadDashboard() {
    try {
      setLoading(true);
      setError("");

      const [customerData, accountData, beneficiaryData] =
        await Promise.all([
          customerApi.getMe(),
          bankAccountApi.getMe(),
          beneficiaryApi.getMe(),
        ]);

      setCustomer(customerData);
      setAccounts(accountData);
      setBeneficiaries(beneficiaryData);
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to load dashboard"
      );
    } finally {
      setLoading(false);
    }
  }

  function logout() {
    keycloak.logout({
      redirectUri: window.location.origin,
    });
  }

  if (loading) {
    return <p>Loading dashboard...</p>;
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>
        <button onClick={loadDashboard}>Retry</button>
      </main>
    );
  }

  return (
    <>
      <nav className="navbar">
        <div className="navbar-inner">
          <Link href="/client" className="navbar-brand">
            Banking App
          </Link>

          <div className="navbar-links">
            <Link href="/accounts">
              My Accounts
            </Link>

            <Link
              href={`/customers/${customer?.id}/beneficiaries`}
            >
              My Beneficiaries
            </Link>

            <button
              type="button"
              className="logout-button"
              onClick={logout}
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <main>
        <div className="page-header">
          <div>
            <h1>Client Dashboard</h1>

            {customer && (
              <p>
                Welcome, <strong>{customer.name}</strong>
              </p>
            )}
          </div>
        </div>

        <div className="list">
          <div className="card">
            <h2>My Profile</h2>

            {customer && (
              <>
                <p>
                  <strong>Name:</strong> {customer.name}
                </p>

                <p>
                  <strong>Email:</strong> {customer.email}
                </p>

                <p>
                  <strong>Phone:</strong>{" "}
                  {customer.phoneNumber}
                </p>
              </>
            )}

            {customer && (
              <Link
                href={`/customers/${customer.id}`}
                className="btn"
              >
                View Profile
              </Link>
            )}
          </div>

          <div className="card">
            <h2>My Bank Accounts</h2>

            <p>
              You have {accounts.length} bank account
              {accounts.length !== 1 ? "s" : ""}.
            </p>

            <Link href="/accounts" className="btn">
              View Accounts
            </Link>
          </div>

          <div className="card">
            <h2>My Beneficiaries</h2>

            <p>
              You have {beneficiaries.length} beneficiary
              {beneficiaries.length !== 1 ? "ies" : ""}.
            </p>

            {customer && (
              <Link
                href={`/customers/${customer.id}/beneficiaries`}
                className="btn"
              >
                View Beneficiaries
              </Link>
            )}
          </div>
        </div>
      </main>
    </>
  );
}
