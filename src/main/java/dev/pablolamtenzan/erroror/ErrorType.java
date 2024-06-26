package dev.pablolamtenzan.erroror;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the different types of errors that can occur.
 */
public class ErrorType {
    /**
     * Indicates a failure error.
     */
    public static final ErrorType FAILURE = new ErrorType(0, "FAILURE");

    /**
     * Indicates an unexpected error.
     */
    public static final ErrorType UNEXPECTED = new ErrorType(1, "UNEXPECTED");

    /**
     * Indicates a validation error.
     */
    public static final ErrorType VALIDATION = new ErrorType(2, "VALIDATION");

    /**
     * Indicates a conflict error.
     */
    public static final ErrorType CONFLICT = new ErrorType(3, "CONFLICT");

    /**
     * Indicates a not found error.
     */
    public static final ErrorType NOT_FOUND = new ErrorType(4, "NOT_FOUND");

    /**
     * Indicates an unauthorized error.
     */
    public static final ErrorType UNAUTHORIZED = new ErrorType(5, "UNAUTHORIZED");

    /**
     * Indicates a forbidden error.
     */
    public static final ErrorType FORBIDDEN = new ErrorType(6, "FORBIDDEN");

    private static final Map<Integer, ErrorType> DEFINED_TYPES = new ConcurrentHashMap<>();

    static {
        for (ErrorType type : new ErrorType[] {
            FAILURE,
            UNEXPECTED,
            VALIDATION,
            CONFLICT,
            NOT_FOUND,
            UNAUTHORIZED,
            FORBIDDEN
        }) {
            DEFINED_TYPES.put(type.ordinal, type);
        }
    }

    private final int ordinal;
    private final String name;

    private ErrorType(int ordinal, String name) {
        this.ordinal = ordinal;
        this.name = name;
    }

    /**
     * Returns the ordinal of the error type.
     *
     * @return The ordinal.
     */
    public int ordinal() {
        return ordinal;
    }

    /**
     * Returns the name of the error type.
     *
     * @return The name.
     */
    public String name() {
        return name;
    }

    /**
     * Returns an ErrorType for the given type. If the type is already defined,
     * it returns the existing ErrorType. Otherwise, it creates a new custom ErrorType.
     *
     * @param type The type as an integer.
     * @return The corresponding ErrorType.
     */
    public static ErrorType of(int type) {
        return DEFINED_TYPES.computeIfAbsent(type, k -> new ErrorType(k, "CUSTOM_" + k));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return ordinal;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ErrorType errorType = (ErrorType) obj;
        return ordinal == errorType.ordinal;
    }
}
