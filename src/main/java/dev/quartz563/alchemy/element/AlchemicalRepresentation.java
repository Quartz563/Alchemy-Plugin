package dev.quartz563.alchemy.element;

import org.jspecify.annotations.NonNull;

import java.util.EnumMap;
import java.util.Map;

public record AlchemicalRepresentation(Map<Element, Integer> elements) {

    public int getQuantity(Element element) {
        return elements.getOrDefault(element, 0);
    }

    public boolean contains(Element element) {
        return elements.containsKey(element);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public @NonNull String toString() {
        StringBuilder sb = new StringBuilder();
        elements.forEach((element, quantity) -> {
            if (!sb.isEmpty())
                sb.append(" + ");
            sb.append(quantity).append(element.getDisplayName());
        });
        return sb.isEmpty() ? "Empty" : sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private final Map<Element, Integer> elements = new EnumMap<>(Element.class);

        private Builder() {
        }

        public Builder add(Element element, int quantity) {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive, got: " + quantity);
            } else {
                elements.merge(element, quantity, Integer::sum);
                return this;
            }
        }

        public AlchemicalRepresentation build() {
            return new AlchemicalRepresentation(new EnumMap<>(elements));
        }
    }
}
