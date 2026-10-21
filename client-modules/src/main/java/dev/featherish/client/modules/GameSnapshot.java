package dev.featherish.client.modules;

/**
 * Read-only snapshot populated by the live game adapter.
 *
 * Missing values are represented with availability flags rather than fake
 * defaults. The adapter should refresh this snapshot from the running client.
 */
public final class GameSnapshot {
    public final int fps;
    public final boolean fpsAvailable;
    public final double x, y, z;
    public final boolean playerAvailable;
    public final String playerName;
    public final boolean[] keysDown;
    public final int leftCps, rightCps;
    public final boolean cpsAvailable;
    public final long worldTime;
    public final boolean worldTimeAvailable;

    public GameSnapshot(
            int fps, boolean fpsAvailable,
            double x, double y, double z, boolean playerAvailable,
            String playerName, boolean[] keysDown,
            int leftCps, int rightCps, boolean cpsAvailable,
            long worldTime, boolean worldTimeAvailable) {
        this.fps = fps;
        this.fpsAvailable = fpsAvailable;
        this.x = x;
        this.y = y;
        this.z = z;
        this.playerAvailable = playerAvailable;
        this.playerName = playerName == null ? "" : playerName;
        this.keysDown = keysDown == null ? new boolean[0] : keysDown.clone();
        this.leftCps = Math.max(0, leftCps);
        this.rightCps = Math.max(0, rightCps);
        this.cpsAvailable = cpsAvailable;
        this.worldTime = worldTime;
        this.worldTimeAvailable = worldTimeAvailable;
    }

    public boolean isKeyDown(int index) {
        return index >= 0 && index < keysDown.length && keysDown[index];
    }
}
