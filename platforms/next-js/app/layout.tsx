import { Metadata } from 'next'
import './globals.css'
import * as React from "react";

export const metadata: Metadata = {
  title: 'Java Franca',
  description: 'Java Franca',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
    <body>{children}</body>
    </html>
  )
}
