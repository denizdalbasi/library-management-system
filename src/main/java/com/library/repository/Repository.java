package com.library.repository;

import java.util.*;

public class Repository<T> {
    private final Map<String, T> storage = new HashMap<>();

    public void add(String id, T item) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        storage.put(id, item);
        assert repOk() : "Repository invariant violated after add";
    }

    public Optional<T> get(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean remove(String id) {
        boolean removed = storage.remove(id) != null;
        assert repOk() : "Repository invariant violated after remove";
        return removed;
    }

    public boolean repOk() {
        if (storage == null) {
            return false;
        }
        for (Map.Entry<String, T> entry : storage.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                return false;
            }
        }
        return true;
    }
}