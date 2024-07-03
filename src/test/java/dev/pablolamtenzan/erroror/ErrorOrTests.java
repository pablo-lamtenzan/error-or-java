package dev.pablolamtenzan.erroror;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ErrorOrTests {

    public static class ErrorOrInstantiationTests {

        private record Person(String name) {}

        private enum Result {
            SUCCESS,
            CREATED,
            UPDATED,
            DELETED
        }

        @Test
        public void createFromFactory_WhenAccessingValue_ShouldReturnValue() {
            // Arrange
            List<String> value = List.of("value");

            // Act
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Assert
            assertThat(errorOrPerson.isError()).isFalse();
            assertThat(errorOrPerson.value()).isSameAs(value);
        }

        @Test
        public void createFromFactory_WhenAccessingErrors_ShouldThrow() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Act
            ThrowableAssert.ThrowingCallable errors = errorOrPerson::errors;

            // Assert
            assertThatThrownBy(errors).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void createFromFactory_WhenAccessingErrors_ShouldThrowUnsupportedOperationException() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Act
            ThrowableAssert.ThrowingCallable action = errorOrPerson::errors;

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("ErrorOr<T>.errors not callable in a value instance.");
        }

        @Test
        public void createFromFactory_WhenAccessingFirstError_ShouldThrow() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Act
            ThrowableAssert.ThrowingCallable action = errorOrPerson::firstError;

            // Assert
            assertThatThrownBy(action).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void createFromValue_WhenAccessingValue_ShouldReturnValue() {
            // Arrange
            List<String> value = List.of("value");

            // Act
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Assert
            assertThat(errorOrPerson.isError()).isFalse();
            assertThat(errorOrPerson.value()).isSameAs(value);
        }

        @Test
        public void createFromValue_WhenAccessingErrors_ShouldThrow() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Act
            ThrowableAssert.ThrowingCallable action = errorOrPerson::errors;

            // Assert
            assertThatThrownBy(action).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void createFromValue_WhenAccessingErrors_ShouldThrowUnsupportedOperationException() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Act
            ThrowableAssert.ThrowingCallable action = errorOrPerson::errors;

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("ErrorOr<T>.errors not callable in a value instance.");
        }

        @Test
        public void createFromValue_WhenAccessingFirstError_ShouldThrow() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> errorOrPerson = ErrorOr.of(value);

            // Act
            ThrowableAssert.ThrowingCallable action = errorOrPerson::firstError;

            // Assert
            assertThatThrownBy(action).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void createFromErrorList_WhenAccessingErrors_ShouldReturnErrorList() {
            // Arrange
            List<Error> errors = new ArrayList<>(
                List.of(Error.validation("User.Name", "Name is too short"))
            );
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Act & Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.errors()).containsExactlyElementsOf(errors);
        }

        @Test
        public void createFromErrorList_WhenAccessingErrorsOrEmptyList_ShouldReturnErrorList() {
            // Arrange
            List<Error> errors = new ArrayList<>(
                List.of(Error.validation("User.Name", "Name is too short"))
            );
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Act & Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.errors()).containsExactlyElementsOf(errors);
        }

        @Test
        public void createFromErrorList_WhenAccessingValue_ShouldThrowInvalidOperationException() {
            // Arrange
            List<Error> errors = new ArrayList<>(
                List.of(Error.validation("User.Name", "Name is too short"))
            );
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Act
            ThrowableAssert.ThrowingCallable act = errorOrPerson::value;

            // Assert
            assertThatThrownBy(act)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("ErrorOr<T>.value not callable in an error instance.");
        }

        @Test
        public void implicitCastResult_WhenAccessingResult_ShouldReturnValue() {
            // Arrange
            Person result = new Person("Amici");

            // Act
            ErrorOr<Person> errorOr = ErrorOr.of(result);

            // Assert
            assertThat(errorOr.isError()).isFalse();
            assertThat(errorOr.value()).isEqualTo(result);
        }

        @Test
        public void implicitCastResult_WhenAccessingErrors_ShouldThrow() {
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));

            // Act
            ThrowableAssert.ThrowingCallable action = errorOrPerson::errors;

            // Assert
            assertThatThrownBy(action).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void implicitCastResult_WhenAccessingFirstError_ShouldThrow() {
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));

            // Act
            ThrowableAssert.ThrowingCallable action = errorOrPerson::firstError;

            // Assert
            assertThatThrownBy(action).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void implicitCastPrimitiveResult_WhenAccessingResult_ShouldReturnValue() {
            // Arrange
            final int result = 4;

            // Act
            ErrorOr<Integer> errorOrInt = ErrorOr.of(result);

            // Assert
            assertThat(errorOrInt.isError()).isFalse();
            assertThat(errorOrInt.value()).isEqualTo(result);
        }

        @Test
        public void implicitCastErrorOrType_WhenAccessingResult_ShouldReturnValue() {
            // Act
            ErrorOr<Result> errorOrSuccess = ErrorOr.of(Result.SUCCESS);
            ErrorOr<Result> errorOrCreated = ErrorOr.of(Result.CREATED);
            ErrorOr<Result> errorOrDeleted = ErrorOr.of(Result.DELETED);
            ErrorOr<Result> errorOrUpdated = ErrorOr.of(Result.UPDATED);

            // Assert
            assertThat(errorOrSuccess.isError()).isFalse();
            assertThat(errorOrSuccess.value()).isEqualTo(Result.SUCCESS);

            assertThat(errorOrCreated.isError()).isFalse();
            assertThat(errorOrCreated.value()).isEqualTo(Result.CREATED);

            assertThat(errorOrDeleted.isError()).isFalse();
            assertThat(errorOrDeleted.value()).isEqualTo(Result.DELETED);

            assertThat(errorOrUpdated.isError()).isFalse();
            assertThat(errorOrUpdated.value()).isEqualTo(Result.UPDATED);
        }

        @Test
        public void implicitCastSingleError_WhenAccessingErrors_ShouldReturnErrorList() {
            // Arrange
            Error error = Error.validation("User.Name", "Name is too short");

            // Act
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(error);

            // Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.errors()).containsExactly(error);
        }

        @Test
        public void implicitCastError_WhenAccessingValue_ShouldThrowInvalidOperationException() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("User.Name", "Name is too short")
            );

            // Act
            ThrowableAssert.ThrowingCallable act = errorOrPerson::value;

            // Assert
            assertThatThrownBy(act)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("ErrorOr<T>.value not callable in an error instance.");
        }

        @Test
        public void implicitCastSingleError_WhenAccessingFirstError_ShouldReturnError() {
            // Arrange
            Error error = Error.validation("User.Name", "Name is too short");

            // Act
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(error);

            // Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.firstError()).isEqualTo(error);
        }

        @Test
        public void implicitCastErrorList_WhenAccessingErrors_ShouldReturnErrorList() {
            // Arrange
            List<Error> errors = Arrays.asList(
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            );

            // Act
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.errors()).hasSize(errors.size()).containsAll(errors);
        }

        @Test
        public void implicitCastErrorArray_WhenAccessingErrors_ShouldReturnErrorArray() {
            // Arrange
            Error[] errors = new Error[] {
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            };

            // Act
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.errors())
                .hasSize(errors.length)
                .containsAll(Arrays.asList(errors));
        }

        @Test
        public void implicitCastErrorList_WhenAccessingFirstError_ShouldReturnFirstError() {
            // Arrange
            List<Error> errors = Arrays.asList(
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            );

            // Act
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.firstError()).isEqualTo(errors.get(0));
        }

        @Test
        public void implicitCastErrorArray_WhenAccessingFirstError_ShouldReturnFirstError() {
            // Arrange
            Error[] errors = new Error[] {
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            };

            // Act
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Assert
            assertThat(errorOrPerson.isError()).isTrue();
            assertThat(errorOrPerson.firstError()).isEqualTo(errors[0]);
        }

        @Test
        public void createErrorOr_WhenEmptyErrorsList_ShouldThrow() {
            // Act
            ThrowableAssert.ThrowingCallable action = () -> ErrorOr.ofError(new ArrayList<>());

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                    "Cannot create error instance of ErrorOr<T> -> 'errors' is empty. Provide at least one error."
                );
        }

        @Test
        public void createErrorOr_WhenEmptyErrorsArray_ShouldThrow() {
            // Act
            ThrowableAssert.ThrowingCallable action = () -> ErrorOr.ofError(new Error[0]);

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                    "Cannot create error instance of ErrorOr<T> -> 'errors' is empty. Provide at least one error."
                );
        }

        @Test
        public void createErrorOr_WhenNullIsPassedAsErrorsList_ShouldThrowArgumentNullException() {
            ThrowableAssert.ThrowingCallable act = () -> ErrorOr.ofError((List<Error>) null);

            assertThatThrownBy(act)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot create error instance of ErrorOr<T> -> 'errors' is null.");
        }

        @Test
        public void createErrorOr_WhenNullIsPassedAsErrorsArray_ShouldThrowArgumentNullException() {
            ThrowableAssert.ThrowingCallable act = () -> ErrorOr.ofError((Error[]) null);

            assertThatThrownBy(act)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot create error instance of ErrorOr<T> -> 'errors' is null.");
        }

        @Test
        public void createErrorOr_WhenValueIsNull_ShouldThrowArgumentNullException() {
            ThrowableAssert.ThrowingCallable act = () -> ErrorOr.of((Integer) null);

            assertThatThrownBy(act)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot create value instance of ErrorOr<T> -> 'value' is null.");
        }

        @Test
        public void copyFromValueInstance_WhenAccessingValue_ShouldReturnValue() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> original = ErrorOr.of(value);

            // Act
            ErrorOr<List<String>> copy = ErrorOr.of(original);

            // Assert
            assertThat(copy.isError()).isFalse();
            assertThat(copy.value()).isSameAs(value);
        }

        @Test
        public void copyFromValueInstance_WhenAccessingErrors_ShouldThrow() {
            // Arrange
            List<String> value = List.of("value");
            ErrorOr<List<String>> original = ErrorOr.of(value);

            // Act
            ThrowableAssert.ThrowingCallable action = ErrorOr.of(original)::errors;

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("ErrorOr<T>.errors not callable in a value instance.");
        }

        @Test
        public void copyFromErrorInstance_WhenAccessingErrors_ShouldReturnErrors() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<Person> original = ErrorOr.ofError(errors);

            // Act
            ErrorOr<Person> copy = ErrorOr.ofError(original);

            // Assert
            assertThat(copy.isError()).isTrue();
            assertThat(copy.errors()).containsExactlyElementsOf(errors);
        }

        @Test
        public void copyFromErrorInstance_WhenAccessingValue_ShouldThrow() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<Person> original = ErrorOr.ofError(errors);

            // Act
            ThrowableAssert.ThrowingCallable action = ErrorOr.ofError(original)::value;

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("ErrorOr<T>.value not callable in an error instance.");
        }

        @Test
        public void copyFromErrorInstanceWithMultipleErrors_WhenAccessingFirstError_ShouldReturnFirstError() {
            // Arrange
            List<Error> errors = List.of(
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            );
            ErrorOr<Person> original = ErrorOr.ofError(errors);

            // Act
            ErrorOr<Person> copy = ErrorOr.ofError(original);

            // Assert
            assertThat(copy.isError()).isTrue();
            assertThat(copy.firstError()).isEqualTo(errors.get(0));
        }

        @Test
        public void copyFromDifferentGenericValueInstance_WhenAccessingValue_ShouldReturnValue() {
            // Arrange
            Result value = Result.SUCCESS;
            ErrorOr<Result> original = ErrorOr.of(value);

            // Act
            ErrorOr<Result> copy = ErrorOr.of(original);

            // Assert
            assertThat(copy.isError()).isFalse();
            assertThat(copy.value()).isEqualTo(value);
        }

        @Test
        public void copyFromDifferentGenericErrorInstance_WhenAccessingErrors_ShouldReturnErrors() {
            // Arrange
            List<Error> errors = List.of(
                Error.validation("Operation", "Failed due to unknown reasons")
            );
            ErrorOr<Result> original = ErrorOr.ofError(errors);

            // Act
            ErrorOr<Result> copy = ErrorOr.ofError(original);

            // Assert
            assertThat(copy.isError()).isTrue();
            assertThat(copy.errors()).containsExactlyElementsOf(errors);
        }
    }

    public static class ErrorOrEqualityTests {

        private record Person(String name) {}

        public static Stream<Arguments> differentErrors() {
            return Stream.of(
                Arguments.of(
                    new Error[] { Error.validation("User.Name", "Name is too short") },
                    new Error[] {
                        Error.validation("User.Name", "Name is too short"),
                        Error.validation("User.Age", "User is too young")
                    }
                ),
                Arguments.of(
                    new Error[] { Error.validation("User.Name", "Name is too short") },
                    new Error[] { Error.validation("User.Age", "User is too young") }
                )
            );
        }

        public static Stream<Arguments> names() {
            return Stream.of(Arguments.of("Amichai"), Arguments.of("feO2x"));
        }

        public static Stream<Arguments> differentNames() {
            return Stream.of(Arguments.of("Amichai", "feO2x"), Arguments.of("Tyrion", "Cersei"));
        }

        @Test
        public void equals_WhenTwoInstancesHaveTheSameErrorsCollection_ShouldReturnTrue() {
            List<Error> errors = Arrays.asList(
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            );
            ErrorOr<Person> errorOrPerson1 = ErrorOr.ofError(errors);
            ErrorOr<Person> errorOrPerson2 = ErrorOr.ofError(errors);

            boolean result = errorOrPerson1.equals(errorOrPerson2);

            assertThat(result).isTrue();
        }

        @Test
        public void equals_WhenTwoInstancesHaveDifferentErrorCollectionsWithSameErrors_ShouldReturnTrue() {
            Error[] errors1 = {
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            };
            Error[] errors2 = {
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            };
            ErrorOr<Person> errorOrPerson1 = ErrorOr.ofError(errors1);
            ErrorOr<Person> errorOrPerson2 = ErrorOr.ofError(errors2);

            boolean result = errorOrPerson1.equals(errorOrPerson2);

            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @MethodSource("differentErrors")
        public void equals_WhenTwoInstancesHaveDifferentErrors_ShouldReturnFalse(
            Error[] errors1,
            Error[] errors2
        ) {
            ErrorOr<Person> errorOrPerson1 = ErrorOr.ofError(errors1);
            ErrorOr<Person> errorOrPerson2 = ErrorOr.ofError(errors2);

            boolean result = errorOrPerson1.equals(errorOrPerson2);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @MethodSource("names")
        public void equals_WhenTwoInstancesHaveEqualValues_ShouldReturnTrue(String name) {
            ErrorOr<Person> errorOrPerson1 = ErrorOr.of(new Person(name));
            ErrorOr<Person> errorOrPerson2 = ErrorOr.of(new Person(name));

            boolean result = errorOrPerson1.equals(errorOrPerson2);

            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @MethodSource("differentNames")
        public void equals_WhenTwoInstancesHaveDifferentValues_ShouldReturnFalse(
            String name1,
            String name2
        ) {
            ErrorOr<Person> errorOrPerson1 = ErrorOr.of(new Person(name1));
            ErrorOr<Person> errorOrPerson2 = ErrorOr.of(new Person(name2));

            boolean result = errorOrPerson1.equals(errorOrPerson2);

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @MethodSource("names")
        public void getHashCode_WhenTwoInstancesHaveEqualValues_ShouldReturnSameHashCode(
            String name
        ) {
            ErrorOr<Person> errorOrPerson1 = ErrorOr.of(new Person(name));
            ErrorOr<Person> errorOrPerson2 = ErrorOr.of(new Person(name));

            int hashCode1 = errorOrPerson1.hashCode();
            int hashCode2 = errorOrPerson2.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @ParameterizedTest
        @MethodSource("differentNames")
        public void getHashCode_WhenTwoInstanceHaveDifferentValues_ShouldReturnDifferentHashCodes(
            String name1,
            String name2
        ) {
            ErrorOr<Person> errorOrPerson1 = ErrorOr.of(new Person(name1));
            ErrorOr<Person> errorOrPerson2 = ErrorOr.of(new Person(name2));

            int hashCode1 = errorOrPerson1.hashCode();
            int hashCode2 = errorOrPerson2.hashCode();

            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }

        @Test
        public void getHashCode_WhenTwoInstancesHaveEqualErrors_ShouldReturnSameHashCode() {
            Error[] errors1 = {
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            };
            Error[] errors2 = {
                Error.validation("User.Name", "Name is too short"),
                Error.validation("User.Age", "User is too young")
            };
            ErrorOr<Person> errorOrPerson1 = ErrorOr.ofError(errors1);
            ErrorOr<Person> errorOrPerson2 = ErrorOr.ofError(errors2);

            int hashCode1 = errorOrPerson1.hashCode();
            int hashCode2 = errorOrPerson2.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @ParameterizedTest
        @MethodSource("differentErrors")
        public void getHashCode_WhenTwoInstancesHaveDifferentErrors_ShouldReturnDifferentHashCodes(
            Error[] errors1,
            Error[] errors2
        ) {
            ErrorOr<Person> errorOrPerson1 = ErrorOr.ofError(errors1);
            ErrorOr<Person> errorOrPerson2 = ErrorOr.ofError(errors2);

            int hashCode1 = errorOrPerson1.hashCode();
            int hashCode2 = errorOrPerson2.hashCode();

            assertThat(hashCode1).isNotEqualTo(hashCode2);
        }
    }

    public static class MatchTests {

        private record Person(String name) {}

        @Test
        public void callingMatch_WhenIsSuccess_ShouldExecuteThenAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, String> ThenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return "Nice";
            };

            Function<List<Error>, String> ElsesAction = errors -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            String result = errorOrPerson.match(ElsesAction, ThenAction);

            // Assert
            assertThat(result).isEqualTo("Nice");
        }

        @Test
        public void callingMatch_WhenIsError_ShouldExecuteElseAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("VALIDATION_ERROR", "Validation error", null),
                Error.conflict("CONFLICT_ERROR", "Conflict error", null)
            );
            Function<Person, String> ThenAction = person -> {
                throw new RuntimeException("Should not be called");
            };

            Function<List<Error>, String> ElsesAction = errors -> {
                assertThat(errors).isEqualTo(errorOrPerson.errors());
                return "Nice";
            };

            // Act
            String result = errorOrPerson.match(ElsesAction, ThenAction);

            // Assert
            assertThat(result).isEqualTo("Nice");
        }

        @Test
        public void callingMatchFirst_WhenIsSuccess_ShouldExecuteThenAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, String> ThenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return "Nice";
            };

            Function<Error, String> OnFirstErrorAction = error -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            String result = errorOrPerson.matchFirst(OnFirstErrorAction, ThenAction);

            // Assert
            assertThat(result).isEqualTo("Nice");
        }

        @Test
        public void callingMatchFirst_WhenIsError_ShouldExecuteOnFirstErrorAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("VALIDATION_ERROR", "Validation error", null),
                Error.conflict("CONFLICT_ERROR", "Conflict error", null)
            );
            Function<Person, String> ThenAction = person -> {
                throw new RuntimeException("Should not be called");
            };

            Function<Error, String> OnFirstErrorAction = error -> {
                assertThat(error)
                    .isEqualTo(errorOrPerson.errors().get(0))
                    .isEqualTo(errorOrPerson.firstError());
                return "Nice";
            };

            // Act
            String result = errorOrPerson.matchFirst(OnFirstErrorAction, ThenAction);

            // Assert
            assertThat(result).isEqualTo("Nice");
        }

        @Test
        public void callingMatchFirstAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, String> ThenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return "Nice";
            };

            Function<Error, String> OnFirstErrorAction = error -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<String> action = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenCompose(
                    res ->
                        CompletableFuture.completedFuture(
                            res.matchFirst(OnFirstErrorAction, ThenAction)
                        )
                );

            // Assert
            assertThat(action.get()).isEqualTo("Nice");
        }

        @Test
        public void callingMatchAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, String> ThenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return "Nice";
            };

            Function<List<Error>, String> ElsesAction = errors -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<String> action = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenCompose(
                    res -> CompletableFuture.completedFuture(res.match(ElsesAction, ThenAction))
                );

            // Assert
            assertThat(action.get()).isEqualTo("Nice");
        }
    }

    public static class MatchAsyncTests {

        private record Person(String name) {}

        @Test
        public void callingMatchAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));

            // Act
            CompletableFuture<String> result = errorOrPerson.matchAsync(
                errors -> {
                    throw new RuntimeException("Should not be called");
                },
                person -> {
                    assertThat(person).isEqualTo(errorOrPerson.value());
                    return CompletableFuture.completedFuture("Nice");
                }
            );

            // Assert
            assertThat(result.get()).isEqualTo("Nice");
        }

        @Test
        public void callingMatchAsync_WhenIsError_ShouldExecuteElseAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                List.of(
                    Error.validation("VALIDATION_ERROR", "Validation error", null),
                    Error.conflict("CONFLICT_ERROR", "Conflict error", null)
                )
            );

            // Act
            CompletableFuture<String> result = errorOrPerson.matchAsync(
                errors -> {
                    assertThat(errors).isEqualTo(errorOrPerson.errors());
                    return CompletableFuture.completedFuture("Nice");
                },
                person -> {
                    throw new RuntimeException("Should not be called");
                }
            );

            // Assert
            assertThat(result.get()).isEqualTo("Nice");
        }

        @Test
        public void callingMatchFirstAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));

            // Act
            CompletableFuture<String> result = errorOrPerson.matchFirstAsync(
                error -> {
                    throw new RuntimeException("Should not be called");
                },
                person -> {
                    assertThat(person).isEqualTo(errorOrPerson.value());
                    return CompletableFuture.completedFuture("Nice");
                }
            );

            // Assert
            assertThat(result.get()).isEqualTo("Nice");
        }

        @Test
        public void callingMatchFirstAsync_WhenIsError_ShouldExecuteOnFirstErrorAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                List.of(
                    Error.validation("VALIDATION_ERROR", "Validation error", null),
                    Error.conflict("CONFLICT_ERROR", "Conflict error", null)
                )
            );

            // Act
            CompletableFuture<String> result = errorOrPerson.matchFirstAsync(
                error -> {
                    assertThat(error)
                        .isEqualTo(errorOrPerson.errors().get(0))
                        .isEqualTo(errorOrPerson.firstError());
                    return CompletableFuture.completedFuture("Nice");
                },
                person -> {
                    throw new RuntimeException("Should not be called");
                }
            );

            // Assert
            assertThat(result.get()).isEqualTo("Nice");
        }

        @Test
        public void callingMatchFirstAsyncAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));

            // Act
            CompletableFuture<String> result = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenCompose(
                    res ->
                        res.matchFirstAsync(
                            error -> {
                                throw new RuntimeException("Should not be called");
                            },
                            person -> {
                                assertThat(person).isEqualTo(errorOrPerson.value());
                                return CompletableFuture.completedFuture("Nice");
                            }
                        )
                );

            // Assert
            assertThat(result.get()).isEqualTo("Nice");
        }

        @Test
        public void callingMatchAsyncAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));

            // Act
            CompletableFuture<String> result = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenCompose(
                    res ->
                        res.matchAsync(
                            errors -> {
                                throw new RuntimeException("Should not be called");
                            },
                            person -> {
                                assertThat(person).isEqualTo(errorOrPerson.value());
                                return CompletableFuture.completedFuture("Nice");
                            }
                        )
                );

            // Assert
            assertThat(result.get()).isEqualTo("Nice");
        }
    }

    public static class ConsumeTests {

        private record Person(String name) {}

        @Test
        public void callingConsume_WhenIsSuccess_ShouldExecuteThenAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Consumer<Person> thenAction = person ->
                assertThat(person).isEqualTo(errorOrPerson.value());
            Consumer<List<Error>> elseAction = errors -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            Throwable throwable = Assertions.catchThrowable(
                () -> errorOrPerson.consume(elseAction, thenAction)
            );

            // Assert
            assertThat(throwable).doesNotThrowAnyException();
        }

        @Test
        public void callingConsume_WhenIsError_ShouldExecuteElseAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("VALIDATION_ERROR", "Validation error", null),
                Error.conflict("CONFLICT_ERROR", "Conflict error", null)
            );
            Consumer<Person> thenAction = person -> {
                throw new RuntimeException("Should not be called");
            };
            Consumer<List<Error>> elseAction = errors ->
                assertThat(errors).isEqualTo(errorOrPerson.errors());

            // Act
            Throwable throwable = Assertions.catchThrowable(
                () -> errorOrPerson.consume(elseAction, thenAction)
            );

            // Assert
            assertThat(throwable).doesNotThrowAnyException();
        }

        @Test
        public void callingConsumeFirst_WhenIsSuccess_ShouldExecuteThenAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Consumer<Person> thenAction = person ->
                assertThat(person).isEqualTo(errorOrPerson.value());
            Consumer<Error> onFirstErrorAction = error -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            Throwable throwable = Assertions.catchThrowable(
                () -> errorOrPerson.consumeFirst(onFirstErrorAction, thenAction)
            );

            // Assert
            assertThat(throwable).doesNotThrowAnyException();
        }

        @Test
        public void callingConsumeFirst_WhenIsError_ShouldExecuteOnFirstErrorAction() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("VALIDATION_ERROR", "Validation error", null),
                Error.conflict("CONFLICT_ERROR", "Conflict error", null)
            );
            Consumer<Person> thenAction = person -> {
                throw new RuntimeException("Should not be called");
            };
            Consumer<Error> onFirstErrorAction = error ->
                assertThat(error)
                    .isEqualTo(errorOrPerson.errors().get(0))
                    .isEqualTo(errorOrPerson.firstError());

            // Act
            Throwable throwable = Assertions.catchThrowable(
                () -> errorOrPerson.consumeFirst(onFirstErrorAction, thenAction)
            );

            // Assert
            assertThat(throwable).doesNotThrowAnyException();
        }

        @Test
        public void callingConsumeFirstAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Consumer<Person> thenAction = person ->
                assertThat(person).isEqualTo(errorOrPerson.value());
            Consumer<Error> onFirstErrorAction = error -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenAccept(r -> r.consumeFirst(onFirstErrorAction, thenAction));

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }

        @Test
        public void callingConsumeAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Consumer<Person> thenAction = person ->
                assertThat(person).isEqualTo(errorOrPerson.value());
            Consumer<List<Error>> elseAction = errors -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenAccept(r -> r.consume(elseAction, thenAction));

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }
    }

    public static class ConsumeAsyncTests {

        private record Person(String name) {}

        @Test
        public void callingConsumeAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, CompletableFuture<Void>> thenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return CompletableFuture.completedFuture(null);
            };
            Function<List<Error>, CompletableFuture<Void>> elseAction = errors -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson.consumeAsync(elseAction, thenAction);

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }

        @Test
        public void callingConsumeAsync_WhenIsError_ShouldExecuteElseAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                List.of(
                    Error.validation("VALIDATION_ERROR", "Validation error", null),
                    Error.conflict("CONFLICT_ERROR", "Conflict error", null)
                )
            );
            Function<Person, CompletableFuture<Void>> thenAction = person -> {
                throw new RuntimeException("Should not be called");
            };
            Function<List<Error>, CompletableFuture<Void>> elseAction = errors -> {
                assertThat(errors).isEqualTo(errorOrPerson.errors());
                return CompletableFuture.completedFuture(null);
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson.consumeAsync(elseAction, thenAction);

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }

        @Test
        public void callingConsumeFirstAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, CompletableFuture<Void>> thenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return CompletableFuture.completedFuture(null);
            };
            Function<Error, CompletableFuture<Void>> onFirstErrorAction = error -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson.consumeFirstAsync(
                onFirstErrorAction,
                thenAction
            );

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }

        @Test
        public void callingConsumeFirstAsync_WhenIsError_ShouldExecuteOnFirstErrorAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                List.of(
                    Error.validation("VALIDATION_ERROR", "Validation error", null),
                    Error.conflict("CONFLICT_ERROR", "Conflict error", null)
                )
            );
            Function<Person, CompletableFuture<Void>> thenAction = person -> {
                throw new RuntimeException("Should not be called");
            };
            Function<Error, CompletableFuture<Void>> onFirstErrorAction = error -> {
                assertThat(error)
                    .isEqualTo(errorOrPerson.errors().get(0))
                    .isEqualTo(errorOrPerson.firstError());
                return CompletableFuture.completedFuture(null);
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson.consumeFirstAsync(
                onFirstErrorAction,
                thenAction
            );

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }

        @Test
        public void callingConsumeFirstAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, CompletableFuture<Void>> thenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return CompletableFuture.completedFuture(null);
            };
            Function<Error, CompletableFuture<Void>> onFirstErrorAction = error -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenCompose(r -> r.consumeFirstAsync(onFirstErrorAction, thenAction));

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }

        @Test
        public void callingConsumeAfterThenAsync_WhenIsSuccess_ShouldExecuteThenAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            Function<Person, CompletableFuture<Void>> thenAction = person -> {
                assertThat(person).isEqualTo(errorOrPerson.value());
                return CompletableFuture.completedFuture(null);
            };
            Function<List<Error>, CompletableFuture<Void>> elseAction = errors -> {
                throw new RuntimeException("Should not be called");
            };

            // Act
            CompletableFuture<Void> action = errorOrPerson
                .mapAsync(person -> CompletableFuture.completedFuture(person))
                .thenCompose(r -> r.consumeAsync(elseAction, thenAction));

            // Assert
            action.get();
            assertThat(action).isCompleted();
        }
    }

    public static class GetOrTests {

        private record Person(String name) {}

        @Test
        public void getOr_WithFunction_WhenIsSuccess_ShouldReturnValue() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            String result = errorOrString.getOr(errors -> "Default Value");

            // Assert
            assertThat(result).isEqualTo("5");
        }

        @Test
        public void getOr_WithFunction_WhenIsError_ShouldReturnFunctionResult() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            String result = errorOrString.getOr(errs -> "Default Value");

            // Assert
            assertThat(result).isEqualTo("Default Value");
        }

        @Test
        public void getOr_WithFunction_WhenFunctionIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ThrowableAssert.ThrowingCallable action = () ->
                errorOrString.getOr((Function<List<Error>, String>) null);

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.getOr : onError is null");
        }

        @Test
        public void getOr_WithSupplier_WhenIsSuccess_ShouldReturnValue() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            String result = errorOrString.getOr(() -> "Default Value");

            // Assert
            assertThat(result).isEqualTo("5");
        }

        @Test
        public void getOr_WithSupplier_WhenIsError_ShouldReturnSupplierResult() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            String result = errorOrString.getOr(() -> "Default Value");

            // Assert
            assertThat(result).isEqualTo("Default Value");
        }

        @Test
        public void getOr_WithSupplier_WhenSupplierIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ThrowableAssert.ThrowingCallable action = () ->
                errorOrString.getOr((Supplier<String>) null);

            // Assert
            assertThatThrownBy(action)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.getOr : onError is null");
        }
    }

    public static class GetOrAsyncTests {

        private record Person(String name) {}

        @Test
        public void getOrAsync_WithFunction_WhenIsSuccess_ShouldReturnValue() throws Exception {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            CompletableFuture<String> futureResult = errorOrString.getOrAsync(
                errors -> CompletableFuture.completedFuture("Default Value")
            );
            String result = futureResult.get();

            // Assert
            assertThat(result).isEqualTo("5");
        }

        @Test
        public void getOrAsync_WithFunction_WhenIsError_ShouldReturnFunctionResult()
            throws Exception {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            CompletableFuture<String> futureResult = errorOrString.getOrAsync(
                errs -> CompletableFuture.completedFuture("Default Value")
            );
            String result = futureResult.get();

            // Assert
            assertThat(result).isEqualTo("Default Value");
        }

        @Test
        public void getOrAsync_WithFunction_WhenFunctionIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act & Assert
            assertThatThrownBy(
                    () ->
                        errorOrString.getOrAsync(
                            (Function<List<Error>, CompletableFuture<String>>) null
                        )
                )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.getOrAsync : onError is null");
        }

        @Test
        public void getOrAsync_WithSupplier_WhenIsSuccess_ShouldReturnValue() throws Exception {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            CompletableFuture<String> futureResult = errorOrString.getOrAsync(
                () -> CompletableFuture.completedFuture("Default Value")
            );
            String result = futureResult.get();

            // Assert
            assertThat(result).isEqualTo("5");
        }

        @Test
        public void getOrAsync_WithSupplier_WhenIsError_ShouldReturnSupplierResult()
            throws Exception {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            CompletableFuture<String> futureResult = errorOrString.getOrAsync(
                () -> CompletableFuture.completedFuture("Default Value")
            );
            String result = futureResult.get();

            // Assert
            assertThat(result).isEqualTo("Default Value");
        }

        @Test
        public void getOrAsync_WithSupplier_WhenSupplierIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act & Assert
            assertThatThrownBy(
                    () -> errorOrString.getOrAsync((Supplier<CompletableFuture<String>>) null)
                )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.getOrAsync : onError is null");
        }
    }

    public static class IfErrorTests {

        private record Person(String name) {}

        @Test
        public void ifError_WithConsumer_WhenIsError_ShouldExecuteConsumer() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);
            AtomicBoolean wasCalled = new AtomicBoolean(false);

            // Act
            errorOrPerson.ifError(errs -> wasCalled.set(true));

            // Assert
            assertThat(wasCalled.get()).isTrue();
        }

        @Test
        public void ifError_WithConsumer_WhenIsSuccess_ShouldNotExecuteConsumer() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            AtomicBoolean wasCalled = new AtomicBoolean(false);

            // Act
            errorOrPerson.ifError(errs -> wasCalled.set(true));

            // Assert
            assertThat(wasCalled.get()).isFalse();
        }

        @Test
        public void ifError_WithConsumer_WhenConsumerIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("User.Name", "Name is too short")
            );

            // Act & Assert
            assertThatThrownBy(() -> errorOrPerson.ifError((Consumer<List<Error>>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.ifError : action is null");
        }

        @Test
        public void ifError_WithSupplier_WhenIsError_ShouldExecuteSupplier() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("User.Name", "Name is too short")
            );
            AtomicBoolean wasCalled = new AtomicBoolean(false);

            // Act
            errorOrPerson.ifError(
                () -> {
                    wasCalled.set(true);
                    return null;
                }
            );

            // Assert
            assertThat(wasCalled.get()).isTrue();
        }

        @Test
        public void ifError_WithSupplier_WhenIsSuccess_ShouldNotExecuteSupplier() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.of(new Person("Amichai"));
            AtomicBoolean wasCalled = new AtomicBoolean(false);

            // Act
            errorOrPerson.ifError(
                () -> {
                    wasCalled.set(true);
                    return null;
                }
            );

            // Assert
            assertThat(wasCalled.get()).isFalse();
        }

        @Test
        public void ifError_WithSupplier_WhenSupplierIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("User.Name", "Name is too short")
            );

            // Act & Assert
            assertThatThrownBy(() -> errorOrPerson.ifError((Supplier<Void>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.ifError : action is null");
        }
    }

    public static class GetOrThrowTests {

        private record Person(String name) {}

        @Test
        public void getOrThrow_WithFunction_WhenIsError_ShouldThrowException() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Act & Assert
            assertThatThrownBy(
                    () ->
                        errorOrPerson.getOrThrow(
                            errs -> new IllegalArgumentException("Errors present")
                        )
                )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Errors present");
        }

        @Test
        public void getOrThrow_WithFunction_WhenIsSuccess_ShouldReturnValue() throws Throwable {
            // Arrange
            Person person = new Person("Amichai");
            ErrorOr<Person> errorOrPerson = ErrorOr.of(person);

            // Act
            Person result = errorOrPerson.getOrThrow(
                errs -> new IllegalArgumentException("Errors present")
            );

            // Assert
            assertThat(result).isEqualTo(person);
        }

        @Test
        public void getOrThrow_WithFunction_WhenFunctionIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("User.Name", "Name is too short")
            );

            // Act & Assert
            assertThatThrownBy(
                    () -> errorOrPerson.getOrThrow((Function<List<Error>, RuntimeException>) null)
                )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.getOrThrow : onError is null");
        }

        @Test
        public void getOrThrow_WithSupplier_WhenIsError_ShouldThrowException() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(errors);

            // Act & Assert
            assertThatThrownBy(
                    () ->
                        errorOrPerson.getOrThrow(
                            () -> new IllegalArgumentException("Errors present")
                        )
                )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Errors present");
        }

        @Test
        public void getOrThrow_WithSupplier_WhenIsSuccess_ShouldReturnValue() throws Throwable {
            // Arrange
            Person person = new Person("Amichai");
            ErrorOr<Person> errorOrPerson = ErrorOr.of(person);

            // Act
            Person result = errorOrPerson.getOrThrow(
                () -> new IllegalArgumentException("Errors present")
            );

            // Assert
            assertThat(result).isEqualTo(person);
        }

        @Test
        public void getOrThrow_WithSupplier_WhenSupplierIsNull_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<Person> errorOrPerson = ErrorOr.ofError(
                Error.validation("User.Name", "Name is too short")
            );

            // Act & Assert
            assertThatThrownBy(() -> errorOrPerson.getOrThrow((Supplier<? extends Throwable>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.getOrThrow : onError is null");
        }
    }

    public static class MapTests {

        @Test
        public void callingMap_WhenIsSuccess_ShouldInvokeGivenFunc() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(num -> num * 2)
                .map(TestUtils.ValueConvert::toString);

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("10");
        }

        @Test
        public void callingMap_WhenIsSuccess_ShouldInvokeGivenAction() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<Integer> result = errorOrString
                .map(str -> str)
                .map(TestUtils.ValueConvert::toInt)
                .map(i -> i);

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(5);
        }

        @Test
        public void callingMap_WhenIsError_ShouldReturnErrors() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(num -> num * 2)
                .map(i -> i)
                .map(TestUtils.ValueConvert::toString);

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError()).isEqualTo(errorOrString.firstError());
        }

        @Test
        public void callingMapAsync_WhenIsSuccess_ShouldInvokeGivenFunc()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(res -> CompletableFuture.completedFuture(res.map(num -> num * 2)))
                .thenCompose(res -> res.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    res -> CompletableFuture.completedFuture(res.map(TestUtils.ValueConvert::toInt))
                )
                .thenCompose(res -> res.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(res -> res.mapAsync(num -> CompletableFuture.supplyAsync(() -> num)))
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("10");
        }

        @Test
        public void callingMapAsync_WhenIsError_ShouldReturnErrors()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(
                    res ->
                        CompletableFuture.completedFuture(res.map(TestUtils.ValueConvert::toString))
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError()).isEqualTo(errorOrString.firstError());
        }
    }

    public static class MapAsyncTests {

        @Test
        public void callingMapAsync_WhenIsSuccess_ShouldInvokeNextThen()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(res -> res.mapAsync(num -> CompletableFuture.completedFuture(num * 2)))
                .thenCompose(
                    res ->
                        res.mapAsync(
                            num ->
                                CompletableFuture
                                    .runAsync(
                                        () -> {
                                            int ignored = 5;
                                        }
                                    )
                                    .thenApply(v -> num)
                        )
                )
                .thenCompose(res -> res.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("10");
        }

        @Test
        public void callingMapAsync_WhenIsError_ShouldReturnErrors()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(res -> res.mapAsync(num -> CompletableFuture.completedFuture(num * 2)))
                .thenCompose(
                    res ->
                        res.mapAsync(
                            num ->
                                CompletableFuture
                                    .runAsync(
                                        () -> {
                                            int ignored = 5;
                                        }
                                    )
                                    .thenApply(v -> num)
                        )
                )
                .thenCompose(res -> res.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError()).isEqualTo(errorOrString.firstError());
        }
    }

    public static class MapErrorTests {

        private record Person(String name) {}

        @Test
        public void callingMapError_WhenIsSuccess_ShouldReturnSameInstance() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString.mapError(
                error -> Error.validation("User.Age", "Invalid age")
            );

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
        }

        @Test
        public void callingMapError_WhenIsError_ShouldApplyErrorMapperFunction() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString.mapError(
                error -> Error.validation("User.Age", "Invalid age")
            );

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).hasSize(1);
            assertThat(result.firstError().type()).isEqualTo(ErrorType.VALIDATION);
            assertThat(result.firstError().code()).isEqualTo("User.Age");
            assertThat(result.firstError().description()).isEqualTo("Invalid age");
        }

        @Test
        public void callingMapError_WithNullMapper_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act & Assert
            assertThatThrownBy(() -> errorOrString.mapError(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.mapError : errorMapper is null");
        }

        @Test
        public void callingMapError_WithMultipleErrors_ShouldApplyErrorMapperFunctionToAllErrors() {
            // Arrange
            List<Error> errors = List.of(
                Error.notFound("NOT_FOUND", "Not found error", null),
                Error.validation("User.Name", "Name is too short")
            );
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            ErrorOr<String> result = errorOrString.mapError(
                error -> Error.validation("User.Age", "Invalid age")
            );

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).hasSize(2);
            assertThat(
                    result.errors().stream().allMatch(error -> error.type() == ErrorType.VALIDATION)
                )
                .isTrue();
            assertThat(result.errors().stream().allMatch(error -> error.code().equals("User.Age")))
                .isTrue();
            assertThat(
                    result
                        .errors()
                        .stream()
                        .allMatch(error -> error.description().equals("Invalid age"))
                )
                .isTrue();
        }
    }

    public static class MapErrorAsyncTests {

        private record Person(String name) {}

        @Test
        public void callingMapErrorAsync_WhenIsSuccess_ShouldReturnSameInstance()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapErrorAsync(
                    error ->
                        CompletableFuture.completedFuture(
                            Error.validation("User.Age", "Invalid age")
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
        }

        @Test
        public void callingMapErrorAsync_WhenIsError_ShouldApplyErrorMapperFunction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapErrorAsync(
                    error ->
                        CompletableFuture.completedFuture(
                            Error.validation("User.Age", "Invalid age")
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).hasSize(1);
            assertThat(result.firstError().type()).isEqualTo(ErrorType.VALIDATION);
            assertThat(result.firstError().code()).isEqualTo("User.Age");
            assertThat(result.firstError().description()).isEqualTo("Invalid age");
        }

        @Test
        public void callingMapErrorAsync_WithNullMapper_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act & Assert
            assertThatThrownBy(() -> errorOrString.mapErrorAsync(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.mapErrorAsync : errorMapper is null");
        }

        @Test
        public void callingMapErrorAsync_WithMultipleErrors_ShouldApplyErrorMapperFunctionToAllErrors()
            throws ExecutionException, InterruptedException {
            // Arrange
            List<Error> errors = List.of(
                Error.notFound("NOT_FOUND", "Not found error", null),
                Error.validation("User.Name", "Name is too short")
            );
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            ErrorOr<String> result = errorOrString
                .mapErrorAsync(
                    error ->
                        CompletableFuture.completedFuture(
                            Error.validation("User.Age", "Invalid age")
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).hasSize(2);
            assertThat(
                    result.errors().stream().allMatch(error -> error.type() == ErrorType.VALIDATION)
                )
                .isTrue();
            assertThat(result.errors().stream().allMatch(error -> error.code().equals("User.Age")))
                .isTrue();
            assertThat(
                    result
                        .errors()
                        .stream()
                        .allMatch(error -> error.description().equals("Invalid age"))
                )
                .isTrue();
        }
    }

    public static class OrTests {

        @Test
        public void callingOrWithValueFunc_WhenIsSuccess_ShouldNotInvokeOrFunc() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .or(errors -> "Error count: " + errors.size());

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrWithValueFunc_WhenIsError_ShouldInvokeOrFunc() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .or(errors -> "Error count: " + errors.size());

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("Error count: 1");
        }

        @Test
        public void callingOrWithValue_WhenIsSuccess_ShouldNotReturnOrValue() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .or(() -> "oh no");

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrWithValue_WhenIsError_ShouldInvokeOrFunc() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .or(() -> "oh no");

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("oh no");
        }

        @Test
        public void callingOrWithError_WhenIsError_ShouldReturnOrError() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .orError(() -> Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null));

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingOrWithError_WhenIsSuccess_ShouldNotReturnOrError() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .orError(() -> Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null));

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrWithErrorsFunc_WhenIsError_ShouldReturnOrError() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .orError(errors -> Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null));

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingOrWithErrorsFunc_WhenIsSuccess_ShouldNotReturnOrError() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .orError(errors -> Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null));

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrWithErrorsFunc_WhenIsError_ShouldReturnOrErrors() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .orErrors(
                    errors ->
                        List.of(Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null))
                );

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingOrWithErrorsFunc_WhenIsSuccess_ShouldNotReturnOrErrors() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .map(TestUtils.ValueConvert::toString)
                .orErrors(
                    errors ->
                        List.of(Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null))
                );

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingElseWithValueAfterThenAsync_WhenIsError_ShouldInvokeElseFunc()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .mapAsync(TestUtils.ValueConvert::toStringAsync)
                .thenCompose(r -> CompletableFuture.completedFuture(r.or(() -> "oh no")))
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("oh no");
        }

        @Test
        public void callingElseWithValueFuncAfterThenAsync_WhenIsError_ShouldInvokeElseFunc()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .mapAsync(TestUtils.ValueConvert::toStringAsync)
                .thenCompose(
                    r ->
                        CompletableFuture.completedFuture(
                            r.or(errors -> "Error count: " + errors.size())
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("Error count: 1");
        }

        @Test
        public void callingElseWithErrorAfterThenAsync_WhenIsError_ShouldReturnElseError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .mapAsync(TestUtils.ValueConvert::toStringAsync)
                .thenCompose(
                    r ->
                        CompletableFuture.completedFuture(
                            r.orError(
                                () -> Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null)
                            )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingElseWithErrorFuncAfterThenAsync_WhenIsError_ShouldReturnElseError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .mapAsync(TestUtils.ValueConvert::toStringAsync)
                .thenCompose(
                    r ->
                        CompletableFuture.completedFuture(
                            r.orError(
                                errors ->
                                    Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null)
                            )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingElseWithErrorFuncAfterThenAsync_WhenIsError_ShouldReturnElseErrors()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .map(TestUtils.ValueConvert::toInt)
                .mapAsync(TestUtils.ValueConvert::toStringAsync)
                .thenCompose(
                    r ->
                        CompletableFuture.completedFuture(
                            r.orErrors(
                                errors ->
                                    List.of(
                                        Error.unexpected(
                                            "UNEXPECTED_ERROR",
                                            "Unexpected error",
                                            null
                                        )
                                    )
                            )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }
    }

    public static class OrAsyncTests {

        @Test
        public void callingOrAsyncWithValueFunc_WhenIsSuccess_ShouldNotInvokeOrFunc()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 -> r2.orAsync(() -> CompletableFuture.completedFuture("Error count: 1"))
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrAsyncWithValueFunc_WhenIsError_ShouldInvokeOrFunc()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 ->
                        r2.orAsync(
                            errors ->
                                CompletableFuture.completedFuture("Error count: " + errors.size())
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("Error count: 1");
        }

        @Test
        public void callingOrAsyncWithValue_WhenIsSuccess_ShouldNotReturnOrValue()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(r2 -> r2.orAsync(() -> CompletableFuture.completedFuture("oh no")))
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrAsyncWithValue_WhenIsError_ShouldInvokeOrFunc()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(r2 -> r2.orAsync(() -> CompletableFuture.completedFuture("oh no")))
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("oh no");
        }

        @Test
        public void callingOrAsyncWithError_WhenIsError_ShouldReturnOrError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 ->
                        r2.orErrorAsync(
                            () ->
                                CompletableFuture.completedFuture(
                                    Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null)
                                )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingOrAsyncWithError_WhenIsSuccess_ShouldNotReturnOrError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 ->
                        r2.orErrorAsync(
                            () ->
                                CompletableFuture.completedFuture(
                                    Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null)
                                )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrAsyncWithErrorFunc_WhenIsError_ShouldReturnOrError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 ->
                        r2.orErrorAsync(
                            errors ->
                                CompletableFuture.completedFuture(
                                    Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null)
                                )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingOrAsyncWithErrorFunc_WhenIsSuccess_ShouldNotReturnOrError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 ->
                        r2.orErrorAsync(
                            errors ->
                                CompletableFuture.completedFuture(
                                    Error.unexpected("UNEXPECTED_ERROR", "Unexpected error", null)
                                )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }

        @Test
        public void callingOrAsyncWithErrorFunc_WhenIsError_ShouldReturnOrErrors()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 ->
                        r2.orErrorsAsync(
                            errors ->
                                CompletableFuture.completedFuture(
                                    List.of(
                                        Error.unexpected(
                                            "UNEXPECTED_ERROR",
                                            "Unexpected error",
                                            null
                                        )
                                    )
                                )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.UNEXPECTED);
        }

        @Test
        public void callingOrAsyncWithErrorFunc_WhenIsSuccess_ShouldNotReturnOrErrors()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString
                .mapAsync(TestUtils.ValueConvert::toIntAsync)
                .thenCompose(r1 -> r1.mapAsync(TestUtils.ValueConvert::toStringAsync))
                .thenCompose(
                    r2 ->
                        r2.orErrorsAsync(
                            errors ->
                                CompletableFuture.completedFuture(
                                    List.of(
                                        Error.unexpected(
                                            "UNEXPECTED_ERROR",
                                            "Unexpected error",
                                            null
                                        )
                                    )
                                )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(errorOrString.value());
        }
    }

    public static class OnValueTests {

        @Test
        public void callingOnValue_WhenIsSuccess_ShouldInvokeAction() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString.onValue(
                value -> {
                    // Assert inside action to ensure it gets called
                    assertThat(value).isEqualTo("5");
                }
            );

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
        }

        @Test
        public void callingOnValue_WhenIsError_ShouldNotInvokeAction() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString.onValue(
                value -> {
                    // This should not be called
                    throw new RuntimeException("This should not be called");
                }
            );

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).hasSize(1);
            assertThat(result.firstError().code()).isEqualTo("NOT_FOUND");
        }

        @Test
        public void callingOnValue_WithNullAction_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act & Assert
            assertThatThrownBy(() -> errorOrString.onValue(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.onValue : action is null");
        }

        @Test
        public void callingOnValue_WhenCompletingActionSuccessfully_ShouldReturnSameInstance() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString.onValue(
                value -> {
                    // Do nothing, just for testing the return value
                }
            );

            // Assert
            assertThat(result).isSameAs(errorOrString);
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
        }

        @Test
        public void callingOnValue_WhenCompletingActionWithException_ShouldReturnSameInstance() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            Throwable thrown = Assertions.catchThrowable(
                () -> {
                    errorOrString.onValue(
                        value -> {
                            throw new RuntimeException("Test Exception");
                        }
                    );
                }
            );

            // Assert
            assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessage("Test Exception");
            assertThat(errorOrString.isError()).isFalse();
            assertThat(errorOrString.value()).isEqualTo("5");
        }
    }

    public static class OnValueAsyncTests {

        @Test
        public void callingOnValueAsync_WhenIsSuccess_ShouldInvokeAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            CompletableFuture<String> future = new CompletableFuture<>();
            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString.onValueAsync(
                valueFuture -> {
                    valueFuture.thenAccept(value -> future.complete(value));
                }
            );

            ErrorOr<String> result = resultFuture.get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
            assertThat(future.get()).isEqualTo("5");
        }

        ///TODO: It blocks
        //        @Test
        //        public void callingOnValueAsync_WhenIsError_ShouldNotInvokeAction() throws ExecutionException, InterruptedException {
        //            // Arrange
        //            ErrorOr<String> errorOrString = ErrorOr.ofError(Error.notFound("NOT_FOUND", "Not found error", null));
        //
        //            // Act
        //            CompletableFuture<String> future = new CompletableFuture<>();
        //            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString.onValueAsync(valueFuture -> {
        //                // This block should not be executed
        //                valueFuture.thenAccept(value -> future.complete(value));
        //            });
        //
        //            ErrorOr<String> result = resultFuture.get();
        //
        //            // Assert
        //            assertThat(result.isError()).isTrue();
        //            assertThat(result.errors()).hasSize(1);
        //            assertThat(result.firstError().code()).isEqualTo("NOT_FOUND");
        //            assertThatThrownBy(future::get).isInstanceOf(ExecutionException.class)
        //                    .hasCauseInstanceOf(IllegalStateException.class)
        //                    .hasMessage("CompletableFuture[Incomplete]");
        //        }

        @Test
        public void callingOnValueAsync_WithNullAction_ShouldThrowNullPointerException() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act & Assert
            assertThatThrownBy(() -> errorOrString.onValueAsync(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.onValueAsync : action is null");
        }

        @Test
        public void callingOnValueAsync_WhenCompletingFutureSuccessfully_ShouldReturnSameInstance()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            CompletableFuture<String> future = new CompletableFuture<>();
            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString.onValueAsync(
                valueFuture -> {
                    valueFuture.thenAccept(future::complete);
                }
            );

            ErrorOr<String> result = resultFuture.get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
            assertThat(future.get()).isEqualTo("5");
        }

        @Test
        public void callingOnValueAsync_WhenCompletingFutureWithException_ShouldReturnSameInstance()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            CompletableFuture<String> future = new CompletableFuture<>();
            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString.onValueAsync(
                valueFuture -> {
                    valueFuture.thenAccept(
                        value ->
                            future.completeExceptionally(new RuntimeException("Test Exception"))
                    );
                }
            );

            ErrorOr<String> result = resultFuture.get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
            assertThatThrownBy(future::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(RuntimeException.class);
        }
    }

    public static class OnErrorTests {

        @Test
        public void callingOnError_WhenIsError_ShouldInvokeAction() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            ErrorOr<String> result = errorOrString.onError(
                errs -> {
                    // Assert inside action to ensure it gets called
                    assertThat(errs).isEqualTo(errors);
                }
            );

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).isEqualTo(errors);
        }

        @Test
        public void callingOnError_WhenIsSuccess_ShouldNotInvokeAction() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            ErrorOr<String> result = errorOrString.onError(
                errs -> {
                    // This should not be called
                    throw new RuntimeException("This should not be called");
                }
            );

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
        }

        @Test
        public void callingOnError_WithNullAction_ShouldThrowNullPointerException() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act & Assert
            assertThatThrownBy(() -> errorOrString.onError(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.onError : action is null");
        }

        @Test
        public void callingOnError_WhenCompletingActionSuccessfully_ShouldReturnSameInstance() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            ErrorOr<String> result = errorOrString.onError(
                errs -> {
                    // Do nothing, just for testing the return value
                }
            );

            // Assert
            assertThat(result).isSameAs(errorOrString);
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).isEqualTo(errors);
        }

        @Test
        public void callingOnError_WhenCompletingActionWithException_ShouldReturnSameInstance() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            Throwable thrown = Assertions.catchThrowable(
                () -> {
                    errorOrString.onError(
                        errs -> {
                            throw new RuntimeException("Test Exception");
                        }
                    );
                }
            );

            // Assert
            assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessage("Test Exception");
            assertThat(errorOrString.isError()).isTrue();
            assertThat(errorOrString.errors()).isEqualTo(errors);
        }
    }

    public static class OnErrorAsyncTests {

        @Test
        public void callingOnErrorAsync_WhenIsError_ShouldInvokeAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString.onErrorAsync(
                errsFuture -> {
                    // Assert inside action to ensure it gets called
                    assertThat(errsFuture.join()).isEqualTo(errors);
                }
            );

            // Wait for the future to complete and get the result
            ErrorOr<String> result = resultFuture.get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).isEqualTo(errors);
        }

        @Test
        public void callingOnErrorAsync_WhenIsSuccess_ShouldNotInvokeAction()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.of("5");

            // Act
            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString.onErrorAsync(
                errsFuture -> {
                    // This should not be called
                    throw new RuntimeException("This should not be called");
                }
            );

            // Wait for the future to complete and get the result
            ErrorOr<String> result = resultFuture.get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo("5");
        }

        @Test
        public void callingOnErrorAsync_WithNullAction_ShouldThrowNullPointerException() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act & Assert
            assertThatThrownBy(() -> errorOrString.onErrorAsync(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ErrorOr<T>.onErrorAsync : action is null");
        }

        @Test
        public void callingOnErrorAsync_WhenCompletingActionSuccessfully_ShouldReturnSameInstance()
            throws ExecutionException, InterruptedException {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString.onErrorAsync(
                errsFuture -> {
                    // Do nothing, just for testing the return value
                }
            );

            // Wait for the future to complete and get the result
            ErrorOr<String> result = resultFuture.get();

            // Assert
            assertThat(result).isSameAs(errorOrString);
            assertThat(result.isError()).isTrue();
            assertThat(result.errors()).isEqualTo(errors);
        }

        @Test
        public void callingOnErrorAsync_WhenCompletingActionWithException_ShouldReturnSameInstance() {
            // Arrange
            List<Error> errors = List.of(Error.validation("User.Name", "Name is too short"));
            ErrorOr<String> errorOrString = ErrorOr.ofError(errors);

            // Act
            CompletableFuture<ErrorOr<String>> resultFuture = errorOrString
                .onErrorAsync(
                    errsFuture -> {
                        errsFuture.thenAccept(
                            errs -> {
                                throw new RuntimeException("Test Exception");
                            }
                        );
                    }
                )
                .exceptionally(
                    ex -> {
                        throw new CompletionException(ex);
                    }
                );

            ///TODO: Issues propagating the exception from asynchronous tasks
            //            // Assert
            //            assertThatThrownBy(resultFuture::join)
            //                    .isInstanceOf(CompletionException.class)
            //                    .hasCauseInstanceOf(RuntimeException.class)
            //                    .hasMessageContaining("Test Exception");
            //
            //            // Validate the state of resultFuture
            //            assertThat(resultFuture).isCompletedExceptionally();

            // Assert the state of the original ErrorOr instance
            assertThat(errorOrString.isError()).isTrue();
            assertThat(errorOrString.errors()).isEqualTo(errors);
        }
    }

    public static class FailIfTests {

        @Test
        public void callingFailIf_WhenFailsIf_ShouldReturnError() {
            // Arrange
            ErrorOr<Integer> errorOrInt = ErrorOr.of(5);

            // Act
            ErrorOr<Integer> result = errorOrInt.failIf(
                num -> num > 3,
                Error.failure("FAILURE", "Failure error", null)
            );

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.FAILURE);
        }

        @Test
        public void callingFailIfExtensionMethod_WhenFailsIf_ShouldReturnError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Integer> errorOrInt = ErrorOr.of(5);

            // Act
            ErrorOr<Integer> result = errorOrInt
                .mapAsync(num -> CompletableFuture.completedFuture(num))
                .thenCompose(
                    res ->
                        CompletableFuture.completedFuture(
                            res.failIf(
                                num -> num > 3,
                                Error.failure("FAILURE", "Failure error", null)
                            )
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.FAILURE);
        }

        @Test
        public void callingFailIf_WhenDoesNotFailIf_ShouldReturnValue() {
            // Arrange
            ErrorOr<Integer> errorOrInt = ErrorOr.of(5);

            // Act
            ErrorOr<Integer> result = errorOrInt.failIf(
                num -> num > 10,
                Error.failure("FAILURE", "Failure error", null)
            );

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(5);
        }

        @Test
        public void callingFailIf_WhenIsError_ShouldNotInvokeFailIfFunc() {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString.failIf(
                str -> str.isEmpty(),
                Error.failure("FAILURE", "Failure error", null)
            );

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }

    public static class FailIfAsyncTests {

        @Test
        public void callingFailIfAsync_WhenFailsIf_ShouldReturnError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Integer> errorOrInt = ErrorOr.of(5);

            // Act
            ErrorOr<Integer> result = errorOrInt
                .failIfAsync(
                    num -> CompletableFuture.completedFuture(num > 3),
                    Error.failure("FAILURE", "Failure error", null)
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.FAILURE);
        }

        @Test
        public void callingFailIfAsyncExtensionMethod_WhenFailsIf_ShouldReturnError()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Integer> errorOrInt = ErrorOr.of(5);

            // Act
            ErrorOr<Integer> result = errorOrInt
                .mapAsync(num -> CompletableFuture.completedFuture(num))
                .thenCompose(
                    res ->
                        res.failIfAsync(
                            num -> CompletableFuture.completedFuture(num > 3),
                            Error.failure("FAILURE", "Failure error", null)
                        )
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.FAILURE);
        }

        @Test
        public void callingFailIfAsync_WhenDoesNotFailIf_ShouldReturnValue()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<Integer> errorOrInt = ErrorOr.of(5);

            // Act
            ErrorOr<Integer> result = errorOrInt
                .failIfAsync(
                    num -> CompletableFuture.completedFuture(num > 10),
                    Error.failure("FAILURE", "Failure error", null)
                )
                .get();

            // Assert
            assertThat(result.isError()).isFalse();
            assertThat(result.value()).isEqualTo(5);
        }

        @Test
        public void callingFailIf_WhenIsError_ShouldNotInvokeFailIfFunc()
            throws ExecutionException, InterruptedException {
            // Arrange
            ErrorOr<String> errorOrString = ErrorOr.ofError(
                Error.notFound("NOT_FOUND", "Not found error", null)
            );

            // Act
            ErrorOr<String> result = errorOrString
                .failIfAsync(
                    str -> CompletableFuture.completedFuture(str.isEmpty()),
                    Error.failure("FAILURE", "Failure error", null)
                )
                .get();

            // Assert
            assertThat(result.isError()).isTrue();
            assertThat(result.firstError().type()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
