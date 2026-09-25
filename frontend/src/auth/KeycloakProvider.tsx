"use client";

import { ReactNode, useEffect, useState } from "react";
import keycloak from "./keycloak";

interface KeycloakProviderProps {
  children: ReactNode;
}

let keycloakInitPromise: Promise<boolean> | null = null;

function initializeKeycloak(): Promise<boolean> {
  if (!keycloakInitPromise) {
    keycloakInitPromise = keycloak.init({
      onLoad: "login-required",
      pkceMethod: "S256",
    });
  }

  return keycloakInitPromise;
}

export default function KeycloakProvider({
  children,
}: KeycloakProviderProps) {
  const [initialized, setInitialized] = useState(false);

  useEffect(() => {
    initializeKeycloak()
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