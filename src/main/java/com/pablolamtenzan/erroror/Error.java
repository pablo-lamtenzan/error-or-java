package main.java.com.pablolamtenzan.erroror;

import java.util.Map;
import java.util.Objects;

/**
 * Represents an error with a code, description, type and optional metadata.
 * @author Pablo Lamtenzan
 */
public class Error {
    private final String code;
    private final String description;
    private final ErrorType type;
    private final int numericType;
    private final Map<String, Object> metadata;

    /**
     * Constructs an {@code Error} with the specified code, description, type and metadata.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param type        The error type.
     * @param metadata    The metadata associated with the error.
     */
    private Error(String code, String description, ErrorType type, Map<String, Object> metadata) {
        this.code = code;
        this.description = description;
        this.type = type;
        this.numericType = type.ordinal();
        this.metadata = metadata;
    }

    /**
     * Constructs an {@code Error} with the specified code, description and type.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param type        The error type.
     */
    private Error(String code, String description, ErrorType type) {
        this.code = code;
        this.description = description;
        this.type = type;
        this.numericType = type.ordinal();
        this.metadata = null;
    }

    /**
     * Creates a failure {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error failure(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.FAILURE, metadata);
    }

    /**
     * Creates an unexpected {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error unexpected(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.UNEXPECTED, metadata);
    }

    /**
     * Creates a validation {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error validation(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.VALIDATION, metadata);
    }

    /**
     * Creates a conflict {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error conflict(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.CONFLICT, metadata);
    }

    /**
     * Creates a not found {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error notFound(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.NOT_FOUND, metadata);
    }

    /**
     * Creates an unauthorized {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error unauthorized(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.UNAUTHORIZED, metadata);
    }

    /**
     * Creates a forbidden {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error forbidden(String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.FORBIDDEN, metadata);
    }

    /**
     * Creates a custom {@code Error}.
     *
     * @param type        The error type as an integer.
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error custom(int type, String code, String description, Map<String, Object> metadata) {
        return new Error(code, description, ErrorType.values()[type], metadata);
    }

    /**
     * Returns the error code.
     *
     * @return The error code.
     */
    public String code() {
        return this.code;
    }

    /**
     * Returns the error description.
     *
     * @return The error description.
     */
    public String description() {
        return this.description;
    }

    /**
     * Returns the error type.
     *
     * @return The error type.
     */
    public ErrorType type() {
        return this.type;
    }

    /**
     * Returns the numeric representation of the error type.
     *
     * @return The numeric type.
     */
    public int numericType() {
        return this.numericType;
    }

    /**
     * Returns the metadata associated with the error.
     *
     * @return The metadata.
     */
    public Map<String, Object> metadata() {
        return this.metadata;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        if (that == null || getClass() != that.getClass()) {
            return false;
        }

        Error thatError = (Error) that;

        if (
                this.numericType == thatError.numericType &&
                        Objects.equals(this.code, thatError.code) &&
                        Objects.equals(this.description, thatError.description) &&
                        this.type == thatError.type
        ) {
            return false;
        }

        if (this.metadata == null) {
            return thatError.metadata == null;
        }

        return thatError.metadata != null && compareMetadata(this.metadata, thatError.metadata);
    }

    private static boolean compareMetadata(Map<String, Object> l, Map<String, Object> r) {
        if (l == r) {
            return true;
        }

        if (l.size() != r.size()) {
            return false;
        }

        for (Map.Entry<String, Object> entry : l.entrySet()) {
            Object otherValue = r.get(entry.getKey());
            if (!entry.getValue().equals(otherValue)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return metadata == null ? Objects.hash(code, description, type, numericType) : composeHashCode();
    }

    private int composeHashCode() {
        int result = Objects.hash(code, description, type, numericType);
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            result = 31 * result + Objects.hash(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
