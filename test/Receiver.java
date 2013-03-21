import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.channel.ConnectionRule;

import java.io.DataInputStream;
import java.util.concurrent.TimeUnit;

/**
 * Test the throughput of the data retrieval.
 *
 * @author Kohsuke Kawaguchi
 */
public class Receiver {
    public static void main(String[] args) throws Exception {
        Connection connection = new ConnectionRule().getConnection();
        final Session session = connection.openSession();

        session.execCommand("cat /dev/zero");
        session.getStdin().close();
        session.getStderr().close();

        byte[] buf = new byte[10*1024*1024];
        DataInputStream in = new DataInputStream(session.getStdout());

        while (true) {
            long start = System.nanoTime();
            in.readFully(buf);
            long end = System.nanoTime();
            System.out.println("Took "+ TimeUnit.NANOSECONDS.toMillis(end-start));
        }
    }
}
