package io.license.onlinevalidation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.license.MappingConfig;
import io.license.exceptions.InvalidLicenseException;
import io.license.exceptions.LicenseException;
import io.license.model.ApiError;
import io.license.model.License;
import io.license.onlinevalidation.api.ValidateByKeyRequest;
import io.license.onlinevalidation.api.ValidateByTokenRequest;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Builder
@Data
public class OnlineLicenseValidator {

    @Builder.Default
    @NonNull
    private final String baseUrl = "https://api.license.io";

    @NonNull
    private final String appId;

    @NonNull
    final ObjectMapper objectMapper = MappingConfig.getObjectMapper();

    @NonNull
    final CloseableHttpClient httpClient = HttpClients.createDefault();


    /**
     * Validate a license with the given key by performing an HTTP POST to /apps/v1/validate/key
     *
     * The license key is a short identifier that is unique for the license
     *
     * @param key
     * @return
     */
    License validateByKey(String key) throws InvalidLicenseException, LicenseException {

        ValidateByKeyRequest request = new ValidateByKeyRequest(key);

        HttpPost post = createPost("/apps/v1/validate/key", request);
        return executePost(post, License.class);
    }

    /**
     * Validate a license with the given token by performing an HTTP POST to /apps/v1/validate/token
     *
     * A license token is the encrypted and signed version of the license
     *
     * @param token
     * @return
     * @throws InvalidLicenseException
     */
    public License validateByToken(String token) throws InvalidLicenseException, LicenseException {

        ValidateByTokenRequest request = new ValidateByTokenRequest(token);

        HttpPost post = createPost("/apps/v1/validate/token", request);
        return executePost(post, License.class);
    }

    private HttpPost createPost(String path, Object postBody) throws LicenseException {
        try {
            HttpPost post = new HttpPost(baseUrl + path);

            post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            post.setHeader("X-licenseio-app-id", appId);

            String requestBody = objectMapper.writeValueAsString(postBody);
            post.setEntity(new StringEntity(requestBody));

            return post;
        } catch (JsonProcessingException e) {
            throw new LicenseException("Error creating request JSON", e);
        } catch (UnsupportedEncodingException e) {
            throw new LicenseException("Error building HTTP request", e);
        }
    }

    private <T> T executePost(HttpPost post, Class<T> responseClass) throws InvalidLicenseException, LicenseException {

        try {
            CloseableHttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                // the request was succesful
                String responseJson = EntityUtils.toString(response.getEntity());
                T responseObject = objectMapper.readValue(responseJson, responseClass);

                return responseObject;

            } else if (statusCode >= 400) {
                // the request failed with a 4xx response which means the request was invalid, the response body may contain a proper error object
                // check by verifying response Content-Type header
                ContentType contentType = ContentType.get(response.getEntity());
                if (contentType != null && contentType.equals(ContentType.APPLICATION_JSON)) {
                    String responseJson = EntityUtils.toString(response.getEntity());
                    ApiError error = objectMapper.readValue(responseJson, ApiError.class);
                    throw new InvalidLicenseException(error.getMessage());
                }

                throw new InvalidLicenseException(response.getStatusLine().toString());
            } else if (statusCode >= 500) {
                // a 5xx response indicates that something went wrong on the backend and retries are in order

                throw new LicenseException(response.getStatusLine().toString());

            } else {
                // failed with another response code
                throw new LicenseException("Unexpected HTTP response received: " + response.getStatusLine().toString());
            }

        } catch (JsonParseException | JsonMappingException e) {
            throw new LicenseException("Error parsing response JSON", e);
        } catch (ClientProtocolException e) {
            throw new LicenseException("Error performing API request", e);
        } catch (IOException e) {
            throw new LicenseException("I/O Exception occurred while performing request", e);
        }
    }

}
