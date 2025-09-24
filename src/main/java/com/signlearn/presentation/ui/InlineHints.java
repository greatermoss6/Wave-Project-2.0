package com.signlearn.presentation.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class InlineHints {

    public void showSuggestions(VBox container, List<String> suggestions) {
        container.getChildren().clear();
        if (suggestions == null || suggestions.isEmpty()) {
            container.setManaged(false);
            container.setVisible(false);
            return;
        }
        container.setManaged(true);
        container.setVisible(true);
        for (String s : suggestions) {
            Label lbl = new Label(s);
            lbl.setStyle("-fx-text-fill: #475569; -fx-font-size: 12px; -fx-padding: 2 0 2 0;");
            container.getChildren().add(lbl);
        }
    }

    public void showPasswordRules(VBox container, Map<String, Boolean> rules) {
        container.getChildren().clear();
        if (rules == null || rules.isEmpty()) {
            container.setManaged(false);
            container.setVisible(false);
            return;
        }
        container.setManaged(true);
        container.setVisible(true);
        for (Map.Entry<String, Boolean> e : rules.entrySet()) {
            Label lbl = new Label((e.getValue() ? "✔ " : "✖ ") + e.getKey());
            lbl.setStyle((e.getValue()
                    ? "-fx-text-fill: #059669;"
                    : "-fx-text-fill: #dc2626;")
                    + " -fx-font-size: 12px; -fx-padding: 1 0 1 0;");
            container.getChildren().add(lbl);
        }
    }

    public void hide(VBox container) {
        container.getChildren().clear();
        container.setManaged(false);
        container.setVisible(false);
    }

    public static void markSuccess(Node field) {
        field.setStyle("-fx-border-color: #059669; -fx-border-width: 2;");
    }

    public static void markError(Node field) {
        field.setStyle("-fx-border-color: #dc2626; -fx-border-width: 2;");
    }

    public static void clear(Node field) {
        field.setStyle("");
    }
}