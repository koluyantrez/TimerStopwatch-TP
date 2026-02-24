package states;

import gui.SwingGUI;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;
import states.timer.*;

@DisplayName("Some test following jacoco.")
public class TestFollowingJacoco {
    Context c;

    @BeforeEach
    void setup() {
        c = new Context();
        // before each test, reset timer and stopwatch values to avoid interference between tests
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
        c.currentState = AbstractStopwatch.Instance();
    }


    @Test
    @DisplayName("Call all transitions to increase coverage")
    void coverAllTransitions() {
        ClockState[] states = {
                IdleTimer.Instance(),
                SetTimer.Instance(),
                RunningTimer.Instance(),
                PausedTimer.Instance(),
                RingingTimer.Instance()
        };

        for (ClockState s : states) {
            s.up();
            s.right();
            s.left();
        }
    }

    @Test
    @DisplayName("Cover right() for all timer states")
    void coverRightTimerStates() {
        ClockState[] states = {
                IdleTimer.Instance(),
                SetTimer.Instance(),
                RunningTimer.Instance(),
                PausedTimer.Instance(),
                RingingTimer.Instance()
        };

        for (ClockState s : states) {
            s.right();
        }
    }

    @Test
    @DisplayName("Cover right() for stopwatch states")
    void coverRightStopwatchStates() {
        ClockState[] states = {
                ResetStopwatch.Instance(),
                RunningStopwatch.Instance(),
                LaptimeStopwatch.Instance()
        };

        for (ClockState s : states) {
            s.right();
        }
    }

    @Test
    @DisplayName("Cover doIt for timer states")
    void coverTimerDoIt() {
        c.currentState = SetTimer.Instance();
        c.tick();
        c.currentState = RunningTimer.Instance();
        c.tick();
        c.currentState = RingingTimer.Instance();
        c.tick();
    }

    @Test
    @DisplayName("GUI init coverage")
    void coverGUI() {
        Context context = new Context();
        new SwingGUI(context);
    }

    @Test
    @DisplayName("LaptimeStopwatch specific methods coverage")
    void coverLaptimeStopwatchMethods() {
        Context c = new Context();
        AbstractStopwatch.resetInitialValues();

        // Timer → Stopwatch
        c.left();
        c.tick();
        // Reset → Running
        c.up();
        c.tick();
        // Running → Laptime
        c.up();
        c.tick();

        assertSame(LaptimeStopwatch.Instance(), c.currentState);

        LaptimeStopwatch state = (LaptimeStopwatch) c.currentState;

        assertNotNull(state.getUpText());
        assertNotNull(state.getDisplayString());

        ClockState next = state.up();
        assertSame(RunningStopwatch.Instance(), next);
    }

    @Test
    @DisplayName("LaptimeStopwatch doIt full coverage")
    void coverLaptimeDoItFull() {
        Context c = new Context();
        AbstractStopwatch.resetInitialValues();

        // Timer -> Stopwatch
        c.left();
        c.tick();

        // Reset -> Running
        c.up();
        c.tick();

        // Running -> Laptime
        c.up();
        c.tick();

        assertSame(LaptimeStopwatch.Instance(), c.currentState);

        int totalBefore = AbstractStopwatch.getTotalTime();
        int lapBefore = AbstractStopwatch.getLapTime();

        // doIt() ticks
        for (int i = 0; i < 5; i++) { // 5 ticks pour couvrir le timeout
            c.tick();
        }

        // totalTime a forcément augmenté
        assertTrue(AbstractStopwatch.getTotalTime() > totalBefore);

        // lapTime reste correct
        assertTrue(AbstractStopwatch.getLapTime() >= lapBefore);

        // après timeout, on doit être revenu à RunningStopwatch
        assertSame(RunningStopwatch.Instance(), c.currentState);

        // cover getDisplayString() et getUpText()
        LaptimeStopwatch state = LaptimeStopwatch.Instance();
        assertNotNull(state.getDisplayString());
        assertNotNull(state.getUpText());

        // cover up() transition
        ClockState next = state.up();
        assertSame(RunningStopwatch.Instance(), next);
    }

}
