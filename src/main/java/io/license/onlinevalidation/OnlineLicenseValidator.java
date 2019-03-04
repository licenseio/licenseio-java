package io.license.onlinevalidation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.license.MappingConfig;
import io.license.exceptions.InvalidLicenseException;
import io.license.exceptions.InvalidRequestException;
import io.license.exceptions.LicenseException;
import io.license.model.ApiError;
import io.license.model.License;
import io.license.onlinevalidation.api.ValidateByKeyRequest;
import io.license.onlinevalidation.api.ValidateByTokenRequest;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Builder
@Data
public class OnlineLicenseValidator {

    @Builder.Default
    @NonNull
    private final String baseUrl = "https://api.license.io";

    @NonNull
    private final String appId;

    @Builder.Default
    @NonNull
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @NonNull
    private final ObjectMapper objectMapper = MappingConfig.getObjectMapper();


    /**
     * Validate a license with the given key by performing an HTTP POST to /apps/v1/validate/key
     * <p>
     * The license key is a short identifier that is unique for the license
     *
     * @param key
     * @return
     */
    License validateByKey(String key) throws InvalidLicenseException, LicenseException {

        ValidateByKeyRequest request = new ValidateByKeyRequest(key);

        HttpPost post = createPost("/apps/v1/validate/key", request);
        try {
            return execute(post, License.class);
        } catch (InvalidRequestException ex) {
            throw new InvalidLicenseException(ex.getMessage(), ex);
        }
    }

    /**
     * Validate a license with the given token by performing an HTTP POST to /apps/v1/validate/token
     * <p>
     * A license token is the encrypted and signed version of the license
     *
     * @param token
     * @return
     * @throws InvalidLicenseException
     */
    public License validateByToken(String token) throws InvalidLicenseException, LicenseException {

        ValidateByTokenRequest request = new ValidateByTokenRequest(token);

        HttpPost post = createPost("/apps/v1/validate/token", request);
        try {
            return execute(post, License.class);
        } catch (InvalidRequestException ex) {
            throw new InvalidLicenseException(ex.getMessage(), ex);
        }
    }


    /**
     * Create a valid HttpPost object for use with the License API
     *
     * This method will add the X-Licenseio-app-id and Content-Type headers
     *
     * @param path
     * @param requestBody an object representing the request body, this will be converted to a JSON string with the Jackson ObjectMapper
     * @return a valid HttpPost object
     * @throws LicenseException if the JSON serialization fails
     */
    private HttpPost createPost(String path, Object requestBody) throws LicenseException {
        try {
            HttpPost post = new HttpPost(baseUrl + path);

            post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            post.setHeader("X-licenseio-app-id", appId);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            post.setEntity(new StringEntity(requestBodyJson));

            return post;
        } catch (JsonProcessingException e) {
            throw new LicenseException("Error creating request JSON", e);
        } catch (UnsupportedEncodingException e) {
            throw new LicenseException("Error building HTTP request", e);
        }
    }

    /**
     * Execute the given HttpRequest
     * @param request the request to execute
     * @param responseClass the expected response class
     * @return an instance of the class passed in responseClass, parsed from the response body
     * @throws LicenseException
     */
    private <T> T execute(HttpRequestBase request, Class<T> responseClass) throws InvalidRequestException, LicenseException {

        try {
            CloseableHttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                // the request was succesful
                String responseJson = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(responseJson, responseClass);

            } else if (statusCode >= 400 && statusCode < 500) {
                // the request failed with a 4xx response which means the request was invalid, the response body may contain a proper error object
                // check by verifying response Content-Type header
                if (isApplicationJson(response)) {
                    String responseJson = EntityUtils.toString(response.getEntity());
                    ApiError error = objectMapper.readValue(responseJson, ApiError.class);
                    throw new InvalidRequestException(error.getMessage());
                }

                throw new InvalidRequestException(response.getStatusLine().toString());
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

    /**
     * helper method for checking if a response has the application/json mime-type within the content-type header
     * @param response
     * @return true if the mime-type within the content-header is application/json or else false
     */
    private boolean isApplicationJson(CloseableHttpResponse response) {

        return Optional.ofNullable(response.getFirstHeader(HttpHeaders.CONTENT_TYPE))
                .map(Header::getValue)
                .map(ContentType::parse)
                .map(ContentType::getMimeType)
                .map(ContentType::getByMimeType)
                .filter(ct -> ct.equals(ContentType.APPLICATION_JSON))
                .isPresent();

    }


}
