package kr.hyfata.najoan.kkutumacro.gui;

import kr.hyfata.najoan.kkutumacro.Main;
import kr.hyfata.najoan.kkutumacro.handler.Handler;
import kr.hyfata.najoan.kkutumacro.handler.dto.Count;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class Design extends JFrame {
    public static final int WIDTH = 550, HEIGHT = 390;
    private final ArrayList<JPanel> panels = new ArrayList<>();
    private final HashMap<Integer, Integer> addedHeights = new HashMap<>(); //index, height
    private static JTextArea text;

    private static JComboBox<String> browsers;
    private static JTextField urlField;
    private static JFormattedTextField delay;

    public static JLabel round = new JLabel("<none>"), count = new JLabel("<none>");

    private static boolean started = false;

    public Design() {
        setTitle("KKuTu Macro Tool");

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (started) {
                    Handler.stop();
                    Main.getDriver().quit();
                }
            }
        });

        design();
        setVisible(true);
    }

    private void design() {
        log();
        urlInput();
        browserSelector();
        start();
        debug();
        add(getPanel());
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        int height = 35;

        int i = 0;
        for (JPanel p : panels) {
            p.setLayout(new FlowLayout(FlowLayout.CENTER));
            p.setPreferredSize(new Dimension(Design.WIDTH, height));
            if (addedHeights.containsKey(i)) {
                p.setPreferredSize(new Dimension(Design.WIDTH, height + addedHeights.get(i)));
            }
            panel.add(p);

            i++;
        }
        return panel;
    }

    public static JPanel getScrollablePanel(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        JPanel scrollablePanel = new JPanel(new BorderLayout());
        scrollablePanel.add(scrollPane, BorderLayout.CENTER);
        return scrollablePanel;
    }

    private void log() {
        text = new JTextArea();
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        PrintStream con = new PrintStream(new TextAreaOutputStream(text));
        System.setOut(con);
        System.setErr(con);

        JScrollPane pane = new JScrollPane(text);
        pane.getViewport().setPreferredSize(new Dimension(WIDTH - 100, 150));

        JPanel panel = getScrollablePanel(pane);
        panels.add(panel);
        addHeight(140);
    }

    private void urlInput() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("URL: "));

        urlField = new JTextField("https://kkutu.hyfata.kr");
        urlField.setPreferredSize(new Dimension(300, 23));
        panel.add(urlField);
        panels.add(panel);
    }

    private void browserSelector() {
        JPanel panel = new JPanel();
        String[] menu = new String[]{"Chrome", "Firefox"};
        browsers = new JComboBox<>(menu);
        browsers.setSelectedIndex(0);
        browsers.setPreferredSize(new Dimension(100, 23));
        panel.add(new JLabel("Browser: "));
        panel.add(browsers);

        panel.add(new JLabel("  "));
        panel.add(new JLabel("Delay(ms): "));
        delay = getIntTextField();
        delay.setPreferredSize(new Dimension(80, 23));
        delay.setValue(1);
        panel.add(delay);

        panels.add(panel);
    }

    private JFormattedTextField getIntTextField() {
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

        formatter.setValueClass(Long.class);
        formatter.setMinimum(0L);
        formatter.setMaximum(Long.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return formatter;
    }

    private void start() {
        JPanel panel = new JPanel();
        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            int ms = Integer.parseInt(delay.getValue().toString());
            if (!started) {
                started = true;
                text.setText("");
                start.setText("Starting");
                start.setEnabled(false);
                browsers.setEnabled(false);
                urlField.setEnabled(false);
                delay.setEnabled(false);
                if (browsers.getSelectedIndex() == 0) {
                    Runnable task = () -> {
                        Main.setDriver("chrome", urlField.getText());
                        start.setText("Stop");
                        start.setEnabled(true);
                        Handler.start(ms);
                    };
                    new Thread(task).start();
                } else {
                    Runnable task = () -> {
                        Main.setDriver("gecko", urlField.getText());
                        start.setText("Stop");
                        start.setEnabled(true);
                        Handler.start(ms);
                    };
                    new Thread(task).start();
                }
            } else {
                start.setText("Start");
                started = false;
                browsers.setEnabled(true);
                urlField.setEnabled(true);
                delay.setEnabled(true);
                round.setText("<none>");
                count.setText("<none>");
                Count.resetCount();
                Handler.stop();
                Main.getDriver().quit();
            }
        });
        panel.add(start);
        panels.add(panel);
    }

    private void debug() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Round: "));
        panel.add(round);
        panel.add(new JLabel("   Count: "));
        panel.add(count);
        panels.add(panel);
    }

    private void addHeight(int height) {
        addedHeights.put(panels.size() - 1, height);
    }
}
