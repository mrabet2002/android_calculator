package ma.enset.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    protected TextView display;
    protected TextView result;
    private TableLayout buttonsTable;
    protected Calculator calculator = new Calculator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init() {
        display = findViewById(R.id.display);
        result = findViewById(R.id.result);

        display.setText(calculator.getFormattedExpression());
        result.setText(String.valueOf(calculator.evaluate()));

        buttonsTable = findViewById(R.id.buttonsTable);

        View.OnClickListener buttonClickListener = view -> {
            Button button = (Button) view;
            String buttonText = button.getText().toString();
            switch (buttonText) {
                case "C":
                    display.setText("");
                    result.setText("");
                    calculator.clear();
                    break;
                case "⌫":
                    String text = display.getText().toString();
                    if (text.length() > 0) {
//                        display.setText(text.substring(0, text.length() - 1));
                        calculator.backSpace();
                        display.setText(calculator.getFormattedExpression());
                        result.setText(calculator.evaluate());
                    }
                    break;
                case "=":
                    try {
                        if (calculator.isExpressionReady()) {
                            String resultText = calculator.evaluate();
                            display.setText(resultText);
                            calculator.setExpression(resultText);
                            result.setText("");
                        }
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(e.getMessage());
                        builder.show();
                    }
                    break;
                default: {
                    calculator.addElement(buttonText);
                    display.setText(calculator.getFormattedExpression());
                    if (calculator.isExpressionReady())
                        result.setText(String.valueOf(calculator.evaluate()));
                }
            }
        };
        for (int i = 0; i < buttonsTable.getChildCount(); i++) {
            View row = buttonsTable.getChildAt(i);
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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, LandscapeActivity.class);
            startActivity(intent);
        }
    }
}