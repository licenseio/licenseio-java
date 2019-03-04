package io.license;

public class OnlineLicenseValidator {

    private String baseUrl;

    private String appId;

    private OnlineLicenseValidator(String baseUrl, String appId) {
        this.baseUrl = baseUrl;
        this.appId = appId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAppId() {
        return appId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String baseUrl;
        private String appId;

        public OnlineLicenseValidator build() {
            return new OnlineLicenseValidator(baseUrl, appId);

        }

        public Builder withBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder withAppId(String appId) {
            this.appId = appId;
            return this;
        }
    }
}
