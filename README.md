# SwiftMe

**SwiftMe** is a cross-platform framework and transpiler that allows you to write UI and application logic once in Swift, and run it natively on multiple platforms.

Write your code once using Swift, and SwiftMe transpiles it to:
- **C++** (with Qt or Skia backend)
- **Java** (with Swing, JavaFX, or raw Canvas)
- **TypeScript** (with HTML Canvas)

## Features

- **Single source of truth** — keep your core logic in one language (Swift)
- **Native performance** — no embedded browsers or JS bridges; generated code runs at native speed
- **Abstracted rendering** — implement platform-specific `Painter`, keep business logic pure
- **Animation system** — declarative animations with automatic repaint scheduling
- **Memory-safe design** — clear ownership rules, `destroy()` semantics (ready for hardcore manual memory management when transpiled to C++)

## Architecture


