package io.license.exceptions;

public class LicenseException extends Exception {

    public LicenseException() {
    }

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }

}
