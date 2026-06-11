'use client';

import { useEffect, useState } from 'react';
import { initialize, isReady } from './next-js-device';

export default function NextJsPage({ children }: { children: React.ReactNode }) {
  const [ready, setReady] = useState(isReady());

  useEffect(() => {
    if (!ready) {
      initialize().then(() => {
        setReady(true);
      });
    }
  }, [ready]);

  if (!ready) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-500" />
      </div>
    );
  }

  return <>{children}</>;
}
