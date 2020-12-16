import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/app")
public class ChatServerEndpoint {
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println ("Connected, sessionID = " + session.getId());
	}
	
	@OnMessage
	public String onMessage(String message, Session session) {
		if (message.equals("quit")) {
				try {
					session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Bye!"));
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return message;
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("Session " + session.getId() +
			" closed because " + closeReason);
	}
}