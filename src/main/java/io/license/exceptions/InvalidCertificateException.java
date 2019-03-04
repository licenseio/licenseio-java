package io.license.exceptions;

public class InvalidCertificateException extends RuntimeException {
    public InvalidCertificateException() {
    }

    public InvalidCertificateException(String message) {
        super(message);
    }

    public InvalidCertificateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCertificateException(Throwable cause) {
        super(cause);
    }
}
