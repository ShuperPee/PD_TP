
import java.io.Serializable;
import java.util.Calendar;

public class Download implements Serializable {

    static final long serialVersionUID = 1L;
    private final String file;
    private final String client_up;
    private final String client_down;
    private Calendar time;

    public Download(String file, String client_up, String client_down, Calendar time) {
        this.file = file;
        this.client_up = client_up;
        this.client_down = client_down;
        this.time = time;
    }

    public String getFile() {
        return file;
    }

    public String getClient_up() {
        return client_up;
    }

    public String getClient_down() {
        return client_down;
    }

    public Calendar getTime() {
        return time;
    }
}
