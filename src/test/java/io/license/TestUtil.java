package io.license;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtil {

    public static String readResource(String resourceName) throws Exception {
        URL url = TestUtil.class.getResource(resourceName);
        Path resPath = Paths.get(url.toURI());
        return new String(Files.readAllBytes(resPath), "UTF8");
    }

}
