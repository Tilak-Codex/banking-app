
"use client";

import { useEffect } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";

import keycloak from "@/auth/keycloak";

export default function HomePage() {
  const router = useRouter();

  useEffect(() => {
    if (keycloak.authenticated && keycloak.hasRealmRole("CLIENT")) {
      router.replace("/client");
    }
  }, [router]);

  return (
    <>
      <nav className="navbar">
        <div className="navbar-inner">
          <Link href="/" className="navbar-brand">
            Banking App
          </Link>

          <div className="navbar-links">
            <Link href="/customers">
              Customers
            </Link>

            <Link href="/accounts">
              Accounts
            </Link>

            <button
              type="button"
              className="logout-button"
              onClick={() =>
                keycloak.logout({
                  redirectUri: window.location.origin,
                })
              }
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <main>
        <div className="page-header">
          <div>
            <h1>Banking Dashboard</h1>
            <p>
              Manage customers, accounts, transactions and
              beneficiaries.
            </p>
          </div>
        </div>

        <div className="list">
          <div className="card">
            <h2>Customers</h2>
            <p>
              View and manage banking customers and their
              beneficiaries.
            </p>

            <Link href="/customers" className="btn">
              View Customers
            </Link>
          </div>

          <div className="card">
            <h2>Bank Accounts</h2>
            <p>
              View accounts and transaction history.
            </p>

            <Link href="/accounts" className="btn">
              View Accounts
            </Link>
          </div>
        </div>
      </main>
    </>
  );
}
