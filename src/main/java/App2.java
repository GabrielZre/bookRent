import org.apache.commons.codec.digest.DigestUtils;
import core.Authenticator;

public class App2 {
    public static void main(String[] args) {
        String hash = DigestUtils.md5Hex("admin" + Authenticator.getInstance().getLoggedUser());

        System.out.println(hash);
    }
}