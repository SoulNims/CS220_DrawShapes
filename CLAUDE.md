# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run

Compile all source files:
```bash
javac *.java
```

Run the application:
```bash
java DrawingApp
```

Build the JAR (manifest must set `Main-Class: DrawingApp`):
```bash
jar cfe drawshapes-njsherpa.jar DrawingApp *.class
java -jar drawshapes-njsherpa.jar
```

## Architecture

This is a Java Swing drawing application ("Enhanced Java Paint") structured around a shape hierarchy and a canvas/app split:

- **`MyShape`** — abstract base class (`Serializable`) for all shapes. Holds `start`, `end`, `color`, `strokeWidth`. Subclasses must implement `draw(Graphics2D)` and `contains(Point)`.
  - **`Line`** — straight line; hit-tests using `Line2D.ptSegDist`.
  - **`Rect`** — rectangle drawn from start to end corner; hit-tests with `java.awt.Rectangle`.
  - **`Oval`** — ellipse drawn from bounding box; hit-tests with `Ellipse2D`.
  - **`Freehand`** — polyline of accumulated points (used for both Pencil and Eraser tools). Overrides `setEnd` to append points instead of replacing the endpoint.

- **`DrawingCanvas`** — `JPanel` subclass that owns the shape list (`List<MyShape>`) and a redo stack (`Stack<MyShape>`). Handles all mouse events to create/update/finalize shapes. Eraser tool creates a `Freehand` painted in the background color; ObjectEraser removes the topmost shape whose `contains()` returns true.

- **`DrawingApp`** — `JFrame` entry point. Builds the toolbar (tool buttons, width/eraser spinners, color picker, background controls) and menu bar (File: New/Save/Load; Edit: Undo/Redo). Save/Load serializes the `List<MyShape>` to a `.draw` file via `ObjectOutputStream`.

## Key Design Points

- Adding a new shape type requires: a new class extending `MyShape`, adding a `case` in `DrawingCanvas.mousePressed`, and optionally a toolbar button in `DrawingApp.createTopPanel`.
- The redo stack is cleared whenever a new shape is finalized (`mouseReleased`) — standard linear undo/redo model.
- Background image and background color are mutually exclusive: setting a color clears the image reference.
- Serialization uses Java's built-in `ObjectOutputStream`; all `MyShape` subclasses must remain `Serializable` for save/load to work.
