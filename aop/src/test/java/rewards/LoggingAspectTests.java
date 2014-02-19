package rewards;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rewards.internal.account.AccountRepository;

@ContextConfiguration(locations = {"classpath:/rewards/system-test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class LoggingAspectTests {

	@Autowired
	AccountRepository repository;
	
	@Test
	public void testLogger(){
		repository.findByCreditCard("1234123412341234");
	}
}
