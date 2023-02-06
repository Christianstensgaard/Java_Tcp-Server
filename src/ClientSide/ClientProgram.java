package ClientSide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import Shared.Print;
import Shared.Common.ACommon;
import Shared.Communication.DataPackage;
import Shared.Communication.CommunicationTable.ActionType;
import Shared.Communication.CommunicationTable.TaskType;

public class ClientProgram extends ACommon {
    
    private Print s;
    private Connection connection;
    
    public ClientProgram(int port, String address) {
        connection = new Connection(port, address);
        s = new Print();
    }

    @Override
    protected void OnStart() throws IOException, InterruptedException {
        s.print("Connection to server");
        for(int i = 0; i < 4; i++){
            if(connection.openConnection()){
                onConnected();
            }
            else Thread.sleep(200);
        }
    }

    private void onConnected() throws IOException{
        
        s.print("Connected to server");
        InputStream reader = connection.socket.getInputStream();
        OutputStream writer = connection.socket.getOutputStream();
        byte[] buffer = new byte[1024];


        //- Doing the Task on the client side

        int[] values = {1,2,3,4,5,6,7,8,9,10};

        for (int i = 0; i < values.length; i++) {
            //Sending data to the Server. 
            writer.write(new DataPackage(ActionType.Get, TaskType.OddEven, ByteBuffer.allocate(4).putInt(values[i]).array()).pack());

            int byteLength = reader.read(buffer);
            //Printing response to the Terminal. 
            DataPackage data = new DataPackage().unPack(buffer, byteLength);

            //- Converting Byte[] to string
            String output = new String(data.getData());
            System.out.println(output);
        }


        //- Doing the Shutdown. 
        writer.write(new DataPackage(ActionType.Exit, TaskType.None, new byte[2]).pack());
        writer.flush();

        //- Closing. 
        reader.close();
        writer.close();
        connection.socket.close();
    }

    private class Connection{
        private int port;
        private String address;
        private Socket socket;
            
        public Connection(int port, String address) {
            this.port = port;
            this.address = address;
            socket = null;
        }

        public boolean isConnected() {
            return socket.isConnected();
        }

        public boolean openConnection(){
            try{
                socket = new Socket(address, port);
                return true;
            } catch(Exception e) {return false;}
        }
    }
}
