package dev.pablolamtenzan.erroror;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ErrorTests {
    private static final String ERROR_CODE = "ErrorCode";
    private static final String ERROR_DESCRIPTION = "ErrorDescription";
    private static final Map<String, Object> METADATA = new HashMap<>(
        Map.of("key1", "value1", "key2", 21)
    );

    public static class ErrorCreationTests {

        @Test
        public void createError_WhenFailureError_ShouldHaveErrorTypeFailure() {
            Error error = Error.failure(ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.FAILURE);
        }

        @Test
        public void createError_WhenUnexpectedError_ShouldHaveErrorTypeUnexpected() {
            Error error = Error.unexpected(ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.UNEXPECTED);
        }

        @Test
        public void createError_WhenValidationError_ShouldHaveErrorTypeValidation() {
            Error error = Error.validation(ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.VALIDATION);
        }

        @Test
        public void createError_WhenConflictError_ShouldHaveErrorTypeConflict() {
            Error error = Error.conflict(ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.CONFLICT);
        }

        @Test
        public void createError_WhenNotFoundError_ShouldHaveErrorTypeNotFound() {
            Error error = Error.notFound(ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.NOT_FOUND);
        }

        @Test
        public void createError_WhenUnauthorizedError_ShouldHaveErrorTypeUnauthorized() {
            Error error = Error.unauthorized(ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.UNAUTHORIZED);
        }

        @Test
        public void createError_WhenForbiddenError_ShouldHaveErrorTypeForbidden() {
            Error error = Error.forbidden(ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.FORBIDDEN);
        }

        @Test
        public void createError_WhenCustomType_ShouldHaveCustomErrorType() {
            Error error = Error.custom(77, ERROR_CODE, ERROR_DESCRIPTION, METADATA);
            validateError(error, ErrorType.of(77));
        }

        private void validateError(Error error, ErrorType expectedErrorType) {
            assertThat(error.code()).isEqualTo(ERROR_CODE);
            assertThat(error.description()).isEqualTo(ERROR_DESCRIPTION);
            assertThat(error.type()).isEqualTo(expectedErrorType);
            assertThat(error.numericType()).isEqualTo(expectedErrorType.ordinal());
            assertThat(error.metadata()).isEqualTo(METADATA);
        }
    }

    public static class ErrorEqualityTests {

        private static Stream<Arguments> validData() {
            return Stream.of(
                Arguments.of("CodeA", "DescriptionA", 1, null),
                Arguments.of(
                    "CodeB",
                    "DescriptionB",
                    3215,
                    new HashMap<>(Map.of("foo", "bar", "baz", "qux"))
                )
            );
        }

        private static Stream<Arguments> differentInstances() {
            return Stream.of(
                Arguments.of(Error.failure(), Error.forbidden()),
                Arguments.of(Error.notFound(), Error.notFound(Map.of("Foo", "Bar"))),
                Arguments.of(Error.unexpected(Map.of("baz", "qux")), Error.unexpected()),
                Arguments.of(
                    Error.failure(Map.of("baz", "qux")),
                    Error.failure(Map.of("Foo", "Bar", "baz", "qux"))
                ),
                Arguments.of(
                    Error.failure(Map.of("baz", "qux")),
                    Error.failure(Map.of("baz", "gorge"))
                )
            );
        }

        @ParameterizedTest
        @MethodSource("validData")
        public void equals_WhenTwoInstancesHaveTheSameValues_ShouldReturnTrue(
            String code,
            String description,
            int numericType,
            Map<String, Object> metadata
        ) {
            Error error1 = Error.custom(numericType, code, description, metadata);
            Map<String, Object> clonedMETADATA = metadata == null ? null : new HashMap<>(metadata);
            Error error2 = Error.custom(numericType, code, description, clonedMETADATA);

            boolean result = error1.equals(error2);

            assertThat(result).isTrue();
        }

        @Test
        public void equals_WhenTwoInstancesHaveTheSameMetadataInstanceAndPropertyValues_ShouldReturnTrue() {
            Map<String, Object> metadata = new HashMap<>(Map.of("foo", "bar"));
            Error error1 = Error.custom(1, "Code", "Description", metadata);
            Error error2 = Error.custom(1, "Code", "Description", metadata);

            boolean result = error1.equals(error2);

            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @MethodSource("differentInstances")
        public void equals_WhenTwoInstancesHaveDifferentValues_ShouldReturnFalse(
            Error error1,
            Error error2
        ) {
            boolean result = error1.equals(error2);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @MethodSource("validData")
        public void hashCode_WhenTwoInstancesHaveSameValues_ShouldReturnSameHashCode(
            String code,
            String description,
            int numericType,
            Map<String, Object> metadata
        ) {
            Error error1 = Error.custom(numericType, code, description, metadata);
            Map<String, Object> clonedMETADATA = metadata == null ? null : new HashMap<>(metadata);
            Error error2 = Error.custom(numericType, code, description, clonedMETADATA);

            int hashCode1 = error1.hashCode();
            int hashCode2 = error2.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @ParameterizedTest
        @MethodSource("differentInstances")
        public void hashCode_WhenTwoInstancesHaveDifferentValues_ShouldReturnDifferentHashCodes(
            Error error1,
            Error error2
        ) {
            int hashCode1 = error1.hashCode();
            int hashCode2 = error2.hashCode();

            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }
    }
}
