'use client';

import * as React from "react";
import { useEffect, useRef } from 'react';
import {Canvas2DDevice} from "../../platform/canvas-2d-device";
import {Canvas2DRouter} from "../../platform/canvas-2d-router";
import {Page} from "../../java_franca/graphics/views/page";
import {TestView0} from "./test-view-0";
import {SkiaDevice} from "../../platform/skia-device";
import {SkiaRouter} from "../../platform/skia-router";
import {useSearchParams} from "next/navigation";

export default function Test0Route() {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const routerRef = useRef<any>(null);
  const searchParams = useSearchParams();
  const useSkia = searchParams.get('useSkia') === 'true';

  useEffect(() => {
    if (!canvasRef.current) return;

    let device: any;
    let router: any;

    if (useSkia) {
      device = SkiaDevice.getInstance();
      router = new SkiaRouter(device);
    } else {
      device = Canvas2DDevice.getInstance();
      router = new Canvas2DRouter(device);
    }

    routerRef.current = router;

    const page = new Page(router);
    page.views.push(new TestView0());

    router.run(canvasRef.current, page);

    return () => {
      router.destroy();
      routerRef.current = null;
    };
  }, [useSkia]);

  useEffect(() => {
    const handleResize = () => {
      if (!canvasRef.current) return;

      const parent = canvasRef.current.parentElement;
      if (!parent) return;

      canvasRef.current.width = parent.clientWidth;
      canvasRef.current.height = parent.clientHeight;

      routerRef.current?.requestRepainting();
    };

    const resizeObserver = new ResizeObserver(handleResize);
    if (canvasRef.current?.parentElement) {
      resizeObserver.observe(canvasRef.current.parentElement);
    }

    handleResize();

    return () => resizeObserver.disconnect();
  }, []);

  return (
    <div style={{ width: '100%', height: '100%' }}>
      <canvas
        ref={canvasRef}
        style={{ width: '100%', height: '100%', display: 'block', border: '1px solid #ccc' }}
      />
    </div>
  );
}
