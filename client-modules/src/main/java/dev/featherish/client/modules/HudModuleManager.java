package dev.featherish.client.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Registry for all HUD modules. Call getVisibleLines once per render frame. */
public final class HudModuleManager {
    private final List<HudModule> modules = new ArrayList<HudModule>();

    public HudModuleManager() {
        register(new BasicHudModule(BasicHudModule.Type.FPS));
        register(new BasicHudModule(BasicHudModule.Type.COORDINATES));
        register(new BasicHudModule(BasicHudModule.Type.KEYSTROKES));
        register(new BasicHudModule(BasicHudModule.Type.CPS));
        register(new BasicHudModule(BasicHudModule.Type.WORLD_TIME));
        for (ExtendedHudModule.Type type : ExtendedHudModule.Type.values()) {
            register(new ExtendedHudModule(type));
        }
    }

    public void register(HudModule module) {
        if (module == null) throw new IllegalArgumentException("module is required");
        if (find(module.id()) != null) throw new IllegalArgumentException("duplicate module: " + module.id());
        modules.add(module);
    }

    public HudModule find(String id) {
        if (id == null) return null;
        for (HudModule module : modules) {
            if (module.id().equalsIgnoreCase(id)) return module;
        }
        return null;
    }

    public List<HudModule> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public boolean setEnabled(String id, boolean enabled) {
        HudModule module = find(id);
        if (module == null) return false;
        module.setEnabled(enabled);
        return true;
    }

    public List<String> getVisibleLines(GameSnapshot snapshot) {
        List<String> lines = new ArrayList<String>();
        for (HudModule module : modules) {
            String text = module.getText(snapshot);
            if (text != null && !text.isEmpty()) lines.add(text);
        }
        return lines;
    }
}
