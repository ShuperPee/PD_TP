
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChatMsg implements Serializable {

    static final long serialVersionUID = 1L;
    String Msg;
    String username;
    Calendar time;

    public ChatMsg(String username, String Msg) {
        this.username = username;
        this.Msg = Msg;
        time = GregorianCalendar.getInstance();
    }

    public String getMsg() {
        return Msg;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return "[" + time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND) + "] ";
    }

    public String toString() {
        return getTime() + username + " : " + Msg;
    }

}
