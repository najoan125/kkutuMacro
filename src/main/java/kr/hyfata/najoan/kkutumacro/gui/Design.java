package kr.hyfata.najoan.kkutumacro.gui;

import kr.hyfata.najoan.kkutumacro.Main;
import kr.hyfata.najoan.kkutumacro.handler.Handler;
import kr.hyfata.najoan.kkutumacro.handler.dto.Count;
import kr.hyfata.najoan.kkutumacro.utils.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
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

    private static boolean started = false, paused = false;

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
        browserAndDelay();
        startButton();
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

    private void browserAndDelay() {
        JPanel panel = new JPanel();
        String[] menu = new String[]{"Chrome", "Firefox"};
        browsers = new JComboBox<>(menu);
        browsers.setSelectedIndex(0);
        browsers.setPreferredSize(new Dimension(100, 23));
        panel.add(new JLabel("Browser: "));
        panel.add(browsers);

        panel.add(new JLabel("  "));
        panel.add(new JLabel("Delay(ms): "));
        delay = SwingUtil.getIntTextField();
        delay.setPreferredSize(new Dimension(80, 23));
        delay.setValue(1);
        panel.add(delay);

        panels.add(panel);
    }

    private void startButton() {
        JPanel panel = new JPanel();
        JButton start = new JButton("Start");
        JButton pause = new JButton("Pause");
        pause.setEnabled(false);
        start.addActionListener(e -> {
            if (delay.getValue().toString().equals("0")) {
                return;
            }
            if (!started) {
                started = true;
                start(start, pause);
            } else {
                started = false;
                stop(start ,pause);
            }
        });
        pause.addActionListener(e -> {
            if (!paused) {
                paused = true;
                pause.setText("Resume");
                Handler.stop();
                Main.LOG.info("The macro is paused");
            } else {
                paused = false;
                pause.setText("Pause");
                int ms = Integer.parseInt(delay.getValue().toString());
                Handler.start(ms);
                Main.LOG.info("The macro is Resumed");
            }
        });
        panel.add(start);
        panel.add(pause);
        panels.add(panel);
    }

    private void start(JButton start, JButton pause) {
        int ms = Integer.parseInt(delay.getValue().toString());
        text.setText("");
        start.setText("Starting");
        start.setEnabled(false);
        browsers.setEnabled(false);
        urlField.setEnabled(false);
        delay.setEnabled(false);

        String browser;
        if (browsers.getSelectedIndex() == 0) {
            browser = "chrome";
        } else {
            browser = "gecko";
        }

        Runnable task = () -> {
            Main.setDriver(browser, urlField.getText());
            pause.setEnabled(true);
            start.setText("Stop");
            start.setEnabled(true);
            Handler.start(ms);
            Main.LOG.info("The macro is started successfully");
        };
        new Thread(task).start();
    }

    private void stop(JButton start, JButton pause) {
        start.setText("Stopping");
        start.setEnabled(false);
        pause.setText("Pause");
        pause.setEnabled(false);
        Runnable task = () -> {
            Handler.stop();
            Main.getDriver().quit();
            start.setText("Start");
            start.setEnabled(true);
        };
        new Thread(task).start();
        browsers.setEnabled(true);
        urlField.setEnabled(true);
        delay.setEnabled(true);
        round.setText("<none>");
        count.setText("<none>");
        Count.resetCount();
        paused = false;
        Main.LOG.info("The macro is stopped successfully");
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
