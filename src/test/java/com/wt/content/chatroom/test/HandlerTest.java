package com.wt.content.chatroom.test;

import com.wt.content.chatroom.ChatroomApplication;
import com.wt.content.chatroom.ws.handler.WebsocketMessageHandler;
import com.wt.content.chatroom.ws.handler.WebsocketUserHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 一贫
 * @date 2020/11/18
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = ChatroomApplication.class, properties = {"application.runner.enabled=false"})
public class HandlerTest {

    @Autowired
    private WebsocketUserHandler websocketUserHandler;

    @Autowired
    private WebsocketMessageHandler websocketMessageHandler;

    @Test
    public void testHandler() {
        EmbeddedChannel channel = new EmbeddedChannel(websocketUserHandler, websocketMessageHandler);
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame("{\"topic\":\"\",\"groupChat\":true,\"userId\":\"1\"}");
        channel.writeInbound(textWebSocketFrame);
    }
}
