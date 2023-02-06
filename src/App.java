import ClientSide.ClientProgram;
import ServerSide.ServerProgram;

public class App {
    public static void main(String[] args) throws Exception {

        ServerProgram server = new ServerProgram(20200);
        ClientProgram client = new ClientProgram(20200, "127.0.0.1");
        server.Start();
        Thread.sleep(1000);
        client.Start();



    }
}
