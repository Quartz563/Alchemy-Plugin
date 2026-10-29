package dev.quartz563.alchemy.structures.util;

/**
 * Represents a specific coordinate in relation to 0, 0, 0 without defining its absolute position.
 * This is used to locate a block in relation to the structure's "Activation Block", which is considered
 * 0, 0, 0 for the structure.
 * @param x Positive for East (Right), Negative for West (Left)
 * @param y Positive for Up, Negative for Down
 * @param z Positive for North (Forward), Negative for South (Back)
 */
public record RelativeCoordinate(int x, int y, int z) {
}
