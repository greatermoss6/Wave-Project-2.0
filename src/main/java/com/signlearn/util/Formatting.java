package com.signlearn.util;

import java.text.DecimalFormat;

public class Formatting {
    public static String percent(int score) {
        return new DecimalFormat("#%").format(score / 100.0);
    }
}