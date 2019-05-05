package bankaccount.service;

public class ServiceException extends RuntimeException {
    private ServiceExceptionCode code;

    public ServiceException(ServiceExceptionCode code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceExceptionCode getCode() {
        return code;
    }
}
