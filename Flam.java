import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Flam extends JFrame implements Combo.ComboListener {
    public Timers timers;
    private final JTextArea comboTextArea;
    private Combo combo;
    private Timer clearTimer;

    public Flam() {
        setTitle("Key Event Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTextArea timerTextArea = new JTextArea();
        timerTextArea.setEditable(false);
        timerTextArea.setFont(new Font("SansSerif", Font.PLAIN, 24));
        mainPanel.add(timerTextArea, BorderLayout.CENTER);

        comboTextArea = new JTextArea();
        comboTextArea.setEditable(false);
        comboTextArea.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainPanel.add(comboTextArea, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
        setFocusable(true);

        SwingUtilities.invokeLater(() -> {
            switchToFullScreen();
            setVisible(true);
            requestFocusInWindow();
        });

        initializeCombo();

        Keyset keyset = new Keyset(combo);
        mainPanel.add(keyset, BorderLayout.WEST);
        addKeyListener(keyset);

        timers = new Timers(timerTextArea, keyset);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                if (gd.getFullScreenWindow() == Flam.this) {
                    gd.setFullScreenWindow(null);
                }
            }
        });
    }

    private void initializeCombo() {
        combo = new Combo();
        combo.setComboListener(this);
    }

    private void switchToFullScreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("フルスクリーンモードはサポートされていません。");
            setSize(1300, 1200);
            setVisible(true);
        }
    }

    @Override
    public void onComboDetected(String combo) {
        System.out.println("Combo detected: " + combo);
        SwingUtilities.invokeLater(() -> {
            comboTextArea.setText(combo);
            startClearTimer();
        });
    }

    private void startClearTimer() {
        if (clearTimer != null && clearTimer.isRunning()) {
            clearTimer.stop();
        }
        clearTimer = new Timer(2000, e -> comboTextArea.setText(""));
        clearTimer.setRepeats(false);
        clearTimer.start();
    }
}
