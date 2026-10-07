
"use client";

import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";

type NotificationType = "success" | "error";

interface Notification {
  type: NotificationType;
  message: string;
}

interface NotificationContextType {
  showSuccess: (message: string) => void;
  showError: (message: string) => void;
}

const NotificationContext =
  createContext<NotificationContextType | undefined>(undefined);

interface NotificationProviderProps {
  children: ReactNode;
}

export default function NotificationProvider({
  children,
}: NotificationProviderProps) {
  const [notification, setNotification] =
    useState<Notification | null>(null);

  function showSuccess(message: string) {
    setNotification({
      type: "success",
      message,
    });
  }

  function showError(message: string) {
    setNotification({
      type: "error",
      message,
    });
  }

  useEffect(() => {
    if (!notification) {
      return;
    }

    const timer = setTimeout(() => {
      setNotification(null);
    }, 3000);

    return () => clearTimeout(timer);
  }, [notification]);

  return (
    <NotificationContext.Provider
      value={{
        showSuccess,
        showError,
      }}
    >
      {children}

      {notification && (
        <div
          style={{
            position: "fixed",
            top: "20px",
            right: "20px",
            zIndex: 9999,
            minWidth: "300px",
            maxWidth: "400px",
            padding: "15px 20px",
            borderRadius: "8px",
            backgroundColor:
              notification.type === "success"
                ? "#198754"
                : "#dc3545",
            color: "white",
            boxShadow: "0 4px 12px rgba(0, 0, 0, 0.2)",
          }}
        >
          <strong>
            {notification.type === "success"
              ? "Success"
              : "Error"}
          </strong>

          <div style={{ marginTop: "5px" }}>
            {notification.message}
          </div>
        </div>
      )}
    </NotificationContext.Provider>
  );
}

export function useNotification() {
  const context = useContext(NotificationContext);

  if (!context) {
    throw new Error(
      "useNotification must be used inside NotificationProvider"
    );
  }

  return context;
}
