package dev.featherish.client.modules;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;

public class HudModuleManagerTest {
    private GameSnapshot snapshot(boolean player, boolean keys, boolean fps, boolean world) {
        return new GameSnapshot(144, fps, 12.25, 64.0, -3.5, player,
                "TestPlayer", keys, new boolean[] { true, false, true, true },
                5, 2, true, 6000L, world);
    }

    @Test
    public void defaultModulesRenderOnlyAvailableData() {
        HudModuleManager manager = new HudModuleManager();
        List<String> lines = manager.getVisibleLines(snapshot(false, false, false, false));
        assertEquals(1, lines.size());
        assertEquals("CPS L 5 / R 2", lines.get(0));
    }

    @Test
    public void coordinatesAndWorldTimeUseSnapshotValues() {
        HudModuleManager manager = new HudModuleManager();
        List<String> lines = manager.getVisibleLines(snapshot(true, true, true, true));
        assertTrue(lines.contains("144 FPS"));
        assertTrue(lines.contains("XYZ 12.3, 64.0, -3.5"));
        assertTrue(lines.contains("W [X]  A [ ]  S [X]  D [X]"));
        assertTrue(lines.contains("World time 6000"));
    }

    @Test
    public void disabledModuleIsNotRendered() {
        HudModuleManager manager = new HudModuleManager();
        manager.find("fps").setEnabled(false);
        assertFalse(manager.getVisibleLines(snapshot(true, true, true, true)).contains("144 FPS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void duplicateModuleIdsAreRejected() {
        HudModuleManager manager = new HudModuleManager();
        manager.register(new BasicHudModule(BasicHudModule.Type.FPS));
    }

    @Test
    public void snapshotDefensivelyCopiesKeyState() {
        boolean[] keys = { true, false, false, true };
        GameSnapshot snapshot = new GameSnapshot(60, true, 0, 0, 0, true,
                "Player", true, keys, 0, 0, true, 0, false);
        keys[0] = false;
        assertTrue(snapshot.isKeyDown(0));
    }
}
