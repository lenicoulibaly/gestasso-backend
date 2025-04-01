package rigeldevsolutions.gestasso.authmodule.keycloak.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.env.KeycloakEnv;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service @RequiredArgsConstructor
public class KeycloakApiUserService implements IKeycloakApiUserService
{
    private final RestTemplate restTemplate;
    private final KeycloakUserRepo kuRepo;
    private final KeycloakEnv env;
    @Override
    public Page<KeycloakUser> searchUsers(String key, int page, int size)
    {
        String url = env.keycloakServerAddress +"/admin/realms/"+env.keycloakRealm+"/users";
        String countUrl = env.keycloakServerAddress +"/admin/realms/"+env.keycloakRealm+"/users/count";
        HttpHeaders headers = new HttpHeaders();
        String accessToken = this.getAdminAccessToken();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("search", key);
        HttpEntity<MultiValueMap<String, Object>> countRequest = new HttpEntity<>(params, headers);
        ResponseEntity<Long> countResponse = restTemplate.exchange(countUrl, HttpMethod.GET, countRequest, Long.class);
        Long nbrUsers = countResponse.getBody();
        params.add("first", page);
        params.add("max", size);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);
        List responseList = response.getBody();
        if(responseList == null) return new PageImpl<>(Collections.EMPTY_LIST);
        return new PageImpl<>(responseList, PageRequest.of(page, size), nbrUsers);
    }

    public List<String> searchUsersIds(String key)
    {
        return kuRepo.searchUserIds(key, env.getKeycloakRealmId());
    }

    @Override
    public KeycloakUser addUser(KeycloakUser user)
    {
        String url = env.keycloakServerAddress +"/admin/realms/"+env.keycloakRealm+"/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.getAdminAccessToken());
        HttpEntity<KeycloakUser> request = new HttpEntity<>(user, headers);
        ResponseEntity<KeycloakUser> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, KeycloakUser.class);
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            // Récupérer l'ID de l'utilisateur créé à partir de l'en-tête Location
            String locationHeader = responseEntity.getHeaders().getLocation().toString();
            String userId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
            return kuRepo.findById(userId).orElse(null);
        }
        throw new RuntimeException("Échec de la création de l'utilisateur Keycloak");
    }

    @Override
    public String getAdminAccessToken()
    {
        String url = env.keycloakServerAddress + "/realms/" + env.keycloakRealm + "/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", env.keycloakClientId);
        body.add("client_secret", env.keycloakClientSecret);
        body.add("username", env.keycloakAdminUsername);
        body.add("password", env.keycloakAdminPassword);
        body.add("grant_type", "password");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class, body);

        return response.getBody().get("access_token").toString();
    }
}
