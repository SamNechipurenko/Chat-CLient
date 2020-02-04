package chat.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {
    private Socket socket;
    private Thread mThread;
    private BufferedReader in;
    private BufferedWriter out;
    private TCPConnectionListener eventListener;
    String nickname;
    
    public TCPConnection (TCPConnectionListener eventListener, String ipAddres, int port) throws IOException{
        this(eventListener, new Socket(ipAddres, port));
    }
    
    public TCPConnection (TCPConnectionListener eventListener, Socket socket) throws IOException{
        nickname = "Sam";
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        mThread = new Thread(() -> {
            try{
                eventListener.onConnectionReady(TCPConnection.this);
                while(!mThread.isInterrupted()){
                    eventListener.onReceiveString(TCPConnection.this ,in.readLine());
                }
            }catch(IOException ex){
                eventListener.onException(TCPConnection.this, ex);
            }finally{
                disconnect();
            }
        });
        mThread.start();
    }
    
    public synchronized void sendString(String value){
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
            disconnect();
        }
    }
    public synchronized void disconnect(){
        mThread.interrupt();
        try {
            socket.close();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
        }
    }
    
    public String toString(){
        return "TCPconnection: " + socket.getInetAddress()+ " port: " + socket.getPort();
    }
}
