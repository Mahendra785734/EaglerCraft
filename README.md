# EaglerCraft Client — 26.2 implementation work

This repository contains the client-side module layer being developed for an original Eaglercraft client. The target game source and its renderer are not currently present in this repository, so the adapter must be connected to the actual client before this becomes a playable build.

## Implemented in this branch

- `LiveGameBridge` captures real FPS, player coordinates/name, WASD state, world time, and click-rate data through a narrow game adapter.
- `HudModuleManager` controls FPS, coordinates, keystrokes, CPS, and world-time modules.
- `ClickRateCounter` counts actual mouse events in a rolling one-second window.
- Gradle configuration and unit tests compile and verify the standalone module layer.
- A GitHub Actions workflow runs the module tests on pushes and pull requests.

## Build and test the module layer

Requires Java 17 and Gradle installed:

```sh
gradle :client-modules:test
```

## What remains for the full client

1. Connect `LiveGameBridge.GameAccess` to the actual 26.2 client state and input callbacks.
2. Adapt `LiveGameBridge.HudRenderer` to the game's font renderer and call `renderFrame` from the real HUD render callback.
3. Replace the game's main menu with an original client menu and bind the player name/head to the real profile and skin-rendering APIs.
4. Build the full browser client and verify it in a browser.

**Status:** the module framework is now real Java code with tests; the full 26.2 game integration and playable build are not yet complete. The required editable game source is not present here, and no successful full-client build is claimed.
