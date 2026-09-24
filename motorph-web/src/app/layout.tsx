import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "MotorPH Enterprise Payroll System | TEAM CTRL+ALT+ELITE",
  description: "Secure, Web-Based Enterprise Payroll System & Employee Self-Service Portal aligned with MO-IT153 Security Implementation Plan.",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className="h-full antialiased">
      <body className="min-h-full flex flex-col bg-slate-50 text-slate-900 font-sans">
        {children}
      </body>
    </html>
  );
}
