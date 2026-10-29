package dev.quartz563.alchemy.transmutation;

/**
 * Describes how a single element's quantity is modified during a preparation stage.
 *
 * <p>Each {@link TransformationType} holds one operation per {@link dev.quartz563.alchemy.element.Element}.
 * When a transformation is applied, each element in the input representation is passed
 * through its corresponding operation to produce the output quantity.
 *
 * <p>Implementations are sealed records to keep the operation set explicit and exhaustive.
 * Use the static factory methods rather than constructing records directly.
 */
public sealed interface ElementOperation
        permits ElementOperation.Strip,
        ElementOperation.Scale,
        ElementOperation.Shift,
        ElementOperation.AddFlat,
        ElementOperation.Neutral {

    /**
     * Applies this operation to the current quantity of an element.
     *
     * @param current the element's current quantity; 0 if the element is absent
     */
    int apply(int current);


    /**
     * Removes the element entirely, regardless of its current quantity.
     * Used when a process fully consumes or expels an element (e.g. Dissolution stripping Fire).
     */
    record Strip() implements ElementOperation {
        @Override
        public int apply(final int current) {
            return 0;
        }
    }

    /**
     * Multiplies the current quantity by a factor, flooring the result.
     * Has no effect if the element is absent — zero in, zero out.
     * Use factors below 1.0 for reduction and above 1.0 for amplification.
     * @param factor value for element multiplication
     */
    record Scale(double factor) implements ElementOperation {
        @Override
        public int apply(final int current) {
            return current == 0 ? 0 : Math.max(0, (int) Math.floor(current * this.factor));
        }
    }


    /**
     * Shifts the current quantity by a flat amount, clamped to 0.
     * Has no effect if the element is absent, use {@link AddFlat} to introduce new elements.
     * @param amount Negative amounts produce a slight reduction; positive amounts produce a slight boost
     */
    record Shift(int amount) implements ElementOperation {
        @Override
        public int apply(final int current) {
            return current == 0 ? 0 : Math.max(0 , current + this.amount);
        }
    }

    /**
     * Adds a flat amount unconditionally, even if the element was previously absent.
     * Used when a process always introduces an element regardless of the input
     * (e.g. Reduction always leaving a trace of Fire as the ash burns).
     * @param amount absolute amount of new element to add
     */
    record AddFlat(int amount) implements ElementOperation {
        @Override
        public int apply(final int current) {
            return Math.max(0, current + this.amount);
        }
    }

    /**
     * Leaves the quantity unchanged. Applied when a process has no effect on an element.
     */
    record Neutral() implements ElementOperation {
        @Override
        public int apply(final int current) {
            return current;
        }
    }

    static ElementOperation strip() {
        return new Strip();
    }

    static ElementOperation scale(double factor) {
        return new Scale(factor);
    }

    static ElementOperation shift(int amount) {
        return new Shift(amount);
    }

    static ElementOperation addFlat(int amount) {
        return new AddFlat(amount);
    }

    static ElementOperation neutral() {
        return new Neutral();
    }
}
