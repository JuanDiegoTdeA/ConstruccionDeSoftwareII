package app.shared.Exceptions;


public class UnauthorizedOperationException extends BusinessException {

    public UnauthorizedOperationException(String message) {
        super(message);
    }

    public UnauthorizedOperationException(String userId, String operation) {
        super("User '" + userId + "' is not authorized to perform: " + operation);
    }
}
