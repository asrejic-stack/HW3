package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffReviewerHistoryPage {

    private final Stage stage;
    private final ReviewerRequestManager manager;

    public StaffReviewerHistoryPage(Stage stage, DatabaseHelper db) {
        this.stage = stage;
        this.manager = new ReviewerRequestManager(db);
    }

    public void show() {

        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        Label title = new Label("Reviewer History Lookup");
        TextField txtReviewer = new TextField();
        txtReviewer.setPromptText("Enter reviewer username...");

        Button btnLoad = new Button("Load History");

        ListView<String> list = new ListView<>();
        Label status = new Label();

        btnLoad.setOnAction(e -> {
            String reviewer = txtReviewer.getText().trim();
            if (reviewer.isEmpty()) {
                status.setText("Enter a reviewer name.");
                return;
            }

            var rows = manager.getReviewerHistory(reviewer);
            list.setItems(FXCollections.observableArrayList());

            for (String[] r : rows) {
                String accepted = r[3].equalsIgnoreCase("TRUE") ? " (Accepted)" : "";
                list.getItems().add("[" + r[0] + "] " + r[2] + accepted);
            }

            status.setText("Found " + list.getItems().size() + " reviews.");
        });

        root.getChildren().addAll(title, txtReviewer, btnLoad, list, status);

        stage.setScene(new Scene(root, 600, 450));
        stage.setTitle("Staff — Reviewer History");
        stage.show();
    }
}
