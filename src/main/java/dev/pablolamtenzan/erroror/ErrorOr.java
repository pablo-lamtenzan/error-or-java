package dev.pablolamtenzan.erroror;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * ErrorOr represents a result that can either be a value or an {@link Error}.
 * <br>
 * <br>
 * <strong>Example usage:</strong>
 * <pre>
 * <code>
 *
 * // Division function that returns either a result or an error
 * public static ErrorOr&lt;Integer&gt; divide(int numerator, int denominator) {
 *     if (denominator == 0) {
 *         return ErrorOr.ofError(MathError.DIVISION_BY_ZERO);
 *     }
 *     return ErrorOr.of(numerator / denominator);
 * }
 *
 * // Using the divide function
 * ErrorOr&lt;Integer&gt; result = divide(10, 2);
 * result.consume(
 *     errors -> System.out.println("Errors: " + errors),
 *     value -> System.out.println("Value: " + value)
 * );
 *
 * public class MathError {
 *     public static final Error DIVISION_BY_ZERO = Error.failure(
 *        "MATH.DIVISION_BY_ZERO",
 *         "Division by zero is not allowed",
 *         null
 *     );
 * }
 * </code>
 * </pre>
 *
 * If the result is a value, it prints the value. If the result is an error, it prints the errors.
 *
 * @param <T> The type of the value in case of success.
 * @author Pablo Lamtenzan
 */
public interface ErrorOr<T> {
    /**
     * Constructs an instance representing a successful value.
     *
     * @param value The value.
     * @param <U>   The type of the value.
     * @return A new instance representing the value.
     */
    static <U> ErrorOr<U> of(U value) {
        return new Success<>(value);
    }

    /**
     * Constructs an instance representing a successful value by copying another instance.
     *
     * @param that The instance to copy.
     * @param <U>  The type of the value.
     * @return A new instance representing the value.
     * @throws UnsupportedOperationException if the given instance is an error.
     */
    static <U> ErrorOr<U> of(ErrorOr<U> that) {
        if (that.isError()) {
            throw new UnsupportedOperationException("ErrorOr.of requires a value instance.");
        }
        return ErrorOr.of(that.value());
    }

    /**
     * Constructs an instance representing multiple errors.
     *
     * @param errors The errors.
     * @param <U>    The type of the value.
     * @return A new instance representing the errors.
     * @see Error
     */
    static <U> ErrorOr<U> ofError(Error... errors) {
        return new Failure<>(errors);
    }

    /**
     * Constructs an instance representing multiple errors from an iterable.
     *
     * @param errors The iterable of errors.
     * @param <U>    The type of the value.
     * @return A new instance representing the errors.
     * @see Error
     */
    static <U> ErrorOr<U> ofError(Iterable<Error> errors) {
        return new Failure<>(errors);
    }

    /**
     * Constructs an instance representing multiple errors by copying another ErrorOr instance.
     *
     * @param that The instance to copy.
     * @param <U>  The type of the value.
     * @return A new instance representing the errors.
     * @throws UnsupportedOperationException if the given instance is not an error.
     */
    static <U> ErrorOr<U> ofError(ErrorOr<?> that) {
        if (!that.isError()) {
            throw new UnsupportedOperationException("ErrorOr.ofError requires an error instance.");
        }
        return ErrorOr.ofError(that.errors());
    }

    /**
     * Checks if the instance represents an error.
     *
     * @return {@code true} if it is an error, otherwise {@code false}.
     */
    boolean isError();

    /**
     * Returns the value if present.
     *
     * @return The value.
     * @throws UnsupportedOperationException if this is an error instance.
     */
    T value();

    /**
     * Returns the list of errors if present.
     *
     * @return The list of errors.
     * @throws UnsupportedOperationException if this is a value instance.
     */
    List<Error> errors();

    /**
     * Returns the first error if present.
     *
     * @return The first error.
     * @throws IllegalStateException if there are no errors.
     */
    Error firstError();

    /**
     * Applies one of two functions depending on whether this instance is a value or an {@link Error}.
     *
     * @param onError Function to apply if this instance is an {@link Error}.
     * @param onValue Function to apply if this instance is a value.
     * @param <R>     The return type of the functions.
     * @return The result of applying either the error function or the value function.
     * @see Error
     */
    default <R> R match(
        Function<List<Error>, ? extends R> onError,
        Function<? super T, ? extends R> onValue
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.match : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.match : onValue is null");

        if (!this.isError()) {
            return onValue.apply(this.value());
        }
        return onError.apply(this.errors());
    }

    /**
     * Asynchronously applies one of two functions depending on whether this instance is a value or an {@link Error}.
     *
     * @param onError Function to apply if this instance is an {@link Error}.
     * @param onValue Function to apply if this instance is a value.
     * @param <R>     The return type of the functions.
     * @return A CompletableFuture representing the result of applying either the error function or the value function.
     * @see Error
     */
    default <R> CompletableFuture<R> matchAsync(
        Function<List<Error>, ? extends CompletableFuture<R>> onError,
        Function<? super T, ? extends CompletableFuture<R>> onValue
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.matchAsync : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.matchAsync : onValue is null");

        if (this.isError()) {
            return onError.apply(this.errors());
        }
        return onValue.apply(this.value());
    }

    /**
     * Applies one of two functions depending on whether this instance is a value or an {@link Error}, using the first error if multiple errors are present.
     *
     * @param onError Function to apply if this instance is an {@link Error}.
     * @param onValue Function to apply if this instance is a value.
     * @param <R>     The return type of the functions.
     * @return The result of applying either the error function or the value function.
     * @see Error
     */
    default <R> R matchFirst(
        Function<Error, ? extends R> onError,
        Function<? super T, ? extends R> onValue
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.matchFirst : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.matchFirst : onValue is null");

        if (!this.isError()) {
            return onValue.apply(this.value());
        }
        return onError.apply(this.firstError());
    }

    /**
     * Asynchronously applies one of two functions depending on whether this instance is a value or an {@link Error}, using the first error if multiple errors are present.
     *
     * @param onError Function to apply if this instance is an {@link Error}.
     * @param onValue Function to apply if this instance is a value.
     * @param <R>     The return type of the functions.
     * @return A CompletableFuture representing the result of applying either the error function or the value function.
     * @see Error
     */
    default <R> CompletableFuture<R> matchFirstAsync(
        Function<Error, ? extends CompletableFuture<R>> onError,
        Function<? super T, ? extends CompletableFuture<R>> onValue
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.matchFirstAsync : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.matchFirstAsync : onValue is null");

        if (!this.isError()) {
            return onValue.apply(this.value());
        } else {
            return onError.apply(this.firstError());
        }
    }

    /**
     * Consumes the value or errors, depending on the state of this instance.
     *
     * @param onError Consumer to accept the {@link Error} list if this instance is an error.
     * @param onValue Consumer to accept the value if this instance is a value.
     */
    default void consume(Consumer<List<Error>> onError, Consumer<? super T> onValue) {
        Objects.requireNonNull(onError, "ErrorOr<T>.consume : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.consume : onValue is null");

        if (!this.isError()) {
            onValue.accept(this.value());
        } else {
            onError.accept(this.errors());
        }
    }

    /**
     * Asynchronously consumes the value or errors, depending on the state of this instance.
     *
     * @param onError Consumer to accept the {@link Error} list if this instance is an error.
     * @param onValue Consumer to accept the value if this instance is a value.
     * @return A CompletableFuture that will complete when the operation is done.
     */
    default CompletableFuture<Void> consumeAsync(
        Function<List<Error>, CompletableFuture<Void>> onError,
        Function<? super T, CompletableFuture<Void>> onValue
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.consumeAsync : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.consumeAsync : onValue is null");

        return this.isError() ? onError.apply(this.errors()) : onValue.apply(this.value());
    }

    /**
     * Consumes the value or the first error, depending on the state of this instance.
     *
     * @param onError Consumer to accept the first {@link Error} if this instance is an error.
     * @param onValue Consumer to accept the value if this instance is a value.
     */
    default void consumeFirst(Consumer<Error> onError, Consumer<? super T> onValue) {
        Objects.requireNonNull(onError, "ErrorOr<T>.consumeFirst : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.consumeFirst : onValue is null");

        if (!this.isError()) {
            onValue.accept(this.value());
        } else {
            onError.accept(this.firstError());
        }
    }

    /**
     * Asynchronously consumes the value or the first error, depending on the state of this instance.
     *
     * @param onError Consumer to accept the first {@link Error} if this instance is an error.
     * @param onValue Consumer to accept the value if this instance is a value.
     * @return A CompletableFuture that will complete when the operation is done.
     */
    default CompletableFuture<Void> consumeFirstAsync(
        Function<Error, CompletableFuture<Void>> onError,
        Function<? super T, CompletableFuture<Void>> onValue
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.consumeFirstAsync : onError is null");
        Objects.requireNonNull(onValue, "ErrorOr<T>.consumeFirstAsync : onValue is null");

        if (!this.isError()) {
            return onValue.apply(this.value());
        } else {
            return onError.apply(this.firstError());
        }
    }

    /**
     * Returns the value if present, otherwise applies the provided function to the errors and returns the result.
     *
     * @param onError Function to apply if this instance is an {@link Error}.
     * @return The value if present, otherwise the result of the onError function.
     * @throws NullPointerException if the onError function is null.
     */
    default T getOr(Function<List<Error>, ? extends T> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.getOr : onError is null");

        if (!isError()) {
            return this.value();
        }
        return onError.apply(this.errors());
    }

    /**
     * Returns the value if present, otherwise applies the provided supplier to the errors and returns the result.
     *
     * @param onError Supplier to apply if this instance is an {@link Error}.
     * @return The value if present, otherwise the result of the onError supplier.
     * @throws NullPointerException if the onError supplier is null.
     */
    default T getOr(Supplier<? extends T> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.getOr : onError is null");

        if (!isError()) {
            return this.value();
        }
        return onError.get();
    }

    /**
     * Asynchronously returns the value if present, otherwise applies the provided function to the errors and returns the result.
     *
     * @param onError Function to apply if this instance is an {@link Error}.
     * @return A CompletableFuture containing the value if present, otherwise the result of the onError function.
     * @throws NullPointerException if the onError function is null.
     */
    default CompletableFuture<T> getOrAsync(
        Function<List<Error>, ? extends CompletableFuture<T>> onError
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.getOrAsync : onError is null");

        if (!isError()) {
            return CompletableFuture.completedFuture(this.value());
        }
        return onError.apply(this.errors());
    }

    /**
     * Asynchronously returns the value if present, otherwise applies the provided supplier to the errors and returns the result.
     *
     * @param onError Supplier to apply if this instance is an {@link Error}.
     * @return A CompletableFuture containing the value if present, otherwise the result of the onError supplier.
     * @throws NullPointerException if the onError supplier is null.
     */
    default CompletableFuture<T> getOrAsync(Supplier<? extends CompletableFuture<T>> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.getOrAsync : onError is null");

        if (!isError()) {
            return CompletableFuture.completedFuture(this.value());
        }
        return onError.get();
    }

    /**
     * Performs the given action if this instance is an {@link Error}.
     *
     * @param onError Consumer to accept the list of errors if this instance is an {@link Error}.
     * @throws NullPointerException if the onError consumer is null.
     */
    default void ifError(Consumer<List<Error>> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.ifError : action is null");

        if (isError()) {
            onError.accept(this.errors());
        }
    }

    /**
     * Performs the given action if this instance is an {@link Error}.
     *
     * @param onError Supplier to execute if this instance is an {@link Error}.
     * @throws NullPointerException if the onError supplier is null.
     */
    default void ifError(Supplier<Void> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.ifError : action is null");

        if (isError()) {
            onError.get();
        }
    }

    /**
     * Returns the value if present, otherwise applies the provided function to the errors and throws the resulting exception.
     *
     * @param onError Function to apply to the list of errors to produce an exception.
     * @param <E>     The type of exception to be thrown.
     * @return The value if present.
     * @throws E if this instance is an {@link Error}.
     * @throws NullPointerException if the onError function is null.
     */
    default <E extends Throwable> T getOrThrow(Function<List<Error>, E> onError) throws E {
        Objects.requireNonNull(onError, "ErrorOr<T>.getOrThrow : onError is null");

        if (!isError()) {
            return this.value();
        }
        throw onError.apply(this.errors());
    }

    /**
     * Returns the value if present, otherwise applies the provided supplier to the errors and throws the resulting exception.
     *
     * @param onError Supplier to apply to the list of errors to produce an exception.
     * @param <E>     The type of exception to be thrown.
     * @return The value if present.
     * @throws E if this instance is an {@link Error}.
     * @throws NullPointerException if the onError supplier is null.
     */
    default <E extends Throwable> T getOrThrow(Supplier<E> onError) throws E {
        Objects.requireNonNull(onError, "ErrorOr<T>.getOrThrow : onError is null");

        if (!isError()) {
            return this.value();
        }
        throw onError.get();
    }

    /**
     * Applies the given function to the value if present, otherwise returns the current {@link ErrorOr} instance.
     *
     * @param valueMapper Function to apply to the value.
     * @param <R>         The type of the result of the mapping function.
     * @return A new {@link ErrorOr} instance with the mapped value, or the current instance if it represents an {@link Error}.
     * @throws NullPointerException if the valueMapper function is null.
     */
    default <R> ErrorOr<R> map(Function<? super T, ? extends R> valueMapper) {
        Objects.requireNonNull(valueMapper, "ErrorOr<T>.map : valueMapper is null");

        if (!isError()) {
            return ErrorOr.of(valueMapper.apply(this.value()));
        }
        return (ErrorOr<R>) this;
    }

    /**
     * Asynchronously applies the given function to the value if present, otherwise returns the current {@link ErrorOr} instance.
     *
     * @param valueMapper Function to apply to the value.
     * @param <R>         The type of the result of the mapping function.
     * @return A CompletableFuture containing a new {@link ErrorOr} instance with the mapped value, or the current instance if it represents an {@link Error}.
     * @throws NullPointerException if the valueMapper function is null.
     */
    default <R> CompletableFuture<ErrorOr<R>> mapAsync(
        Function<? super T, ? extends CompletableFuture<R>> valueMapper
    ) {
        Objects.requireNonNull(valueMapper, "ErrorOr<T>.mapAsync : valueMapper is null");

        if (!isError()) {
            return valueMapper.apply(this.value()).thenApply(ErrorOr::of);
        }
        return CompletableFuture.completedFuture((ErrorOr<R>) this);
    }

    /**
     * Applies the given function to each {@link Error} if present, otherwise returns the current {@link ErrorOr} instance.
     *
     * @param errorMapper Function to apply to each error.
     * @return A new {@link ErrorOr} instance with the mapped errors, or the current instance if it represents a value.
     * @throws NullPointerException if the errorMapper function is null.
     */
    default ErrorOr<T> mapError(Function<Error, Error> errorMapper) {
        Objects.requireNonNull(errorMapper, "ErrorOr<T>.mapError : errorMapper is null");

        if (isError()) {
            return ErrorOr.ofError(
                this.errors().stream().map(errorMapper).collect(Collectors.toList())
            );
        }
        return this;
    }

    /**
     * Asynchronously applies the given function to each {@link Error} if present, otherwise returns the current {@link ErrorOr} instance.
     *
     * @param errorMapper Function to apply to each error.
     * @return A CompletableFuture containing a new {@link ErrorOr} instance with the mapped errors, or the current instance if it represents a value.
     * @throws NullPointerException if the errorMapper function is null.
     */
    default CompletableFuture<ErrorOr<T>> mapErrorAsync(
        Function<Error, CompletableFuture<Error>> errorMapper
    ) {
        Objects.requireNonNull(errorMapper, "ErrorOr<T>.mapErrorAsync : errorMapper is null");

        if (isError()) {
            List<CompletableFuture<Error>> mappedErrorsFutures =
                this.errors().stream().map(errorMapper).collect(Collectors.toList());

            return CompletableFuture
                .allOf(mappedErrorsFutures.toArray(new CompletableFuture[0]))
                .thenApply(
                    v ->
                        mappedErrorsFutures
                            .stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList())
                )
                .thenApply(ErrorOr::ofError);
        }
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Returns this instance if it is not an {@link Error}, otherwise returns the provided alternative.
     *
     * @param other The alternative {@link ErrorOr} to return if this instance is an {@link Error}.
     * @return This instance if it is not an {@link Error}, otherwise the provided alternative.
     * @throws NullPointerException if the other {@link ErrorOr} is null.
     */
    default ErrorOr<T> or(ErrorOr<? extends T> other) {
        Objects.requireNonNull(other, "ErrorOr<T>.or : other is null");

        return !this.isError() ? this : (ErrorOr<T>) other;
    }

    /**
     * Returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the function.
     *
     * @param onError The function to apply to the errors to return an alternative value if this instance is an {@link Error}.
     * @return This instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the function.
     * @throws NullPointerException if the function is null.
     */
    default ErrorOr<T> or(Function<List<Error>, ? extends T> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.or : onError is null");

        return !this.isError() ? this : ErrorOr.of(onError.apply(this.errors()));
    }

    /**
     * Returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the supplier.
     *
     * @param onError The supplier to provide an alternative value if this instance is an {@link Error}.
     * @return This instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the supplier.
     * @throws NullPointerException if the supplier is null.
     */
    default ErrorOr<T> or(Supplier<? extends T> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.or : onError is null");

        return !this.isError() ? this : ErrorOr.of(onError.get());
    }

    /**
     * Returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the function.
     *
     * @param onError The function to apply to the errors to return an alternative error if this instance is an {@link Error}.
     * @return This instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the function.
     * @throws NullPointerException if the function is null.
     */
    default ErrorOr<T> orError(Function<List<Error>, Error> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orError : onError is null");

        return !this.isError() ? this : ErrorOr.ofError(onError.apply(this.errors()));
    }

    /**
     * Returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the supplier.
     *
     * @param onError The supplier to provide an alternative error if this instance is an {@link Error}.
     * @return This instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the supplier.
     * @throws NullPointerException if the supplier is null.
     */
    default ErrorOr<T> orError(Supplier<Error> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orError : onError is null");

        return !this.isError() ? this : ErrorOr.ofError(onError.get());
    }

    /**
     * Returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the function.
     *
     * @param onError The function to apply to the errors to return an alternative iterable of errors if this instance is an {@link Error}.
     * @return This instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the function.
     * @throws NullPointerException if the function is null.
     */
    default ErrorOr<T> orErrors(Function<List<Error>, Iterable<Error>> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orErrors : onError is null");

        return !this.isError() ? this : ErrorOr.ofError(onError.apply(this.errors()));
    }

    /**
     * Returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the supplier.
     *
     * @param onError The supplier to provide an alternative iterable of errors if this instance is an {@link Error}.
     * @return This instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the supplier.
     * @throws NullPointerException if the supplier is null.
     */
    default ErrorOr<T> orErrors(Supplier<Iterable<Error>> onError) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orErrors : onError is null");

        return !this.isError() ? this : ErrorOr.ofError(onError.get());
    }

    /**
     * Asynchronously returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the function.
     *
     * @param onError The function to apply to the errors to return an alternative value if this instance is an {@link Error}.
     * @return A CompletableFuture containing this instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the function.
     * @throws NullPointerException if the function is null.
     */
    default CompletableFuture<ErrorOr<T>> orAsync(
        Function<List<Error>, ? extends CompletableFuture<T>> onError
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orAsync : onError is null");

        if (!this.isError()) {
            return CompletableFuture.completedFuture(this);
        }
        return onError.apply(this.errors()).thenApply(ErrorOr::of);
    }

    /**
     * Asynchronously returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the supplier.
     *
     * @param onError The supplier to provide an alternative value if this instance is an {@link Error}.
     * @return A CompletableFuture containing this instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the supplier.
     * @throws NullPointerException if the supplier is null.
     */
    default CompletableFuture<ErrorOr<T>> orAsync(
        Supplier<? extends CompletableFuture<T>> onError
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orAsync : onError is null");

        if (!this.isError()) {
            return CompletableFuture.completedFuture(this);
        }
        return onError.get().thenApply(ErrorOr::of);
    }

    /**
     * Asynchronously returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the function.
     *
     * @param onError The function to apply to the errors to return an alternative error if this instance is an {@link Error}.
     * @return A CompletableFuture containing this instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the function.
     * @throws NullPointerException if the function is null.
     */
    default CompletableFuture<ErrorOr<T>> orErrorAsync(
        Function<List<Error>, ? extends CompletableFuture<Error>> onError
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orErrorAsync : onError is null");

        if (!this.isError()) {
            return CompletableFuture.completedFuture(this);
        }
        return onError.apply(this.errors()).thenApply(ErrorOr::ofError);
    }

    /**
     * Asynchronously returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the supplier.
     *
     * @param onError The supplier to provide an alternative error if this instance is an {@link Error}.
     * @return A CompletableFuture containing this instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the supplier.
     * @throws NullPointerException if the supplier is null.
     */
    default CompletableFuture<ErrorOr<T>> orErrorAsync(
        Supplier<? extends CompletableFuture<Error>> onError
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orErrorAsync : onError is null");

        if (!this.isError()) {
            return CompletableFuture.completedFuture(this);
        }
        return onError.get().thenApply(ErrorOr::ofError);
    }

    /**
     * Asynchronously returns this instance if it is not an {@link Error}, otherwise applies the provided function to the errors
     * and returns a new {@link ErrorOr} instance containing the provided errors.
     *
     * @param onError Function to apply if this instance is an {@link Error}.
     * @return A CompletableFuture containing this instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the function.
     * @throws NullPointerException if the onError function is null.
     */
    default CompletableFuture<ErrorOr<T>> orErrorsAsync(
        Function<List<Error>, ? extends CompletableFuture<Iterable<Error>>> onError
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orErrorAsync : onError is null");

        if (!this.isError()) {
            return CompletableFuture.completedFuture(this);
        }
        return onError.apply(this.errors()).thenApply(errors -> ErrorOr.ofError(errors));
    }

    /**
     * Asynchronously returns this instance if it is not an {@link Error}, otherwise returns the {@link ErrorOr} provided by the supplier.
     *
     * @param onError Supplier of an alternative {@link ErrorOr} to return if this instance is an {@link Error}.
     * @return A CompletableFuture containing this instance if it is not an {@link Error}, otherwise the {@link ErrorOr} provided by the supplier.
     * @throws NullPointerException if the onError supplier is null.
     */
    default CompletableFuture<ErrorOr<T>> orErrorsAsync(
        Supplier<? extends CompletableFuture<Iterable<Error>>> onError
    ) {
        Objects.requireNonNull(onError, "ErrorOr<T>.orErrorAsync : onError is null");

        if (!this.isError()) {
            return CompletableFuture.completedFuture(this);
        }
        return onError.get().thenApply(errors -> ErrorOr.ofError(errors));
    }

    /**
     * Performs the given action if this instance is a value.
     *
     * @param action Consumer to accept the value if this instance is a value.
     * @return This instance.
     * @throws NullPointerException if the action is null.
     */
    default ErrorOr<T> onValue(Consumer<? super T> action) {
        Objects.requireNonNull(action, "ErrorOr<T>.onValue : action is null");

        if (!this.isError()) {
            action.accept(this.value());
        }
        return this;
    }

    /**
     * Asynchronously performs the given action if this instance is a value.
     *
     * @param action Consumer to accept the CompletableFuture of the value if this instance is a value.
     * @return A CompletableFuture containing this instance.
     * @throws NullPointerException if the action is null.
     */
    default CompletableFuture<ErrorOr<T>> onValueAsync(
        Consumer<? super CompletableFuture<T>> action
    ) {
        Objects.requireNonNull(action, "ErrorOr<T>.onValueAsync : action is null");

        if (!this.isError()) {
            CompletableFuture<T> valueFuture = CompletableFuture.supplyAsync(this::value);
            action.accept(valueFuture);
            return valueFuture.thenApply(v -> this);
        }
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Performs the given action if this instance is an {@link Error}.
     *
     * @param action Consumer to accept the list of errors if this instance is an {@link Error}.
     * @return This instance.
     * @throws NullPointerException if the action is null.
     */
    default ErrorOr<T> onError(Consumer<List<Error>> action) {
        Objects.requireNonNull(action, "ErrorOr<T>.onError : action is null");

        if (this.isError()) {
            action.accept(this.errors());
        }
        return this;
    }

    /**
     * Asynchronously performs the given action if this instance is an {@link Error}.
     *
     * @param action Consumer to accept the CompletableFuture of the list of errors if this instance is an {@link Error}.
     * @return A CompletableFuture containing this instance.
     * @throws NullPointerException if the action is null.
     */
    default CompletableFuture<ErrorOr<T>> onErrorAsync(
        Consumer<? super CompletableFuture<List<Error>>> action
    ) {
        Objects.requireNonNull(action, "ErrorOr<T>.onErrorAsync : action is null");

        if (this.isError()) {
            CompletableFuture<List<Error>> errorFuture = CompletableFuture.supplyAsync(
                this::errors
            );
            action.accept(errorFuture);
            return errorFuture.thenApply(errors -> this);
        }
        return CompletableFuture.completedFuture(this);
    }

    /**
     * Returns this instance if it is an {@link Error}, otherwise applies the given predicate to the value.
     * If the predicate returns {@code true}, returns a new {@link ErrorOr} instance containing the provided errors.
     *
     * @param predicate The predicate to apply to the value.
     * @param errors    The errors to return if the predicate is satisfied.
     * @return This instance if it is an {@link Error}, otherwise a new {@link ErrorOr} instance containing the errors if the predicate is satisfied.
     * @throws NullPointerException if the predicate or errors are null.
     */
    default ErrorOr<T> failIf(Predicate<? super T> predicate, Iterable<Error> errors) {
        Objects.requireNonNull(predicate, "ErrorOr<T>.failIf : predicate is null");
        Objects.requireNonNull(errors, "ErrorOr<T>.failIf : errors is null");

        if (this.isError()) {
            return this;
        }
        return predicate.test(this.value()) ? ErrorOr.ofError(errors) : this;
    }

    /**
     * Returns this instance if it is an {@link Error}, otherwise applies the given predicate to the value.
     * If the predicate returns {@code true}, returns a new {@link ErrorOr} instance containing the provided errors.
     *
     * @param predicate The predicate to apply to the value.
     * @param errors    The array of errors to return if the predicate is satisfied.
     * @return This instance if it is an {@link Error}, otherwise a new {@link ErrorOr} instance containing the errors if the predicate is satisfied.
     * @throws NullPointerException if the predicate or errors are null.
     */
    default ErrorOr<T> failIf(Predicate<? super T> predicate, Error... errors) {
        return this.failIf(predicate, List.of(errors));
    }

    /**
     * Asynchronously returns this instance if it is an {@link Error}, otherwise applies the given predicate to the value.
     * If the predicate returns {@code true}, returns a new {@link ErrorOr} instance containing the provided errors.
     *
     * @param predicate The predicate to apply to the value.
     * @param error     The errors to return if the predicate is satisfied.
     * @return A CompletableFuture containing this instance if it is an {@link Error}, otherwise a new {@link ErrorOr} instance containing the errors if the predicate is satisfied.
     * @throws NullPointerException if the predicate or errors are null.
     */
    default CompletableFuture<ErrorOr<T>> failIfAsync(
        Function<? super T, CompletableFuture<Boolean>> predicate,
        Iterable<Error> error
    ) {
        Objects.requireNonNull(predicate, "ErrorOr<T>.failIfAsync : predicate is null");
        Objects.requireNonNull(error, "ErrorOr<T>.failIfAsync : error is null");

        if (this.isError()) {
            return CompletableFuture.completedFuture(this);
        }

        return predicate
            .apply(this.value())
            .thenApply(predicateResult -> predicateResult ? ErrorOr.ofError(error) : this);
    }

    /**
     * Asynchronously returns this instance if it is an {@link Error}, otherwise applies the given predicate to the value.
     * If the predicate returns {@code true}, returns a new {@link ErrorOr} instance containing the provided errors.
     *
     * @param predicate The predicate to apply to the value.
     * @param errors    The array of errors to return if the predicate is satisfied.
     * @return A CompletableFuture containing this instance if it is an {@link Error}, otherwise a new {@link ErrorOr} instance containing the errors if the predicate is satisfied.
     * @throws NullPointerException if the predicate or errors are null.
     */
    default CompletableFuture<ErrorOr<T>> failIfAsync(
        Function<? super T, CompletableFuture<Boolean>> predicate,
        Error... errors
    ) {
        return this.failIfAsync(predicate, List.of(errors));
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * Represents a successful result of an {@link ErrorOr}.
     *
     * @param <T> The type of the value contained in the success case.
     * @author Pablo Lamtenzan
     */
    final class Success<T> implements ErrorOr<T> {
        private final T value;

        /**
         * Constructs a {@code Success}.
         *
         * @param value The successful value.
         * @throws IllegalArgumentException if the value is null.
         */
        private Success(T value) {
            if (value == null) {
                throw new IllegalArgumentException(
                    "Cannot create value instance of ErrorOr<T> -> 'value' is null."
                );
            }
            this.value = value;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public T value() {
            return this.value;
        }

        @Override
        public List<Error> errors() {
            throw new UnsupportedOperationException(
                "ErrorOr<T>.errors not callable in a value instance."
            );
        }

        @Override
        public Error firstError() {
            throw new UnsupportedOperationException(
                "ErrorOr<T>.firstError not callable in a value instance."
            );
        }

        @Override
        public boolean equals(Object that) {
            return (
                (that == this) ||
                (that instanceof Success<?> && Objects.equals(value, ((Success<?>) that).value))
            );
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Success(" + value + ")";
        }
    }

    /**
     * Represents a failed result of an {@link ErrorOr}.
     *
     * @param <T> The type of the value that could have been contained.
     * @author Pablo Lamtenzan
     */
    final class Failure<T> implements ErrorOr<T> {
        private final List<Error> errors;

        /**
         * Constructs a {@code Failure}.
         *
         * @param errors The list of errors.
         * @throws IllegalArgumentException if errors is null or empty.
         */
        private Failure(List<Error> errors) {
            this.errors = validateErrors(errors);
        }

        /**
         * Constructs a {@code Failure} from an iterable of errors.
         *
         * @param errors The iterable of errors.
         */
        private Failure(Iterable<Error> errors) {
            this(
                validateErrors(
                    errors != null
                        ? StreamSupport
                            .stream(errors.spliterator(), false)
                            .collect(Collectors.toList())
                        : null
                )
            );
        }

        /**
         * Constructs a {@code Failure} from an array of errors.
         *
         * @param errors The array of errors.
         */
        private Failure(Error... errors) {
            this(validateErrors(errors != null ? List.of(errors) : null));
        }

        private static List<Error> validateErrors(List<Error> errors) {
            if (errors == null) {
                throw new IllegalArgumentException(
                    "Cannot create error instance of ErrorOr<T> -> 'errors' is null."
                );
            }

            if (errors.isEmpty()) {
                throw new IllegalArgumentException(
                    "Cannot create error instance of ErrorOr<T> -> 'errors' is empty. Provide at least one error."
                );
            }

            return errors;
        }

        @Override
        public boolean isError() {
            return true;
        }

        @Override
        public T value() {
            throw new UnsupportedOperationException(
                "ErrorOr<T>.value not callable in an error instance."
            );
        }

        @Override
        public List<Error> errors() {
            return this.errors;
        }

        @Override
        public Error firstError() {
            if (this.errors.isEmpty()) {
                throw new IllegalStateException("ErrorOr<T>.firstError has no errors.");
            }
            return this.errors.get(0);
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }

            if (that == null || this.getClass() != that.getClass()) {
                return false;
            }

            final ErrorOr<?> thatErrorOr = (ErrorOr<?>) that;
            if (this.errors.size() != thatErrorOr.errors().size()) {
                return false;
            }

            for (int i = 0; i < this.errors.size(); i++) {
                if (!this.errors.get(i).equals(thatErrorOr.errors().get(i))) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.errors);
        }

        @Override
        public String toString() {
            return errors
                .stream()
                .map(error -> String.format("%d: %s", errors.indexOf(error), error))
                .collect(Collectors.joining("\n"));
        }
    }
}
