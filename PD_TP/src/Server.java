
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class Server {

    public static final int TIMEOUT = 5; //segundos

    public static void main(String[] args) {
        InetAddress Addr;
        int Port;
        ServerSocket serverSocket = null;
        boolean toQuit = false;
        ObjectInputStream in;
        Socket socketToClient;
        InitClient initData = null;
        if (args.length != 2) {
            System.out.println("Sintaxe: java Server Server_addr Server_port");
            return;
        }
        try {
            Addr = InetAddress.getByName(args[0]);
            Port = Integer.parseInt(args[1]);
            serverSocket = new ServerSocket(Port);
            serverSocket.setSoTimeout(TIMEOUT);

            new DataBaseConnect();
        } catch (UnknownHostException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }

        /*Recebe Clientes
        *Espera receber os dados do cliente
        *Guarda a informação do cliente + os ficheiros dele na base de dados
         */
        try {

            while (!toQuit) {

                try {

                    socketToClient = serverSocket.accept();
                    //Recebe um Objecto da Class InitCliente e adiciona o Cliente a Base de dados
                    in = new ObjectInputStream(socketToClient.getInputStream());
                    Object oData = in.readObject();

                    if (oData instanceof InitClient) {
                        initData = (InitClient) oData;
                    }
                    if (initData != null) {
                        DataBaseConnect.addClient(initData);
                        new ProcessClient(socketToClient).start();
                    }

                } catch (IOException e) {
                    if (toQuit) {
                        return;
                    }
                    System.out.println("Ocorreu uma excepcao no socket enquanto aguardava por um pedido de ligação: \n" + e);
                    System.out.println("O servidor vai terminar...");
                    return;
                } catch (ClassNotFoundException ex) {
                    System.out.println("Erro - " + ex);
                    System.exit(1);
                } catch (SQLException ex) {
                    System.out.println("Erro - " + ex);
                    System.exit(1);
                } catch (Exception ex) {
                    System.out.println("Erro - " + ex);
                    System.exit(1);
                }
            } //while(!toQuit)

        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }
}

class ProcessClient extends Thread {

    public Socket socketToClient;
    public static final int TIMEOUT = 5; //segundos

    public ProcessClient(Socket socketToClient) {
        this.socketToClient = socketToClient;
    }

    @Override
    public void run() {

        try {
            socketToClient.close();
        } catch (IOException e) {
        }

    }
}
