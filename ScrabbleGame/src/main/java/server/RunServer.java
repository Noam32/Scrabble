package server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;


public class RunServer {

    boolean stop;
    private static boolean isTcpPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            // setReuseAddress(false) is required only on macOS,
            // otherwise the code will not work correctly on that platform
            serverSocket.setReuseAddress(false);
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port), 1);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }



    public static void main(String[] args){
        //Random r=new Random();
        //int port=6000+r.nextInt(1000);
        int port = 8000;
        MyServer s=new MyServer(port, new BookScrabbleHandler(),1);
        System.out.println("Dictionary Server says: is port "+port+ " available = " +isTcpPortAvailable(port) );
        s.start();
        System.out.println("Dictionary server was started!\nlistening to port:"+port);
        while(!s.stop) {
            try {
                Thread.sleep(1000);//10 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            
        }
        System.out.println("ended the loop in main on RunServer - closing the Dictionary Server");
        s.close();
    }
}
