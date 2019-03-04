package io.license.crypto;

import lombok.Data;
import lombok.NonNull;

import java.nio.file.Path;
import java.security.Key;

@Data
public class Certificate {

    @NonNull
    private final String name;

    @NonNull
    private final Key publicKey;

    public Certificate(String name, String pemData) {
        this.name = name;
        this.publicKey = PEMUtils.parsePEM(pemData);
    }

    public Certificate(String name, Path pemFile) {
        this.name = name;
        this.publicKey = PEMUtils.parsePEM(pemFile);
    }

}
