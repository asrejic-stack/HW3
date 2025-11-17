package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffReviewerRequestPage {

    private Stage stage;
    private ReviewerRequestManager manager;

    public StaffReviewerRequestPage(Stage stage, DatabaseHelper db) {
        this.stage = stage;
        this.manager = new ReviewerRequestManager(db);
    }

    public void show() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        Label title = new Label("Pending Reviewer Requests");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<ReviewerRequest> list = new ListView<>();
        list.getItems().addAll(manager.getPendingRequests());

        Button btnApprove = new Button("Approve");
        Button btnDeny = new Button("Deny");
        Button btnRefresh = new Button("Refresh");

        Label status = new Label();

        btnRefresh.setOnAction(e ->
                list.getItems().setAll(manager.getPendingRequests()));

        btnApprove.setOnAction(e -> {
            ReviewerRequest selected = list.getSelectionModel().getSelectedItem();
            if (selected == null) {
                status.setText("Select a request.");
                return;
            }
            if (manager.approveRequest(selected.getRequestId(), selected.getUsername())) {
                status.setText("Approved " + selected.getUsername());
            }
            btnRefresh.fire();
        });

        btnDeny.setOnAction(e -> {
            ReviewerRequest selected = list.getSelectionModel().getSelectedItem();
            if (selected == null) {
                status.setText("Select a request.");
                return;
            }
            manager.denyRequest(selected.getRequestId());
            status.setText("Denied " + selected.getUsername());
            btnRefresh.fire();
        });

        root.getChildren().addAll(title, list, btnApprove, btnDeny, btnRefresh, status);

        stage.setScene(new Scene(root, 500, 400));
        stage.setTitle("Staff — Reviewer Requests");
        stage.show();
    }
}
