package com.signlearn.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class UsernameSuggester {
    private static final String[] WORDS = {
            "nova","echo","zen","pulse","spark","drift","byte","flux","orbit","rune",
            "vibe","forge","stride","trail","mint","loom","rally","ember","quill","vault"
    };

    private static final SecureRandom RND = new SecureRandom();

    public static List<String> suggest(String base, int count) {
        String seed = (base == null ? "user" : base.replaceAll("[^A-Za-z0-9_]", "")).toLowerCase();
        List<String> out = new ArrayList<>(count);
        while (out.size() < count) {
            String word = WORDS[RND.nextInt(WORDS.length)];
            int n = 10 + RND.nextInt(89);
            String s;
            switch (RND.nextInt(3)) {
                case 0 -> s = seed + "_" + word;
                case 1 -> s = seed + n;
                default -> s = seed + "_" + word + n;
            }
            if (!out.contains(s)) out.add(s);
        }
        return out;
    }
}