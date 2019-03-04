package io.license.crypto;

import io.license.exceptions.InvalidCertificateException;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.PublicKey;

public class PEMUtils {
    /**
     * Parse the given PEM data and extract the public key
     *
     * @param data
     * @return
     */
    public static Key parsePEM(String data) {
        try {
            PEMParser parser = new PEMParser(new StringReader(data));
            Object o = parser.readObject();
            SubjectPublicKeyInfo publicKeyInfo = null;

            if (o instanceof SubjectPublicKeyInfo) {
                publicKeyInfo = ((SubjectPublicKeyInfo) o);
            } else if (o instanceof X509CertificateHolder) {
                X509CertificateHolder certificateHolder = ((X509CertificateHolder) o);
                publicKeyInfo = certificateHolder.getSubjectPublicKeyInfo();
            } else {
                throw new InvalidCertificateException("Could not parse PEM certificate data");
            }
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PublicKey publicKey = converter.getPublicKey(publicKeyInfo);

            return publicKey;
        } catch (IOException e) {
            throw new InvalidCertificateException("Could not parse PEM certificate data", e);
        }

    }

    public static Key parsePEM(Path pemFile) {
        try {
            String pemData = new String(Files.readAllBytes(pemFile));
            return parsePEM(pemData);
        } catch (IOException e) {
            throw new InvalidCertificateException("Could not parse PEM certificate data", e);
        }
    }
}
