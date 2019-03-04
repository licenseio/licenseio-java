package io.license.offlinevalidation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolver;
import io.license.MappingConfig;
import io.license.crypto.Certificate;
import io.license.crypto.CertificatesSigningKeyResolver;
import io.license.exceptions.CertificateNotFoundException;
import io.license.exceptions.InvalidLicenseException;
import io.license.exceptions.LicenseException;
import io.license.model.License;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.io.IOException;
import java.util.List;

@Builder
@Data
public class OfflineLicenseValidator {

    @NonNull
    private final String appId;

    @Singular
    private final List<Certificate> certificates;

    @NonNull
    private final ObjectMapper objectMapper = MappingConfig.getObjectMapper();


    public License validate(String token) throws InvalidLicenseException, LicenseException, CertificateNotFoundException {

        SigningKeyResolver signingKeyResolver = new CertificatesSigningKeyResolver(certificates);

        try {
            Claims claims = Jwts.parser().setSigningKeyResolver(signingKeyResolver).parseClaimsJws(token).getBody();

            String json = objectMapper.writeValueAsString(claims);

            return objectMapper.readValue(json, License.class);
        } catch (JsonProcessingException | JwtException e) {
            throw new InvalidLicenseException("License token could not be parsed", e);
        } catch (IOException e) {
            throw new LicenseException("Error parsing certificate", e);
        }

    }


}
