package server.api.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public final class WebSocketController {

    public class ChangeEvent {
        private String message;
        private Object data;

        public String getMessage () {
            return message;
        }

        public void setMessage (String message) {
            this.message = message;
        }

        public Object getData () {
            return data;
        }

        public void setData (Object data) {
            this.data = data;
        }

        public ChangeEvent (String message, Object data) {
            this.message = message;
            this.data = data;

        }
    }

    @MessageMapping
    @SendTo("/topic/updates")
    public String handleUpdate(ChangeEvent event) {
        return "Update received: " + event.getMessage();
    }


}
