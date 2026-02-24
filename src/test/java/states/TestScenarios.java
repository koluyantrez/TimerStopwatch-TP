package states;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import states.stopwatch.*;
import states.timer.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestScenarios {

    private Context c;

    @Before
    public void setup() {
        c = new Context();
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
    }

    //======================== Timer initial ========================
    @Given("the timer is idle with empty memory")
    public void timerIsIdleWithEmptyMemory() {
        assertSame(IdleTimer.Instance(), c.currentState, "Initial state should be IdleTimer");
        assertEquals(0, AbstractTimer.getTimer(), "Timer should start at 0");
        assertEquals(0, AbstractTimer.getMemTimer(), "memTimer should start at 0");
    }

    @When("the user sets the timer memory to two seconds")
    public void setTimerMemoryToTwo() {
        c.right(); // Idle -> SetTimer
        c.tick();  // memTimer = 1
        c.tick();  // memTimer = 2
        c.right(); // SetTimer -> Idle
    }

    @When("the user starts the timer")
    public void startTimer() {
        c.up(); // Idle -> RunningTimer
        assertSame(RunningTimer.Instance(), c.currentState, "Timer should be running");
    }

    @Then("the timer reaches the ringing state after two ticks")
    public void timerRingsAfterTwoTicks() {
        c.tick();
        assertEquals(1, AbstractTimer.getTimer(), "Timer should decrement to 1");
        c.tick();
        assertSame(RingingTimer.Instance(), c.currentState, "Timer should switch to RingingTimer");
        assertEquals(0, AbstractTimer.getTimer(), "Timer should reach 0");
    }

    //======================== Timer pause and history ========================
    @When("the user pauses the timer")
    public void pauseTimer() {
        c.up(); // Running -> Paused
        assertSame(PausedTimer.Instance(), c.currentState, "Timer should be paused");
    }

    @When("the user switches to stopwatch mode")
    public void switchToStopwatch() {
        c.left();
        assertSame(ResetStopwatch.Instance(), c.currentState, "Stopwatch should reset");
    }

    //======================== Stopwatch ========================
    @When("the user starts the stopwatch")
    public void startStopwatch() {
        c.up(); // Reset -> Running
        assertSame(RunningStopwatch.Instance(), c.currentState, "Stopwatch should be running");
        c.tick();
    }

    @When("the user records a laptime")
    public void recordLapTime() {
        c.up(); // Running -> LaptimeStopwatch
        assertSame(LaptimeStopwatch.Instance(), c.currentState, "Stopwatch should record laptime");
        assertEquals(1, AbstractStopwatch.getLapTime(), "LapTime should increment");
    }

    @Then("the stopwatch returns automatically to running mode after timeout")
    public void stopwatchReturnsToRunning() {
        // LaptimeStopwatch timeout = 5 ticks
        for(int i = 0; i < 5; i++) c.tick();
        assertSame(RunningStopwatch.Instance(), c.currentState, "Stopwatch should return to running");
    }

    @Then("switching back restores the paused timer history state")
    public void switchBackRestoresTimerHistory() {
        c.left(); // Stopwatch -> Timer history
        assertSame(PausedTimer.Instance(), c.currentState, "Timer should restore paused state");
    }

    @Then("the timer can continue and eventually return to idle")
    public void timerContinuesAndReturnsToIdle() {
        c.up(); // Paused -> Running
        c.tick(); // decrement timer
        assertSame(RingingTimer.Instance(), c.currentState, "Timer should ring when timer reaches 0");
        c.right(); // Ringing -> Idle
        c.tick();
        assertSame(IdleTimer.Instance(), c.currentState, "Timer should return to IdleTimer");
    }
}