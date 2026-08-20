package de.raumfahrt.rendering;

import java.awt.Color;

public record ProjectedObject(double worldX, double worldY, double depth, double radiusPx, Color color) {}
