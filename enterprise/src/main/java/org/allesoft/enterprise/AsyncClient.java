package org.allesoft.enterprise;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CountDownLatch;

public class AsyncClient {
    public static void main(String[] args) throws Exception {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        CountDownLatch waitBarrier = new CountDownLatch(1);
        TextWebSocketHandler webSocketHandler = new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                super.handleTextMessage(session, message);
                System.out.println(message.getPayload());
                waitBarrier.countDown();
            }
        };
        ListenableFuture<WebSocketSession> webSocketSessionListenableFuture =
                webSocketClient.doHandshake(webSocketHandler, "ws://127.0.0.1:8081/hello");
        WebSocketSession webSocketSession = webSocketSessionListenableFuture.get();
        webSocketSession.sendMessage(new TextMessage("test"));

        waitBarrier.await();
    }
}
