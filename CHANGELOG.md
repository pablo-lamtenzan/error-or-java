# Changelog

All notable changes to this project are documented in this file.

## [1.1.0] - 2024-07-03

### Added

- Added `ErrorOr.of(ErrorOr<U> that)` to create a copy of a value instance.
- Added `ErrorOr.ofError(ErrorOr<U> that)` to create a copy of an error instance.

## [1.1.1] - 2024-07-03

### Fixed

- Updated `ErrorOr.ofError(ErrorOr<?> that)` factory method to allow type conversion and simplify error handling.
