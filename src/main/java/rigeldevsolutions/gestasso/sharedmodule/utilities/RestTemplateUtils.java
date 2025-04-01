package rigeldevsolutions.gestasso.sharedmodule.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.services.IJwtService;

@Service @RequiredArgsConstructor
public class RestTemplateUtils
{
    private final RestTemplate restTemplate;
    private final IJwtService jwtService;
    public <T, R>ResponseEntity<R> sendHttpRequest(String url, T data, HttpMethod method, Class<R> responseType)
    {
        String accessToken = jwtService.getCurrentAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<T> request = new HttpEntity(data, headers);
        ResponseEntity response =  restTemplate.exchange(url, method, request, responseType);
        return response;
    }
}
