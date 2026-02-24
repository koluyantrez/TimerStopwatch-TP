package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;
import states.timer.*;

@DisplayName("Full Timer and Stopwatch Scenario Tests")
class TestScenarios {

	Context c;

	@BeforeEach
	void setup() {
		c = new Context();
		// before each test, reset timer and stopwatch values to avoid interference between tests
		AbstractTimer.resetInitialValues();
		AbstractStopwatch.resetInitialValues();
	}

	@Test
	@DisplayName("Complete scenario: timer and stopwatch transitions with history and laptime")
	void completeScenario() {
		assertEquals(IdleTimer.Instance(), c.currentState, "Initial state should be IdleTimer");
		assertEquals(0, AbstractTimer.getMemTimer(), "Initial memTimer should be 0");

		c.right(); // start incrementing the memTimer variable
		c.tick();
		assertSame(SetTimer.Instance(), c.currentState, "Right event should go to SetTimer");
		assertEquals(1, AbstractTimer.getMemTimer(), "memTimer should be incremented to 1");
		assertEquals(0, AbstractTimer.getTimer(), "timer should remain 0");

		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer(), "memTimer should be incremented to 2");
		assertEquals(0, AbstractTimer.getTimer(), "timer should remain 0");

		c.right(); // stop incrementing memTimer
		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer(), "memTimer should remain 2");
		assertEquals(0, AbstractTimer.getTimer(), "timer should remain 0");

		c.up(); // start running the timer
		assertEquals(2, AbstractTimer.getTimer(), "timer should start at 2");
		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer(), "memTimer should remain 2");
		assertEquals(1, AbstractTimer.getTimer(), "timer should increment to 1");

		c.up(); // pause the timer
		c.tick();
		assertSame(PausedTimer.Instance(), c.currentState, "Timer should be paused");
		assertEquals(2, AbstractTimer.getMemTimer(), "memTimer remains 2");
		assertEquals(1, AbstractTimer.getTimer(), "timer remains 1");

		c.left(); // switch to stopwatch mode
		c.tick();
		assertSame(ResetStopwatch.Instance(), c.currentState, "Stopwatch should reset");
		assertEquals(0, AbstractStopwatch.getTotalTime(), "totalTime should be 0");
		assertEquals(0, AbstractStopwatch.getLapTime(), "lapTime should be 0");

		c.up(); // start running stopwatch
		c.tick();
		assertSame(RunningStopwatch.Instance(), c.currentState, "Stopwatch should be running");
		assertEquals(1, AbstractStopwatch.getTotalTime(), "totalTime should increment to 1");
		assertEquals(0, AbstractStopwatch.getLapTime(), "lapTime remains 0");

		c.up(); // record laptime
		c.tick();
		assertSame(LaptimeStopwatch.Instance(), c.currentState, "Stopwatch should record laptime");
		assertEquals(2, AbstractStopwatch.getTotalTime(), "totalTime should increment to 2");
		assertEquals(1, AbstractStopwatch.getLapTime(), "lapTime should increment to 1");

		c.left(); // return to timer mode (history state)
		c.tick();
		assertSame(PausedTimer.Instance(), c.currentState, "Timer should return to paused state from history");
		assertEquals(2, AbstractTimer.getMemTimer(), "memTimer should remain 2");
		assertEquals(1, AbstractTimer.getTimer(), "timer should remain 1");

		c.up(); // continue running timer
		assertSame(RunningTimer.Instance(), c.currentState, "Timer should continue running");
		c.tick();
		// automatic switch to ringing timer since timer has reached 0
		assertSame(RingingTimer.Instance(), c.currentState, "Timer should switch to RingingTimer");
		assertEquals(2, AbstractTimer.getMemTimer(), "memTimer should remain 2");
		assertEquals(0, AbstractTimer.getTimer(), "timer should be 0");

		c.right(); // return to idle timer state
		c.tick();
		assertSame(IdleTimer.Instance(), c.currentState, "Timer should return to IdleTimer");
		assertEquals(2, AbstractTimer.getMemTimer(), "memTimer should remain 2");
		assertEquals(0, AbstractTimer.getTimer(), "timer should be 0");
	}
}