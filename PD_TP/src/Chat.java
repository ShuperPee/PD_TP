
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {

    public List<String> msgs;

    public Chat() {
        msgs = new ArrayList<>();
    }

    public synchronized void addMsg(ChatMsg msg) {
        msgs.add(msg.toString());
    }

    public synchronized List<String> getMsgs() {
        return msgs;
    }

    public synchronized void clear() {
        msgs.clear();
    }
}
