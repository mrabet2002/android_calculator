package ma.enset.calculator;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mXparser;

import ma.enset.calculator.enums.Mode;

public class Calculator {

    // This variable is used to display the expression in the calculator's display
    private static final StringBuilder expression = new StringBuilder();
    // This variable is used to evaluate the expression
    private static final StringBuilder calculableExpression = new StringBuilder();

    public String getFormattedExpression() {
        return expression.toString();
    }

    public void addElement(String element) {
        switch (element) {
            case "log":
                appendToExpression("lg(");
                break;
            case "e":
                appendToExpression("exp(");
                break;
            case "x!":
                appendToExpression("!");
                break;
            case "x²":
                appendToExpression("^2");
                break;
            case "x³":
                appendToExpression("^3");
                break;
            case "x^y":
                appendToExpression("^(");
                break;
            case "y√x":
                appendToExpression("^(1/");
                break;
            case "1/x":
                appendToExpression("^(-1)");
                break;
            default:
                if (isFunction(element)) {
                    addFunction(element);
                } else if (isOperator(element)) {
                    addOperator(element);
                } else if (isTrigonometricFunctionInv(element)) {
                    addTrigonometricFunctionInv(element);
                } else {
                    appendToExpression(element);
                }
                break;
        }
    }

    public void switchMode(Mode mode) {
        if (mode == Mode.DEG) {
            mXparser.setDegreesMode();
        } else if (mode == Mode.RAD) {
            mXparser.setRadiansMode();
        }
    }

    private void appendToExpression(String element) {
        expression.append(element);
        calculableExpression.append(element);
    }

    private void addFunction(String element) {
        expression.append(element).append("(");
        calculableExpression.append(element).append("(");
    }

    public void addTrigonometricFunctionInv(String element) {
        element = element.replace("⁻¹", "");
        expression.append("a").append(element).append("(");
        calculableExpression.append("a").append(element).append("(");
    }

    public void addOperator(String operator) {
        String calcOperator = "+".equals(operator) ? "+" :
                "-".equals(operator) ? "-" :
                        "×".equals(operator) ? "*" :
                                "÷".equals(operator) ? "/" : operator;

        if (isSign(operator) || (operator.matches("[×÷]") && !(expression.length() == 0))) {
            String lastElement = getLastElement();
            if (lastElement == null || !isOperator(lastElement)) {
                expression.append(operator);
                calculableExpression.append(calcOperator);
            } else {
                replaceLastElement(operator);
            }
        }
    }


    public String getLastElement() {
        return calculableExpression.length() > 0 ? String.valueOf(calculableExpression.charAt(calculableExpression.length() - 1)) : null;
    }

    public String getBeforeLastElement() {
        return calculableExpression.length() > 1 ? String.valueOf(calculableExpression.charAt(calculableExpression.length() - 2)) : null;
    }

    public void replaceLastElement(String element) {
        if (!(expression.length() == 0)) {
            expression.replace(expression.length() - 1, expression.length(), element);
        }
    }

    public boolean isNumber(String element) {
        return element.matches("\\d+");
    }

    public boolean isTrigonometricFunctionInv(String element) {
        return element.matches("sin⁻¹|cos⁻¹|tan⁻¹");
    }

    public boolean isOperator(String element) {
        return element.matches("[+\\-×÷]");
    }

    public boolean isFunction(String element) {
        return element.matches("sin|cos|tan|ln|√");
    }

    public boolean isSign(String element) {
        return element.matches("[+\\-]");
    }

    public String evaluate() {
        if (expression.length() == 0) {
            return "";
        }
        Expression e = new Expression(calculableExpression.toString());
        Double result = e.calculate();
        if (result.isInfinite() || result.isNaN()) {
            return "Error!";
        }
        return String.valueOf(result);
    }

    public void clear() {
        expression.delete(0, expression.length());
        calculableExpression.delete(0, calculableExpression.length());
    }

    public void backSpace() {
        if (calculableExpression.length() > 0) {
            calculableExpression.delete(calculableExpression.length() - 1, calculableExpression.length());
            expression.delete(expression.length() - 1, expression.length());
        }
//        if (calculableExpression.length() > 0) {
//            if (isNumber(getLastElement()) || isOperator(getLastElement()) || getLastElement().equals(".") || getLastElement().equals(")")) {
//                calculableExpression.delete(calculableExpression.length() - 1, calculableExpression.length());
//                expression.delete(expression.length() - 1, expression.length());
//            } else if (getLastElement().equals("(")) {
//                while ((!isOperator(getBeforeLastElement())
//                        && !getBeforeLastElement().equals("(")
//                        && !isNumber(getBeforeLastElement())) ||
//                        (!isOperator(getLastElement())
//                                && !isNumber(getLastElement()))) {
//                    calculableExpression.delete(calculableExpression.length() - 1, calculableExpression.length());
//                    expression.delete(expression.length() - 1, expression.length());
//                }
//            }
//        } else {
//            clear();
//        }
    }

    public boolean isExpressionReady() {
        return expression.length() > 0 && !isOperator(getLastElement()) && !getLastElement().equals("(");
    }

    public void setExpression(String resultText) {
        clear();
        expression.append(resultText);
        calculableExpression.append(resultText);
    }
}