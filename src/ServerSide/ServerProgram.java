package ServerSide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import Shared.Common.ACommon;
import Shared.Communication.DataPackage;
import Shared.Communication.CommunicationTable.ActionType;
import Shared.Communication.CommunicationTable.TaskType;

public class ServerProgram extends ACommon {

    public ServerProgram(int port){
        connection = new Connection(port);
        isRunning = false;
    }

    @Override
    protected void OnStart() throws SocketException {
        isRunning = true;
        if(connection.openConnection()){
            connection.getSocket().setSoTimeout(2500);
            while(isRunning)
                try{new ClientConnection(connection.getSocket().accept()).Start();}
                catch(Exception e){}
        }
    }

    private boolean isRunning;
    private Connection connection;

    private class Connection{
        public Connection(int port){
            this.port = port;
        }
        
        public boolean openConnection(){
            try{
                socket = new ServerSocket(port);
                return true;
            } catch(Exception e){return false;}
        }
        
        public ServerSocket getSocket(){
            return socket;
        }
        
        private int port;
        private ServerSocket socket;
    }

    private class ClientConnection extends ACommon{

        public ClientConnection(Socket socket){
            this.socket = socket;
        }

        @Override
        protected void OnStart() throws IOException {

            //Stream 
            InputStream reader = socket.getInputStream();
            OutputStream writer = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            DataPackage datapackage;

            //Command Send to Server.
            while(true){
                int readCount = reader.read(buffer);
                datapackage = new DataPackage().unPack(buffer, readCount);
                if(ActionType.Exit == datapackage.getAction()){
                    connection.socket.close();
                    System.out.println("|Server|-Closing Connected after request");
                    break;
                }

                if(datapackage.getTask() == TaskType.OddEven){
                    writer.write(new DataPackage(ActionType.Return, TaskType.None, new String("Hej fra Server").getBytes()).pack());
                    writer.flush();
                }
            }
        }
        private Socket socket;
    }
}