package rewards.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import rewards.RewardNetwork;

@Endpoint
public class RewardNetworkEndpoint {

	private static final String NAMESPACE_URI = "http://www.springsource.com/reward-network";

	private RewardNetwork rewardNetwork;

	@Autowired
	public RewardNetworkEndpoint(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}
	
	// TODO 3: Create a new method which accepts a RewardAccountForDiningRequest
	// as unmarshalled request payload, processes the request and returns
	// a RewardAccountForDiningResponse as the response payload to be marshalled
	// for the appropriate payload root element. 


}