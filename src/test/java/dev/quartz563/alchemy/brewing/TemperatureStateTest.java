package dev.quartz563.alchemy.brewing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureStateTest {

    @Test
    void nextMovesOneStepHotter() {
        assertEquals(TemperatureState.WARM, TemperatureState.COLD.next());
    }

    @Test
    void previousMovesOneStepColder() {
        assertEquals(TemperatureState.COLD, TemperatureState.WARM.previous());
    }

    @Test
    void cannotHeatBeyondBoiling() {
        assertEquals(TemperatureState.BOILING, TemperatureState.BOILING.next());
    }

    @Test
    void cannotCoolBelowFrozen() {
        assertEquals(TemperatureState.FROZEN, TemperatureState.FROZEN.previous());
    }

    @Test
    void comparesTemperatures() {
        assertTrue(TemperatureState.COLD.isLowerTemp(TemperatureState.HOT));
        assertTrue(TemperatureState.HOT.isHigherTemp(TemperatureState.COLD));
    }

    @Test
    void stateIsNeitherHigherNorLowerThanItself() {
        assertFalse(TemperatureState.HOT.isHigherTemp(TemperatureState.HOT));
        assertFalse(TemperatureState.HOT.isLowerTemp(TemperatureState.HOT));
    }
}
