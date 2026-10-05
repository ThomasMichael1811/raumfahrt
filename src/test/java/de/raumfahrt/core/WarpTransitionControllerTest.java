package de.raumfahrt.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class WarpTransitionControllerTest {

    private final WarpState state = new WarpState();
    private final SceneType[] current = {SceneType.NORMAL};
    private final List<SceneType> switches = new ArrayList<>();
    private final boolean[] paused = {false};
    private final WarpTransitionController controller = new WarpTransitionController(
            state,
            scene -> {
                current[0] = scene;
                switches.add(scene);
            },
            () -> current[0],
            () -> paused[0]);

    @Test
    void controllerStartetInaktiv() {
        assertFalse(controller.active());
        assertFalse(state.active());
    }

    @Test
    void startSceneTransitionAktiviertWarpMitZielszene() {
        assertTrue(controller.startSceneTransition(SceneType.RED_SUN));

        assertTrue(controller.active());
        assertTrue(state.active());
        assertEquals(SceneType.RED_SUN, controller.targetScene());
        assertEquals(WarpTransitionController.DURATION_SECONDS, state.durationSeconds(), 1e-9);
    }

    @Test
    void szenenwechselErfolgtBeiFuenfzigProzent() {
        controller.startSceneTransition(SceneType.RED_SUN);

        controller.update(0.9);
        assertTrue(switches.isEmpty(), "vor der Mitte darf nicht gewechselt werden");

        controller.update(0.2);
        assertEquals(List.of(SceneType.RED_SUN), switches);
    }

    @Test
    void szenenwechselErfolgtNurEinmal() {
        controller.startSceneTransition(SceneType.RED_SUN);

        controller.update(1.0);
        controller.update(0.5);

        assertEquals(1, switches.size());
    }

    @Test
    void transitionEndetNachZweiSekunden() {
        controller.startSceneTransition(SceneType.RED_SUN);

        controller.update(2.0);

        assertFalse(controller.active());
        assertFalse(state.active());
    }

    @Test
    void eigenstaendigerWarpWechseltKeineSzene() {
        assertTrue(controller.startIndependentWarp());

        controller.update(2.0);

        assertTrue(switches.isEmpty());
        assertFalse(controller.active());
    }

    @Test
    void gleicheZielszeneLoestKeinenWarpAus() {
        assertFalse(controller.startSceneTransition(SceneType.NORMAL));
        assertFalse(state.active());
    }

    @Test
    void doppelstartWirdIgnoriert() {
        assertTrue(controller.startSceneTransition(SceneType.RED_SUN));

        assertFalse(controller.startSceneTransition(SceneType.TWO_SUNS));
        assertFalse(controller.startIndependentWarp());
        assertEquals(SceneType.RED_SUN, controller.targetScene());
    }

    @Test
    void pauseHaeltTransitionAn() {
        paused[0] = true;
        controller.startSceneTransition(SceneType.RED_SUN);

        controller.update(1.0);

        assertTrue(controller.active());
        assertTrue(switches.isEmpty());
        assertEquals(WarpTransitionController.DURATION_SECONDS, state.remainingSeconds(), 1e-9);
    }

    @Test
    void nachPauseLaeuftTransitionWeiter() {
        paused[0] = true;
        controller.startSceneTransition(SceneType.RED_SUN);
        controller.update(1.0);

        paused[0] = false;
        controller.update(0.1);

        assertTrue(controller.active());
        assertEquals(WarpTransitionController.DURATION_SECONDS - 0.1, state.remainingSeconds(), 1e-9);
    }

    @Test
    void fortschrittSpiegeltElapsedZeit() {
        controller.startSceneTransition(SceneType.RED_SUN);

        controller.update(0.5);

        assertEquals(0.25, controller.progress(), 1e-9);
    }
}
