package dev.featherish.client.modules;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClickRateCounterTest {
    @Test
    public void countsClicksWithinRollingSecond() {
        ClickRateCounter counter = new ClickRateCounter();
        counter.recordLeftClick(100);
        counter.recordLeftClick(500);
        counter.recordRightClick(700);

        ClickRateCounter.Counts counts = counter.snapshot(900);
        assertEquals(2, counts.left);
        assertEquals(1, counts.right);
    }

    @Test
    public void expiresClicksAtOneSecondBoundary() {
        ClickRateCounter counter = new ClickRateCounter();
        counter.recordLeftClick(100);
        counter.recordRightClick(101);

        ClickRateCounter.Counts counts = counter.snapshot(1100);
        assertEquals(0, counts.left);
        assertEquals(0, counts.right);
    }

    @Test
    public void ignoresOutOfOrderInputEvents() {
        ClickRateCounter counter = new ClickRateCounter();
        counter.recordLeftClick(500);
        counter.recordLeftClick(400);

        assertEquals(1, counter.snapshot(700).left);
    }
}
