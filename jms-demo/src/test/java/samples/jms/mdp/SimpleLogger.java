/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package samples.jms.mdp;

import java.util.Map;

/**
 * A simple logger to be used as a Message Driven POJO.
 */
public class SimpleLogger {

	public void log(String s) {
		System.out.println("Received a String: " + s);
	}
	
	public void log(byte[] bytes) {
		System.out.print("Received a byte array: [");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(bytes[i]);
			if (i != (bytes.length -1)) {
				System.out.print(',');
			}
		}
		System.out.println("]");
	}
	
	public void log(Map map) {
		System.out.println("Received a Map: " + map);
	}

	public void log(Money money) {
		System.out.println("Received Money: " + money);
	}
	
}
