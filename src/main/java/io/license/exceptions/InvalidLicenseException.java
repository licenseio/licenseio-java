package io.license.exceptions;

public class InvalidLicenseException extends Exception {

    public InvalidLicenseException() {
    }

    public InvalidLicenseException(String message) {
        super(message);
    }

    public InvalidLicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
