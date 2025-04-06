package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner sourceSpinner, destinationSpinner;
    private EditText inputValue;
    private TextView result;
    private Button btnConvert;
    double convertedValue = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Match IDs from your XML:
        sourceSpinner = findViewById(R.id.sourceSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        inputValue = findViewById(R.id.inputValue);
        btnConvert = findViewById(R.id.btnConvert);
        result = findViewById(R.id.result);

        // Populate the spinners
        List<String> units = getUnitsList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(adapter);
        destinationSpinner.setAdapter(adapter);

        // Button click listener
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputStr = inputValue.getText().toString().trim();

                // Check for empty input
                if (inputStr.isEmpty()) {
                    result.setText("Please enter a value.");
                    return;
                }

                // Attempt to parse input as a double
                double input;
                try {
                    input = Double.parseDouble(inputStr);
                } catch (NumberFormatException e) {
                    // Invalid numerical input
                    result.setText("Invalid number format. Please enter a valid numeric value.");
                    return;
                }

                // Get selected units
                String sourceUnit = sourceSpinner.getSelectedItem().toString();
                String destinationUnit = destinationSpinner.getSelectedItem().toString();

                // Check if source and destination are in compatible categories
                if ((isLength(sourceUnit) && !isLength(destinationUnit)) ||
                        (isWeight(sourceUnit) && !isWeight(destinationUnit)) ||
                        (isTemperature(sourceUnit) && !isTemperature(destinationUnit))) {

                    result.setText("Cannot convert between different categories.");
                    return;
                }

                // Perform conversion
                convertedValue = convertUnits(sourceUnit, destinationUnit, input);
                String output = String.format("%.4f", convertedValue);

                // Display result
                result.setText(output);
            }
        });
    }

    /**
     * Returns a list of units to populate the spinners.
     */
    private List<String> getUnitsList() {
        List<String> units = new ArrayList<>();
        // Length
        units.add("Inch");
        units.add("Foot");
        units.add("Yard");
        units.add("Mile");
        units.add("Centimeter");
        units.add("Kilometer");

        // Weight
        units.add("Pound");
        units.add("Ounce");
        units.add("Ton");
        units.add("Gram");
        units.add("Kilogram");

        // Temperature
        units.add("Celsius");
        units.add("Fahrenheit");
        units.add("Kelvin");

        return units;
    }

    /**
     * Converts a value from one unit to another.
     */
    private double convertUnits(String sourceUnit, String destinationUnit, double value) {
        // Length
        if (isLength(sourceUnit) && isLength(destinationUnit)) {
            double valueInCm = convertLengthToCm(sourceUnit, value);
            return convertCmToLength(destinationUnit, valueInCm);
        }

        // Weight
        if (isWeight(sourceUnit) && isWeight(destinationUnit)) {
            double valueInKg = convertWeightToKg(sourceUnit, value);
            return convertKgToWeight(destinationUnit, valueInKg);
        }

        // Temperature
        if (isTemperature(sourceUnit) && isTemperature(destinationUnit)) {
            return convertTemperature(sourceUnit, destinationUnit, value);
        }

        // Fallback if categories differ (handled earlier, but just in case)
        return 0;
    }

    private boolean isLength(String unit) {
        switch (unit) {
            case "Inch":
            case "Foot":
            case "Yard":
            case "Mile":
            case "Centimeter":
            case "Kilometer":
                return true;
        }
        return false;
    }

    private boolean isWeight(String unit) {
        switch (unit) {
            case "Pound":
            case "Ounce":
            case "Ton":
            case "Gram":
            case "Kilogram":
                return true;
        }
        return false;
    }

    private boolean isTemperature(String unit) {
        switch (unit) {
            case "Celsius":
            case "Fahrenheit":
            case "Kelvin":
                return true;
        }
        return false;
    }

    // ---------- LENGTH CONVERSIONS ----------
    private double convertLengthToCm(String sourceUnit, double value) {
        switch (sourceUnit) {
            case "Inch":        // 1 inch = 2.54 cm
                return value * 2.54;
            case "Foot":        // 1 foot = 30.48 cm
                return value * 30.48;
            case "Yard":        // 1 yard = 91.44 cm
                return value * 91.44;
            case "Mile":        // 1 mile = 1.60934 km = 160934 cm
                return value * 160934;
            case "Centimeter":
                return value;
            case "Kilometer":   // 1 km = 100000 cm
                return value * 100000;
        }
        return 0;
    }

    private double convertCmToLength(String destinationUnit, double valueInCm) {
        switch (destinationUnit) {
            case "Inch":
                return valueInCm / 2.54;
            case "Foot":
                return valueInCm / 30.48;
            case "Yard":
                return valueInCm / 91.44;
            case "Mile":
                return valueInCm / 160934;
            case "Centimeter":
                return valueInCm;
            case "Kilometer":
                return valueInCm / 100000;
        }
        return 0;
    }

    // ---------- WEIGHT CONVERSIONS ----------
    private double convertWeightToKg(String sourceUnit, double value) {
        switch (sourceUnit) {
            case "Pound":       // 1 pound = 0.453592 kg
                return value * 0.453592;
            case "Ounce":       // 1 ounce = 0.0283495 kg
                return value * 0.0283495;
            case "Ton":         // 1 ton = 907.185 kg
                return value * 907.185;
            case "Gram":        // 1 gram = 0.001 kg
                return value * 0.001;
            case "Kilogram":
                return value;
        }
        return 0;
    }

    private double convertKgToWeight(String destinationUnit, double valueInKg) {
        switch (destinationUnit) {
            case "Pound":
                return valueInKg / 0.453592;
            case "Ounce":
                return valueInKg / 0.0283495;
            case "Ton":
                return valueInKg / 907.185;
            case "Gram":
                return valueInKg * 1000;
            case "Kilogram":
                return valueInKg;
        }
        return 0;
    }

    // ---------- TEMPERATURE CONVERSIONS ----------
    private double convertTemperature(String sourceUnit, String destinationUnit, double value) {
        // Convert source unit to Celsius first
        double valueInCelsius;

        switch (sourceUnit) {
            case "Celsius":
                valueInCelsius = value;
                break;
            case "Fahrenheit":  // C = (F - 32) / 1.8
                valueInCelsius = (value - 32) / 1.8;
                break;
            case "Kelvin":      // C = K - 273.15
                valueInCelsius = value - 273.15;
                break;
            default:
                valueInCelsius = 0;
                break;
        }

        // Now convert from Celsius to destination
        switch (destinationUnit) {
            case "Celsius":
                return valueInCelsius;
            case "Fahrenheit":          // F = (C * 1.8) + 32
                return (valueInCelsius * 1.8) + 32;
            case "Kelvin":             // K = C + 273.15
                return valueInCelsius + 273.15;
        }

        return 0;
    }
}