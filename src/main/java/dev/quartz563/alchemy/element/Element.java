package dev.quartz563.alchemy.element;

import dev.quartz563.alchemy.configs.AlchemySettings;

public enum Element {

    FIRE, WATER, EARTH, AIR, LIFE, DEATH, MAGIC, MUNDANE;

    private String displayName;
    private Element opposite;
    private Element[] harmonics;
    private Element[] conjunctions;

    static {
        FIRE.init(AlchemySettings.Elements.FIRE, WATER, new Element[]{MAGIC, LIFE}, new Element[]{MUNDANE, DEATH});
        WATER.init(AlchemySettings.Elements.WATER, FIRE, new Element[]{MUNDANE, DEATH}, new Element[]{MAGIC, LIFE});
        EARTH.init(AlchemySettings.Elements.EARTH, AIR, new Element[]{LIFE, MUNDANE}, new Element[]{MAGIC, DEATH});
        AIR.init(AlchemySettings.Elements.AIR, EARTH, new Element[]{MAGIC, DEATH}, new Element[]{MUNDANE, LIFE});
        LIFE.init(AlchemySettings.Elements.LIFE, DEATH, new Element[]{FIRE, EARTH}, new Element[]{WATER, AIR});
        DEATH.init(AlchemySettings.Elements.DEATH, LIFE, new Element[]{WATER, AIR}, new Element[]{FIRE, EARTH});
        MAGIC.init(AlchemySettings.Elements.MAGIC, MUNDANE, new Element[]{FIRE, AIR}, new Element[]{EARTH, WATER});
        MUNDANE.init(AlchemySettings.Elements.MUNDANE, MAGIC, new Element[]{EARTH, WATER}, new Element[]{FIRE, AIR});
    }

    private void init(String displayName, Element opposite, Element[] harmonics, Element[] conjunctions) {
        this.displayName = displayName;
        this.opposite = opposite;
        this.harmonics = harmonics;
        this.conjunctions = conjunctions;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Returns the element directly opposing this one. */
    public Element getOpposite() {
        return opposite;
    }

    /** Returns the two elements that naturally bind with this one. */
    public Element[] getHarmonics() {
        return harmonics;
    }

    /** Returns the two elements that can bind this element with its opposite. */
    public Element[] getConjunctions() {
        return conjunctions;
    }

    /** Returns true if the given element is harmonic with this one. */
    public boolean isHarmonicWith(Element other) {
        return harmonics[0] == other || harmonics[1] == other;
    }

    /** Returns true if the given element is a conjunction of this one. */
    public boolean isConjunctionWith(Element other) {
        return conjunctions[0] == other || conjunctions[1] == other;
    }
}
