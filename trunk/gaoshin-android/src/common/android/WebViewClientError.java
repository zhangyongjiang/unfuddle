package common.android;

public enum WebViewClientError {
    ERROR_UNKNOWN(-1),
    ERROR_HOST_LOOKUP(-2),
    ERROR_UNSUPPORTED_AUTH_SCHEME(-3),
    ERROR_AUTHENTICATION(-4),
    ERROR_PROXY_AUTHENTICATION(-5),
    ERROR_CONNECT(-6),
    ERROR_IO(-7),
    ERROR_TIMEOUT(-8),
    ERROR_REDIRECT_LOOP(-9),
    ERROR_UNSUPPORTED_SCHEME(-10),
    ERROR_FAILED_SSL_HANDSHAKE(-11),
    ERROR_BAD_URL(-12),
    ERROR_FILE(-13),
    ERROR_FILE_NOT_FOUND(-14),
    ERROR_TOO_MANY_REQUESTS(-15), ;

    private int errorCode;

    private WebViewClientError(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static WebViewClientError getErrorByCode(int errorCode) {
        for (WebViewClientError e : WebViewClientError.class.getEnumConstants()) {
            if (e.getErrorCode() == errorCode)
                return e;
        }
        return null;
    }
}
