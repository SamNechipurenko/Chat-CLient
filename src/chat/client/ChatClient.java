package chat.client;

import java.io.IOException;
import java.util.Scanner;

public class ChatClient implements TCPConnectionListener{
    
    private final String ipAddres = "192.168.31.214";
    private final int port = 7707;
    private String log = "";
    private final String nickname = "Sam";
    private String message;
    private final TCPConnection connection;
    
    public static void main(String[] args) throws IOException {
        new ChatClient();
    }
    
    public ChatClient() throws IOException{
        connection = new TCPConnection(ChatClient.this, ipAddres, port);
        while (true) {
            String msg = (new Scanner(System.in, "Cp1251")).nextLine();
            if (msg.equalsIgnoreCase("exit")){
                onDisconnect(connection);
                connection.disconnect();
                break;
            }
            connection.sendString(nickname + ": " + msg);
        }
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnect, String value) {
        System.out.println(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnect) {
        System.out.println("connection closed.");
    }

    @Override
    public void onConnectionReady(TCPConnection aThis) {
        System.out.println("connection is ready...");
    }

    @Override
    public void onException(TCPConnection aThis, IOException ex) {
        System.out.println("Connection Exception: " + ex);
    }

    
    
    
}
