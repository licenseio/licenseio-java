This is the Java SDK for [License.io](https://license.io)

## Installation

TODO: link to gradle/maven when the sdk is published

## Usage

### Online validation by key

The simplest way to validate a license is to validate the short license key
against the API.

```java
OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
    .baseUrl("https://api.license.io")
    .appId("fd0bfc5e-03e1-4dae-806a-97c25c295482")
    .build();

try {
    License license = validator.validateByKey("abcd-efgh-ijkl-mn");

    System.out.printf("License id: %s\n", license.getId();
    System.out.printf("Licensed to: %s\n", license.getLicensee().getName());
    System.out.printf("License status: %s\n", license.getStatus().name());
} catch (InvalidLicenseException e) {
    // the license with the given key could not be found
} catch (NetworkUnavailableException e) {
    // the network is not available, impossible to use online validation
}

```

If succesful this prints the following output
```
License id: 70b3cc42-9289-46c2-9aa8-73fe0f08c1bb
Licensed to: Demo User
License status: ACTIVE
```

### Online validation by token

This is similar to online validation by key but instead of the short license key
you pass the longer signed license token into the validate method.

```java
OnlineLicenseValidator validator = OnlineLicenseValidator.builder()
    .baseUrl("https://api.license.io")
    .appId("fd0bfc5e-03e1-4dae-806a-97c25c295482")
    .build();

try {
    License license = validator.validateByToken("eyJraWQiOiJmaXJzdGtleSIsIng1dSI6Imh0dHBzOi8vZGV2LmxpY2Vuc2UuaW8vY2VydGlmaWNhdGVzLzNjY2YwZjFiLWRkM2YtNDhkOS05MTFhLWRkZjQ3OTA3OGMzNy9maXJzdGtleS5jcnQiLCJhbGciOiJSUzI1NiJ9.eyJzdGFydHNfYXQiOiIyMDE5LTAzLTA0VDEzOjEzOjEzLjU3NTMyN1oiLCJsaWNlbnNlZSI6eyJuYW1lIjoiU3RldmVuIFZhbiBCYWVsIiwiZW1haWwiOiJzdGV2ZW5AcXVhbnR1cy5pbyIsImNvbXBhbnkiOiJRdWFudHVzIEJWQkEifSwiY3JlYXRlZF9hdCI6IjIwMTktMDMtMDRUMTM6MTM6MTMuNTc1MzI3WiIsInZlcnNpb24iOnsibWluIjp7ImNvZGUiOjAsIm5hbWUiOiIxLjAuMCJ9LCJtYXgiOnsiY29kZSI6MTAwMCwibmFtZSI6IjIuMC4wIn19LCJsaWNlbnNlX2tleSI6ImRlbW8tbGljZW5zZS1rZXkiLCJleHBpcmF0aW9uX3R5cGUiOiJkYXRlIiwiZmVhdHVyZXMiOltdLCJleHBpcmVzX2F0IjoiMjAyNC0xMi0zMVQyMzowMDowMFoiLCJhcHBsaWNhdGlvbiI6eyJpZCI6IjNjY2YwZjFiLWRkM2YtNDhkOS05MTFhLWRkZjQ3OTA3OGMzNyIsIm5hbWUiOiJRdWFudHVzIFRhc2tzIn0sInVwZGF0ZWRfYXQiOiIyMDE5LTAzLTA0VDEzOjEzOjEzLjU3NTMyN1oiLCJuYW1lIjoic2ltcGxlIGxpY2Vuc2UiLCJsaW5rcyI6W10sImlkIjoiMjQxOWVmZjktODIxMi00YTA5LWJmMDAtYzY3Zjc4OWQwOWQ5IiwicGFyYW1ldGVycyI6e30sInN0YXR1cyI6ImFjdGl2ZSJ9.EPPAG1dMUzdq2S39JJY5aZmUeHjxrby9v2wVn_oiUKK8GGRXGm5oqKqNeKjMtlGJjLG69SuMx8EpKlSWtPCD9YhKR9OoEa_8pgDRFeQK9MMp9Jy-mS6CKwdFoEXrUGJeKUZjSxQKyM3BHDumxkpyGalFGPCccfJAeMO0ujwPMCt8-I_Gz3cm66lWvpf07OBI0iaN1H6CrLiYspo7U-FQiHriRzFpiw_S6vISbHLqg5_HVn3RGQqxnmRferZYs4Z_E4p1jEEZf0k3anv9711HipV6pXrqg5XSIBfLw1AWLE9f_1R-65TZxro4Jq5_pEpq2reHz8owAj306CzQa-ecgw");

    System.out.printf("License id: %s\n", license.getId();
    System.out.printf("Licensed to: %s\n", license.getLicensee().getName());
    System.out.printf("License status: %s\n", license.getStatus().name());

} catch (InvalidLicenseException e) {
    // the license with the given key could not be found
} catch (NetworkUnavailableException e) {
    // the network is not available, impossible to use online validation
}

```

If succesful this prints the following output
```
License id: 70b3cc42-9289-46c2-9aa8-73fe0f08c1bb
Licensed to: Demo User
License status: ACTIVE
```

### Offline validation by token

When your app needs to be able to validate licenses without a network connection
you can use the offline validation.

For this to work you need to specificy one or more public certificates to verify
the signed license token. (You can find these certificates in the dashboard)

```java
OfflineLicenseValidator validator = OfflineLicenseValidator.builder()
    .appId("fd0bfc5e-03e1-4dae-806a-97c25c295482")
    .withCertificate("certificatename", 
        "-----BEGIN CERTIFICATE-----"
        + "MIICrzCCAZegAwIBAgIJAJ4rNZtxGNA0MA0GCSqGSIb3DQEBCwUAMBcxFTATBgNV"
        + "BAMTDFF1YW50dXMgQlZCQTAeFw0xODAzMDUxNjAyMjlaFw0zODAyMjgxNjAyMjla"
        + "MBcxFTATBgNVBAMTDFF1YW50dXMgQlZCQTCCASIwDQYJKoZIhvcNAQEBBQADggEP"
        + "ADCCAQoCggEBAJk9x0N8wcklReGneQ6SyoSLXOm+8EBO9dmgEmSQtV81/Z2ZZzUl"
        + "k1fMufh4hIBwIKJev8Gklv9I82U5ZKVm6r67kv7Zbf8IlgNFUNeQrNP8nrGyxmfK"
        + "fYpW2Qxw6ym2i48EFCLx2JMVhxqLQ4GzD9RVjui+Ygvpi5YayJVnjjP+lBl8ZASH"
        + "y6SIiv/O7TkOPRq2xHehbgNQXr8CyDYEdu8WR0x07eMG8RErvdu9OjH/59xeQQGP"
        + "FDJnD9dCYEQkRmq7hyZOF0Cp6BP4be0dqRq8TZv8MvZA1HF7ull1NrCnTnMOq0TJ"
        + "BhPcq61Iu1OZlBne2su68wNzgFVUlrL3W2cCAwEAATANBgkqhkiG9w0BAQsFAAOC"
        + "AQEADVJS2qOhOiC4hQtAMcyAaJs6yKG5IfMKO2ElqCkP2Afe/zphyDI2lvQTtqLW"
        + "7N02m7SgX9zdAuDmMI9B7pzJpJ9W7wj4s0BIxTPyVX9GQ+zhUlL+GdFsRTZpXEsM"
        + "8rGuhydAApMzB9jgV1+iFw4KeXIwo++dVLYyMZQj6lkqKEnZy0kDj9uWIpXHRHzF"
        + "Hp4qwLXzJBdpJZpGE8umIXmcg2t0XlB7OlDjayTdwi01u+h4Qf35zWjY47+n+n2u"
        + "itAkhdtyiYu4fgICI9Hi8Zywo5iBTUlk0zMATYv6tiT7Rn4SGucqN5CeNspt3jE5"
        + "rtqqWG0WMKy/1lAYC6yGVcCS2A=="
        + "-----END CERTIFICATE-----")
    .build();

try {
    License license = validator.validateByToken("eyJraWQiOiJmaXJzdGtleSIsIng1dSI6Imh0dHBzOi8vZGV2LmxpY2Vuc2UuaW8vY2VydGlmaWNhdGVzLzNjY2YwZjFiLWRkM2YtNDhkOS05MTFhLWRkZjQ3OTA3OGMzNy9maXJzdGtleS5jcnQiLCJhbGciOiJSUzI1NiJ9.eyJzdGFydHNfYXQiOiIyMDE5LTAzLTA0VDEzOjEzOjEzLjU3NTMyN1oiLCJsaWNlbnNlZSI6eyJuYW1lIjoiU3RldmVuIFZhbiBCYWVsIiwiZW1haWwiOiJzdGV2ZW5AcXVhbnR1cy5pbyIsImNvbXBhbnkiOiJRdWFudHVzIEJWQkEifSwiY3JlYXRlZF9hdCI6IjIwMTktMDMtMDRUMTM6MTM6MTMuNTc1MzI3WiIsInZlcnNpb24iOnsibWluIjp7ImNvZGUiOjAsIm5hbWUiOiIxLjAuMCJ9LCJtYXgiOnsiY29kZSI6MTAwMCwibmFtZSI6IjIuMC4wIn19LCJsaWNlbnNlX2tleSI6ImRlbW8tbGljZW5zZS1rZXkiLCJleHBpcmF0aW9uX3R5cGUiOiJkYXRlIiwiZmVhdHVyZXMiOltdLCJleHBpcmVzX2F0IjoiMjAyNC0xMi0zMVQyMzowMDowMFoiLCJhcHBsaWNhdGlvbiI6eyJpZCI6IjNjY2YwZjFiLWRkM2YtNDhkOS05MTFhLWRkZjQ3OTA3OGMzNyIsIm5hbWUiOiJRdWFudHVzIFRhc2tzIn0sInVwZGF0ZWRfYXQiOiIyMDE5LTAzLTA0VDEzOjEzOjEzLjU3NTMyN1oiLCJuYW1lIjoic2ltcGxlIGxpY2Vuc2UiLCJsaW5rcyI6W10sImlkIjoiMjQxOWVmZjktODIxMi00YTA5LWJmMDAtYzY3Zjc4OWQwOWQ5IiwicGFyYW1ldGVycyI6e30sInN0YXR1cyI6ImFjdGl2ZSJ9.EPPAG1dMUzdq2S39JJY5aZmUeHjxrby9v2wVn_oiUKK8GGRXGm5oqKqNeKjMtlGJjLG69SuMx8EpKlSWtPCD9YhKR9OoEa_8pgDRFeQK9MMp9Jy-mS6CKwdFoEXrUGJeKUZjSxQKyM3BHDumxkpyGalFGPCccfJAeMO0ujwPMCt8-I_Gz3cm66lWvpf07OBI0iaN1H6CrLiYspo7U-FQiHriRzFpiw_S6vISbHLqg5_HVn3RGQqxnmRferZYs4Z_E4p1jEEZf0k3anv9711HipV6pXrqg5XSIBfLw1AWLE9f_1R-65TZxro4Jq5_pEpq2reHz8owAj306CzQa-ecgw");
} catch (InvalidLicenseException e) {
    // the license with the given key could not be found
}
```

If succesful this prints the following output
```
License id: 70b3cc42-9289-46c2-9aa8-73fe0f08c1bb
Licensed to: Demo User
License status: ACTIVE
```
