'use client';

import NextJsPage from '@/platform/NextJsPage';
import { getSkia, getFont, isReady } from '@/platform/NextJsDevice';
import { useEffect, useRef } from 'react';

export default function Page0() {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const hasRendered = useRef(false);

  useEffect(() => {
    if (!isReady() || hasRendered.current) return;
    hasRendered.current = true;

    const CanvasKit = getSkia();
    if (!CanvasKit || !canvasRef.current) return;

    const surface = CanvasKit.MakeWebGLCanvasSurface(canvasRef.current);
    if (!surface) return;

    const canvas = surface.getCanvas();
    canvas.clear(CanvasKit.WHITE);

    const font = getFont('/fonts/Roboto.ttf');
    const paint = new CanvasKit.Paint();
    paint.setColor(CanvasKit.BLACK);

    canvas.drawText('Hello World', 100, 200, paint, font);
    surface.flush();
  }, []);

  return (
    <NextJsPage>
      <div style={{ width: '100vw', height: '100vh' }}>
        <canvas
          ref={canvasRef}
          width={800}
          height={600}
          style={{ width: '100%', height: '100%' }}
        />
      </div>
    </NextJsPage>
  );
}
