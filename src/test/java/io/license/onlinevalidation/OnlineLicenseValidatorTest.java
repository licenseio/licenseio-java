package io.license.onlinevalidation;

import io.license.exceptions.InvalidLicenseException;
import io.license.model.License;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class OnlineLicenseValidatorTest {


    @Test
    public void testCreateOnlineLicenseValidator() throws Exception {

        OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
                .baseUrl("http://localhost:8000")
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .build();

        assertNotNull(validator);

        assertEquals("http://localhost:8000", validator.getBaseUrl());
        assertEquals("3ccf0f1b-dd3f-48d9-911a-ddf479078c37", validator.getAppId());

    }

    @Test(expected = NullPointerException.class)
    public void testCreateOnlineLicenseValidatorNoAppId() throws Exception {
        OnlineLicenseValidator validator = OnlineLicenseValidator.builder().build();
    }

    @Test
    public void testCreateOnlineLicenseValidatorDefaultBaseUrl() throws Exception {

        OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .build();

        assertNotNull(validator);

        assertEquals("https://api.license.io", validator.getBaseUrl());
    }

    @Test
    public void testValidateByKey() throws Exception {

        OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
                .baseUrl("http://localhost:8000")
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .build();

        String key = "demoli-censek-ey";

        License license = validator.validateByKey(key);

        assertNotNull(license);


        assertNotNull(license.getId());
        assertNotNull(license.getName());

        assertNotNull(license.getLicensee());
        assertNotNull(license.getLicensee().getCompany());
        assertNotNull(license.getLicensee().getName());
        assertNotNull(license.getLicensee().getEmail());

        assertNotNull(license.getApplication());
        assertNotNull(license.getApplication().getId());
        assertNotNull(license.getApplication().getName());

        assertNotNull(license.getStatus());

        assertNotNull(license.getStartsAt());
        assertNotNull(license.getExpiresAt());
        assertNotNull(license.getExpirationType());

        assertNotNull(license.getFeatures());
        assertNotNull(license.getParameters());

        assertNotNull(license.getLicenseKey());
        assertNotNull(license.getCreatedAt());
        assertNotNull(license.getUpdatedAt());

        assertNotNull(license.getVersion());
        assertNotNull(license.getVersion().getMax());
        assertNotNull(license.getVersion().getMin());

        assertNotNull(license.getVersion().getMin().getName());

        System.out.printf("validated license: %s\n", license);
    }

    @Test(expected = InvalidLicenseException.class)
    public void validateByKey_invalidLicense() throws Exception {

        OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
                .baseUrl("http://localhost:8000")
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .build();

        License license = validator.validateByKey("invalid-key");
    }

    @Test
    public void testValidateByToken() throws Exception {

        OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
                .baseUrl("http://localhost:8000")
                .appId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .build();

        String token = "eyJraWQiOiJmaXJzdGtleSIsIng1dSI6Imh0dHBzOi8vZGV2LmxpY2Vuc2UuaW8vY2VydGlmaWNhdGVzLzNjY2YwZjFiLWRkM2YtNDhkOS05MTFhLWRkZjQ3OTA3OGMzNy9maXJzdGtleS5jcnQiLCJhbGciOiJSUzI1NiJ9.eyJzdGFydHNfYXQiOiIyMDE5LTAzLTA0VDE0OjQ4OjIwLjUzNDg1NFoiLCJsaWNlbnNlZSI6eyJuYW1lIjoiU3RldmVuIFZhbiBCYWVsIiwiZW1haWwiOiJzdGV2ZW5AcXVhbnR1cy5pbyIsImNvbXBhbnkiOiJRdWFudHVzIEJWQkEifSwiY3JlYXRlZF9hdCI6IjIwMTktMDMtMDRUMTQ6NDg6MjAuNTM0ODU0WiIsInZlcnNpb24iOnsibWluIjp7ImNvZGUiOjAsIm5hbWUiOiIxLjAuMCJ9LCJtYXgiOnsiY29kZSI6MTAwMCwibmFtZSI6IjIuMC4wIn19LCJsaWNlbnNlX2tleSI6ImRlbW9saWNlbnNla2V5IiwiZXhwaXJhdGlvbl90eXBlIjoiZGF0ZSIsImZlYXR1cmVzIjpbXSwiZXhwaXJlc19hdCI6IjIwMjQtMTItMzFUMjM6MDA6MDBaIiwiYXBwbGljYXRpb24iOnsiaWQiOiIzY2NmMGYxYi1kZDNmLTQ4ZDktOTExYS1kZGY0NzkwNzhjMzciLCJuYW1lIjoiUXVhbnR1cyBUYXNrcyJ9LCJ1cGRhdGVkX2F0IjoiMjAxOS0wMy0wNFQxNDo0ODoyMC41MzQ4NTRaIiwibmFtZSI6InNpbXBsZSBsaWNlbnNlIiwibGlua3MiOltdLCJpZCI6IjI0MTllZmY5LTgyMTItNGEwOS1iZjAwLWM2N2Y3ODlkMDlkOSIsInBhcmFtZXRlcnMiOnt9LCJzdGF0dXMiOiJhY3RpdmUifQ.Baw3PwPE-8pZyi3DW0zTf7p8WT1oCLW3atH8c56WEcNHtzYs-U_d9T53cpELzwPniSHRbjZuNZ08CziOCL6aEur6LdgkcoUHdeebCP_1XRaeRHEw0EIX811p3KnemkIRdafPruLPHZUp8gYFw1JxmqZuRHXnO-CHqd2QfxQa4vN68e7kGoHl4ROy0mRsn_IfzBIwDvZk56a6F9hC7XGILItJ7yOgwtrJrkl5nl9-1fCV9ZOxEAViZEavokk3PvOdahQje_jrbHcryepIMGmcpsIWxERQuc2Ec-4tifwJ_B_HzLV5B9PQ5KXYfwIqApvlHEUpAuj1at9CxD0TmLXwhg";

        License license = validator.validateByToken(token);

        assertNotNull(license);

        assertNotNull(license.getId());
        assertNotNull(license.getName());

        System.out.printf("validated license: %s\n", license);
    }
}
