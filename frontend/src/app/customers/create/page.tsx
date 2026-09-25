"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { customerApi, CustomerRequest } from "@/api/customerApi";
import Link from "next/link";

export default function CreateCustomerPage() {
  const router = useRouter();

  const [formData, setFormData] = useState<CustomerRequest>({
    name: "",
    email: "",
    phoneNumber: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [validationErrors, setValidationErrors] = useState<
    Record<string, string>
  >({});
  function handleChange(event: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target;

    setFormData((previous) => ({
      ...previous,
      [name]: value,
    }));
  }
  function validateForm(): boolean {
    const errors: Record<string, string> = {};

    if (!formData.name.trim()) {
      errors.name = "Name is required";
    }

    if (!formData.email.trim()) {
      errors.email = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      errors.email = "Enter a valid email address";
    }

    if (!formData.phoneNumber.trim()) {
      errors.phoneNumber = "Phone number is required";
    } else if (!/^\d{10}$/.test(formData.phoneNumber)) {
      errors.phoneNumber = "Phone number must contain exactly 10 digits";
    }

    setValidationErrors(errors);

    return Object.keys(errors).length === 0;
  }
  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!validateForm()) {
      return;
    }
    try {
      setLoading(true);
      setError("");

      await customerApi.create(formData);

      router.push("/customers");
    } catch (error) {
  if (error instanceof Error) {
    const apiError = error as Error & {
      validationErrors?: Record<string, string>;
    };

    if (apiError.validationErrors) {
      setValidationErrors(apiError.validationErrors);
    }

    setError(apiError.message);
  } else {
    setError("Failed to create customer");
  }
}
  }

  return (
    <main>
      <h1>Create Customer</h1>
      <Link href="/customers"> Back to Customers </Link> <br /> <br />
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
          <label htmlFor="email">Email</label>
          <br />
          <input
            id="email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
          />
          {validationErrors.email && (
  <p>{validationErrors.email}</p>
)}
        </div>

        <br />

        <div>
          <label htmlFor="phoneNumber">Phone Number</label>
          <br />
          <input
            id="phoneNumber"
            name="phoneNumber"
            type="text"
            value={formData.phoneNumber}
            onChange={handleChange}
          />
          {validationErrors.phoneNumber && (
  <p>{validationErrors.phoneNumber}</p>
)}
        </div>

        <br />

        <button type="submit" disabled={loading}>
          {loading ? "Creating..." : "Create Customer"}
        </button>
      </form>
    </main>
  );
}
