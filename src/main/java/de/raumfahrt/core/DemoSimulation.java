package de.raumfahrt.core;

public final class DemoSimulation {

    private final MonitorPairProjection projection;
    private final double depth;
    private final double exitX;
    private final double entryX;
    private final double startX;
    private double worldX;
    private double speed;
    private double elapsed;
    private double exitElapsed;
    private boolean crossedExit;
    private double lastMeasuredGapTime;

    public DemoSimulation(MonitorPairProjection projection, double depth, double speed) {
        this.projection = projection;
        this.depth = depth;
        this.speed = speed;
        this.exitX = projection.leftExitWorldX(depth);
        this.entryX = projection.rightEntryWorldX(depth);
        this.startX = exitX - (entryX - exitX);
        this.worldX = startX;
    }

    public void advance(double deltaSeconds) {
        if (speed == 0.0) {
            return;
        }
        elapsed += deltaSeconds;
        worldX += speed * deltaSeconds;
        if (!crossedExit && worldX >= exitX && worldX < entryX) {
            crossedExit = true;
            exitElapsed = elapsed;
        }
        if (crossedExit && worldX >= entryX) {
            lastMeasuredGapTime = elapsed - exitElapsed;
            crossedExit = false;
        }
        if (worldX >= entryX + (entryX - exitX)) {
            worldX = startX;
        }
    }

    public double worldX() {
        return worldX;
    }

    public double startX() {
        return startX;
    }

    public double exitX() {
        return exitX;
    }

    public double entryX() {
        return entryX;
    }

    public double speed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double expectedGapTime() {
        return (entryX - exitX) / speed;
    }

    public double measuredGapTime() {
        return lastMeasuredGapTime;
    }

    public MonitorPairProjection projection() {
        return projection;
    }

    public double depth() {
        return depth;
    }
}
