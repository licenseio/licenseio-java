package io.license;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class OnlineLicenseValidator {

    @Builder.Default
    @NonNull
    private final String baseUrl = "https://api.license.io";

    @NonNull
    private final String appId;

}
