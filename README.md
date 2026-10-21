# EaglerCraft Client Modules

This repository is the development workspace for a Feather-inspired client UI and a small, testable gameplay-data module layer.

## Target

The intended client target is Minecraft/Eaglercraft 26.2. Do not copy compiled HTML/WebAssembly output into the source tree and do not claim that a module is integrated until the game-side adapter has been connected and tested.

## Module integration status

- Module lifecycle and game snapshot contract: scaffold in progress.
- FPS, coordinates, keystrokes, CPS, and world-time HUD modules: depend on a live `GameSnapshot` supplied by the client adapter.
- Actual game hook / renderer integration: pending the editable 26.2 game source tree. A compiled `.html` / `.wasm` build alone does not expose the Java source hooks needed for this step.

## Integration rule

The game adapter must populate snapshots from the running client's real state every frame/tick. Never substitute fixed demo values. If player/world data is unavailable, modules should display an unavailable state or no line.
