package io.license.crypto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import io.license.exceptions.CertificateNotFoundException;

import java.security.Key;
import java.util.List;

public class CertificatesSigningKeyResolver implements SigningKeyResolver {

    private List<Certificate> certificates;

    public CertificatesSigningKeyResolver(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        return resolve(header);
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, String plaintext) {
        return resolve(header);
    }

    private Key resolve(JwsHeader header) {
        // first based on kid;
        String keyId = header.getKeyId();
        if (keyId == null) return null;

        return certificates.stream()
                .filter(cert -> cert.getName().equals(keyId))
                .map(Certificate::getPublicKey)
                .findFirst()
                .orElseThrow(() -> new CertificateNotFoundException("No certificate found with name '" + keyId + "'"));
    }


}
