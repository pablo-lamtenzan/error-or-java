package dev.pablolamtenzan.erroror;

import java.util.Map;
import java.util.Objects;

/**
 * Represents an error with a code, description, type and optional metadata.
 * @author Pablo Lamtenzan
 */
public class Error {
    private static final String GENERAL_FAILURE_CODE = "General.Failure";
    private static final String GENERAL_FAILURE_DESCRIPTION = "A failure has occurred.";
    private static final String GENERAL_UNEXPECTED_CODE = "General.Unexpected";
    private static final String GENERAL_UNEXPECTED_DESCRIPTION =
        "An unexpected error has occurred.";
    private static final String GENERAL_VALIDATION_CODE = "General.Validation";
    private static final String GENERAL_VALIDATION_DESCRIPTION = "A validation error has occurred.";
    private static final String GENERAL_CONFLICT_CODE = "General.Conflict";
    private static final String GENERAL_CONFLICT_DESCRIPTION = "A conflict error has occurred.";
    private static final String GENERAL_NOT_FOUND_CODE = "General.NotFound";
    private static final String GENERAL_NOT_FOUND_DESCRIPTION = "A 'Not Found' error has occurred.";
    private static final String GENERAL_UNAUTHORIZED_CODE = "General.Unauthorized";
    private static final String GENERAL_UNAUTHORIZED_DESCRIPTION =
        "An 'Unauthorized' error has occurred.";
    private static final String GENERAL_FORBIDDEN_CODE = "General.Forbidden";
    private static final String GENERAL_FORBIDDEN_DESCRIPTION = "A 'Forbidden' error has occurred.";

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
     * Creates a failure {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error failure(String code, String description) {
        return Error.failure(code, description, null);
    }

    /**
     * Creates a failure {@code Error}.
     *
     * @param code        The error code.
     * @return A new {@code Error} instance.
     */
    public static Error failure(String code) {
        return Error.failure(code, GENERAL_FAILURE_DESCRIPTION, null);
    }

    /**
     * Creates a failure {@code Error}.
     *
     * @return A new {@code Error} instance.
     */
    public static Error failure() {
        return Error.failure(GENERAL_FAILURE_CODE, GENERAL_FAILURE_DESCRIPTION, null);
    }

    /**
     * Creates a failure {@code Error}.
     *
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error failure(Map<String, Object> metadata) {
        return Error.failure(GENERAL_FAILURE_CODE, GENERAL_FAILURE_DESCRIPTION, metadata);
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
     * Creates an unexpected {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error unexpected(String code, String description) {
        return Error.unexpected(code, description, null);
    }

    /**
     * Creates an unexpected {@code Error}.
     *
     * @param code        The error code.
     * @return A new {@code Error} instance.
     */
    public static Error unexpected(String code) {
        return Error.unexpected(code, GENERAL_UNEXPECTED_DESCRIPTION, null);
    }

    /**
     * Creates an unexpected {@code Error}.
     *
     * @return A new {@code Error} instance.
     */
    public static Error unexpected() {
        return Error.unexpected(GENERAL_UNEXPECTED_CODE, GENERAL_UNEXPECTED_DESCRIPTION, null);
    }

    /**
     * Creates an unexpected {@code Error}.
     *
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error unexpected(Map<String, Object> metadata) {
        return Error.unexpected(GENERAL_UNEXPECTED_CODE, GENERAL_UNEXPECTED_DESCRIPTION, metadata);
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
     * Creates a validation {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error validation(String code, String description) {
        return Error.validation(code, description, null);
    }

    /**
     * Creates a validation {@code Error}.
     *
     * @param code        The error code.
     * @return A new {@code Error} instance.
     */
    public static Error validation(String code) {
        return Error.validation(code, GENERAL_VALIDATION_DESCRIPTION, null);
    }

    /**
     * Creates a validation {@code Error}.
     *
     * @return A new {@code Error} instance.
     */
    public static Error validation() {
        return Error.validation(GENERAL_VALIDATION_CODE, GENERAL_VALIDATION_DESCRIPTION, null);
    }

    /**
     * Creates a validation {@code Error}.
     *
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error validation(Map<String, Object> metadata) {
        return Error.validation(GENERAL_VALIDATION_CODE, GENERAL_VALIDATION_DESCRIPTION, metadata);
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
     * Creates a conflict {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error conflict(String code, String description) {
        return Error.conflict(code, description, null);
    }

    /**
     * Creates a conflict {@code Error}.
     *
     * @param code        The error code.
     * @return A new {@code Error} instance.
     */
    public static Error conflict(String code) {
        return Error.conflict(code, GENERAL_CONFLICT_DESCRIPTION, null);
    }

    /**
     * Creates a conflict {@code Error}.
     *
     * @return A new {@code Error} instance.
     */
    public static Error conflict() {
        return Error.conflict(GENERAL_CONFLICT_CODE, GENERAL_CONFLICT_DESCRIPTION, null);
    }

    /**
     * Creates a conflict {@code Error}.
     *
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error conflict(Map<String, Object> metadata) {
        return Error.conflict(GENERAL_CONFLICT_CODE, GENERAL_CONFLICT_DESCRIPTION, metadata);
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
     * Creates a not found {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error notFound(String code, String description) {
        return Error.notFound(code, description, null);
    }

    /**
     * Creates a not found {@code Error}.
     *
     * @param code        The error code.
     * @return A new {@code Error} instance.
     */
    public static Error notFound(String code) {
        return Error.notFound(code, GENERAL_NOT_FOUND_DESCRIPTION, null);
    }

    /**
     * Creates a not found {@code Error}.
     *
     * @return A new {@code Error} instance.
     */
    public static Error notFound() {
        return Error.notFound(GENERAL_NOT_FOUND_CODE, GENERAL_NOT_FOUND_DESCRIPTION, null);
    }

    /**
     * Creates a not found {@code Error}.
     *
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error notFound(Map<String, Object> metadata) {
        return Error.notFound(GENERAL_NOT_FOUND_CODE, GENERAL_NOT_FOUND_DESCRIPTION, metadata);
    }

    /**
     * Creates an unauthorized {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error unauthorized(
        String code,
        String description,
        Map<String, Object> metadata
    ) {
        return new Error(code, description, ErrorType.UNAUTHORIZED, metadata);
    }

    /**
     * Creates an unauthorized {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error unauthorized(String code, String description) {
        return Error.unauthorized(code, description, null);
    }

    /**
     * Creates an unauthorized {@code Error}.
     *
     * @param code        The error code.
     * @return A new {@code Error} instance.
     */
    public static Error unauthorized(String code) {
        return Error.unauthorized(code, GENERAL_UNAUTHORIZED_DESCRIPTION, null);
    }

    /**
     * Creates an unauthorized {@code Error}.
     *
     * @return A new {@code Error} instance.
     */
    public static Error unauthorized() {
        return Error.unauthorized(
            GENERAL_UNAUTHORIZED_CODE,
            GENERAL_UNAUTHORIZED_DESCRIPTION,
            null
        );
    }

    /**
     * Creates an unauthorized {@code Error}.
     *
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error unauthorized(Map<String, Object> metadata) {
        return Error.unauthorized(
            GENERAL_UNAUTHORIZED_CODE,
            GENERAL_UNAUTHORIZED_DESCRIPTION,
            metadata
        );
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
     * Creates a forbidden {@code Error}.
     *
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error forbidden(String code, String description) {
        return Error.forbidden(code, description, null);
    }

    /**
     * Creates a forbidden {@code Error}.
     *
     * @param code        The error code.
     * @return A new {@code Error} instance.
     */
    public static Error forbidden(String code) {
        return Error.forbidden(code, GENERAL_FORBIDDEN_DESCRIPTION, null);
    }

    /**
     * Creates a forbidden {@code Error}.
     *
     * @return A new {@code Error} instance.
     */
    public static Error forbidden() {
        return Error.forbidden(GENERAL_FORBIDDEN_CODE, GENERAL_FORBIDDEN_DESCRIPTION, null);
    }

    /**
     * Creates a forbidden {@code Error}.
     *
     * @param metadata    The metadata associated with the error.
     * @return A new {@code Error} instance.
     */
    public static Error forbidden(Map<String, Object> metadata) {
        return Error.forbidden(GENERAL_FORBIDDEN_CODE, GENERAL_FORBIDDEN_DESCRIPTION, metadata);
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
    public static Error custom(
        int type,
        String code,
        String description,
        Map<String, Object> metadata
    ) {
        return new Error(code, description, ErrorType.of(type), metadata);
    }

    /**
     * Creates a custom {@code Error}.
     *
     * @param type        The error type as an integer.
     * @param code        The error code.
     * @param description The error description.
     * @return A new {@code Error} instance.
     */
    public static Error custom(int type, String code, String description) {
        return Error.custom(type, description, null);
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
            this.numericType != thatError.numericType ||
            !Objects.equals(this.code, thatError.code) ||
            !Objects.equals(this.description, thatError.description) ||
            this.type != thatError.type
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
            if (!Objects.equals(entry.getValue(), otherValue)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return metadata == null
            ? Objects.hash(code, description, type, numericType)
            : composeHashCode();
    }

    private int composeHashCode() {
        int result = Objects.hash(code, description, type, numericType);
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            result = 31 * result + Objects.hash(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
