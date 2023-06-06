package io.github.nullptr.tools.list;

import java.util.List;
import java.util.function.Predicate;

public class ListHelper {
    
    public static <T> boolean contains(List<T> list, Predicate<T> predicate) {
        return list.stream().anyMatch(predicate);
    }
}
