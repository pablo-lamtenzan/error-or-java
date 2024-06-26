<div align="center">

[![Maven Central](https://img.shields.io/maven-central/v/dev.pablolamtenzan/error-or.svg)](https://search.maven.org/artifact/dev.pablolamtenzan/error-or)
[![Build](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/build.yml/badge.svg)](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/build.yml) 
[![Publish](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/publish.yml/badge.svg)](https://github.com/pablo-lamtenzan/error-or-java/actions/workflows/publish.yml)
[![GitHub contributors](https://img.shields.io/github/contributors/pablo-lamtenzan/error-or-java)](https://github.com/pablo-lamtenzan/error-or-java/graphs/contributors/) 
[![GitHub Stars](https://img.shields.io/github/stars/pablo-lamtenzan/error-or-java.svg)](https://github.com/pablo-lamtenzan/error-or-java/stargazers) 
[![GitHub license](https://img.shields.io/github/license/pablo-lamtenzan/error-or-java)](https://github.com/pablo-lamtenzan/error-or-java/blob/main/LICENSE)
[![codecov](https://codecov.io/github/pablo-lamtenzan/error-or-java/graph/badge.svg?token=E1HR4A7D15)](https://codecov.io/github/pablo-lamtenzan/error-or-java)

---

### A simple, fluent discriminated union of an error or a result for Java.

```xml
<dependency>
    <groupId>dev.pablolamtenzan</groupId>
    <artifactId>error-or</artifactId>
    <version>1.0.0</version>
</dependency>
```

```groovy
implementation 'dev.pablolamtenzan:error-or:1.0.0'
```

</div>

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Overview](#overview)
- [Installation](#installation)
	- [Maven](#maven)
	- [Gradle](#gradle)
	- [PGP Public Key](#pgp-public-key)
- [Usage](#usage)
	- [Key Features](#key-features)
- [Features](#features)
- [Contribution 🤲](#contribution-)
- [Credits 🙏](#credits-)
- [License 🪪](#license-)

## Overview

ErrorOr is a Java implementation of the discriminated union pattern, providing a fluent and expressive way to handle operations that can either result in a value or an error. Inspired by the original [ErrorOr project in C# by Amichai Mantinband](https://github.com/amantinband/error-or), this library aims to bring similar functionality to the Java ecosystem.

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>dev.pablolamtenzan</groupId>
    <artifactId>error-or</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle`:

```groovy
implementation 'dev.pablolamtenzan:error-or:1.0.0'
```

### PGP Public Key

To verify the library's integrity, use the PGP public key provided below:

```
-----BEGIN PGP PUBLIC KEY BLOCK-----
...
-----END PGP PUBLIC KEY BLOCK-----
```

## Usage

ErrorOr provides a fluent API to handle results and errors in a clean and expressive manner. It allows chaining operations and handling errors gracefully without resorting to exceptions.

### Key Features

- **Result Handling**: Seamlessly handle operations that may result in a value or an error.
- **Error Aggregation**: Collect multiple errors during a single operation.
- **Functional Methods**: Use a variety of methods to operate on results and errors in a functional style.
- **Fluent API**: Chain methods together to build complex operations in a readable way.

## Features

- **Simple API**: Easy to understand and use API for error and result handling.
- **Flexible**: Supports multiple error types and custom error definitions.
- **Integrated**: Works well with existing Java code and libraries.
- **Lightweight**: Minimal dependencies and overhead.

## Contribution 🤲

We welcome contributions from the community. If you have any questions, comments, or suggestions, please open an issue or create a pull request on GitHub. Your feedback is greatly appreciated.

## Credits 🙏

This project was inspired by the original [ErrorOr project in C#](https://github.com/amantinband/error-or) by Amichai Mantinband. All credit for the concept and original implementation goes to him. This Java implementation aims to extend his work and bring the same functionality to Java developers.

## License 🪪

This project is licensed under the terms of the [Apache-2.0](https://github.com/pablo-lamtenzan/error-or-java/blob/main/LICENSE) license.
