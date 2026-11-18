package taller.velvetnails.exception;

public class ExistClienteByDocument extends RuntimeException {
    public ExistClienteByDocument(String message) {
        super(message);
    }
}
