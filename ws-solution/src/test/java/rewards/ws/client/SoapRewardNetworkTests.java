package rewards.ws.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import rewards.ws.types.RewardAccountForDiningRequest;
import rewards.ws.types.RewardAccountForDiningResponse;

@ContextConfiguration(locations = {"classpath:rewards/ws/client/client-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SoapRewardNetworkTests {
	
	private static final String NAMESPACE_URI = "http://www.springsource.com/reward-network";

	@Autowired
	private WebServiceTemplate webServiceTemplate;
	
	@Test
	public void testWebServiceWithJAXB() throws Exception {
		RewardAccountForDiningRequest request = new RewardAccountForDiningRequest();
		request.setAmount(new BigDecimal("100.00"));
		request.setCreditCardNumber("1234123412341234");
		request.setMerchantNumber("1234567890");
		RewardAccountForDiningResponse confirmation = (RewardAccountForDiningResponse) webServiceTemplate.marshalSendAndReceive(request);
		
		// assert the expected reward confirmation results
		assertNotNull(confirmation);
		
		// the account number should be '123456789'
		assertEquals("123456789", confirmation.getAccountNumber());
		
		// the total contribution amount should be 8.00 (8% of 100.00)
		assertEquals(new BigDecimal("8.00"), confirmation.getAmount());
	}
	
	@Test
	public void testWebServiceWithXml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element requestElement = document.createElementNS(NAMESPACE_URI, "rewardAccountForDiningRequest");
		requestElement.setAttribute("amount", "100.00");
		requestElement.setAttribute("creditCardNumber", "1234123412341234");
		requestElement.setAttribute("merchantNumber", "1234567890");
		DOMSource source = new DOMSource(requestElement);
		DOMResult result = new DOMResult();
		webServiceTemplate.sendSourceAndReceiveToResult(source, result);
		Element responseElement = (Element) result.getNode().getFirstChild();
		
		// assert the expected reward confirmation results
		assertNotNull(responseElement);
		
		// the account number should be '123456789'
		assertEquals("123456789", responseElement.getAttribute("accountNumber"));
		
		// the total contribution amount should be 8.00 (8% of 100.00)
		assertEquals("8.00", responseElement.getAttribute("amount"));
	}

}
