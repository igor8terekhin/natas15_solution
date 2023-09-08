import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.io.*;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseClass {
    public Response getRequestWithParams(String url, String authToken, Map<String, String> params) {
        return given()
                .header(new Header("Authorization", authToken))
                .queryParams(params)
                .get(url)
                .andReturn();
    }

    public Response getRequestWithParamsAndCookie(String url, String authToken, Map<String, String> params, String cookieName, String cookie) {
        return given()
                .header(new Header("Authorization", authToken))
                .queryParams(params)
                .cookie(cookieName, cookie)
                .get(url)
                .andReturn();
    }

    public void writeTextToFile(String fileName, String text) throws IOException {
        try(FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHeader(Response response, String name) {
        Headers headers = response.getHeaders();

        return headers.getValue(name);
    }

    protected String getCookie(Response response, String name) {
        Map<String, String> cookies = response.getCookies();

        return cookies.get(name);
    }

}
