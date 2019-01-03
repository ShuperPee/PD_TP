
import java.io.Serializable;
import java.util.List;

class InitClient implements Serializable {

    static final long serialVersionUID = 1L;
    String ClientAddr;
    List<String>[] ficheiros;
    String username;
    String password;
    String directory;
    int port_udp, port_tcp;

    public InitClient(String username, String password, String ClientAddr, List<String>[] ficheiros, String directory, int port_udp, int port_tcp) {
        this.username = username;
        this.password = password;
        this.ClientAddr = ClientAddr;
        this.ficheiros = ficheiros;
        this.directory = directory;
        this.port_udp = port_udp;
        this.port_tcp = port_tcp;
    }

    public InitClient(RegisterClient client) {
        username = client.getUsername();
        password = client.getPassword();
        ClientAddr = client.getClientAddr();
        ficheiros = client.getFicheiros();
        directory = client.getDirectory();
        port_udp = client.getPort_udp();
        port_tcp = client.getPort_tcp();
    }

    public String toStringSQL() {
        return "'" + ClientAddr + "','" + username + "','" + password + "'," + "TRUE" + ",'" + directory + "'," + port_udp + "," + port_tcp + "," + 3 + ");";
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
