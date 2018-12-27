
import java.io.Serializable;
import java.util.List;

class InitClient implements Serializable {

    static final long serialVersionUID = 1L;
    String ClientAddr;
    List<String> ficheiros;

    public InitClient(String ClientAddr, List<String> ficheiros) {
        this.ClientAddr = ClientAddr;
        this.ficheiros = ficheiros;
    }
}
