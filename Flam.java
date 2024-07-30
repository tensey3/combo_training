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

public class Flam extends JFrame implements Combo.Listener {
    private final Timers timers;
    private final JTextArea comboText;
    private Combo combo;
    private Timer clearTimer;

    public Flam() {
        setTitle("Key Event Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTextArea timerText = new JTextArea();
        timerText.setEditable(false);
        timerText.setFont(new Font("SansSerif", Font.PLAIN, 24));
        mainPanel.add(timerText, BorderLayout.CENTER);

        comboText = new JTextArea();
        comboText.setEditable(false);
        comboText.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainPanel.add(comboText, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
        setFocusable(true);

        SwingUtilities.invokeLater(() -> {
            goFullScreen();
            setVisible(true);
            requestFocusInWindow();
        });

        initCombo();

        Keyset keyset = new Keyset(combo);
        mainPanel.add(keyset, BorderLayout.WEST);
        addKeyListener(keyset);

        timers = new Timers(timerText, keyset);

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

    private void initCombo() {
        combo = new Combo();
        combo.setListener(this);
    }

    private void goFullScreen() {
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
            comboText.setText(combo);
            startClearTimer();
        });
    }

    private void startClearTimer() {
        if (clearTimer != null && clearTimer.isRunning()) {
            clearTimer.stop();
        }
        clearTimer = new Timer(2000, e -> comboText.setText(""));
        clearTimer.setRepeats(false);
        clearTimer.start();
    }
}