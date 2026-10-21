package dev.featherish.client.modules;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedHudModuleTest {
    @Test
    public void registersExpandedModuleSet() {
        HudModuleManager manager = new HudModuleManager();
        assertTrue(manager.getModules().size() >= 16);
        assertNotNull(manager.find("fps"));
        assertNotNull(manager.find("ping"));
        assertNotNull(manager.find("armor"));
        assertNotNull(manager.find("target"));
        assertNotNull(manager.find("memory"));
    }

    @Test
    public void optionalModulesStayHiddenUntilRealDataIsAvailable() {
        ExtendedHudModule ping = new ExtendedHudModule(ExtendedHudModule.Type.PING);
        GameSnapshot empty = new GameSnapshot(0, false, 0, 0, 0, false, "",
                false, null, 0, 0, false, 0, false);
        assertNull(ping.getText(empty));

        GameSnapshot.Extras extras = new GameSnapshot.Extras(
                42, true, "North", true, 16, true, "Speed II", true,
                "play.example.invalid", true, "Plains", true, 4.25, true,
                "Iron Sword", true, "TestMob", 3.5, true, 512, 2048, true);
        GameSnapshot populated = new GameSnapshot(144, true, 10, 64, -20,
                true, "PlayerOne", true, new boolean[] {true, false, false, true},
                7, 3, true, 6000, true, extras);

        assertEquals("Ping 42 ms", ping.getText(populated));
        assertEquals("Armor 16/20",
                new ExtendedHudModule(ExtendedHudModule.Type.ARMOR).getText(populated));
        assertEquals("Target TestMob (3.5 m)",
                new ExtendedHudModule(ExtendedHudModule.Type.TARGET).getText(populated));
        assertEquals("Memory 512/2048 MB",
                new ExtendedHudModule(ExtendedHudModule.Type.MEMORY).getText(populated));
    }

    @Test
    public void moduleCanBeToggledThroughManager() {
        HudModuleManager manager = new HudModuleManager();
        assertTrue(manager.setEnabled("ping", false));
        assertFalse(manager.find("ping").isEnabled());
        assertFalse(manager.setEnabled("does-not-exist", true));
    }
}
