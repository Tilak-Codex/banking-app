
"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { customerApi, CustomerResponse } from "@/api/customerApi";
import keycloak from "@/auth/keycloak";

export default function CustomersPage() {
  const [customers, setCustomers] = useState<CustomerResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [pageNo, setPageNo] = useState(0);
  const [pageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [name, setName] = useState("");
  const [searchName, setSearchName] = useState("");

  const [sort, setSort] = useState("id,asc");

  const isAdmin =
    keycloak.realmAccess?.roles.includes("ADMIN") ?? false;

  useEffect(() => {
    loadCustomers();
  }, [pageNo, searchName, sort]);

  async function loadCustomers() {
    try {
      setLoading(true);
      setError("");

      const data = await customerApi.getAll(
        pageNo,
        pageSize,
        sort,
        searchName,
      );

      setCustomers(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (error) {
      setError(
        error instanceof Error
          ? error.message
          : "Failed to load customers",
      );
    } finally {
      setLoading(false);
    }
  }

  function handleSearch(event: React.FormEvent) {
    event.preventDefault();

    setPageNo(0);
    setSearchName(name);
  }

  function handleClearSearch() {
    setName("");
    setSearchName("");
    setPageNo(0);
  }

  function handleSortChange(
    event: React.ChangeEvent<HTMLSelectElement>,
  ) {
    setSort(event.target.value);
    setPageNo(0);
  }

  function handlePrevious() {
    if (pageNo > 0) {
      setPageNo((currentPage) => currentPage - 1);
    }
  }

  function handleNext() {
    if (pageNo < totalPages - 1) {
      setPageNo((currentPage) => currentPage + 1);
    }
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>
        <button onClick={loadCustomers}>Retry</button>
      </main>
    );
  }

  return (
    <main>
      <h1>Customers</h1>

      {/* Only ADMIN can create customers */}
      {isAdmin && (
        <>
          <Link href="/customers/create">
            Create Customer
          </Link>

          <br />
          <br />
        </>
      )}

      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Search by customer name"
          value={name}
          onChange={(event) => setName(event.target.value)}
        />

        <button type="submit">Search</button>

        <button type="button" onClick={handleClearSearch}>
          Clear
        </button>
      </form>

      <br />

      <label>
        Sort by:{" "}
        <select value={sort} onChange={handleSortChange}>
          <option value="id,asc">ID - Ascending</option>
          <option value="id,desc">ID - Descending</option>
          <option value="name,asc">Name - A to Z</option>
          <option value="name,desc">Name - Z to A</option>
        </select>
      </label>

      <br />
      <br />

      {loading ? (
        <p>Loading customers...</p>
      ) : customers.length === 0 ? (
        <p>No customers found.</p>
      ) : (
        <>
          <p>
            Showing page {pageNo + 1} of {totalPages} (
            {totalElements} customers)
          </p>

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

          <div>
            <button
              onClick={handlePrevious}
              disabled={pageNo === 0}
            >
              Previous
            </button>

            <span>
              {" "}
              Page {pageNo + 1} of {totalPages}{" "}
            </span>

            <button
              onClick={handleNext}
              disabled={pageNo >= totalPages - 1}
            >
              Next
            </button>
          </div>
        </>
      )}
    </main>
  );
}
