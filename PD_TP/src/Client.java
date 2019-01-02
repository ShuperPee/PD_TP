
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Observable {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket server;
    private InetAddress Addr;
    private int Port;
    private File localDirectory;
    private String username;
    private String password;
    public static final String DATA = "ack";

    public Client(InetAddress Addr, int Port, File localDirectory) throws IOException {
        this.Addr = Addr;
        this.Port = Port;
        this.localDirectory = localDirectory;
        this.server = new Socket(Addr, Port);
        in = new ObjectInputStream(server.getInputStream());
        out = new ObjectOutputStream(server.getOutputStream());
    }

    public void InputServer() {
        //Temporário só para esquematizar
        Object oData;
        try {
            oData = in.readObject();
            if (oData instanceof Chat) {
                //Display  Chat na interface
                setChanged();
                notifyObservers(oData);
            }
            if (oData instanceof List) {
                if (((List) oData).get(0) instanceof Download) {
                    //Display Downloads na interface
                }
                if (((List) oData).get(0) instanceof String[]) {
                    //Display Files na interface
                    //[0] = name
                    //[1] = size
                }
            }
            if (oData instanceof String[]) {
                //Connection Peer to Peer
                //[0]ClientAddr String
                //[1]Port String
                //[2]File name
                String[] P2P = (String[]) oData;
                //Fazer isto numa thread!
                requestFile(P2P[2], localDirectory, Integer.parseInt(P2P[1]), P2P[0]);
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

    public void OutputServer() {
        String FileName = "";
        ChatMsg Msg = new ChatMsg("Username", "Olá");
        Download download = new Download("FileName", "Ip do que fez upload", Addr.toString(), Calendar.getInstance());
        try {
            //Pedir um ficheiro
            out.writeObject(FileName);
            //Recebe um String[] no input

            //Enviar Mensagem ao servidor
            out.writeObject(Msg);

            //Acabei de fazer um download
            out.writeObject(download);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }
    }

    public List<String> getFiles() {
        // from update?
        return null;
    }

    public void requestFile(String fileName, File localDirectory, int ClientPort, String addr) {

        String localFilePath = null;
        FileOutputStream localFileOutputStream = null;
        PrintWriter pout;
        InputStream in;
        Socket socketToClient = null;
        byte[] fileChunk = new byte[5000]; // CRIAR VARIAVEL ESTATICA
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

                socketToClient.setSoTimeout(5 * 1000); // CRIAR VARIAVEL ESTATICA TIMEOUT

                in = socketToClient.getInputStream();
                pout = new PrintWriter(socketToClient.getOutputStream(), true);

                pout.println(fileName);
                pout.flush();

                while ((nbytes = in.read(fileChunk)) > 0) {
                    //System.out.println("Recebido o bloco n. " + ++contador + " com " + nbytes + " bytes.");
                    localFileOutputStream.write(fileChunk, 0, nbytes);
                    //System.out.println("Acrescentados " + nbytes + " bytes ao ficheiro " + localFilePath+ ".");
                }

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

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Sintaxe: java Client Server_addr Server_port Dir");
            return;
        }
        try {
            Client client = new Client(InetAddress.getByName(args[0]), Integer.parseInt(args[1]), new File(args[2]));
        } catch (UnknownHostException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
