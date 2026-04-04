package app.shared.Exceptions;

public class InvalidStateTransitionException extends BusinessException {

    public InvalidStateTransitionException(String aggregateType, Object currentState, Object attemptedOperation) {
        super("Cannot perform '" + attemptedOperation + "' on " + aggregateType
                + " in state: " + currentState);
    }

    public InvalidStateTransitionException(String message) {
        super(message);
    }
}