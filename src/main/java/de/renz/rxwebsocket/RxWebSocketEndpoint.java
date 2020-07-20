package de.renz.rxwebsocket;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

import javax.websocket.*;

public class RxWebSocketEndpoint extends Endpoint implements ObservableOnSubscribe<RxMessage> {

	private ObservableEmitter<? super RxMessage> emitter;

	public void defineFlow(final Session session, final Observable<RxMessage> root) {
		root.doOnNext(rxMessage -> {rxMessage.getSession().getBasicRemote().sendText("angekommen!");})
				.subscribe();
	}

	@Override
	public void onOpen(final Session session, final EndpointConfig config) {
		System.out.println("OnOpen called!");
		defineFlow(session, Observable.create(this));
		session.addMessageHandler(new Handler(this, session));
	}

	@Override
	public void onClose(final Session session, final CloseReason closeReason) {
		emitter.onComplete();
	}

	@Override
	public void onError(final Session session, final Throwable throwable) {

		emitter.onError(throwable);
	}

	public void subscribe(@NonNull ObservableEmitter emitter) throws Throwable {
		this.emitter = emitter;
	}

	private static class Handler implements MessageHandler.Whole<String> {
		private final RxWebSocketEndpoint parent;
		private final Session session;

		private Handler(final RxWebSocketEndpoint subscriber, final Session session) {
			this.parent = subscriber;
			this.session = session;
		}

		@Override
		public void onMessage(final String message) {
			this.parent.emitter.onNext(new RxMessage(session, message));
		}
	}
}