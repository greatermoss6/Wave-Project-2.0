package com.signlearn.domain.value;

public record Email(String value) {
    public Email
    {
        if (value == null)
        {
            throw new IllegalArgumentException("Emaill can't be null");
        }

        int atIndex = value.indexOf('@');
        if (atIndex <= 0 || atIndex == value.length() - 1)
        {
            throw new IllegalArgumentException("Email invalid: " + value);
        }

        String domainName = value.substring(atIndex + 1);
        if (!domainName.contains("."))
        {
            throw new IllegalArgumentException("Email invalid: " + value);
        }
    }
}