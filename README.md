# EaglerCraft Client — client module development

This branch contains an original, version-independent HUD module layer. It is not yet a complete playable client: the repository does not currently include the target game's editable engine, menu classes, skin renderer, or browser build configuration.

## HUD modules in this branch

The registry includes 16 modules:

- FPS
- Coordinates (XYZ)
- Keystrokes (WASD)
- CPS (left/right clicks)
- World time
- Player name
- Ping
- Direction
- Armor status
- Potion effects
- Server address
- Biome
- Movement speed
- Held item
- Target information
- Memory usage

The first five use the existing snapshot fields. The additional eleven use optional fields in `GameSnapshot.Extras`; they remain invisible until a game adapter provides real values. This is intentional: no fake ping, armor, biome, potion, target, or memory values are shown.

## Build and test this module layer

Requires Java 17 and Gradle:

```sh
gradle :client-modules:test
```

GitHub Actions also runs the test suite on pushes and pull requests.

## Remaining integration work

1. Implement `LiveGameBridge.GameAccess` using the actual target game's state and input APIs.
2. Implement `getOptionalHudData()` for optional values supported by that game.
3. Connect the HUD renderer to the game's real render callback.
4. Build an original main menu that reads the actual profile name and skin/head renderer.
5. Run the complete browser build and verify the generated HTML/WebAssembly output.

**Status:** the Java module layer and tests are source-controlled; full game integration and a playable HTML build are not claimed. The uploaded Rise client archive is a compiled client and its included license restricts modification/rebranding, so its compiled files are not being copied into this project.
