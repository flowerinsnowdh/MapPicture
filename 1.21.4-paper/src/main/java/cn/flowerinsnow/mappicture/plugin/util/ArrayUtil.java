package cn.flowerinsnow.mappicture.plugin.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public final class ArrayUtil {
    private ArrayUtil() {
    }

    public static @NotNull Object @NotNull [] validateNotNull(@Nullable Object @Nullable [] array) throws NullPointerException {
        Objects.requireNonNull(array);
        if (Arrays.stream(array).anyMatch(Objects::isNull)) {
            throw new NullPointerException();
        }
        //noinspection NullableProblems
        return array;
    }
}
