import org.glassfish.tyrus.server.Server;

public class MyServer {
	
  public static void main (String[] args) {
    Server server;
    server = new Server ("localhost", 8025, "/project", MyServerEndpoint.class);
    try {
      server.start();
      System.out.println("--- server is running");
      while(true){}
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      server.stop();
    }
  }
	
}
