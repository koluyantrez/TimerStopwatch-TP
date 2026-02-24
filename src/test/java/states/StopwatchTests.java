package states;

import states.stopwatch.AbstractStopwatch;
import states.stopwatch.ResetStopwatch;
import states.timer.AbstractTimer;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stopwatch State Machine Tests")
class StopwatchTests {

	private static Context context;
	private ClockState current, newState;

	@BeforeEach
	void setup() {
		context = new Context(); // create the state machine context
		AbstractStopwatch.resetInitialValues();
		context.currentState = AbstractStopwatch.Instance();
	}

	@Test
	@DisplayName("Initial state: AbstractStopwatch should start at ResetStopwatch with zero times")
	void testInitialState() {
		current = context.currentState;

		assertEquals(Mode.stopwatch, current.getMode(), "Mode should be stopwatch");
		assertSame(ResetStopwatch.Instance(), current, "Initial state should be ResetStopwatch");
		assertEquals(0, AbstractStopwatch.getTotalTime(), "TotalTime should start at 0");
		assertEquals(0, AbstractStopwatch.getLapTime(), "LapTime should start at 0");
	}

	@Test
	@DisplayName("AbstractStopwatch initial instance should match ResetStopwatch singleton")
	void testInitialAbstractStopwatch() {
		// The initial state of composite state AbstractStopwatch should be ResetStopwatch
		assertSame(AbstractStopwatch.Instance(), ResetStopwatch.Instance(),
				"AbstractStopwatch instance should match ResetStopwatch singleton");
	}

	@Test
	@DisplayName("History state: left events should navigate correctly using history")
	void testHistoryState() {
		current = AbstractStopwatch.Instance();
		// after processing the left() event, we should arrive in the initial state of AbstractStopwatch
		newState = current.left();
		assertEquals(AbstractTimer.Instance(), newState, "First left() should go to AbstractTimer");
		// after another occurrence of the left() event, we should return to the original state because of history states
		assertEquals(current, newState.left(), "Second left() should return to previous state due to history");
	}

}