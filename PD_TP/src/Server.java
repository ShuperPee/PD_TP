
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static final int TIMEOUT = 5; //segundos

    public static void main(String[] args) {
        InetAddress Addr;
        int Port;
        ServerSocket serverSocket = null;
        boolean toQuit = false;
        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socketToClient;
        InitClient initData = null;
        DataBaseConnect DataBase = null;
        Chat chat = new Chat();
        if (args.length != 2) {
            System.out.println("Sintaxe: java Server DataBase_addr:port Server_port");
            return;
        }
        try {
            Addr = InetAddress.getByName(args[0]);
            Port = Integer.parseInt(args[1]);
            serverSocket = new ServerSocket(Port);
            serverSocket.setSoTimeout(TIMEOUT);
            DataBase = new DataBaseConnect(args[0]);
            new ProcessUDPClients(DataBase).start();
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
                    InitClient:
                    if (oData instanceof InitClient) {
                        initData = (InitClient) oData;
                        //Verificar LogIn
                        List<String>[] details = DataBase.getClientsDetails();
                        int i = 0;
                        for (String username : details[0]) {
                            if (username.compareTo(initData.getUsername()) == 0) {
                                if (details[1].get(i).compareTo(initData.getPassword()) == 0) {
                                    if (DataBase.addClient(initData)) {
                                        out = new ObjectOutputStream(socketToClient.getOutputStream());
                                        out.writeObject("SuccessLogin");
                                        new ProcessTCPClient(socketToClient, DataBase, chat).start();
                                        break InitClient;
                                    }
                                }
                            }
                            i++;
                        }
                        out = new ObjectOutputStream(socketToClient.getOutputStream());
                        out.writeObject("RejectedLogin");
                    } else if (oData instanceof RegisterClient) {
                        InitClient client = new InitClient((RegisterClient) oData);
                        if (DataBase.addClient(client)) {
                            out = new ObjectOutputStream(socketToClient.getOutputStream());
                            out.writeObject("SuccessRegister");
                            new ProcessTCPClient(socketToClient, DataBase, chat).start();
                        }
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

class ProcessUDPClients extends Thread {

    public DatagramSocket socket;
    public List<DatagramPacket> packets;
    public List<InetAddress> ClientsAddr;
    public static final int TIMEOUT = 5; //segundos
    public static final int PORT = 5000;
    public static final String DATA = "keepalive";
    public String msg;
    private DataBaseConnect DataBase;

    public ProcessUDPClients(DataBaseConnect DataBase) throws SocketException {
        this.packets = new ArrayList<>();
        this.ClientsAddr = new ArrayList<>();
        this.DataBase = DataBase;
        this.socket = new DatagramSocket(PORT);
        socket.setSoTimeout(TIMEOUT * 1000);
    }

    @Override
    public void run() {

        while (true) {
            //inicia todos os cliente em base de dados
            try {
                ClientsAddr.clear();
                packets.clear();
                for (String str : DataBase.getAllClientsAddr()) {
                    ClientsAddr.add(InetAddress.getByName(str));
                }
                for (InetAddress addr : ClientsAddr) {
                    packets.add(new DatagramPacket(DATA.getBytes(), DATA.length(), addr, PORT));
                }
            } catch (Exception ex) {
                System.out.println("Erro - " + ex);
                System.exit(1);
            }

            for (DatagramPacket packet : packets) {
                try {
                    socket.send(packet);
                    socket.receive(packet);
                    msg = new String(packet.getData(), 0, packet.getLength());
                    if (msg.equals("ack")) {
                        DataBase.resetClientUDP(packet.getAddress().toString());
                    } else {
                        if (DataBase.badClientUDP(packet.getAddress().toString()) <= 0) {
                            DataBase.removeClient(packet.getAddress().toString());
                            //Desligar a ligação TCP
                        }
                    }
                    //Conseguiu, dar reset, tentativas = 5
                } catch (IOException ex) {
                    try {
                        if (DataBase.badClientUDP(packet.getAddress().toString()) <= 0) {
                            DataBase.removeClient(packet.getAddress().toString());
                            //Desligar a ligação TCP
                        }
                    } catch (Exception ex1) {
                        System.out.println("Erro - " + ex1);
                    }
                } catch (Exception ex) {
                    System.out.println("Erro - " + ex);
                }
            }
        }

    }
}

class ProcessTCPClient extends Thread {

    private Socket socketToClient;
    private DataBaseConnect DataBase;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public Chat chat;
    public static final int TIMEOUT = 5; //segundos

    public ProcessTCPClient(Socket socketToClient, DataBaseConnect DataBase, Chat chat) {
        this.socketToClient = socketToClient;
        this.DataBase = DataBase;
        this.chat = chat;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socketToClient.getInputStream());
            out = new ObjectOutputStream(socketToClient.getOutputStream());
        } catch (Exception ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }

        //Update?
        try {
            out.writeObject(DataBase.getFiles());
            out.writeObject(chat);
            out.writeObject(DataBase.getDownloads(socketToClient.getInetAddress().toString()));
        } catch (Exception ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
        //Recebe Informação do cliente
        try {
            Object oData;
            oData = in.readObject();
            //Cliente Quer um ficheiro
            if (oData instanceof String) {
                String[] ClientAddr = DataBase.getClientAddr((String) oData);
                //Enviar Ip do Cliente/Porto do cliente que contem o ficheiro
                out.writeObject(ClientAddr);
            }
            //Cliente quer enviar uma mensagem
            if (oData instanceof ChatMsg) {
                chat.addMsg((ChatMsg) oData);
                out.writeObject(chat);
            }
            //Cliente fez um download
            if (oData instanceof Download) {

            }

        } catch (Exception ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
        try {
            socketToClient.close();
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
    }
}
