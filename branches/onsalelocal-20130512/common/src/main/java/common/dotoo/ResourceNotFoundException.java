package common.dotoo;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String file) {
        super("cannot find resource " + file);
    }
}
