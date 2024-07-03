<div align="center">

[![Maven Central](https://img.shields.io/maven-central/v/dev.pablolamtenzan/error-or.svg)](https://search.maven.org/artifact/dev.pablolamtenzan/error-or)

[![Build](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/build.yml/badge.svg)](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/build.yml)
[![Publish](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/publish.yml/badge.svg)](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/publish.yml)

[![GitHub contributors](https://img.shields.io/github/contributors/pablo-lamtenzan/error-or-java)](https://github.com/pablo-lamtenzan/error-or-java/graphs/contributors/)
[![GitHub Stars](https://img.shields.io/github/stars/pablo-lamtenzan/error-or-java.svg)](https://github.com/pablo-lamtenzan/error-or-java/stargazers)
[![GitHub license](https://img.shields.io/github/license/pablo-lamtenzan/error-or-java)](https://github.com/pablo-lamtenzan/error-or-java/blob/main/LICENSE)
[![codecov](https://codecov.io/github/pablo-lamtenzan/error-or-java/graph/badge.svg?token=E1HR4A7D15)](https://codecov.io/github/pablo-lamtenzan/error-or-java)

</div>

---

### A simple, fluent discriminated union of an error or a result for Java

```xml
<dependency>
    <groupId>dev.pablolamtenzan</groupId>
    <artifactId>error-or</artifactId>
    <version>1.1.1</version>
</dependency>
```

```groovy
implementation 'dev.pablolamtenzan:error-or:1.1.1'
```

- [Star the Project ⭐](#star-the-project-)
- [Overview](#overview)
  - [Features](#features)
  - [Replace Throwing Exceptions with `ErrorOr<T>`](#replace-throwing-exceptions-with-errorort)
  - [Support For Multiple Errors](#support-for-multiple-errors)
  - [Various Functional Methods](#various-functional-methods)
    - [`map` and `mapAsync`](#map-and-mapasync)
    - [`match` and `matchAsync`](#match-and-matchasync)
    - [`consume` and `consumeAsync`](#consume-and-consumeasync)
    - [`or` and `orAsync`](#or-and-orasync)
- [Installation](#installation)
  - [Maven](#maven)
  - [Gradle](#gradle)
  - [PGP Public Key](#pgp-public-key)
- [Creating an ErrorOr instance](#creating-an-erroror-instance)
  - [Using `of`](#using-of)
  - [Using `ofError`](#using-oferror)
- [Properties](#properties)
  - [`isError`](#iserror)
  - [`value`](#value)
  - [`errors`](#errors)
  - [`firstError`](#firsterror)
- [Methods](#methods)
  - [`match`](#match)
  - [`matchAsync`](#matchasync)
  - [`matchFirst`](#matchfirst)
  - [`matchFirstAsync`](#matchfirstasync)
  - [`consume`](#consume)
  - [`consumeAsync`](#consumeasync)
  - [`consumeFirst`](#consumefirst)
  - [`consumeFirstAsync`](#consumefirstasync)
  - [`getOr`](#getor)
  - [`getOrAsync`](#getorasync)
  - [`ifError`](#iferror)
  - [`getOrThrow`](#getorthrow)
  - [`map`](#map)
  - [`mapAsync`](#mapasync)
  - [`mapError`](#maperror)
  - [`mapErrorAsync`](#maperrorasync)
  - [`or`](#or)
  - [`orAsync`](#orasync)
  - [`orError`](#orerror)
  - [`orErrorAsync`](#orerrorasync)
  - [`orErrors`](#orerrors)
  - [`orErrorsAsync`](#orerrorsasync)
  - [`onValue`](#onvalue)
  - [`onValueAsync`](#onvalueasync)
  - [`onError`](#onerror)
  - [`onErrorAsync`](#onerrorasync)
  - [`failIf`](#failif)
  - [`failIfAsync`](#failifasync)
  - [Error Types](#error-types)
    - [Built-in Error Types](#built-in-error-types)
    - [Custom Error Types](#custom-error-types)
  - [Organizing Errors](#organizing-errors)
- [Contribution 🤲](#contribution-)
- [Credits 🙏](#credits-)
- [License 🪪](#license-)

## Star the Project ⭐

Found this project helpful? Show your appreciation by giving us a star!

## Overview

ErrorOr is a Java implementation of the discriminated union pattern, providing a fluent and expressive way to handle operations that can either result in a value or an error. Inspired by the original [ErrorOr project in C# by Amichai Mantinband](https://github.com/amantinband/error-or), this library aims to bring similar functionality to the Java ecosystem.

### Features

- **Result Handling**: Seamlessly handle operations that may result in a value or an error.
- **Error Aggregation**: Collect multiple errors during a single operation.
- **Functional Methods**: Use a variety of methods to operate on results and errors in a functional style.
- **Fluent API**: Chain methods together to build complex operations in a readable way.
- **Simple API**: Easy to understand and use API for error and result handling.
- **Flexible**: Supports multiple error types and custom error definitions.
- **Integrated**: Works well with existing Java code and libraries.
- **Lightweight**: Minimal dependencies and overhead.

### Replace Throwing Exceptions with `ErrorOr<T>`

Instead of relying on traditional exception handling, `ErrorOr` allows you to represent the result of an operation as either a success or an error. This approach makes your code cleaner and more predictable.

**Traditional Approach:**

```java
public int divide(int numerator, int denominator) {
    if (denominator == 0) {
        throw new IllegalArgumentException("Cannot divide by zero");
    }
    return numerator / denominator;
}

try {
    int result = divide(10, 2);
    System.out.println(result);
} catch (IllegalArgumentException e) {
    System.err.println(e.getMessage());
}
```

**With `ErrorOr<T>`:**

```java
public ErrorOr<Integer> divide(int numerator, int denominator) {
    if (denominator == 0) {
        return ErrorOr.ofError(MathErrors.DIVISION_BY_ZERO);
    }
    return ErrorOr.of(numerator / denominator);
}

ErrorOr<Integer> result = divide(10, 2);
result.consume(
    errors -> errors.forEach(error -> System.err.println(error.description())),
    value -> System.out.println(value)
);
```

### Support For Multiple Errors

`ErrorOr` can encapsulate multiple errors, providing a comprehensive way to handle all potential issues in a single operation.

```java
public static ErrorOr<User> createUser(String name, int age) {
    List<Error> errors = new ArrayList<>();

    if (name == null || name.isBlank()) {
        errors.add(Error.validation("USER.INVALID_NAME", "Name cannot be blank"));
    }
    if (age < 0) {
        errors.add(Error.validation("USER.INVALID_AGE", "Age cannot be negative"));
    }

    if (!errors.isEmpty()) {
        return ErrorOr.ofError(errors);
    }

    return ErrorOr.of(new User(name, age));
}

ErrorOr<User> userResult = createUser("", -1);
userResult.consume(
    errors -> errors.forEach(error -> System.err.println(error.description())),
    user -> System.out.println("User created: " + user)
);
```

### Various Functional Methods

`ErrorOr` provides a rich set of methods to operate on the results and errors in a functional and fluent manner.

#### `map` and `mapAsync`

Transform the value if present, otherwise return the current instance:

```java
ErrorOr<String> result = divide(10, 2).map(value -> "Result: " + value);
CompletableFuture<ErrorOr<String>> asyncResult = divide(10, 2).mapAsync(value -> CompletableFuture.completedFuture("Result: " + value));
```

#### `match` and `matchAsync`

Apply functions to handle both success and error cases:

```java
String message = divide(10, 2).match(
    errors -> "Errors: " + errors.size(),
    value -> "Value: " + value
);
CompletableFuture<String> asyncMessage = divide(10, 2).matchAsync(
    errors -> CompletableFuture.completedFuture("Errors: " + errors.size()),
    value -> CompletableFuture.completedFuture("Value: " + value)
);
```

#### `consume` and `consumeAsync`

Consume the value or errors using the provided consumers:

```java
divide(10, 2).consume(
    errors -> System.err.println("Errors: " + errors),
    value -> System.out.println("Value: " + value)
);
CompletableFuture<Void> asyncConsume = divide(10, 2).consumeAsync(
    errors -> CompletableFuture.runAsync(() -> System.err.println("Errors: " + errors)),
    value -> CompletableFuture.runAsync(() -> System.out.println("Value: " + value))
);
```

#### `or` and `orAsync`

Provide an alternative value or `ErrorOr` if the current instance contains an error:

```java
ErrorOr<Integer> alternativeResult = divide(10, 0).or(ErrorOr.of(0));
CompletableFuture<ErrorOr<Integer>> asyncAlternativeResult = divide(10, 0).orAsync(() -> CompletableFuture.completedFuture(0));
```

---

These methods, among others, provide a robust and flexible approach to error handling and result management, promoting clean and maintainable code. By using `ErrorOr`, you can handle complex error scenarios gracefully without resorting to exception-based control flow.

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>dev.pablolamtenzan</groupId>
    <artifactId>error-or</artifactId>
    <version>1.1.1</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle`:

```groovy
implementation 'dev.pablolamtenzan:error-or:1.1.1'
```

### PGP Public Key

For verifying the integrity of the project, you can use the PGP public key provided. The public key is now available on a key server. You can retrieve the public key using the following command:

```sh
gpg --keyserver keyserver.ubuntu.com --search-keys pablolamtenzan.dev@proton.me
```

Alternatively, you can find the key in the [PGP-PUBLIC-KEY](./PGP-PUBLIC-KEY.asc) file in the repository.

## Creating an ErrorOr instance

The `ErrorOr` class provides multiple ways to create instances representing either a successful value or one or more errors. Below are the different methods available for creating `ErrorOr` instances.

### Using `of`

The `of` factory method is used to create an `ErrorOr` instance that holds a successful value.

```java
ErrorOr<Integer> successResult = ErrorOr.of(5);
```

The `of` factory method can also be used to create an `ErrorOr` instance by copying another `ErrorOr` instance.

```java
ErrorOr<String> stringSuccess = ErrorOr.of("Success");
ErrorOr<String> anotherStringSuccess = ErrorOr.of(stringSuccess);
```

### Using `ofError`

The `ofError` factory method is used to create an `ErrorOr` instance that holds one or more errors.\
This method has several overloads:

1. **Single Error**

```java
Error validationError = Error.validation("Invalid Input", "The provided input is invalid.");
ErrorOr<Integer> errorResult = ErrorOr.ofError(validationError);
```

2. **Multiple Errors (varargs)**

```java
Error validationError = Error.validation("Invalid Input", "The provided input is invalid.");
Error conflictError = Error.conflict("Conflict Error", "A conflict occurred.");
ErrorOr<Integer> errorResult = ErrorOr.ofError(validationError, conflictError);
```

3. **Iterable of Errors**

```java
List<Error> errors = List.of(
    Error.validation("Invalid Input", "The provided input is invalid."),
    Error.conflict("Conflict Error", "A conflict occurred.")
);
ErrorOr<Integer> errorResult = ErrorOr.ofError(errors);
```

4. **Other ErrorOr instance**

```java
ErrorOr<String> stringErrorOr = ErrorOr.ofError(Error.validation("Invalid Input", "The provided input is invalid."));
ErrorOr<Integer> integerErrorOr = ErrorOr.ofError(stringErrorOr);
```

## Properties

### `isError`

Indicates whether the `ErrorOr` object contains an error.

```java
ErrorOr<Integer> result = User.create();

if (result.isError()) {
    // the result contains one or more errors
}
```

### `value`

Returns the value contained in the `ErrorOr` object. Not available if the object contains an error.

```java
ErrorOr<Integer> result = User.create();

if (!result.isError()) {
    System.out.println(result.value());
}
```

### `errors`

Returns a list of errors contained in the `ErrorOr` object. Not available if the object contains a valid value.

```java
ErrorOr<Integer> result = User.create();

if (result.isError()) {
    result.errors().forEach(error -> System.out.println(error.description()));
}
```

### `firstError`

Returns the first error in the list of errors contained in the `ErrorOr` object. Not available if the object contains a valid value.

```java
ErrorOr<Integer> result = User.create();

if (result.isError()) {
    Error firstError = result.firstError();
    System.out.println(firstError.description());
}
```

## Methods

### `match`

Applies one of two functions depending on whether the `ErrorOr` instance is a value or an error.

```java
String message = result.match(
    errors -> "Errors: " + errors.size(),
    value -> "Value: " + value
);
```

### `matchAsync`

Asynchronously applies one of two functions depending on whether the `ErrorOr` instance is a value or an error.

```java
CompletableFuture<String> message = result.matchAsync(
    errors -> CompletableFuture.completedFuture("Errors: " + errors.size()),
    value -> CompletableFuture.completedFuture("Value: " + value)
);
```

### `matchFirst`

Applies one of two functions depending on whether the `ErrorOr` instance is a value or an error, using the first error if multiple errors are present.

```java
String message = result.matchFirst(
    error -> "Error: " + error.description(),
    value -> "Value: " + value
);
```

### `matchFirstAsync`

Asynchronously applies one of two functions depending on whether the `ErrorOr` instance is a value or an error, using the first error if multiple errors are present.

```java
CompletableFuture<String> message = result.matchFirstAsync(
    error -> CompletableFuture.completedFuture("Error: " + error.description()),
    value -> CompletableFuture.completedFuture("Value: " + value)
);
```

### `consume`

Consumes the value or errors, depending on the state of the `ErrorOr` instance.

```java
result.consume(
    errors -> System.out.println("Errors: " + errors),
    value -> System.out.println("Value: " + value)
);
```

### `consumeAsync`

Asynchronously consumes the value or errors, depending on the state of the `ErrorOr` instance.

```java
CompletableFuture<Void> consumption = result.consumeAsync(
    errors -> CompletableFuture.runAsync(() -> System.out.println("Errors: " + errors)),
    value -> CompletableFuture.runAsync(() -> System.out.println("Value: " + value))
);
```

### `consumeFirst`

Consumes the value or the first error, depending on the state of the `ErrorOr` instance.

```java
result.consumeFirst(
    error -> System.out.println("Error: " + error.description()),
    value -> System.out.println("Value: " + value)
);
```

### `consumeFirstAsync`

Asynchronously consumes the value or the first error, depending on the state of the `ErrorOr` instance.

```java
CompletableFuture<Void> consumption = result.consumeFirstAsync(
    error -> CompletableFuture.runAsync(() -> System.out.println("Error: " + error.description())),
    value -> CompletableFuture.runAsync(() -> System.out.println("Value: " + value))
);
```

### `getOr`

Returns the value if present, otherwise applies the provided function to the errors and returns the result.

```java
Integer value = result.getOr(errors -> -1);
Integer value = result.getOr(() -> -1);
```

### `getOrAsync`

Asynchronously returns the value if present, otherwise applies the provided function to the errors and returns the result.

```java
CompletableFuture<Integer> value = result.getOrAsync(errors -> CompletableFuture.completedFuture(-1));
CompletableFuture<Integer> value = result.getOrAsync(() -> CompletableFuture.completedFuture(-1));
```

### `ifError`

Performs the given action if the `ErrorOr` instance contains an error.

```java
result.ifError(errors -> System.out.println("Errors: " + errors));
result.ifError(() -> System.out.println("An error occurred"));
```

### `getOrThrow`

Returns the value if present, otherwise applies the provided function to the errors and throws the resulting exception.

```java
Integer value = result.getOrThrow(errors -> new RuntimeException("Error occurred"));
Integer value = result.getOrThrow(() -> new RuntimeException("Error occurred"));
```

### `map`

Applies the given function to the value if present, otherwise returns the current `ErrorOr` instance.

```java
ErrorOr<String> mappedResult = result.map(value -> "Value: " + value);
```

### `mapAsync`

Asynchronously applies the given function to the value if present, otherwise returns the current `ErrorOr` instance.

```java
CompletableFuture<ErrorOr<String>> mappedResult = result.mapAsync(value -> CompletableFuture.completedFuture("Value: " + value));
```

### `mapError`

Applies the given function to each error if present, otherwise returns the current `ErrorOr` instance.

```java
ErrorOr<Integer> mappedResult = result.mapError(error -> Error.unexpected("MappedError", "Mapped error description"));
```

### `mapErrorAsync`

Asynchronously applies the given function to each error if present, otherwise returns the current `ErrorOr` instance.

```java
CompletableFuture<ErrorOr<Integer>> mappedResult = result.mapErrorAsync(error -> CompletableFuture.completedFuture(Error.unexpected("MappedError", "Mapped error description")));
```

### `or`

Returns this instance if it is not an error, otherwise returns the provided alternative.

```java
ErrorOr<Integer> alternativeResult = result.or(ErrorOr.of(0));
ErrorOr<Integer> alternativeResult = result.or(errors -> 0);
ErrorOr<Integer> alternativeResult = result.or(() -> 0);
```

### `orAsync`

Asynchronously returns this instance if it is not an error, otherwise returns the provided alternative.

```java
CompletableFuture<ErrorOr<Integer>> alternativeResult = result.orAsync(errors -> CompletableFuture.completedFuture(0));
CompletableFuture<ErrorOr<Integer>> alternativeResult = result.orAsync(() -> CompletableFuture.completedFuture(0));
```

### `orError`

Returns this instance if it is not an error, otherwise returns the provided error alternative.

```java
ErrorOr<Integer> alternativeResult = result.orError(errors -> Error.unexpected());
ErrorOr<Integer> alternativeResult = result.orError(() -> Error.unexpected());
```

### `orErrorAsync`

Asynchronously returns this instance if it is not an error, otherwise returns the provided error alternative.

```java
CompletableFuture<ErrorOr<Integer>> alternativeResult = result.orErrorAsync(errors -> CompletableFuture.completedFuture(Error.unexpected()));
CompletableFuture<ErrorOr<Integer>> alternativeResult = result.orErrorAsync(() -> CompletableFuture.completedFuture(Error.unexpected()));
```

### `orErrors`

Returns this instance if it is not an error, otherwise returns the provided list of errors.

```java
ErrorOr<Integer> alternativeResult = result.orErrors(errors -> List.of(Error.unexpected()));
ErrorOr<Integer> alternativeResult = result.orErrors(() -> List.of(Error.unexpected()));
```

### `orErrorsAsync`

Asynchronously returns this instance if it is not an error, otherwise returns the provided list of errors.

```java
CompletableFuture<ErrorOr<Integer>> alternativeResult = result.orErrorsAsync(errors -> CompletableFuture.completedFuture(List.of(Error.unexpected())));
CompletableFuture<ErrorOr<Integer>> alternativeResult = result.orErrorsAsync(() -> CompletableFuture.completedFuture(List.of(Error.unexpected())));
```

### `onValue`

Performs the given action if this instance is a value.

```java
result.onValue(value -> System.out.println("Value: " + value));
```

### `onValueAsync`

Asynchronously performs the given action if this instance is a value.

```java
CompletableFuture<ErrorOr<Integer>> valueAction = result.onValueAsync(value -> CompletableFuture.runAsync(() -> System.out.println("Value: " + value)));
```

### `onError`

Performs the given action if this instance is an error.

```java
result.onError(errors -> System.out.println("Errors: " + errors));
```

### `onErrorAsync`

Asynchronously performs the given action if this instance is an error.

```java
CompletableFuture<ErrorOr<Integer>> errorAction = result.onErrorAsync(errors -> CompletableFuture.runAsync(() -> System.out.println("Errors: " + errors)));
```

### `failIf`

Returns this instance if it is not an error, otherwise applies the given predicate to the value and returns a new `ErrorOr` instance containing the provided errors if the predicate is satisfied.

```java
ErrorOr<Integer> checkedResult = result.failIf(value -> value < 0, Error.validation("NegativeValue", "Value cannot be negative"));
ErrorOr<Integer> checkedResult = result.failIf(value -> value < 0, List.of(Error.validation("NegativeValue", "Value cannot be negative")));
```

### `failIfAsync`

Asynchronously returns this instance if it is not an error, otherwise applies the given predicate to the value and returns a new `ErrorOr` instance containing the provided errors if the predicate is satisfied.

```java
CompletableFuture<ErrorOr<Integer>> checkedResult = result.failIfAsync(
    value -> CompletableFuture.completedFuture(value < 0),
    Error.validation("NegativeValue", "Value cannot be negative")
);
CompletableFuture<ErrorOr<Integer>> checkedResult = result.failIfAsync(
    value -> CompletableFuture.completedFuture(value < 0),
    List.of(Error.validation("NegativeValue", "Value cannot be negative"))
);
```

### Error Types

Error handling in `ErrorOr` is facilitated through the use of the `Error` class, which supports various built-in error types and custom error definitions.

#### Built-in Error Types

The `ErrorType` class defines several standard error types that can be used throughout your application. These built-in error types include:

1. **Failure**
2. **Unexpected**
3. **Validation**
4. **Conflict**
5. **Not Found**
6. **Unauthorized**
7. **Forbidden**

These types can be instantiated using the `Error` factory methods. Below is an example of the `failure` error type, showcasing the various overloads available. Other error types follow the same pattern.

```java
// Failure
Error failureError = Error.failure("FAILURE_CODE", "A failure has occurred.");
Error failureErrorWithMetadata = Error.failure("FAILURE_CODE", "A failure has occurred.", metadata);
Error generalFailureError = Error.failure();
Error generalFailureErrorWithMetadata = Error.failure(metadata);
```

Each of these types can be instantiated using the `Error` factory methods:

```java
Error failureError = Error.failure("FAILURE_CODE", "A failure has occurred.");
Error unexpectedError = Error.unexpected("UNEXPECTED_CODE", "An unexpected error has occurred.");
Error validationError = Error.validation("VALIDATION_CODE", "A validation error has occurred.");
Error conflictError = Error.conflict("CONFLICT_CODE", "A conflict has occurred.");
Error notFoundError = Error.notFound("NOT_FOUND_CODE", "A 'Not Found' error has occurred.");
Error unauthorizedError = Error.unauthorized("UNAUTHORIZED_CODE", "An 'Unauthorized' error has occurred.");
Error forbiddenError = Error.forbidden("FORBIDDEN_CODE", "A 'Forbidden' error has occurred.");
```

Each method has overloads to include metadata if necessary:

```java
Map<String, Object> metadata = Map.of("key", "value");
Error errorWithMetadata = Error.validation("VALIDATION_CODE", "A validation error has occurred.", metadata);
```

#### Custom Error Types

If the built-in error types do not suit your needs, you can define custom error types. Custom error types can be created using the `custom` method:

```java
int customTypeOrdinal = 99;
Error customError = Error.custom(customTypeOrdinal, "CUSTOM_CODE", "A custom error has occurred.");
Error customErrorWithMetadata = Error.custom(customTypeOrdinal, "CUSTOM_CODE", "A custom error has occurred.", metadata);
```

### Organizing Errors

A good practice is to organize your errors in a static class for better manageability and readability.\
For instance:

```java
public class MathErrors {
    public static final Error DIVISION_BY_ZERO = Error.failure("MATH.DIVISION_BY_ZERO", "Division by zero is not allowed.");
    public static final Error NEGATIVE_NUMBER = Error.validation("MATH.NEGATIVE_NUMBER", "Negative numbers are not allowed.");
}
```

You can then use these predefined errors in your methods:

```java
public static ErrorOr<Integer> divide(int numerator, int denominator) {
    if (denominator == 0) {
        return ErrorOr.ofError(MathErrors.DIVISION_BY_ZERO);
    }
    return ErrorOr.of(numerator / denominator);
}
```

## Contribution 🤲

We welcome contributions from the community. If you have any questions, comments, or suggestions, please open an issue or create a pull request. Your feedback is greatly appreciated.

## Credits 🙏

This project was inspired by the original [ErrorOr project in C#](https://github.com/amantinband/error-or) by **Amichai Mantinband**. All credit for the concept and original implementation goes to him. This Java implementation aims to extend his work and bring the same functionality to Java developers.

## License 🪪

This project is licensed under the terms of the [MIT](https://github.com/pablo-lamtenzan/error-or-java/blob/main/LICENSE) license.
