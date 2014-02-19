package rewards.internal.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import rewards.internal.exception.RewardDataAccessException;


@Aspect	
public class DBExceptionHandlingAspect {
	
	private Logger logger = Logger.getLogger(getClass());

	@AfterThrowing(value="execution(public * rewards.internal.*.*Repository.*(..))", throwing="e")
	public void implExceptionHandling(RewardDataAccessException e) { 
		logger.info(" Sending an email to Mister Smith : " + e + "\n");
	}
	
}
