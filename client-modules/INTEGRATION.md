# Live client integration

The HUD module layer now has a concrete bridge in `LiveGameBridge.java`. It is intentionally isolated from Minecraft-specific classes so it can be compiled and tested independently, then connected to the actual target client.

## Wire it into the game

1. Implement `LiveGameBridge.GameAccess` using the real client APIs:
   - FPS measurement and availability
   - Local player coordinates and player name
   - W/A/S/D key states in that exact array order
   - Active world time and availability
2. Implement `LiveGameBridge.HudRenderer` by forwarding `drawString(text, x, y, color)` to the game's font renderer.
3. Create one `LiveGameBridge` when the client initializes.
4. Call `renderFrame(monotonicMillis)` from the real HUD render callback. Pass a monotonic clock, not wall-clock time.
5. Call `onLeftClick(monotonicMillis)` and `onRightClick(monotonicMillis)` only from actual mouse-button press events.
6. Expose `getModules()` to the client's settings screen so toggles call `setEnabled` on the matching module.

The bridge avoids fabricated player, keyboard, FPS, and world-time values. If those values are unavailable, the related module returns no line.

## Build and tests

Run `gradle :client-modules:test` with Java 17. Unit tests cover availability, module toggles, duplicate IDs, defensive key-state copying, and the one-second CPS window. GitHub Actions also runs this task.

## Still blocked on target source

This repository does not contain the editable 26.2 game source, main-menu classes, profile/skin renderer, or the real HUD renderer. Therefore the bridge is implemented but cannot yet be wired to those target APIs or produce a playable client here. The full-client integration is pending until the appropriate source tree is added.
