
import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";

import "./globals.css";

import KeycloakProvider from "@/auth/KeycloakProvider";
import NotificationProvider from "@/components/NotificationProvider";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Banking Application",
  description: "Banking application",
};

export default function RootLayout({
  children,
}: LayoutProps<"/">) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable}`}
      >
        <KeycloakProvider>
          <NotificationProvider>
            {children}
          </NotificationProvider>
        </KeycloakProvider>
      </body>
    </html>
  );
}
