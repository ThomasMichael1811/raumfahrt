package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class WarpSchedulerTest {

    private final WarpState state = new WarpState();
    private final List<SceneType> switches = new ArrayList<>();
    private final WarpTransitionController controller =
            new WarpTransitionController(state, switches::add, () -> SceneType.NORMAL);

    @Test
    void schedulerStartetInaktiv() {
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), controller, () -> SceneType.COMET);

        assertFalse(controller.active());
    }

    @Test
    void schedulerLoestNachIntervallAutomatischenWechselAus() {
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), controller, () -> SceneType.COMET);

        scheduler.update(180.0);

        assertTrue(controller.active());
        assertEquals(SceneType.COMET, controller.targetScene());
    }

    @Test
    void schedulerLoestVorIntervallNichtAus() {
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), controller, () -> SceneType.COMET);

        scheduler.update(30.0);

        assertFalse(controller.active());
    }

    @Test
    void automatischerWechselWechseltSzeneInDerMitte() {
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), controller, () -> SceneType.COMET);
        scheduler.update(180.0);

        controller.update(0.9);
        assertTrue(switches.isEmpty());
        controller.update(0.2);

        assertEquals(List.of(SceneType.COMET), switches);
    }

    @Test
    void schedulerUnterdruecktTriggerWaehrendAktiverTransition() {
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), controller, () -> SceneType.COMET);
        controller.startSceneTransition(SceneType.RED_SUN);

        scheduler.update(200.0);

        assertEquals(SceneType.RED_SUN, controller.targetScene());
        assertTrue(switches.isEmpty());
    }

    @Test
    void schedulerPlantNachTransitionNeu() {
        WarpScheduler scheduler = new WarpScheduler(new Random(1L), controller, () -> SceneType.COMET);
        controller.startSceneTransition(SceneType.RED_SUN);
        scheduler.update(1.0);
        controller.update(2.0);
        scheduler.update(1.0);

        assertFalse(controller.active());
        assertTrue(scheduler.timeToNextWarp() >= 60.0 && scheduler.timeToNextWarp() <= 180.0);
    }
}
