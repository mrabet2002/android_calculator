package ma.enset.calculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;

import ma.enset.calculator.enums.Mode;

public class LandscapeActivity extends MainActivity {
    private TableLayout advancedFunctionsButtons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landscape_activity);
        init();
        advancedFunctionsButtons = findViewById(R.id.advancedFunctionsBtns);
        View.OnClickListener buttonClickListener = view -> {
            Button button = (Button) view;
            String buttonText = button.getText().toString();
            switch (buttonText) {
                case "Inv":
                    toggleAdvancedFunctions();
                    break;
                case "Rad":
                case "Deg":
                    switchMode();
                    break;
                default: {
                    calculator.addElement(buttonText);
                    display.setText(calculator.getFormattedExpression());
                    if (calculator.isExpressionReady())
                        result.setText(String.valueOf(calculator.evaluate()));
                }
            }
        };
        for (int i = 0; i < advancedFunctionsButtons.getChildCount(); i++) {
            View row = advancedFunctionsButtons.getChildAt(i);
            if (row instanceof TableRow) {
                TableRow tableRow = (TableRow) row;
                for (int j = 0; j < tableRow.getChildCount(); j++) {
                    View child = tableRow.getChildAt(j);
                    if (child instanceof Button) {
                        child.setOnClickListener(buttonClickListener);
                    }
                }
            }
        }
    }

    private void switchMode() {
        Button mode = findViewById(R.id.mode);
        if (mode.getText().equals("Deg")) {
            mode.setText("Rad");
            calculator.switchMode(Mode.RAD);
        } else {
            mode.setText("Deg");
            calculator.switchMode(Mode.DEG);
        }
        if (calculator.isExpressionReady())
            result.setText(String.valueOf(calculator.evaluate()));
    }

    private void toggleAdvancedFunctions() {
        Button sin = findViewById(R.id.sin);
        Button cos = findViewById(R.id.cos);
        Button tan = findViewById(R.id.tan);
        if (sin.getText().equals("sin⁻¹")) {
            sin.setText("sin");
            cos.setText("cos");
            tan.setText("tan");
        } else {
            sin.setText("sin⁻¹");
            cos.setText("cos⁻¹");
            tan.setText("tan⁻¹");
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, PortraitActivity.class);
            startActivity(intent);
        }
    }
}
