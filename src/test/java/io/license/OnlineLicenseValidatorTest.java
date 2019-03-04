package io.license;

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
}
