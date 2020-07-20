package de.renz.rxwebsocket;

import javax.websocket.Session;
import java.io.IOException;

public class RxMessage {
	private final String message;
	private final Session session;

	RxMessage(final Session session, final String message) {
		this.session = session;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Session getSession() {
		return session;
	}

	public void send(final String s) {
		try {
			session.getBasicRemote().sendText(s);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
