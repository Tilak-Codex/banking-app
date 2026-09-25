"use client";

import { ReactNode, useEffect, useState } from "react";
import keycloak from "./keycloak";

interface KeycloakProviderProps {
  children: ReactNode;
}

export default function KeycloakProvider({
  children,
}: KeycloakProviderProps) {
  const [initialized, setInitialized] = useState(false);

  useEffect(() => {
    keycloak
      .init({
        onLoad: "login-required",
        pkceMethod: "S256",
      })
      .then((authenticated) => {
        if (authenticated) {
          setInitialized(true);
        }
      })
      .catch((error) => {
        console.error("Keycloak initialization failed:", error);
      });
  }, []);

  if (!initialized) {
    return <p>Loading authentication...</p>;
  }

  return <>{children}</>;
}