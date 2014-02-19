package rewards.internal.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;

import rewards.internal.exception.RewardDataAccessException;


@Aspect	
public class DBExceptionHandlingAspect {
	
	private Logger logger = Logger.getLogger(getClass());


// TODO-09 (Optional): Which type of advice seems to be the most appropriate here? (optional)
	//Define the advice and the Pointcut expression 
	public void implExceptionHandling(RewardDataAccessException e) { 
		logger.info(" Sending an email to Mister Smith : " + e + "\n");
	}
	
}
