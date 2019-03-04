package io.license.offlinevalidation;

import io.license.TestUtil;
import io.license.crypto.Certificate;
import io.license.exceptions.CertificateNotFoundException;
import io.license.exceptions.InvalidCertificateException;
import io.license.exceptions.InvalidLicenseException;
import io.license.model.License;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class OfflineLicenseValidatorTest {


    private String CERTIFICATE_NAME = "firstkey";
    private String CERTIFICATE_PEM = "-----BEGIN CERTIFICATE-----\n"
            + "MIICrzCCAZegAwIBAgIJAJ4rNZtxGNA0MA0GCSqGSIb3DQEBCwUAMBcxFTATBgNV"
            + "BAMTDFF1YW50dXMgQlZCQTAeFw0xODAzMDUxNjAyMjlaFw0zODAyMjgxNjAyMjla"
            + "MBcxFTATBgNVBAMTDFF1YW50dXMgQlZCQTCCASIwDQYJKoZIhvcNAQEBBQADggEP"
            + "ADCCAQoCggEBAJk9x0N8wcklReGneQ6SyoSLXOm+8EBO9dmgEmSQtV81/Z2ZZzUl"
            + "k1fMufh4hIBwIKJev8Gklv9I82U5ZKVm6r67kv7Zbf8IlgNFUNeQrNP8nrGyxmfK"
            + "fYpW2Qxw6ym2i48EFCLx2JMVhxqLQ4GzD9RVjui+Ygvpi5YayJVnjjP+lBl8ZASH"
            + "y6SIiv/O7TkOPRq2xHehbgNQXr8CyDYEdu8WR0x07eMG8RErvdu9OjH/59xeQQGP"
            + "FDJnD9dCYEQkRmq7hyZOF0Cp6BP4be0dqRq8TZv8MvZA1HF7ull1NrCnTnMOq0TJ"
            + "BhPcq61Iu1OZlBne2su68wNzgFVUlrL3W2cCAwEAATANBgkqhkiG9w0BAQsFAAOC"
            + "AQEADVJS2qOhOiC4hQtAMcyAaJs6yKG5IfMKO2ElqCkP2Afe/zphyDI2lvQTtqLW"
            + "7N02m7SgX9zdAuDmMI9B7pzJpJ9W7wj4s0BIxTPyVX9GQ+zhUlL+GdFsRTZpXEsM"
            + "8rGuhydAApMzB9jgV1+iFw4KeXIwo++dVLYyMZQj6lkqKEnZy0kDj9uWIpXHRHzF"
            + "Hp4qwLXzJBdpJZpGE8umIXmcg2t0XlB7OlDjayTdwi01u+h4Qf35zWjY47+n+n2u"
            + "itAkhdtyiYu4fgICI9Hi8Zywo5iBTUlk0zMATYv6tiT7Rn4SGucqN5CeNspt3jE5"
            + "rtqqWG0WMKy/1lAYC6yGVcCS2A==\n"
            + "-----END CERTIFICATE-----";

    @Test
    public void testBuilder_success() throws Exception {
        OfflineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .certificate(new Certificate(CERTIFICATE_NAME, CERTIFICATE_PEM))
                .build();
    }

    @Test
    public void testbuilder_successWithCertificatePath() throws Exception {

        OfflineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .certificate(new Certificate(CERTIFICATE_NAME,
                                             TestUtil.getResourcePath("/io/license/offlinevalidation/firstkey.crt")))
                .build();
    }

    @Test(expected = InvalidCertificateException.class)
    public void testBuilder_invalidCertificateString() throws Exception {
        OfflineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .certificate(new Certificate(CERTIFICATE_NAME, "invalid-certificate-data"))
                .build();
    }

    @Test(expected = InvalidCertificateException.class)
    public void testBuilder_invalidCertificateFile() throws Exception {
        OfflineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .certificate(new Certificate(CERTIFICATE_NAME, Paths.get("/invalid/path")))
                .build();
    }


    @Test
    public void testValidate_success() throws Exception {

        OfflineLicenseValidator validator = OfflineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .certificate(new Certificate(CERTIFICATE_NAME, CERTIFICATE_PEM))
                .build();

        String token = "eyJraWQiOiJmaXJzdGtleSIsIng1dSI6Imh0dHBzOi8vZGV2LmxpY2Vuc2UuaW8vY2VydGlmaWNhdGVzLzNjY2YwZjFiLWRkM2YtNDhkOS05MTFhLWRkZjQ3OTA3OGMzNy9maXJzdGtleS5jcnQiLCJhbGciOiJSUzI1NiJ9.eyJzdGFydHNfYXQiOiIyMDE5LTAzLTA0VDE0OjQ4OjIwLjUzNDg1NFoiLCJsaWNlbnNlZSI6eyJuYW1lIjoiU3RldmVuIFZhbiBCYWVsIiwiZW1haWwiOiJzdGV2ZW5AcXVhbnR1cy5pbyIsImNvbXBhbnkiOiJRdWFudHVzIEJWQkEifSwiY3JlYXRlZF9hdCI6IjIwMTktMDMtMDRUMTQ6NDg6MjAuNTM0ODU0WiIsInZlcnNpb24iOnsibWluIjp7ImNvZGUiOjAsIm5hbWUiOiIxLjAuMCJ9LCJtYXgiOnsiY29kZSI6MTAwMCwibmFtZSI6IjIuMC4wIn19LCJsaWNlbnNlX2tleSI6ImRlbW9saWNlbnNla2V5IiwiZXhwaXJhdGlvbl90eXBlIjoiZGF0ZSIsImZlYXR1cmVzIjpbXSwiZXhwaXJlc19hdCI6IjIwMjQtMTItMzFUMjM6MDA6MDBaIiwiYXBwbGljYXRpb24iOnsiaWQiOiIzY2NmMGYxYi1kZDNmLTQ4ZDktOTExYS1kZGY0NzkwNzhjMzciLCJuYW1lIjoiUXVhbnR1cyBUYXNrcyJ9LCJ1cGRhdGVkX2F0IjoiMjAxOS0wMy0wNFQxNDo0ODoyMC41MzQ4NTRaIiwibmFtZSI6InNpbXBsZSBsaWNlbnNlIiwibGlua3MiOltdLCJpZCI6IjI0MTllZmY5LTgyMTItNGEwOS1iZjAwLWM2N2Y3ODlkMDlkOSIsInBhcmFtZXRlcnMiOnt9LCJzdGF0dXMiOiJhY3RpdmUifQ.Baw3PwPE-8pZyi3DW0zTf7p8WT1oCLW3atH8c56WEcNHtzYs-U_d9T53cpELzwPniSHRbjZuNZ08CziOCL6aEur6LdgkcoUHdeebCP_1XRaeRHEw0EIX811p3KnemkIRdafPruLPHZUp8gYFw1JxmqZuRHXnO-CHqd2QfxQa4vN68e7kGoHl4ROy0mRsn_IfzBIwDvZk56a6F9hC7XGILItJ7yOgwtrJrkl5nl9-1fCV9ZOxEAViZEavokk3PvOdahQje_jrbHcryepIMGmcpsIWxERQuc2Ec-4tifwJ_B_HzLV5B9PQ5KXYfwIqApvlHEUpAuj1at9CxD0TmLXwhg";

        License license = validator.validate(token);

        assertNotNull(license);
        assertNotNull(license.getId());
        assertNotNull(license.getName());

    }

    @Test(expected = InvalidLicenseException.class)
    public void testValidate_invalidToken() throws Exception {
        OfflineLicenseValidator validator = OfflineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .certificate(new Certificate(CERTIFICATE_NAME, CERTIFICATE_PEM))
                .build();

        validator.validate("invalid-token-string");
    }

    @Test(expected = CertificateNotFoundException.class)
    public void testValidate_certificateNotPresent() throws Exception {
        OfflineLicenseValidator validator = OfflineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .build();

        String token = "eyJraWQiOiJmaXJzdGtleSIsIng1dSI6Imh0dHBzOi8vZGV2LmxpY2Vuc2UuaW8vY2VydGlmaWNhdGVzLzNjY2YwZjFiLWRkM2YtNDhkOS05MTFhLWRkZjQ3OTA3OGMzNy9maXJzdGtleS5jcnQiLCJhbGciOiJSUzI1NiJ9.eyJzdGFydHNfYXQiOiIyMDE5LTAzLTA0VDE0OjQ4OjIwLjUzNDg1NFoiLCJsaWNlbnNlZSI6eyJuYW1lIjoiU3RldmVuIFZhbiBCYWVsIiwiZW1haWwiOiJzdGV2ZW5AcXVhbnR1cy5pbyIsImNvbXBhbnkiOiJRdWFudHVzIEJWQkEifSwiY3JlYXRlZF9hdCI6IjIwMTktMDMtMDRUMTQ6NDg6MjAuNTM0ODU0WiIsInZlcnNpb24iOnsibWluIjp7ImNvZGUiOjAsIm5hbWUiOiIxLjAuMCJ9LCJtYXgiOnsiY29kZSI6MTAwMCwibmFtZSI6IjIuMC4wIn19LCJsaWNlbnNlX2tleSI6ImRlbW9saWNlbnNla2V5IiwiZXhwaXJhdGlvbl90eXBlIjoiZGF0ZSIsImZlYXR1cmVzIjpbXSwiZXhwaXJlc19hdCI6IjIwMjQtMTItMzFUMjM6MDA6MDBaIiwiYXBwbGljYXRpb24iOnsiaWQiOiIzY2NmMGYxYi1kZDNmLTQ4ZDktOTExYS1kZGY0NzkwNzhjMzciLCJuYW1lIjoiUXVhbnR1cyBUYXNrcyJ9LCJ1cGRhdGVkX2F0IjoiMjAxOS0wMy0wNFQxNDo0ODoyMC41MzQ4NTRaIiwibmFtZSI6InNpbXBsZSBsaWNlbnNlIiwibGlua3MiOltdLCJpZCI6IjI0MTllZmY5LTgyMTItNGEwOS1iZjAwLWM2N2Y3ODlkMDlkOSIsInBhcmFtZXRlcnMiOnt9LCJzdGF0dXMiOiJhY3RpdmUifQ.Baw3PwPE-8pZyi3DW0zTf7p8WT1oCLW3atH8c56WEcNHtzYs-U_d9T53cpELzwPniSHRbjZuNZ08CziOCL6aEur6LdgkcoUHdeebCP_1XRaeRHEw0EIX811p3KnemkIRdafPruLPHZUp8gYFw1JxmqZuRHXnO-CHqd2QfxQa4vN68e7kGoHl4ROy0mRsn_IfzBIwDvZk56a6F9hC7XGILItJ7yOgwtrJrkl5nl9-1fCV9ZOxEAViZEavokk3PvOdahQje_jrbHcryepIMGmcpsIWxERQuc2Ec-4tifwJ_B_HzLV5B9PQ5KXYfwIqApvlHEUpAuj1at9CxD0TmLXwhg";
        validator.validate(token);

    }
}