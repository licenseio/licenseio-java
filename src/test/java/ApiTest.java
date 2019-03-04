import io.license.OnlineLicenseValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class ApiTest {


    @Test
    public void testCreateOnlineLicenseValidator() throws Exception {

        OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
                .withBaseUrl("http://localhost:8000")
                .withAppId("3ccf0f1b-dd3f-48d9-911a-ddf479078c37")
                .build();

        assertNotNull(validator);

        assertEquals("http://localhost:8000", validator.getBaseUrl());
        assertEquals("3ccf0f1b-dd3f-48d9-911a-ddf479078c37", validator.getAppId());

    }
}
