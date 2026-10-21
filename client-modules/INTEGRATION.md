# HUD module integration contract

The Java files in this directory are a framework scaffold, not yet wired into the game renderer.

## Required live adapter

In the target 26.2 source tree, implement a small adapter that builds a fresh `GameSnapshot` from actual client state each frame:

- FPS: use the client's measured FPS/frame counter.
- Coordinates and player name: read the current local player entity.
- Keystrokes: sample the real movement-key bindings.
- CPS: count real mouse click events in a rolling one-second window.
- World time: read the active world's time value only when a world exists.

Then create one `HudModuleManager`, call `getVisibleLines(snapshot)` during HUD rendering, and draw each returned line using the game's font renderer. Do not use placeholder values to make a module appear functional.

## Current limitations

- No client renderer hooks are included because the editable Eaglercraft 26.2 source tree has not been provided in this workspace.
- The Feather-inspired main menu, player skin/head rendering, and player-name binding still require integration with the real menu and player-profile APIs.
- These classes have not been compiled against the target game yet. Treat integration as pending until the correct 26.2 source tree is available and a build passes.
