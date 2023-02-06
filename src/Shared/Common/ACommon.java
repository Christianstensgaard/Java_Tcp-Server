package Shared.Common;

import java.io.IOException;
import java.net.SocketException;

public abstract class ACommon {
    
    public Thread Start(){
        
        thread = new Thread(new Runnable(){

            @Override
            public void run() {
                Handle();
            }
        });
        thread.start();
        return thread;
    }
    public Thread getThread() {
        return thread;
    }

    protected abstract void OnStart() throws SocketException, IOException, InterruptedException;

    private Thread thread;
    private void Handle(){
        try{OnStart();}
        catch(Exception e){System.out.println(e);}
    }

}
