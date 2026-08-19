package de.raumfahrt.core;

public final class MeteorField {

    private static final double SIZE_GROWTH_PER_SECOND = 4.0;

    private final int width;
    private Meteor meteor;

    public MeteorField(int width, Meteor meteor) {
        this.width = width;
        this.meteor = meteor;
    }

    public Meteor meteor() {
        return meteor;
    }

    public void update(double deltaSeconds) {
        if (meteor == null) {
            return;
        }
        double x = meteor.x() + meteor.speedX() * deltaSeconds;
        double y = meteor.y() + meteor.speedY() * deltaSeconds;
        double size = meteor.size() + SIZE_GROWTH_PER_SECOND * deltaSeconds;
        double rotation = meteor.rotation() + meteor.rotationSpeed() * deltaSeconds;
        meteor = new Meteor(x, y, size, meteor.speedX(), meteor.speedY(),
            rotation, meteor.rotationSpeed());
        if (x - size > width) {
            meteor = null;
        }
    }
}
