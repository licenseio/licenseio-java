package io.license;

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
}
