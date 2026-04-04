package app.shared.Exceptions;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceType, Object id) {
        super(resourceType + " not found with id: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}