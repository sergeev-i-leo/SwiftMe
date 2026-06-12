# Java Franca

**Java Franca** is an attempt to create a cross-platform transpiler that allows you to write UI and application logic once in Java, and transpiles it to web and ios.
The next goal is to make it AS SIMPLE AS POSSIBLE.
The process must be clear, observable and straight-forward.

Write your business-logic once using Java, and Java Franca transpiles it to:
- **C++** (with Qt or Skia backend)
- **Tpescript** (with Graphics2D or Skia backend)
- **Swift** (with Canvas)

## Features

- **Single source of truth** — keep your core logic in one language (Java)
- **Native performance** — no embedded browsers or bridges; generated code runs at native speed

## Additiona Features
- **Graphics framework** — implement platform-specific `Device`, `Router`, `Painter`, and keep business logic shared between platforms
- **Animation system** — declarative animations with automatic repaint scheduling
- **Memory-safe design** — clear ownership rules, `destroy()` semantics (ready for hardcore manual memory management when transpiled to C++)
- **Office Franca** — simple applications for editing documents

## Design Principles

* Simplicity first — if a feature makes the transpiler complex, leave it out initially
* Observability — generated code must be traceable and debuggable
* No magic — every transformation must be traceable from source to target
* Iterative — start with minimal subset, expand gradually
