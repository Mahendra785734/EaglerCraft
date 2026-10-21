package dev.featherish.client.modules;

/** Minimal data-backed HUD modules; drawing is owned by the target renderer. */
public final class BasicHudModule implements HudModule {
    public enum Type { FPS, COORDINATES, KEYSTROKES, CPS, WORLD_TIME }

    private final Type type;
    private boolean enabled = true;

    public BasicHudModule(Type type) {
        if (type == null) throw new IllegalArgumentException("type is required");
        this.type = type;
    }

    @Override public String id() { return type.name().toLowerCase(java.util.Locale.ROOT); }

    @Override public String displayName() {
        switch (type) {
            case FPS: return "FPS";
            case COORDINATES: return "Coordinates";
            case KEYSTROKES: return "Keystrokes";
            case CPS: return "CPS";
            case WORLD_TIME: return "World Time";
            default: return id();
        }
    }

    @Override public boolean isEnabled() { return enabled; }
    @Override public void setEnabled(boolean enabled) { this.enabled = enabled; }

    @Override public String getText(GameSnapshot s) {
        if (!enabled || s == null) return null;
        switch (type) {
            case FPS:
                return s.fpsAvailable ? s.fps + " FPS" : null;
            case COORDINATES:
                return s.playerAvailable
                        ? String.format(java.util.Locale.ROOT, "XYZ %.1f, %.1f, %.1f", s.x, s.y, s.z)
                        : null;
            case KEYSTROKES:
                return s.keysAvailable
                        ? "W " + key(s, 0) + "  A " + key(s, 1) + "  S " + key(s, 2) + "  D " + key(s, 3)
                        : null;
            case CPS:
                return s.cpsAvailable ? "CPS L " + s.leftCps + " / R " + s.rightCps : null;
            case WORLD_TIME:
                return s.worldTimeAvailable ? "World time " + s.worldTime : null;
            default:
                return null;
        }
    }

    private static String key(GameSnapshot s, int index) {
        return s.isKeyDown(index) ? "[X]" : "[ ]";
    }
}
