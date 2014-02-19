package rewards.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.RewardNetwork;
import rewards.ws.types.RewardAccountForDiningRequest;
import rewards.ws.types.RewardAccountForDiningResponse;

@Endpoint
public class RewardNetworkEndpoint {

	private static final String NAMESPACE_URI = "http://www.springsource.com/reward-network";

	private RewardNetwork rewardNetwork;

	@Autowired
	public RewardNetworkEndpoint(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="rewardAccountForDiningRequest")
	@ResponsePayload
	public RewardAccountForDiningResponse invoke(@RequestPayload RewardAccountForDiningRequest req) {
		Dining dining = Dining.createDining(req.getAmount().toString(), req.getCreditCardNumber(), req.getMerchantNumber());
		RewardConfirmation confirmation = rewardNetwork.rewardAccountFor(dining);
		RewardAccountForDiningResponse response = new RewardAccountForDiningResponse();
		response.setAccountNumber(confirmation.getAccountContribution().getAccountNumber());
		response.setAmount(confirmation.getAccountContribution().getAmount().asBigDecimal());
		response.setConfirmationNumber(confirmation.getConfirmationNumber());
		return response;
	}


}