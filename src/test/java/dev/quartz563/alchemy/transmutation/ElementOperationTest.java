package dev.quartz563.alchemy.transmutation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementOperationTest {

    @Nested
    class Strip {
        @Test
        void removesAnyQuantity() {
            assertEquals(0, ElementOperation.strip().apply(7));
        }

        @Test
        void leavesAbsentElementAbsent() {
            assertEquals(0, ElementOperation.strip().apply(0));
        }
    }

    @Nested
    class Scale {
        @Test
        void floorsFractionalResults() {
            assertEquals(3, ElementOperation.scale(0.5).apply(7));   // 3.5 -> 3
            assertEquals(4, ElementOperation.scale(1.5).apply(3));   // 4.5 -> 4
        }

        @Test
        void doesNotIntroduceAbsentElement() {
            assertEquals(0, ElementOperation.scale(2.0).apply(0));
        }

        @Test
        void neverProducesNegativeQuantity() {
            assertEquals(0, ElementOperation.scale(-1.0).apply(5));
        }
    }

    @Nested
    class Shift {
        @Test
        void addsToPresentElement() {
            assertEquals(7, ElementOperation.shift(2).apply(5));
        }

        @Test
        void clampsAtZero() {
            assertEquals(0, ElementOperation.shift(-3).apply(2));
        }

        @Test
        void doesNotIntroduceAbsentElement() {
            assertEquals(0, ElementOperation.shift(2).apply(0));
        }
    }

    @Nested
    class AddFlat {
        @Test
        void introducesAbsentElement() {
            assertEquals(2, ElementOperation.addFlat(2).apply(0));
        }

        @Test
        void clampsAtZero() {
            assertEquals(0, ElementOperation.addFlat(-5).apply(3));
        }
    }

    @Test
    void neutralLeavesQuantityUnchanged() {
        assertEquals(4, ElementOperation.neutral().apply(4));
        assertEquals(0, ElementOperation.neutral().apply(0));
    }
}
