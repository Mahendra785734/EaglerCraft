package dev.featherish.client.modules;

/**
 * Immutable read-only snapshot populated by the live game adapter.
 * Values that are unavailable are marked unavailable; they are never fabricated.
 */
public final class GameSnapshot {
    public final int fps;
    public final boolean fpsAvailable;
    public final double x, y, z;
    public final boolean playerAvailable;
    public final String playerName;
    public final boolean keysAvailable;
    public final boolean[] keysDown;
    public final int leftCps, rightCps;
    public final boolean cpsAvailable;
    public final long worldTime;
    public final boolean worldTimeAvailable;
    public final Extras extras;

    public GameSnapshot(
            int fps, boolean fpsAvailable,
            double x, double y, double z, boolean playerAvailable,
            String playerName, boolean keysAvailable, boolean[] keysDown,
            int leftCps, int rightCps, boolean cpsAvailable,
            long worldTime, boolean worldTimeAvailable) {
        this(fps, fpsAvailable, x, y, z, playerAvailable, playerName, keysAvailable,
                keysDown, leftCps, rightCps, cpsAvailable, worldTime,
                worldTimeAvailable, Extras.unavailable());
    }

    public GameSnapshot(
            int fps, boolean fpsAvailable,
            double x, double y, double z, boolean playerAvailable,
            String playerName, boolean keysAvailable, boolean[] keysDown,
            int leftCps, int rightCps, boolean cpsAvailable,
            long worldTime, boolean worldTimeAvailable, Extras extras) {
        this.fps = Math.max(0, fps);
        this.fpsAvailable = fpsAvailable;
        this.x = x;
        this.y = y;
        this.z = z;
        this.playerAvailable = playerAvailable;
        this.playerName = playerName == null ? "" : playerName;
        this.keysAvailable = keysAvailable;
        this.keysDown = keysDown == null ? new boolean[0] : keysDown.clone();
        this.leftCps = Math.max(0, leftCps);
        this.rightCps = Math.max(0, rightCps);
        this.cpsAvailable = cpsAvailable;
        this.worldTime = worldTime;
        this.worldTimeAvailable = worldTimeAvailable;
        this.extras = extras == null ? Extras.unavailable() : extras;
    }

    public boolean isKeyDown(int index) {
        return keysAvailable && index >= 0 && index < keysDown.length && keysDown[index];
    }

    /** Optional data supplied by version-specific adapters when the game exposes it. */
    public static final class Extras {
        public final int pingMs;
        public final boolean pingAvailable;
        public final String direction;
        public final boolean directionAvailable;
        public final int armorPoints;
        public final boolean armorAvailable;
        public final String potionEffects;
        public final boolean potionsAvailable;
        public final String serverAddress;
        public final boolean serverAvailable;
        public final String biome;
        public final boolean biomeAvailable;
        public final double blocksPerSecond;
        public final boolean speedAvailable;
        public final String selectedItem;
        public final boolean selectedItemAvailable;
        public final String targetName;
        public final double targetDistance;
        public final boolean targetAvailable;
        public final long memoryUsedMb;
        public final long memoryMaxMb;
        public final boolean memoryAvailable;

        public Extras(int pingMs, boolean pingAvailable, String direction, boolean directionAvailable,
                int armorPoints, boolean armorAvailable, String potionEffects, boolean potionsAvailable,
                String serverAddress, boolean serverAvailable, String biome, boolean biomeAvailable,
                double blocksPerSecond, boolean speedAvailable, String selectedItem,
                boolean selectedItemAvailable, String targetName, double targetDistance,
                boolean targetAvailable, long memoryUsedMb, long memoryMaxMb, boolean memoryAvailable) {
            this.pingMs = Math.max(0, pingMs);
            this.pingAvailable = pingAvailable;
            this.direction = clean(direction);
            this.directionAvailable = directionAvailable;
            this.armorPoints = Math.max(0, Math.min(20, armorPoints));
            this.armorAvailable = armorAvailable;
            this.potionEffects = clean(potionEffects);
            this.potionsAvailable = potionsAvailable;
            this.serverAddress = clean(serverAddress);
            this.serverAvailable = serverAvailable;
            this.biome = clean(biome);
            this.biomeAvailable = biomeAvailable;
            this.blocksPerSecond = Math.max(0.0D, blocksPerSecond);
            this.speedAvailable = speedAvailable;
            this.selectedItem = clean(selectedItem);
            this.selectedItemAvailable = selectedItemAvailable;
            this.targetName = clean(targetName);
            this.targetDistance = Math.max(0.0D, targetDistance);
            this.targetAvailable = targetAvailable;
            this.memoryUsedMb = Math.max(0L, memoryUsedMb);
            this.memoryMaxMb = Math.max(0L, memoryMaxMb);
            this.memoryAvailable = memoryAvailable;
        }

        public static Extras unavailable() {
            return new Extras(0, false, "", false, 0, false, "", false,
                    "", false, "", false, 0.0D, false, "", false,
                    "", 0.0D, false, 0L, 0L, false);
        }

        private static String clean(String value) { return value == null ? "" : value; }
    }
}
