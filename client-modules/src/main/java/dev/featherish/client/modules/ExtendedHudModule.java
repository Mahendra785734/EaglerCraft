package dev.featherish.client.modules;

/**
 * HUD entries backed by optional fields in GameSnapshot.Extras.
 * An entry stays invisible until its adapter supplies the corresponding real value.
 */
public final class ExtendedHudModule implements HudModule {
    public enum Type {
        PLAYER_NAME, PING, DIRECTION, ARMOR, POTIONS, SERVER,
        BIOME, SPEED, HELD_ITEM, TARGET, MEMORY
    }

    private final Type type;
    private boolean enabled = true;

    public ExtendedHudModule(Type type) {
        if (type == null) throw new IllegalArgumentException("type is required");
        this.type = type;
    }

    @Override public String id() { return type.name().toLowerCase(java.util.Locale.ROOT); }

    @Override public String displayName() {
        switch (type) {
            case PLAYER_NAME: return "Player Name";
            case PING: return "Ping";
            case DIRECTION: return "Direction";
            case ARMOR: return "Armor Status";
            case POTIONS: return "Potion Effects";
            case SERVER: return "Server Address";
            case BIOME: return "Biome";
            case SPEED: return "Movement Speed";
            case HELD_ITEM: return "Held Item";
            case TARGET: return "Target Info";
            case MEMORY: return "Memory";
            default: return id();
        }
    }

    @Override public boolean isEnabled() { return enabled; }
    @Override public void setEnabled(boolean enabled) { this.enabled = enabled; }

    @Override public String getText(GameSnapshot s) {
        if (!enabled || s == null) return null;
        GameSnapshot.Extras e = s.extras;
        switch (type) {
            case PLAYER_NAME:
                return s.playerAvailable && !s.playerName.isEmpty() ? "Player " + s.playerName : null;
            case PING:
                return e.pingAvailable ? "Ping " + e.pingMs + " ms" : null;
            case DIRECTION:
                return e.directionAvailable ? "Facing " + e.direction : null;
            case ARMOR:
                return e.armorAvailable ? "Armor " + e.armorPoints + "/20" : null;
            case POTIONS:
                return e.potionsAvailable && !e.potionEffects.isEmpty() ? "Effects " + e.potionEffects : null;
            case SERVER:
                return e.serverAvailable && !e.serverAddress.isEmpty() ? "Server " + e.serverAddress : null;
            case BIOME:
                return e.biomeAvailable && !e.biome.isEmpty() ? "Biome " + e.biome : null;
            case SPEED:
                return e.speedAvailable ? String.format(java.util.Locale.ROOT, "Speed %.2f b/s", e.blocksPerSecond) : null;
            case HELD_ITEM:
                return e.selectedItemAvailable && !e.selectedItem.isEmpty() ? "Held " + e.selectedItem : null;
            case TARGET:
                return e.targetAvailable && !e.targetName.isEmpty()
                        ? String.format(java.util.Locale.ROOT, "Target %s (%.1f m)", e.targetName, e.targetDistance)
                        : null;
            case MEMORY:
                return e.memoryAvailable && e.memoryMaxMb > 0
                        ? String.format(java.util.Locale.ROOT, "Memory %d/%d MB", e.memoryUsedMb, e.memoryMaxMb)
                        : null;
            default:
                return null;
        }
    }
}
