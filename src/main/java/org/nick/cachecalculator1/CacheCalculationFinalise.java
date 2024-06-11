package org.nick.cachecalculator1;

import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CacheCalculationFinalise extends Application {

    private TextField mmSizeField;
    private TextField cacheSizeField;
    private TextField wordSizeField;
    private RadioButton directMappingRadioButton;
    private RadioButton fullyMappingRadioButton;
    private RadioButton setMappingRadioButton;
    private ToggleGroup mappingToggleGroup;

    private static final double BITS = 3;
    private static final double KB = 10;
    private static final double MB = 20;
    private static final double GB = 30;
    private static final double TB = 40;

    @Override
    public void start(Stage primaryStage) {
        welcomeScreen(primaryStage);
    }

    private void welcomeScreen(Stage stage) {
        Label labelName = new Label();
        labelName.setFont(Font.font("Impact", 30));
        labelName.setText("Welcome !");

        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20, 20, 20, 20));

        String imagePath = "file:C:\\Users\\syami\\Downloads\\logoIIUM.png";
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(350);
        imageView.setFitHeight(350);
        imageView.setPreserveRatio(true);

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> cacheCalculationScreen(stage));

        welcomeLayout.getChildren().addAll(labelName, imageView, startButton);

        Scene scene = new Scene(welcomeLayout, 600, 400);
        stage.setTitle("Cache Calculation");
        stage.setScene(scene);
        stage.show();
    }

    private void cacheCalculationScreen(Stage stage) {
        stage.setTitle("Cache Calculation");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);

        Label choiceLabel = new Label("Choose Mapping:");
        GridPane.setConstraints(choiceLabel, 0, 0);

        mappingToggleGroup = new ToggleGroup();

        directMappingRadioButton = new RadioButton("Direct Mapping");
        directMappingRadioButton.setToggleGroup(mappingToggleGroup);
        GridPane.setConstraints(directMappingRadioButton, 0, 1);

        fullyMappingRadioButton = new RadioButton("Fully/Associative Mapping");
        fullyMappingRadioButton.setToggleGroup(mappingToggleGroup);
        GridPane.setConstraints(fullyMappingRadioButton, 0, 2);

        setMappingRadioButton = new RadioButton("Set-Associative Mapping");
        setMappingRadioButton.setToggleGroup(mappingToggleGroup);
        GridPane.setConstraints(setMappingRadioButton, 0, 3);

        Label mmSizeLabel = new Label("Main Memory Size (GB):");
        GridPane.setConstraints(mmSizeLabel, 0, 5);

        mmSizeField = new TextField();
        GridPane.setConstraints(mmSizeField, 1, 5);

        Label cacheSizeLabel = new Label("Cache Memory Size (MB):");
        GridPane.setConstraints(cacheSizeLabel, 0, 6);

        cacheSizeField = new TextField();
        GridPane.setConstraints(cacheSizeField, 1, 6);

        Label wordSizeLabel = new Label("Word Size (bits):");
        GridPane.setConstraints(wordSizeLabel, 0, 7);

        wordSizeField = new TextField();
        GridPane.setConstraints(wordSizeField, 1, 7);

        Button calculateButton = new Button("Calculate");
        GridPane.setConstraints(calculateButton, 1, 8);
        calculateButton.setOnAction(e -> calculate());

        grid.getChildren().addAll(choiceLabel, directMappingRadioButton, fullyMappingRadioButton,
                setMappingRadioButton, mmSizeLabel, mmSizeField, cacheSizeLabel, cacheSizeField,
                wordSizeLabel, wordSizeField, calculateButton);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void calculate() {
        int mmSize = Integer.parseInt(mmSizeField.getText());
        int cacheSize = Integer.parseInt(cacheSizeField.getText());
        int wordSize = Integer.parseInt(wordSizeField.getText());

        if (directMappingRadioButton.isSelected()) {
            directMapping(mmSize, cacheSize, wordSize);
        } else if (fullyMappingRadioButton.isSelected()) {
            fullyMapping(mmSize, cacheSize, wordSize);
        } else if (setMappingRadioButton.isSelected()) {
            setMapping(mmSize, cacheSize, wordSize);
        } else {
            showAlert("Error", "Please select a mapping type.");
        }
    }

    private void directMapping(int mmSize, int cacheSize, int wordSize) {
        int mmToBits = (int) (decimalToExponential(mmSize) + GB);
        int cacheToBits = (int) (decimalToExponential(cacheSize) + MB);
        int wordToBits = (int) decimalToExponential(wordSize);
        int tag = mmToBits - cacheToBits;
        int line = cacheToBits - wordToBits;

        displayResult(mmToBits, cacheToBits, wordToBits, tag, line);
    }

    private void fullyMapping(int mmSize, int cacheSize, int wordSize) {
        int mmToBits = (int) (decimalToExponential(mmSize) + GB);
        int cacheToBits = (int) (decimalToExponential(cacheSize) + MB);
        int wordToBits = (int) decimalToExponential(wordSize);
        int tag = mmToBits - wordToBits;

        displayResult(mmToBits, cacheToBits, wordToBits, tag, 0);
    }

    private void setMapping(int mmSize, int cacheSize, int wordSize) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set-Associative Mapping");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the number of ways:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(wayStr -> {
            int way = Integer.parseInt(wayStr);
            calculateSetMapping(mmSize, cacheSize, wordSize, way);
        });
    }

    private void calculateSetMapping(int mmSize, int cacheSize, int wordSize, int way) {
        int cacheSet = cacheSize / way;

        int mmToBits = (int) (decimalToExponential(mmSize) + GB);
        int cacheToBits = (int) (decimalToExponential(cacheSet) + MB);
        int wordToBits = (int) decimalToExponential(wordSize);
        int tag = mmToBits - cacheToBits;
        int line = cacheToBits - wordToBits;

        displayResult(mmToBits, cacheToBits, wordToBits, tag, line);
    }

    private void displayResult(int mmToBits, int cacheToBits, int wordToBits, int tag, int line) {
        showAlert("Result",
                "Main Memory Address Size = " + mmToBits + " bits\n" +
                        "Cache Memory Size = " + cacheToBits + " bits\n" +
                        "Word Size = " + wordToBits + " bits\n" +
                        "Tag = " + tag + " bits\n" +
                        "Line = " + line + " bits");
    }

    private double decimalToExponential(int decimal) {
        int exponent = 0;
        while (decimal % 2 == 0 && decimal > 1) {
            decimal /= 2;
            exponent++;
        }
        return exponent;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
