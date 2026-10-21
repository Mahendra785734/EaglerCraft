# Original Client Menu Prototype

This is an original, standalone UI prototype for the planned EaglerCraft client. It does not use Rise Client code or assets.

## Preview

Open `index.html` directly in a browser, or serve this folder with any static file server.

## Interactions currently implemented

- Navigation between Home, Play, HUD Modules, and Settings.
- Editable profile name reflected in the profile card.
- HUD module selection toggles.
- Compact-layout and profile-card preferences.
- Status messages explaining which actions are not yet connected.

## Not yet connected to the game

This page is a UI prototype, not a playable Minecraft/Eaglercraft client. The play buttons do not start worlds or connect to servers. The avatar is a CSS placeholder, not the user's actual skin/head. HUD toggles do not yet alter the game's renderer.

## Next integration work

1. Start from a complete, authorized Eaglercraft 26.2 source project and verify its build.
2. Port this menu design into the game's actual title-screen class and preserve existing navigation callbacks.
3. Replace the placeholder avatar/name with the authenticated local profile and skin renderer.
4. Create a per-frame adapter that reads actual FPS, player coordinates, movement-key states, click timestamps, and world time.
5. Render enabled HUD modules through the game's real HUD/font renderer.
6. Run Java compilation, browser build, and a smoke test of title screen, world entry, and return-to-menu behavior.

Do not mark any item complete until it has been implemented and tested in the actual client.
