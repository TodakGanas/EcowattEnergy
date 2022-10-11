import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class App {
    public static void main(String[] args) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest postRequest = HttpRequest.newBuilder()      
            .header("Authorization", "Bearer st6eLWRnZyBfH0PVdo4461MWMZqUz59V4o8oUXJUHpFLS0mjIt5oP3")
            .uri(URI.create("https://digital.iservices.rte-france.com/open_api/ecowatt/v4/signals"))
            .build();

        HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());

        System.out.println(postResponse.body());
    }
}

