package dev.pablolamtenzan.erroror;

import java.util.concurrent.CompletableFuture;

public class TestUtils {

    public class Convert {

        public static ErrorOr<String> toString(int num) {
            return ErrorOr.of(ValueConvert.toString(num));
        }

        public static ErrorOr<Integer> toInt(String str) {
            Integer result = ValueConvert.toInt(str);
            if (result == null) {
                return ErrorOr.ofError(
                    Error.validation(
                        "INVALID_NUMBER",
                        "The provided string is not a valid integer",
                        null
                    )
                );
            }
            return ErrorOr.of(result);
        }

        public static CompletableFuture<ErrorOr<Integer>> toIntAsync(String str) {
            return CompletableFuture.supplyAsync(() -> toInt(str));
        }

        public static CompletableFuture<ErrorOr<String>> toStringAsync(int num) {
            return CompletableFuture.supplyAsync(() -> toString(num));
        }
    }

    public static class ValueConvert {

        public static String toString(int num) {
            return String.valueOf(num);
        }

        public static Integer toInt(String str) {
            try {
                int value = Integer.parseInt(str);
                return value;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public static CompletableFuture<Integer> toIntAsync(String str) {
            return CompletableFuture.supplyAsync(() -> toInt(str));
        }

        public static CompletableFuture<String> toStringAsync(int num) {
            return CompletableFuture.supplyAsync(() -> toString(num));
        }
    }
}
