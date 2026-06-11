'use client';

import * as React from "react";
import { useEffect, useRef } from 'react';
import {Canvas2DDevice} from "../../platform/canvas-2d-device";
import {Canvas2DRouter} from "../../platform/canvas-2d-router";
import {Page} from "../../java_franca/graphics/views/page";
import {TestView0} from "./test-view-0";

export default function Test0Route() {
  const canvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    if (!canvasRef.current) {
      return;
    }

    const device = Canvas2DDevice.getInstance();
    const router = new Canvas2DRouter(device);

    const page = new Page(router);
    page.views.push(new TestView0());

    router.run(canvasRef.current, page);
  }, []);

  return (
    <div style={{ width: '100vw', height: '100vh' }}>
      <canvas
        ref={canvasRef}
        width={800}
        height={600}
        style={{ width: '100%', height: '100%', border: '1px solid #ccc' }}
      />
    </div>
  );
}
