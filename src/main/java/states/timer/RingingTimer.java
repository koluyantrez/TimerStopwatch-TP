package states.timer;

import states.ClockState;
import java.util.logging.Logger;

public class RingingTimer extends ActiveTimer {
 	
	// use Singleton design pattern
	private RingingTimer() {}; // make constructor invisible to clients
    private static RingingTimer instance = null;
    private static final Logger logger = Logger.getLogger(ClockState.class.getName());

    private static boolean isRinging = false;

    public static RingingTimer Instance() {
        if(instance == null) instance = new RingingTimer();        
        return instance;
    }
    
    @Override
    public ClockState doIt() {
    	java.awt.Toolkit.getDefaultToolkit().beep();
    	return this;
    }
    
    public String getDisplayString() {
    	// display decreasing values starting from memTimer counting down to 0
        return "Time's up !";
    }

    public static boolean isRinging() {
        // getter to know if it ringings
        return isRinging;
    }

    @Override
    protected void entry() {
        super.entry();
        isRinging=true;
        logger.info(this.getClass().getName()+" is ringing");
    }

    @Override
    protected void exit() {
        super.entry();
        isRinging=false;
        logger.info(this.getClass().getName()+" is not ringing anymore");
    }
    
}
