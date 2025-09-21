package com.signlearn.app.dependency_injection;

public interface DependencyAware {
    void injectDependencies(ServiceRegistry registry);
}