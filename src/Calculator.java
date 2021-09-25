/*
19251076 CHENG Yifeng
This calculator implements full functionality of a simple calculator by converting
infix expression to postfix expression.

 */

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;


public class Calculator extends JFrame {
    List<String> memory = new LinkedList<>();


    public Calculator() {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setTitle("Calculator");
        this.setSize(400, 450);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JTextField numberBar = new JTextField();
        numberBar.setEditable(false);
        numberBar.setPreferredSize(new Dimension(400, 40));
        numberBar.setBackground(Color.darkGray);
        numberBar.setForeground(Color.white);
        numberBar.setFont(new Font("Serif", Font.BOLD, 20));


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 4));


        HashMap<String, JButton> keyMap = new HashMap<>();

        String[] symbols = {"7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "*", "C", "0", "=", "/"};


        for (String s : symbols) {
            JButton btn = new JButton(s);
            setButtonStyle(btn);
            buttonPanel.add(btn);
            keyMap.put(s, btn);
        }

        for (int i = 0; i < keyMap.size(); i++) {
            JButton btn = keyMap.get(symbols[i]);
            String symbol = symbols[i];
            btn.addActionListener(e -> {
                if (symbol.equals("C")) {
                    memory.clear();
                    updateTextField(numberBar);
                } else if (symbol.equals("=")) {
                    if (!evaluateExpression()) {
                        numberBar.setText("Error");
                    } else {
                        if (memory.size() != 0)
                            numberBar.setText(memory.get(0));
                    }
                    memory.clear();
                } else {
                    memory.add(symbol);
                    updateTextField(numberBar);
                }


            });
        }

        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(numberBar, BorderLayout.NORTH);
        this.setVisible(true);
    }


    private boolean evaluateExpression() {
        List<Double> result = new LinkedList<>();
        List<String> stack = new LinkedList<>();
        List<String> numParseStack = new LinkedList<>();
        String symbol;
        boolean isPrevOperator = false;

        if (memory.size() == 0) {
            return true;
        }
        if (!isNumber(memory.get(0)) || !isNumber(memory.get(memory.size() - 1))) {
            return false;
        }


        int initialMemSize = memory.size();


        for (int i = 0; i < initialMemSize; i++) {
            symbol = memory.remove(0);

            if (isNumber(symbol)) {
                numParseStack.add(0, symbol);
                isPrevOperator = false;
                if (i == initialMemSize - 1) {
                    result.add(0, (double) parseNum(numParseStack));
                    calcStackExpression(result, stack);
                }

            } else {
                if (isPrevOperator) {
                    return false;
                }

                isPrevOperator = true;
                result.add(0, (double) parseNum(numParseStack));


                if (stack.size() == 0) {
                    stack.add(0, symbol);
                } else {
                    String compare = stack.get(0);
                    if (operatorPriority(compare.charAt(0)) < operatorPriority(symbol.charAt(0))) {
                        stack.add(0, symbol);
                        // the operator has higher priority than operator at the top of the stack
                        // add operator to the stack
                    } else {
                        // The operator has same or inferior priority than operator at the top of the stack
                        // Pop stack, and start using popped operators to conduct arithmetic operations
                        calcStackExpression(result, stack);
                        stack.add(symbol);
                    }


                }

            }
        }

        memory.add("" + result.get(0));


        return true;
    }

    private int parseNum(List<String> numParseStack) {
        int result = 0;
        int times = 1;
        for (int i = 0; i < numParseStack.size(); ) {
            result += Integer.parseInt(numParseStack.remove(0)) * times;
            times *= 10;
        }
        return result;
    }

    private void calcStackExpression(List<Double> result, List<String> stack) {
        Double temp = result.remove(0);
        String operator;
        for (int j = 0; j < stack.size(); ) {
            operator = stack.remove(0);
            switch (operator.charAt(0)) {
                case '*':
                    temp = temp * result.remove(0);
                    break;
                case '/':
                    temp = result.remove(0) / temp;
                    break;
                case '-':
                    temp = result.remove(0) - temp;
                    break;
                case '+':
                    temp = temp + result.remove(0);
                    break;
            }
        }
        result.add(temp);
    }

    private int operatorPriority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 0;
            case '*':
            case '/':
                return 1;
            case '^':
                return 2;
            default:
                return -1;
        }
    }

    private boolean isNumber(String str) {
        char symbol = str.charAt(0);
        return symbol >= 48 && symbol <= 57;
    }


    private void updateTextField(JTextField text) {
        text.setText(mergeMemoryToString());
    }

    private String mergeMemoryToString() {
        StringBuilder sb = new StringBuilder();
        for (String str : memory) {
            sb.append(str);
        }
        return sb.toString();
    }

    private void setButtonStyle(JButton button) {


        button.setFont(new Font("Serif", Font.BOLD, 30));
        button.setBackground(new Color(187, 110, 32));
        button.setForeground(Color.white);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(true);

    }


    public static void main(String[] args) {
        new Calculator();
    }

}
