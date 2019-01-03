
import java.io.Serializable;
import java.util.List;

public class RegisterClient implements Serializable {

    static final long serialVersionUID = 1L;
    String ClientAddr;
    List<String>[] ficheiros;
    String username;
    String password;
    String directory;
    int port_udp, port_tcp;

    public RegisterClient(String username, String password, String ClientAddr, List<String>[] ficheiros, String directory, int port_udp, int port_tcp) {
        this.username = username;
        this.password = password;
        this.ClientAddr = ClientAddr;
        this.ficheiros = ficheiros;
        this.directory = directory;
        this.port_udp = port_udp;
        this.port_tcp = port_tcp;
    }

    public String getClientAddr() {
        return ClientAddr;
    }

    public List<String>[] getFicheiros() {
        return ficheiros;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDirectory() {
        return directory;
    }

    public int getPort_udp() {
        return port_udp;
    }

    public int getPort_tcp() {
        return port_tcp;
    }

}
