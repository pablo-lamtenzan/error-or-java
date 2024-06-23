package main.java.com.pablolamtenzan.erroror;

/**
 * Represents the different types of errors that can occur.
 */
public enum ErrorType {
    /**
     * Indicates a failure error.
     */
    FAILURE,

    /**
     * Indicates an unexpected error.
     */
    UNEXPECTED,

    /**
     * Indicates a validation error.
     */
    VALIDATION,

    /**
     * Indicates a conflict error.
     */
    CONFLICT,

    /**
     * Indicates a not found error.
     */
    NOT_FOUND,

    /**
     * Indicates an unauthorized error.
     */
    UNAUTHORIZED,

    /**
     * Indicates a forbidden error.
     */
    FORBIDDEN
}
