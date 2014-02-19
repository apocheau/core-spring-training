package rewards.internal.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import rewards.internal.monitor.Monitor;
import rewards.internal.monitor.MonitorFactory;

	// TODO-01: Indicate this class is an aspect

public class LoggingAspect {

	private Logger logger = Logger.getLogger(getClass());
	private MonitorFactory monitorFactory;

	
	public LoggingAspect(MonitorFactory monitorFactory) {
		super();
		this.monitorFactory = monitorFactory;
	}

	
	// TODO-02: Which type of advice seems to be the most appropriate here?
	//Define the advice and the Pointcut expression 	
	public void implLogging(JoinPoint joinPoint) {
		logger.info("Logging: Class - "+ joinPoint.getTarget().getClass()+"; Executing before " + joinPoint.getSignature().getName() + "() method");
	}
	
	
	// TODO-06: Which type of advice seems to be the most appropriate here?
	//Define the advice and the Pointcut expression 
	public Object monitor(ProceedingJoinPoint repositoryMethod) throws Throwable {
		String name = createJoinPointTraceName(repositoryMethod);
		Monitor monitor = monitorFactory.start(name);
		try {

			// TODO-07: Add the logic to proceed with the target method invocation.
			
			return new String("Comment this line after completing TODO-07");
			
		} finally {
			monitor.stop();
			logger.info(monitor);
		}
	}

	private String createJoinPointTraceName(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		StringBuilder sb = new StringBuilder();
		sb.append(signature.getDeclaringType().getSimpleName());
		sb.append('.').append(signature.getName());
		return sb.toString();
	} 
}