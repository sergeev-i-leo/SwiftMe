export interface Painter {
  drawText(text: string, x: number, y: number, fontSize: number): void;
  drawRect(x: number, y: number, width: number, height: number): void;
  clear(color: string): void;
  flush(): void;
}
