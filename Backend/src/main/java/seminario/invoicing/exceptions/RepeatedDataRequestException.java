package seminario.invoicing.exceptions;

public class RepeatedDataRequestException extends RuntimeException {
    public RepeatedDataRequestException(String s) {
        super(s);
    }
}
