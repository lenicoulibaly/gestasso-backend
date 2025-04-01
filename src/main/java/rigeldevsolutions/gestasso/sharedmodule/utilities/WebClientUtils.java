package rigeldevsolutions.gestasso.sharedmodule.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientUtils
{
    private final WebClient webClient;

    @Autowired
    public WebClientUtils(@Qualifier("keycloak") WebClient webClient)
    {
        this.webClient = webClient;
    }

    public <T, R> R sendHttpRequest(String url, T requestData, HttpMethod method, Class<R> responseType) {
        return webClient.method(method)
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestData)
                .retrieve()
                .bodyToMono(responseType)
                .block(); // Pour une exécution synchrone
    }
}
