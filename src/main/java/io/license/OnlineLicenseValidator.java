package io.license;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.license.api.ValidateLicenseByKeyRequest;
import io.license.exceptions.InvalidLicenseException;
import io.license.model.ApiError;
import io.license.model.License;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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


    /**
     * Validate a license with the given key by performing an HTTP POST to /apps/v1/validate/key
     * @param key
     * @return
     */
    License validateByKey(String key) throws InvalidLicenseException {

        ValidateLicenseByKeyRequest request = new ValidateLicenseByKeyRequest(key);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost(baseUrl + "/apps/v1/validate/key");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("X-licenseio-app-id", appId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.registerModule(new JavaTimeModule());

        try {

            String requestBody = objectMapper.writeValueAsString(request);
            post.setEntity(new StringEntity(requestBody));

            CloseableHttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // success

                String responseJson = EntityUtils.toString(response.getEntity());
                License license = objectMapper.readValue(responseJson, License.class);

                return license;

            } else if (statusCode >= 400 && statusCode < 500) {
                String responseJson = EntityUtils.toString(response.getEntity());
                ApiError error = objectMapper.readValue(responseJson, ApiError.class);

                throw new InvalidLicenseException(error.getMessage());
            } else {


                // failed
                // TODO handle this, probably throw an exception
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // TODO fix this
        throw new RuntimeException("Request failed");
    }

}
