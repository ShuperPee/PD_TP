
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        InetAddress Addr;
        int Port;
        Socket s;
        if (args.length != 3) {
            System.out.println("Sintaxe: java Client Server_addr Server_port Dir");
            return;
        }
        try {
            Addr = InetAddress.getByName(args[0]);
            Port = Integer.parseInt(args[1]);
            s = new Socket(Addr, Port);
        } catch (UnknownHostException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Erro - " + ex);
            System.exit(1);
        }

    }
}
