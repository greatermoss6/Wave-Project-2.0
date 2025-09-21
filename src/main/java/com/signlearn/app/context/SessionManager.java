package com.signlearn.app.context;

import com.signlearn.domain.model.User;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private User currentUser;
    private final Map<String, Object> attributes = new HashMap<>();

    public void set(User user) { this.currentUser = user; }
    public User get() { return currentUser; }
    public void clear() { currentUser = null; attributes.clear(); }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }
}