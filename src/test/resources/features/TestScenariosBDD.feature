Feature: Timer and Stopwatch full scenario

  Scenario: Complete timer and stopwatch transitions
    Given the timer is idle with empty memory
    When the user sets the timer memory to two seconds
    And the user starts the timer
    Then the timer reaches the ringing state after two ticks
    When the user pauses the timer
    And the user switches to stopwatch mode
    And the user starts the stopwatch
    And the user records a laptime
    Then the stopwatch returns automatically to running mode after timeout
    Then switching back restores the paused timer history state
    Then the timer can continue and eventually return to idle