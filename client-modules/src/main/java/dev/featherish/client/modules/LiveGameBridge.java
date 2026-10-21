package dev.featherish.client.modules;

/**
 * Connects the HUD module registry to a version-specific game adapter.
 * GameAccess must read real game state; HudRenderer must use the game's renderer.
 */
public final class LiveGameBridge {
    private final GameAccess game;
    private final HudRenderer renderer;
    private final HudModuleManager modules;
    private final ClickRateCounter clicks;
    private final int left, top, lineSpacing, textColor;

    public LiveGameBridge(GameAccess game, HudRenderer renderer) {
        this(game, renderer, new HudModuleManager(), new ClickRateCounter(), 8, 8, 10, 0xFFFFFFFF);
    }

    public LiveGameBridge(GameAccess game, HudRenderer renderer, HudModuleManager modules,
            ClickRateCounter clicks, int left, int top, int lineSpacing, int textColor) {
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

    /** Call from the actual HUD render callback once per frame. */
    public void renderFrame(long monotonicMillis) {
        GameSnapshot snapshot = captureSnapshot(monotonicMillis);
        int y = top;
        for (String line : modules.getVisibleLines(snapshot)) {
            renderer.drawString(line, left, y, textColor);
            y += lineSpacing;
        }
    }

    /** Call from actual mouse-button press events. */
    public void onLeftClick(long monotonicMillis) { clicks.recordLeftClick(monotonicMillis); }
    public void onRightClick(long monotonicMillis) { clicks.recordRightClick(monotonicMillis); }
    public HudModuleManager getModules() { return modules; }

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
        GameSnapshot.Extras extras = game.getOptionalHudData();
        return new GameSnapshot(fps, fpsAvailable, x, y, z, playerAvailable, playerName,
                keysAvailable, keys, cps.left, cps.right, true, worldTime,
                worldTimeAvailable, extras);
    }

    /**
     * Adapter over actual game APIs. Movement key order is W, A, S, D.
     * Optional methods must return real values from the target game or mark them unavailable.
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

        /** Override for values supported by the target game; default keeps them unavailable. */
        default GameSnapshot.Extras getOptionalHudData() {
            return GameSnapshot.Extras.unavailable();
        }
    }

    public interface HudRenderer {
        void drawString(String text, int x, int y, int color);
    }
}
