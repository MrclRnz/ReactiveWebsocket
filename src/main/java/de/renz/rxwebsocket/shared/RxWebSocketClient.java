package de.renz.rxwebsocket.shared;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public abstract class RxWebSocketClient {
	public Session session;

	public abstract void defineFlow(String message);

	public RxWebSocketClient(final String uri) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI(uri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@OnOpen
	public void onOpen(Session userSession) {
		System.out.println("opening websocket");
		this.session = userSession;
		try{
			session.getBasicRemote().sendText(Integer.toString(WorkflowSteps.STEP_ESTABLISH_WEBSOCKET));
		} catch(IOException e){

		}
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket");
		this.session = null;
	}

	@OnMessage
	public void onMessage(String message) {
		defineFlow(message);
	}

	public Session getSession() {
		return session;
	}

}
