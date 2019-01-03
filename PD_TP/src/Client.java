
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

public class Client extends Observable {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket server;
    private InetAddress Addr;
    private int Port;
    private File localDirectory;
    private String username;
    private String password;
    public ProcessServer processServer;
    private Object oData;
    private int port_udp = 3000;
    private int port_tcp = 7000;
    public static final String DATA = "ack";
    public static final int FILE_CHUNK = 5000;
    public static final int TIMEOUT = 50000;
    public boolean quit;

    public Client(InetAddress Addr, int Port, File localDirectory) throws IOException {
        this.Addr = Addr;
        this.Port = Port;
        this.localDirectory = localDirectory;
        System.out.println("E1");
        this.server = new Socket(Addr, Port);
        System.out.println("E2");
        out = new ObjectOutputStream(server.getOutputStream());
        System.out.println("E3");
        in = new ObjectInputStream(server.getInputStream());
        System.out.println("E4");
        quit = false;
    }

    public boolean doLogin(String username, String password) {
        List<String>[] ficheiros;
        ficheiros = getLocalFiles();

        try {
            InitClient client = new InitClient(username, password, Addr.toString(), ficheiros, localDirectory.getCanonicalPath(), port_udp, port_tcp);
            //synchronized (oData) {
            out.writeObject((Object) client);
            out.flush();
            oData = in.readObject();
            if (((String) oData).equals(("SuccessLogin"))) {
                return true;
            } else {
                return false;
            }
            //  }
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
        return false;
    }

    public boolean register(String username, String password) {
        List<String>[] ficheiros;
        ficheiros = getLocalFiles();

        try {
            RegisterClient client = new RegisterClient(username, password, Addr.toString(), ficheiros, localDirectory.getCanonicalPath(), port_udp, port_tcp);
            //synchronized (oData) {
            out.writeObject((Object) client);
            out.flush();
            System.out.println("Mandou para o Servidor");
            oData = in.readObject();
            System.out.println("Recebeu do Servidor");
            if (((String) oData).equals(("SuccessLogin"))) {
                return true;
            } else {
                return false;
            }
            //}
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
        return false;
    }

    public void sendMsg(String msg) {
        ChatMsg Msg = new ChatMsg(username, msg);
        try {
            out.writeObject(Msg);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
    }

    public void requestFileServer(String FileName) {
        try {
            out.writeObject(FileName);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
    }

    public List<String>[] getLocalFiles() {
        ArrayList<String>[] ficheiros = (ArrayList<String>[]) new ArrayList[2];
        ficheiros[0] = new ArrayList<String>();
        ficheiros[1] = new ArrayList<String>();
        File ficheirosFile[] = localDirectory.listFiles();
        for (int i = 0; i < ficheirosFile.length; i++) {
            ficheiros[0].add(ficheirosFile[i].getName());
            ficheiros[1].add("" + ficheirosFile[i].length() / 1024);
            System.out.println(ficheiros[0].get(i) + " + " + ficheiros[1].get(i));
        }

        return ficheiros;
    }

    private void finishDownload(String Filename, String client_up) {
        Download download = new Download(Filename, client_up, Addr.toString(), Calendar.getInstance());
        try {
            out.writeObject(download);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
    }

    public void requestFile(String fileName, File localDirectory, int ClientPort, String addr) {

        String localFilePath = null;
        FileOutputStream localFileOutputStream = null;
        PrintWriter pout;
        InputStream in;
        Socket socketToClient = null;
        byte[] fileChunk = new byte[FILE_CHUNK]; // CRIAR VARIAVEL ESTATICA
        int nbytes;

        if (!localDirectory.exists()) {
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return;
        }

        if (!localDirectory.isDirectory()) {
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return;
        }

        if (!localDirectory.canWrite()) {
            System.out.println("Sem permissoes de escrita na directoria " + localDirectory);
            return;
        }

        try {

            try {

                localFilePath = localDirectory.getCanonicalPath() + File.separator + fileName;
                localFileOutputStream = new FileOutputStream(localFilePath);
                System.out.println("Ficheiro " + localFilePath + " criado.");

            } catch (IOException e) {

                if (localFilePath == null) {
                    System.out.println("Ocorreu a excepcao {" + e + "} ao obter o caminho canonico para o ficheiro local!");
                } else {
                    System.out.println("Ocorreu a excepcao {" + e + "} ao tentar criar o ficheiro " + localFilePath + "!");
                }

                return;
            }

            try {

                socketToClient = new Socket(addr, ClientPort);

                socketToClient.setSoTimeout(TIMEOUT); // CRIAR VARIAVEL ESTATICA TIMEOUT

                in = socketToClient.getInputStream();
                pout = new PrintWriter(socketToClient.getOutputStream(), true);

                pout.println(fileName);
                pout.flush();

                while ((nbytes = in.read(fileChunk)) > 0) {
                    //System.out.println("Recebido o bloco n. " + ++contador + " com " + nbytes + " bytes.");
                    localFileOutputStream.write(fileChunk, 0, nbytes);
                    //System.out.println("Acrescentados " + nbytes + " bytes ao ficheiro " + localFilePath+ ".");
                }
                finishDownload(fileName, addr);
                System.out.println("Transferencia concluida.");

            } catch (UnknownHostException e) {
                System.out.println("Destino desconhecido:\n\t" + e);
            } catch (NumberFormatException e) {
                System.out.println("O porto do servidor deve ser um inteiro positivo:\n\t" + e);
            } catch (SocketTimeoutException e) {
                System.out.println("Não foi recebida qualquer bloco adicional, podendo a transferencia estar incompleta:\n\t" + e);
            } catch (SocketException e) {
                System.out.println("Ocorreu um erro ao nível do socket TCP:\n\t" + e);
            } catch (IOException e) {
                System.out.println("Ocorreu um erro no acesso ao socket ou ao ficheiro local " + localFilePath + ":\n\t" + e);
            }

        } finally {

            if (socketToClient != null) {
                try {
                    socketToClient.close();
                } catch (IOException ex) {
                }
            }

            if (localFileOutputStream != null) {
                try {
                    localFileOutputStream.close();
                } catch (IOException e) {
                }
            }

        }
    }

    public void startProcessServer() {
        //new ProcessServer().start();
        try {
            new ProcessUDP().start();
        } catch (SocketException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
    }

    public void quit() {
        quit = true;
    }

    public static void main(String[] args) {
//        if (args.length != 3) {
//            System.out.println("Sintaxe: java Client Server_addr Server_port Dir");
//            return;
//        }
//localhost 7000 c:\temp2
        args = new String[3];
        args[0] = "localhost";
        args[1] = "7000";
        args[2] = "c:\\temp2";
        try {

            Client client = new Client(InetAddress.getByName(args[0]), Integer.parseInt(args[1]), new File(args[2]));
            TextUI textUI = new TextUI(client);
            client.startProcessServer();
            textUI.ui();
        } catch (UnknownHostException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }

    }

    class ProcessServer extends Thread {

        public ProcessServer() {

        }

        @Override
        public void run() {

            //out.writeObject(getLocalFiles());
            //out.flush();
            while (!quit) {
                try {
                    oData = in.readObject();
                    if (oData instanceof Chat) {
                        //Display  Chat na interface
                        List<String>[] files = (ArrayList<String>[]) oData;
                        setChanged();
                        notifyObservers(((Chat) oData).getMsgs());
                    }
                    if (oData instanceof List) {
                        if (((List) oData).get(0) instanceof Download) {
                            List<String> downloads = new ArrayList<>();
                            for (Download i : (List<Download>) oData) {
                                downloads.add(i.toString());
                            }
                            setChanged();
                            notifyObservers(downloads);
                        }
                        if (((List) oData).get(0) instanceof String[]) {
                            //Display Files na interface
                            //[0] = name
                            //[1] = size
                            setChanged();
                            List<String>[] files = (ArrayList<String>[]) oData;
                            List<String> showStrings = new ArrayList<>();
                            int i = 0;
                            for (String j : files[0]) {
                                showStrings.add(j);
                                showStrings.add(files[1].get(i));
                                i++;
                            }
                            notifyObservers(showStrings);
                        }
                    }
                    if (oData instanceof String[]) {
                        //Connection Peer to Peer
                        String[] P2P = (String[]) oData;
                        new ProcessrequestFile(P2P[2], Integer.parseInt(P2P[1]), P2P[0]).start();

                    }
                } catch (IOException ex) {
                    System.out.println("Erro - " + ex);
                    System.exit(1);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Erro - " + ex);
                    System.exit(1);
                } catch (Exception ex) {
                    System.out.println("Erro - " + ex);
                    System.exit(1);
                }
            }
        }
    }

    class ProcessUDP extends Thread {

        public DatagramSocket socket;
        public DatagramPacket packet;

        public ProcessUDP() throws SocketException {
            socket = new DatagramSocket(Port);
            socket.setSoTimeout(TIMEOUT * 10);
        }

        @Override
        public void run() {
            while (!quit) {
                try {
                    while (!quit) {

                    }
                    socket.receive(packet);
                    packet = new DatagramPacket(DATA.getBytes(), DATA.length(), Addr, Port);
                    socket.send(packet);
                } catch (IOException ex) {
                    System.out.println("Erro - " + ex);
                }
            }

        }
    }

    class ProcessrequestFile extends Thread {

        private String Filename;
        private String clientAddr;
        private int PORT;

        public ProcessrequestFile(String Filename, int PORT, String clientAddr) {
            this.Filename = Filename;
            this.PORT = PORT;
        }

        @Override
        public void run() {
            requestFile(Filename, localDirectory, PORT, clientAddr);
        }
    }
}
