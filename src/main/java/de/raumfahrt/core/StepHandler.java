package de.raumfahrt.core;

@FunctionalInterface
public interface StepHandler {

    void step(double deltaSeconds);
}
