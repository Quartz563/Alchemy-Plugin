package dev.quartz563.alchemy.brewing;

public enum TemperatureState {
    FROZEN(0),
    COLD(1),
    WARM(2),
    HOT(3),
    BOILING(4);

    private static final TemperatureState[] vals = values();

    private Integer temperatureValue;

    public TemperatureState next() {
        int nextIndex = Math.min(this.ordinal() + 1, vals.length - 1);
        return vals[nextIndex];
    }

    public TemperatureState previous() {
        int prevIndex = Math.max(this.ordinal() - 1, 0);
        return vals[prevIndex];
    }

    public boolean isLowerTemp(TemperatureState other) {
        return this.temperatureValue < other.temperatureValue;
    }

    public boolean isHigherTemp(TemperatureState other) {
        return this.temperatureValue > other.temperatureValue;
    }

    TemperatureState(int tempLevel) {
        this.temperatureValue = tempLevel;
    }
}
