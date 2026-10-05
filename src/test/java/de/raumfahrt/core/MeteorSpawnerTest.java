package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class MeteorSpawnerTest {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Test
    void createMeteorErzeugtMeteorIm3dRaum() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(5L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createMeteor();

        assertTrue(Math.abs(meteor.size()) >= 15 && meteor.size() <= 45);
        assertTrue(Math.abs(meteor.speedX()) >= 60 && Math.abs(meteor.speedX()) <= 120);
        assertTrue(meteor.rotation() == 0.0);
        assertTrue(Math.abs(meteor.rotationSpeed()) >= 1.0 * 2.0 * Math.PI / 60.0
                && Math.abs(meteor.rotationSpeed()) <= 10.0 * 2.0 * Math.PI / 60.0);
        assertTrue(meteor.depth() >= 200 && meteor.depth() <= 900);
        assertTrue(meteor.speedZ() < 0);
    }

    @Test
    void meteorIstBeiStandardFocalImSichtbereich() {
        assertImSichtbereich(new MeteorSpawner(new Random(5L), WIDTH, HEIGHT), 400.0, 500);
    }

    @Test
    void meteoreSindBeiKalibriertemFocalImSichtbereich() {
        double calibratedFocal = 100.0 * WIDTH / 53.0;
        MeteorSpawner spawner = new MeteorSpawner(new Random(21L), WIDTH, HEIGHT, calibratedFocal);

        for (int i = 0; i < 500; i++) {
            assertImSichtbereich(spawner, calibratedFocal, 1);
        }
    }

    @Test
    void meteoreBleibenUeberDieLebensdauerImSichtbereichStartendAmRand() {
        double focal = 2264.0;
        MeteorSpawner spawner = new MeteorSpawner(new Random(31L), WIDTH, HEIGHT, focal);

        for (int i = 0; i < 200; i++) {
            Meteor meteor = spawner.createMeteor();
            assertTrue(Math.abs(focal * meteor.x() / meteor.depth()) <= WIDTH / 2.0);
        }
    }

    @Test
    void spawnerLehntUngueltigenFocalAb() {
        assertThrows(IllegalArgumentException.class, () -> new MeteorSpawner(new Random(), WIDTH, HEIGHT, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new MeteorSpawner(new Random(), WIDTH, HEIGHT, -1.0));
    }

    private void assertImSichtbereich(MeteorSpawner spawner, double focal, int warmup) {
        for (int i = 0; i < warmup; i++) {
            spawner.createMeteor();
        }
        Meteor meteor = spawner.createMeteor();
        assertTrue(Math.abs(focal * meteor.x() / meteor.depth()) <= WIDTH / 2.0);
        assertTrue(Math.abs(focal * meteor.y() / meteor.depth()) <= HEIGHT / 2.0);
    }

    @Test
    void rotationsgeschwindigkeitVariiertZwischenMeteoren() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(13L), WIDTH, HEIGHT);

        double first = spawner.createMeteor().rotationSpeed();
        double second = spawner.createMeteor().rotationSpeed();

        assertTrue(first != second);
    }

    @Test
    void rotationsrichtungVariiertZwischenMeteoren() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(13L), WIDTH, HEIGHT);
        boolean positive = false;
        boolean negative = false;
        for (int i = 0; i < 100; i++) {
            if (spawner.createMeteor().rotationSpeed() > 0) {
                positive = true;
            } else {
                negative = true;
            }
        }
        assertTrue(positive && negative);
    }

    @Test
    void gleicherSeedErzeugtGleichenMeteor() {
        MeteorSpawner first = new MeteorSpawner(new Random(9L), WIDTH, HEIGHT);
        MeteorSpawner second = new MeteorSpawner(new Random(9L), WIDTH, HEIGHT);

        assertEquals(first.createMeteor(), second.createMeteor());
    }

    @Test
    void verschiedeneAufrufeErzeugenUnterschiedlicheMeteore() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(11L), WIDTH, HEIGHT);

        Meteor first = spawner.createMeteor();
        Meteor second = spawner.createMeteor();

        assertTrue(first.y() != second.y() || first.size() != second.size() || first.speedX() != second.speedX());
    }

    @Test
    void spawnIntervalLiegtInnerhalbDerGrenzen() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(3L), WIDTH, HEIGHT, 2.0, 4.0);

        for (int i = 0; i < 100; i++) {
            double interval = spawner.nextSpawnInterval();
            assertTrue(interval >= 2.0 && interval <= 4.0);
        }
    }

    @Test
    void tiefenverteilungHatFernenSchwerpunkt() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(7L), WIDTH, HEIGHT);
        int farCount = 0;
        int total = 200;

        for (int i = 0; i < total; i++) {
            if (spawner.createMeteor().depth() >= 500.0) {
                farCount++;
            }
        }

        assertTrue(farCount > total / 2);
    }

    @Test
    void explosionsfragmenteFliegenVonDerPositionWeg() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(15L), WIDTH, HEIGHT);

        java.util.List<ExplosionFragment> fragments = spawner.createExplosionFragments(0, 0, 100);

        assertTrue(fragments.size() >= 8);
        for (ExplosionFragment fragment : fragments) {
            assertTrue(fragment.x() == 0.0 && fragment.y() == 0.0);
            assertTrue(fragment.speedX() != 0.0 || fragment.speedY() != 0.0);
            assertTrue(fragment.size() >= 3 && fragment.size() <= 8);
        }
    }

    @Test
    void zielMeteorFliegtGeradeAufsFensterZu() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(17L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createAimedMeteor();

        assertEquals(0.0, meteor.x(), 1e-9);
        assertEquals(0.0, meteor.y(), 1e-9);
        assertEquals(0.0, meteor.speedX(), 1e-9);
        assertEquals(0.0, meteor.speedY(), 1e-9);
        assertTrue(meteor.speedZ() < 0);
        assertEquals(MeteorBehavior.STRAIGHT, meteor.behavior());
        assertTrue(meteor.depth() >= 200 && meteor.depth() <= 900);
    }

    @Test
    void crossingMeteorStartetAmLinkenRandUndFliegtRechts() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(23L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createCrossingMeteor(true);

        assertTrue(meteor.x() < 0);
        assertTrue(meteor.speedX() > 0);
        assertTrue(meteor.speedZ() < 0);
    }

    @Test
    void crossingMeteorStartetAmRechtenRandUndFliegtLinks() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(29L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createCrossingMeteor(false);

        assertTrue(meteor.x() > 0);
        assertTrue(meteor.speedX() < 0);
        assertTrue(meteor.speedZ() < 0);
    }

    @Test
    void animatedGifMeteorFliegtVonLinksDurchBeideMonitore() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(37L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createAnimatedGifMeteor();

        assertTrue(meteor.x() < 0);
        assertTrue(meteor.speedX() > 0);
        assertEquals(80.0, meteor.speedX(), 1e-9);
        assertEquals(28.0, meteor.speedY(), 1e-9);
        assertTrue(meteor.size() >= 16.0 && meteor.size() <= 20.0);
        assertEquals(450.0, meteor.depth(), 1e-9);
        assertEquals(-70.0, meteor.speedZ(), 1e-9);
        assertEquals(MeteorAppearance.ANIMATED_GIF, meteor.appearance());
        assertEquals(0.0, meteor.rotationSpeed());
    }

    @Test
    void zweiterAnimatedGifMeteorIstGezieltAuswaehlbar() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(37L), WIDTH, HEIGHT);

        Meteor meteor = spawner.createAnimatedGif2Meteor();

        assertEquals(MeteorAppearance.ANIMATED_GIF_2, meteor.appearance());
        assertTrue(meteor.x() > 0);
        assertEquals(-200.0, meteor.speedX(), 1e-9);
        assertEquals(-55.0, meteor.speedY(), 1e-9);
        assertTrue(meteor.size() >= 64.0 && meteor.size() <= 72.0);
        assertEquals(700.0, meteor.depth(), 1e-9);
        assertEquals(-180.0, meteor.speedZ(), 1e-9);
    }

    @Test
    void animatedGifAsteroidenVerwendenUnabhaengigeFlugprofile() {
        MeteorSpawner spawner = new MeteorSpawner(new Random(43L), WIDTH, HEIGHT);

        Meteor first = spawner.createAnimatedGifMeteor();
        Meteor second = spawner.createAnimatedGif2Meteor();

        assertTrue(first.size() < second.size());
        assertTrue(first.speedX() > 0);
        assertTrue(second.speedX() < 0);
        assertTrue(first.speedX() != second.speedX());
        assertTrue(first.speedY() != second.speedY());
        assertTrue(first.depth() != second.depth());
        assertTrue(second.size() > first.size() * 3.0);

        MonitorPairProjection projection =
                new MonitorPairProjection(WIDTH, HEIGHT, 0.0, MeteorSpawner.DEFAULT_FOCAL_PX);
        assertEquals(WIDTH * 0.3, projection.screenXCentered(first.x(), first.depth()), 1e-9);
        assertEquals(HEIGHT * 0.25, projection.screenY(first.y(), first.depth()), 1e-9);
        assertEquals(WIDTH * 0.7, projection.screenXCentered(second.x(), second.depth()), 1e-9);
        assertEquals(HEIGHT * 0.75, projection.screenY(second.y(), second.depth()), 1e-9);
    }
}
