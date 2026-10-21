# EaglerCraft Client — 26.2 Development Scaffold

This repository is the workspace for an original client UI concept and a small HUD module layer.

## Current work

- **Original menu prototype:** `prototype/index.html` — interactive standalone UI preview with Home, Play, HUD Modules, Settings, and an editable profile-name preview.
- **HUD module scaffold:** `client-modules/src/main/java/dev/featherish/client/modules` — module registry, snapshot contract, and FPS/coordinates/keystrokes/CPS/world-time text modules.
- **Click counter:** `ClickRateCounter.java` — rolling one-second counts when the real input adapter supplies actual mouse click events.
- **Integration notes:** `client-modules/INTEGRATION.md`.

## Important status

This is **not yet a playable 26.2 client**. The menu prototype is standalone; the avatar is a placeholder; the HUD modules are not yet connected to a live game renderer. No full game build has been run.

A public 26.2 porting workspace documents the expected architecture, but explicitly does not include decompiled Minecraft game source. A full integration requires a legitimate local 26.2 client input and the matching source-generation/patching workflow. Do not commit proprietary game JARs, decompiled game source, or game assets into this repository.

## Next milestones

1. Establish a complete, authorized 26.2 source project and run its baseline build.
2. Port the original menu design into the actual title-screen class while preserving existing navigation.
3. Replace the CSS avatar with the live profile skin/head renderer and profile name.
4. Connect the game snapshot adapter to FPS, local-player coordinates, movement key bindings, real mouse events, and world time.
5. Render enabled HUD modules with the game's font/HUD renderer.
6. Run compile, browser build, and smoke tests before calling the client playable.

## Version integrity

Do not relabel an older client as 26.2 or treat a compiled HTML/WebAssembly build as editable Java source. Every feature should be marked complete only after it works against the actual game state and passes a test.
