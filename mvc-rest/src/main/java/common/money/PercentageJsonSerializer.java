package common.money;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BasicSerializerFactory.NumberSerializer;

public class PercentageJsonSerializer extends JsonSerializer<Percentage> {

	private NumberSerializer numberSerializer = new NumberSerializer();
	
	@Override
	public void serialize(Percentage value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		numberSerializer.serialize(value.asBigDecimal(), jgen, provider);
	}

}
