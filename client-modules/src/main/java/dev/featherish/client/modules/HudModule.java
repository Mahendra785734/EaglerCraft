package dev.featherish.client.modules;

/** A small HUD component whose data comes only from a live game snapshot. */
public interface HudModule {
    String id();
    String displayName();
    boolean isEnabled();
    void setEnabled(boolean enabled);

    /**
     * @return text to render, or null when required game data is unavailable.
     */
    String getText(GameSnapshot snapshot);
}
