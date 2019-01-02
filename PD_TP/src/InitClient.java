
import java.io.Serializable;
import java.util.List;

class InitClient implements Serializable {

    static final long serialVersionUID = 1L;
    String ClientAddr;
    List<String>[] ficheiros;
    String username;
    String password;

    public InitClient(String username, String password, String ClientAddr, List<String>[] ficheiros) {
        this.username = username;
        this.password = password;
        this.ClientAddr = ClientAddr;
        this.ficheiros = ficheiros;
    }

    public InitClient(RegisterClient client) {
        username = client.getUsername();
        password = client.getPassword();
        ClientAddr = client.getClientAddr();
        ficheiros = client.getFicheiros();
    }

    public String toStringSQL() {
        return ClientAddr + ",TRUE";
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
}
