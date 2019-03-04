package io.license.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {

    private String id;

    private String licenseKey;

    private String name;

    private Licensee licensee;

    private Application application;

    private Status status;

    private Instant startsAt;

    private ExpirationType expirationType;

    private Instant expiresAt;

    private List<String> features;

    private Map<String, String> parameters;

    private Versions version;

    private Instant createdAt;

    private Instant updatedAt;



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Licensee {

        private String name;
        private String company;
        private String email;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Application {

        private String id;
        private String name;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Versions {

        private VersionInfo min;
        private VersionInfo max;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VersionInfo {

        private int code;
        private String name;

    }

    enum Status {
        PENDING,
        ACTIVE,
        EXPIRED,
        CANCELLED
    }

    enum ExpirationType {
        NEVER,
        DATE
    }


}
