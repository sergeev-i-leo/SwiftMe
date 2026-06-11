'use client';

import * as React from "react";
import Link from 'next/link';

export default function HomePage() {
  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Skia Playground</h1>
      <p style={styles.subtitle}>Выбери режим рендеринга:</p>

      <div style={styles.grid}>
        <Link href="/tests/test-0?useSkia=false" style={styles.card}>
          <h3 style={styles.cardTitle}>Canvas2D</h3>
          <p style={styles.cardDescription}>Рисование через Canvas2D, клик — движение квадрата</p>
        </Link>

        <Link href="/tests/test-0?useSkia=true" style={styles.card}>
          <h3 style={styles.cardTitle}>Skia (CanvasKit)</h3>
          <p style={styles.cardDescription}>Рисование через Skia, производительность, шрифты</p>
        </Link>
      </div>
    </div>
  );
}

const styles = {
  container: { maxWidth: 1200, margin: '0 auto', padding: '2rem', fontFamily: 'system-ui' },
  title: { fontSize: '2.5rem', fontWeight: 600, marginBottom: '0.5rem' },
  subtitle: { fontSize: '1.1rem', color: '#666', marginBottom: '2rem' },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '1.5rem' },
  card: { display: 'block', padding: '1.5rem', backgroundColor: '#f5f5f5', borderRadius: '12px', textDecoration: 'none', transition: 'all 0.2s' },
  cardTitle: { fontSize: '1.25rem', fontWeight: 600, marginBottom: '0.5rem', color: '#1a1a1a' },
  cardDescription: { fontSize: '0.9rem', color: '#666', margin: 0 },
} as const;
