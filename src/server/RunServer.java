package server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Random;

public class RunServer {

    boolean stop;
    public static boolean isTcpPortAvailable(int port) {
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
        System.out.println("is port available = " +isTcpPortAvailable(port) );
        s.start();
        while(!s.stop) {
            try {
                Thread.sleep(10000);//10 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            s.close();
        }
    }
}
