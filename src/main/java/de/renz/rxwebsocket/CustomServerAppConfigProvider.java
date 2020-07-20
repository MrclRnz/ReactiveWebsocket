package de.renz.rxwebsocket;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CustomServerAppConfigProvider implements ServerApplicationConfig {

	@Override

	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		Set<ServerEndpointConfig> result = new HashSet<>();
		for (Class epClass : endpointClasses) {
			//need to ignore Client endpoint class
			if (epClass.equals(RxWebSocketEndpoint.class)) {
				ServerEndpointConfig sec = ServerEndpointConfig.Builder.create(epClass, "/ws").build();
				result.add(sec);
			}
		}
		return result;
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		return Collections.emptySet();
	}
}