package net.dreamlu.example.ws;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

public class TimeTaskTest {

    private static Timer timer = new Timer(true);
    
    public static void start() {
        System.out.println("start");
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                CopyOnWriteArraySet<WebSocketTest> webSocketSet = WebSocketTest.webSocketSet;
                
                for(WebSocketTest item: webSocketSet){
                    try {
                        item.sendMessage("hello: " + System.currentTimeMillis());
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                
            }
        }, 1000, 5000);
    }
}
