package kr.hyfata.najoan.kkutumacro.utils;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;

public class SwingUtil {
    public static JFormattedTextField getIntTextField() {
        NumberFormatter formatter = getNumberFormatter();
        JFormattedTextField textField = new JFormattedTextField(formatter);

        textField.addCaretListener(e -> {
            int caretPosition = textField.getCaretPosition();
            int textLength = textField.getText().length();

            if (caretPosition < textLength && textField.getText().equals("0")) {
                textField.setCaretPosition(textLength);
            }
        });

        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JTextField tf = (JTextField)e.getSource();
                    int offset = tf.viewToModel2D(e.getPoint());
                    tf.setCaretPosition(offset);
                });
            }
        });

        return textField;
    }

    private static NumberFormatter getNumberFormatter() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text != null && text.isEmpty()) {
                    return 0L;
                }
                return super.stringToValue(text);
            }
        };

        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0L);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }
}
