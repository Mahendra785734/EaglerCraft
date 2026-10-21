package dev.featherish.client.modules;

/**
 * Connects the HUD module registry to the real game through a narrow adapter.
 *
 * Implement GameAccess with the target client's real player, keyboard, FPS,
 * world-time, and input APIs; implement HudRenderer with its font renderer.
 * No game values are fabricated when the relevant subsystem is unavailable.
 */
public final class LiveGameBridge {
    private final GameAccess game;
    private final HudRenderer renderer;
    private final HudModuleManager modules;
    private final ClickRateCounter clicks;
    private final int left;
    private final int top;
    private final int lineSpacing;
    private final int textColor;

    public LiveGameBridge(GameAccess game, HudRenderer renderer) {
        this(game, renderer, new HudModuleManager(), new ClickRateCounter(), 8, 8, 10, 0xFFFFFFFF);
    }

    public LiveGameBridge(
            GameAccess game,
            HudRenderer renderer,
            HudModuleManager modules,
            ClickRateCounter clicks,
            int left,
            int top,
            int lineSpacing,
            int textColor) {
        if (game == null) throw new IllegalArgumentException("game access is required");
        if (renderer == null) throw new IllegalArgumentException("HUD renderer is required");
        if (modules == null) throw new IllegalArgumentException("module manager is required");
        if (clicks == null) throw new IllegalArgumentException("click counter is required");
        if (lineSpacing < 1) throw new IllegalArgumentException("line spacing must be positive");
        this.game = game;
        this.renderer = renderer;
        this.modules = modules;
        this.clicks = clicks;
        this.left = Math.max(0, left);
        this.top = Math.max(0, top);
        this.lineSpacing = lineSpacing;
        this.textColor = textColor;
    }

    /** Call from the target game's HUD render callback once per frame. */
    public void renderFrame(long monotonicMillis) {
        GameSnapshot snapshot = captureSnapshot(monotonicMillis);
        int y = top;
        for (String line : modules.getVisibleLines(snapshot)) {
            renderer.drawString(line, left, y, textColor);
            y += lineSpacing;
        }
    }

    /** Call from the game's actual left mouse-button press event. */
    public void onLeftClick(long monotonicMillis) {
        clicks.recordLeftClick(monotonicMillis);
    }

    /** Call from the game's actual right mouse-button press event. */
    public void onRightClick(long monotonicMillis) {
        clicks.recordRightClick(monotonicMillis);
    }

    public HudModuleManager getModules() {
        return modules;
    }

    private GameSnapshot captureSnapshot(long nowMillis) {
        boolean fpsAvailable = game.hasFpsMeasurement();
        int fps = fpsAvailable ? Math.max(0, game.getFps()) : 0;

        boolean playerAvailable = game.hasLocalPlayer();
        double x = playerAvailable ? game.getPlayerX() : 0.0D;
        double y = playerAvailable ? game.getPlayerY() : 0.0D;
        double z = playerAvailable ? game.getPlayerZ() : 0.0D;
        String playerName = playerAvailable ? game.getPlayerName() : "";

        boolean keysAvailable = game.canReadMovementKeys();
        boolean[] keys = keysAvailable ? game.getMovementKeysWASD() : new boolean[0];
        if (keysAvailable && (keys == null || keys.length < 4)) {
            keysAvailable = false;
            keys = new boolean[0];
        }

        ClickRateCounter.Counts cps = clicks.snapshot(nowMillis);
        boolean worldTimeAvailable = game.hasWorldTime();
        long worldTime = worldTimeAvailable ? game.getWorldTime() : 0L;

        return new GameSnapshot(
                fps, fpsAvailable,
                x, y, z, playerAvailable,
                playerName, keysAvailable, keys,
                cps.left, cps.right, true,
                worldTime, worldTimeAvailable);
    }

    /**
     * Adapter over the target game's real client APIs. Movement key order is
     * W, A, S, D. Return availability flags instead of fake placeholder data.
     */
    public interface GameAccess {
        boolean hasFpsMeasurement();
        int getFps();

        boolean hasLocalPlayer();
        double getPlayerX();
        double getPlayerY();
        double getPlayerZ();
        String getPlayerName();

        boolean canReadMovementKeys();
        boolean[] getMovementKeysWASD();

        boolean hasWorldTime();
        long getWorldTime();
    }

    /** Adapt this to the game's actual font renderer. */
    public interface HudRenderer {
        void drawString(String text, int x, int y, int color);
    }
}
