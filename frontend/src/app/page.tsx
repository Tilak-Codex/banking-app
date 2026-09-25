
import Link from "next/link";

export default function HomePage() {
  return (
    <main>
      <h1>Banking Application</h1>

      <p>Frontend is running successfully.</p>

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

