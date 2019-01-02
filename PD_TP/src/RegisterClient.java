
import java.io.Serializable;
import java.util.List;

public class RegisterClient implements Serializable {

    static final long serialVersionUID = 1L;
    String ClientAddr;
    List<String>[] ficheiros;
    String username;
    String password;

    public RegisterClient(String username, String password, String ClientAddr, List<String>[] ficheiros) {
        this.username = username;
        this.password = password;
        this.ClientAddr = ClientAddr;
        this.ficheiros = ficheiros;
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
