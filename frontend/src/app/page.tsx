"use client";
import keycloak from "@/auth/keycloak";
import Link from "next/link";

export default function HomePage() {
  return (
    <main>
      <h1>Banking Application</h1>

      <p>Frontend is running successfully.</p>
<button
  onClick={() => {
    console.log("Authenticated:", keycloak.authenticated);
    console.log(
      "Username:",
      keycloak.tokenParsed?.preferred_username
    );
    console.log("Token:", keycloak.token);
  }}
>
  Check Token
</button>
<button
  onClick={() => {
    keycloak.logout({
      redirectUri: window.location.origin,
    });
  }}
>
  Logout
</button>
      <nav>
        <ul>
          <li>
            <Link href="/customers">
              Customers
            </Link>
          </li>

          <li>
            <Link href="/accounts">
              Bank Accounts
            </Link>
          </li>
        </ul>
      </nav>
    </main>
  );
}

