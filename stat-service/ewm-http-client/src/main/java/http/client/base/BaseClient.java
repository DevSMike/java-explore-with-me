package http.client.base;

import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequestWithoutBody(path, parameters);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return makeAndSendRequest(path, null, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(String path, @Nullable Map<String, Object> parameters, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);

        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }

    private <T> ResponseEntity<Object> makeAndSendRequestWithoutBody(String path, @Nullable Map<String, Object> parameters) {
        HttpEntity<T> requestEntity = new HttpEntity<>(null);

        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, HttpMethod.GET, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, HttpMethod.GET, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
