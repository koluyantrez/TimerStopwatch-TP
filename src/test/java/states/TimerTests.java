package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.timer.*;
import states.stopwatch.AbstractStopwatch;

@DisplayName("Timer State Machine Unit Tests")
class TimerTests {

	private static Context context;
	private ClockState current, newState;

	@BeforeEach
	void setup() {
		context = new Context(); // create the state machine context
		AbstractTimer.resetInitialValues();
	}

	@Test
	@DisplayName("Initial state: Context should start in IdleTimer with zero values")
	void testInitialState() {
		current = context.currentState;

		assertEquals(Mode.timer, current.getMode(), "Mode should be timer");
		assertSame(IdleTimer.Instance(), current, "Initial state should be IdleTimer");
		assertEquals(0, AbstractTimer.getTimer(), "Timer should start at 0");
		assertEquals(0, AbstractTimer.getMemTimer(), "MemTimer should start at 0");
	}

	@Test
	@DisplayName("AbstractTimer instance should match IdleTimer singleton")
	void testInitialAbstractTimer() {
		assertSame(AbstractTimer.Instance(), IdleTimer.Instance(),
				"AbstractTimer instance should match IdleTimer singleton");
	}

	@Test
	@DisplayName("ActiveTimer instance should start in RunningTimer")
	void testInitialActiveTimer() {
		assertSame(ActiveTimer.Instance(), RunningTimer.Instance(),
				"ActiveTimer initial state should be RunningTimer");
	}

	@Test
	@DisplayName("History state: left events should navigate correctly between Timer and Stopwatch")
	void testHistoryState() {
		current = AbstractTimer.Instance();
		// after processing the left() event, we should arrive in the initial state of AbstractStopwatch
		newState = current.left();
		assertEquals(AbstractStopwatch.Instance(), newState, "Left event should switch to AbstractStopwatch");
		// after another left() event, we should return to the original state due to history
		assertEquals(current, newState.left(), "Second left() should return to previous state using history");
	}

}