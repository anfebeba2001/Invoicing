package seminario.invoicing.exceptions;

public class InsufficientStockException extends Throwable {
    public InsufficientStockException(String s) {
        super(s);
    }
}
