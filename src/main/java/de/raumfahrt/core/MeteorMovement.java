package de.raumfahrt.core;

final class MeteorMovement {

    private static final double ACCELERATION_BASE_DEPTH = 500.0;

    Meteor move(Meteor meteor, double deltaSeconds) {
        BehaviorStep step = behaviorStep(meteor, deltaSeconds);
        return new Meteor(
                meteor.id(),
                meteor.x() + step.deltaX(),
                meteor.y() + step.deltaY(),
                meteor.depth() + step.deltaZ(),
                meteor.size(),
                meteor.speedX(),
                meteor.speedY(),
                meteor.speedZ(),
                meteor.shapeSeed(),
                meteor.rotation() + meteor.rotationSpeed() * deltaSeconds,
                meteor.rotationSpeed(),
                meteor.behavior(),
                meteor.zigzagAmplitude(),
                meteor.zigzagFrequency(),
                step.phase());
    }

    private BehaviorStep behaviorStep(Meteor meteor, double deltaSeconds) {
        double deltaX = meteor.speedX() * deltaSeconds;
        double deltaY = meteor.speedY() * deltaSeconds;
        double deltaZ = meteor.speedZ() * deltaSeconds;
        double phase = meteor.zigzagPhase();
        if (meteor.behavior() == MeteorBehavior.ACCELERATING) {
            double factor = Math.max(1.0, ACCELERATION_BASE_DEPTH / meteor.depth());
            deltaX *= factor;
            deltaY *= factor;
            deltaZ *= factor;
        } else if (meteor.behavior() == MeteorBehavior.ZIGZAG) {
            deltaX += Math.sin(phase) * meteor.zigzagAmplitude() * deltaSeconds;
            phase += meteor.zigzagFrequency() * deltaSeconds;
        }
        return new BehaviorStep(deltaX, deltaY, deltaZ, phase);
    }

    private record BehaviorStep(double deltaX, double deltaY, double deltaZ, double phase) {}
}
