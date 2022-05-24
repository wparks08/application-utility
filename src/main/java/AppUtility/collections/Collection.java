package AppUtility.collections;

import AppUtility.domains.datakey.DataKey;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Collection<T> {
    void add(T t);

    void addAll(java.util.Collection<? extends T> ts);

    void addAll(Collection<T> ts);

    void clear();

    T[] toArray();

    default List<T> toList() {
        return Arrays.asList(toArray());
    }

    default Set<T> toSet() {
        return new HashSet<>(toList());
    }
}
