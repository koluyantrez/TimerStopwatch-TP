package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;
import states.timer.*;

public class AddTest {
    Context c;

    @BeforeEach
    void setup() {
        c = new Context();
        // before each test, reset timer and stopwatch values to avoid interference between tests
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
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
}
