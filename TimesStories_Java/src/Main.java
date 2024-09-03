import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {

        HttpServer webServer = HttpServer.create(new InetSocketAddress(8080), 0);
        webServer.createContext("/getTimeStories", new LatestStoriesHandler());
        webServer.setExecutor(null);
        webServer.start();
        System.out.println("Server started on http://localhost:8080/getTimeStories");
    }

    static class LatestStoriesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if ("GET".equals(httpExchange.getRequestMethod())) {
                List<String> storiesJsonList = retrieveLatestStories();
                String jsonResponse = "[" + String.join(",\n", storiesJsonList) + "]";
                httpExchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(jsonResponse.getBytes());
                outputStream.close();
            } else {
                httpExchange.sendResponseHeaders(405, -1); 
            }
        }
    }

    public static List<String> retrieveLatestStories() {
        String websiteUrl = "https://time.com";
        List<String> jsonStoriesList = new ArrayList<>();
        try {
            URL timeUrl = new URL(websiteUrl);
            HttpURLConnection connection = (HttpURLConnection) timeUrl.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder webpageContent = new StringBuilder();
            String webpageLine;

            while ((webpageLine = bufferedReader.readLine()) != null) {
                webpageContent.append(webpageLine);
            }

            bufferedReader.close();
            connection.disconnect();

            String htmlContent = webpageContent.toString();
            Pattern htmlPattern = Pattern.compile(
                    "<li class=\"latest-stories__item\">\\s*<a href=\"([^\"]+)\">\\s*<h3 class=\"latest-stories__item-headline\">(.*?)</h3>\\s*</a>"
            );
            Matcher contentMatcher = htmlPattern.matcher(htmlContent);

            int storyCounter = 0;
            while (contentMatcher.find() && storyCounter < 6) {
                String partialLink = contentMatcher.group(1);
                String headline = contentMatcher.group(2).trim();
                String fullLink = "https://time.com" + partialLink;

                String jsonFormattedStory = String.format("{\"title\": \"%s\",\n  \"link\": \"%s\"}", headline, fullLink);
                jsonStoriesList.add(jsonFormattedStory);

                storyCounter++;
            }

        } catch (Exception error) {
            error.printStackTrace();
        }

        return jsonStoriesList;
    }
}
