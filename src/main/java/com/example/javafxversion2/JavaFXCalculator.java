package com.example.javafxversion2;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


/**
 * A simple calculator application built using JavaFX that offers a graphical user interface for basic
 * arithmetic operations, memory operations, power, and square root calculations. This class manages the
 * setup and behavior of the calculator, including the layout of buttons and display fields, and handles
 * user interactions through event handlers.
 *
 * @authors Kaeden Snyder & Steven Cain
 * @github https://github.com/kasnyd/JavaFxVersion2
 * @since 2024.04.26
 * @version 2.0 beta
 */
public class JavaFXCalculator extends Application {

    final double USD_TO_EUR = 0.93;  // Example rate
    final double KG_TO_LBS = 2.20462;  // 1 kg = 2.20462 lbs
    final double LBS_TO_KG = 0.453592;  // 1 lb = 0.453592 kg

    private TextField tfDisplay;    // The display text field where numbers and results are shown.
    private Button[] btns;          // Array of buttons for the calculator.
    private String[] btnLabels = {  // Labels for calculator buttons, defining their displayed text.
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "x",
            ".", "0", "=", "\u00F7",
            "C", "←", "^","√",
            "M+", "M-", "MR", "MC",
            "$", "€", "Lbs", "Kg"
    };

    private double result = 0;     // Stores the current result of calculations.
    private String inStr = "0";   // Stores the current input as a string.
    private char lastOperator = ' ';// The last operator entered by the user. ' '(nothing), '+', '-', '*', '/', '='
    private Text memoryText;// Displays the current memory value.
    private double memoryValue = 0.0; // Stores the current value in memory.


    /**
     * Event handler for handling calculator button clicks. This handler processes each button press,
     * performing actions such as digit entry, operations, and memory management based on the button label.
     */
    EventHandler handler = evt -> {
        String currentBtnLabel = ((Button)evt.getSource()).getText();
        switch (currentBtnLabel) {
            // Number buttons
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
            case ".":
                if (inStr.equals("0")) {
                    inStr = currentBtnLabel;  // no leading zero
                } else {
                    inStr += currentBtnLabel; // append input digit
                }
                tfDisplay.setText(inStr);
                // Clear buffer if last operator is '='
                if (lastOperator == '=') {
                    result = 0;
                    lastOperator = ' ';
                }
                break;

            // Operator buttons: '+', '-', 'x', '÷' and '='
            case "+":
                compute();
                lastOperator = '+';
                break;
            case "-":
                compute();
                lastOperator = '-';
                break;
            case "x":
                compute();
                lastOperator = '*';
                break;
            case "\u00F7":
                compute();
                lastOperator = '/';
                break;
            case "=":
                compute();
                lastOperator = '=';
                break;

            //Power button logic
            case "^":
                compute();
                lastOperator = '^';
                break;

            //sqrt button logic
            case "√":
                if (lastOperator != '=') {
                    result = Math.sqrt(Double.parseDouble(inStr));
                    inStr = String.valueOf(result);
                }
                tfDisplay.setText(inStr);
                lastOperator = '=';
                break;

            // backspace button logic
            case "←":
                if (!inStr.equals("0") && inStr.length() > 1) {
                    inStr = inStr.substring(0, inStr.length() - 1); // Remove last character
                } else {
                    inStr = "0"; // Reset to 0 if the string is empty after backspacing
                }
                tfDisplay.setText(inStr);
                break;

            // Clear button
            case "C":
                result = 0;
                inStr = "0";
                lastOperator = ' ';
                tfDisplay.setText("0");
                break;

            // Memory add button logic
            case "M+":
                if (this.lastOperator != '='){
                    memoryValue += Double.parseDouble(inStr);
                    memoryText.setText("Memory: " + memoryValue);
                    break;
                }else{
                    memoryValue += result;
                    memoryText.setText("Memory: " + memoryValue);
                }

                break;

                // Memory Minus button logic
            case "M-":
                if (lastOperator != '=') {
                    memoryValue -= Double.parseDouble(inStr);
                } else {
                    memoryValue -= result;
                }
                memoryText.setText("Memory: " + memoryValue);
                break;

            // Memory Recall
            case "MR":
                inStr = String.valueOf(memoryValue);
                tfDisplay.setText(memoryValue + "");
                break;

            // Memory Clear button logic
            case "MC":
                memoryValue = 0.0;
                memoryText.setText("Memory: " + memoryValue);
                break;

            case "$":
                if (!inStr.isEmpty()) {
                    double usd = Double.parseDouble(inStr);
                    double eur = usd * USD_TO_EUR;
                    tfDisplay.setText(String.format("%.2f EUR", eur));
                }
                break;

            // Currency conversion from EUR to USD (reverse calculation for example)
            case "€":
                if (!inStr.isEmpty()) {
                    double eur = Double.parseDouble(inStr);
                    double usd = eur / USD_TO_EUR;
                    tfDisplay.setText(String.format("%.2f USD", usd));
                }
                break;


        }
    };


    /**
     * Handles logic for computing results based on the last operator
     * the user pushes '+', '-', '*', '/' or '=', and the current input.
     * This method performs arithmetic operations by updating the 'result' based on the 'lastOperator'
     * and the new input captured in 'inStr'.
     *
     */
    private void compute() {
        double inNum = Double.parseDouble(inStr);
        inStr = "0";
        if (lastOperator == ' ') {
            result = inNum;
        } else if (lastOperator == '+') {
            result += inNum;
        } else if (lastOperator == '-') {
            result -= inNum;
        } else if (lastOperator == '*') {
            result *= inNum;
        } else if (lastOperator == '/') { // thinking about divide by zero  to get gold but Not sure yet
            result /= inNum;
        } else if (lastOperator == '^') {
            result = Math.pow(result, inNum); // Calculates the power of the result raised to the input number
        } else if (lastOperator == '=') {
            // Keep the result for the next operation
        }
        tfDisplay.setText(result + "");
    }


    /**
     *
     * Starts and initializes the JavaFX application. This method sets up the GUI layout,
     * initializes all components including buttons and display fields, and arranges them
     * in the primary stage.
     *
     * @param primaryStage The primary window of this application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Setup the Display TextField
        tfDisplay = new TextField("0");
        tfDisplay.setEditable(false);
        tfDisplay.setAlignment(Pos.CENTER_RIGHT);

        // Setup a GridPane for 4x4 Buttons
        int numCols = 4;
        int numRows = 6;
        GridPane paneButton = new GridPane();
        paneButton.setPadding(new Insets(15, 0, 15, 0));  // top, right, bottom, left
        paneButton.setVgap(5);  // Vertical gap between nodes
        paneButton.setHgap(5);  // Horizontal gap between nodes
        // Setup 4 columns of equal width, fill parent
        ColumnConstraints[] columns = new ColumnConstraints[numCols];
        for (int i = 0; i < numCols; ++i) {
            columns[i] = new ColumnConstraints();
            columns[i].setHgrow(Priority.ALWAYS) ;  // Allow column to grow
            columns[i].setFillWidth(true);  // Ask nodes to fill space for column
            paneButton.getColumnConstraints().add(columns[i]);
        }

        // Setup 24 Buttons and add to GridPane; and event handler
        btns = new Button[28];
        for (int i = 0; i < btns.length; ++i) {
            btns[i] = new Button(btnLabels[i]);
            btns[i].setOnAction(handler);  // Register event handler
            btns[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  // full-width
            paneButton.add(btns[i], i % numCols, i / numCols);  // control, col, row

            // sets the buttons that match each cordinating btnLabel to the proper color
            if (btns[i].getText().matches("[0-9]")) {
               btns[i].setStyle("-fx-background-color: #808A9F; -fx-text-fill: white; -fx-border-color:grey ;");
            }
            else if (btns[i].getText().matches("[\\+\\-x÷\\^√]")) {
                btns[i].setStyle("-fx-background-color: #5e7d3b; -fx-text-fill: White;");
            }
            else if (btns[i].getText().matches("MC|MR|M\\+|M-")) {
                btns[i].setStyle("-fx-background-color: #345798; -fx-text-fill: White;");
            }
            else if (btns[i].getText().matches("=")) {
                btns[i].setStyle("-fx-background-color: #d18f23; -fx-text-fill: White;");
            }
            else if (btns[i].getText().matches("\\.|C|←")) {
                btns[i].setStyle("-fx-background-color: #CFCAB4; -fx-text-fill: Black;");
            }


            /* Key For Colors
            * CFCAB4 - sand beige   [For ., C, and ← buttons]
            * d18f23 - Harvest Gold [For = button]
            * 345798 - YinMn Blue   [For Memory Buttons]
            * 5e7d3b - Fern Green   [For Operation sign buttons]
            * 808A9F - Cool Grey    [For num buttons]
             */
        }


        // memoryText display
        memoryText = new Text();
        memoryText.setText("Memory: " + memoryValue);


        // Setup up the scene graph rooted at a BorderPane (of 5 zones)
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #666666;");  // Set background color
        root.setPadding(new Insets(15, 15, 15, 15));  // top, right, bottom, left
        root.setTop(tfDisplay);     // Top zone contains the TextField
        root.setCenter(paneButton); // Center zone contains the GridPane of Buttons
        root.setBottom(memoryText); // Bottom zone contains the memoryText

        // Set up scene and stage
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.setTitle("The Calc-U-Later");
        primaryStage.show();
    }


    /**
     * The main entry point for JavaFX applications. Launches the JavaFX runtime and subsequently calls
     * code start method.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}