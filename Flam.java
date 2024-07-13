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

public class Flam extends JFrame {
    public Timers timers; // Timersインスタンスをフィールドとして保持

    public Flam() {
        setTitle("Key Event Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // メインパネルを作成してBorderLayoutで配置
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        Keyset keyset = new Keyset(); // Keysetクラスのインスタンスを生成
        mainPanel.add(keyset, BorderLayout.WEST); // Keysetクラスのインスタンスを左側に追加

        JTextArea timerTextArea = new JTextArea();
        timerTextArea.setEditable(false);
        timerTextArea.setFont(new Font("SansSerif", Font.PLAIN, 24));
        mainPanel.add(timerTextArea, BorderLayout.CENTER); // タイマー表示用のエリアを中央に追加

        timers = new Timers(timerTextArea, keyset); // Timersクラスのインスタンスを生成し、Keysetインスタンスを渡す

        add(mainPanel, BorderLayout.CENTER); // メインパネルをフレームに追加

        addKeyListener(keyset); // KeysetをKeyListenerとして追加
        setFocusable(true);
        requestFocusInWindow();

        // フレームを表示する準備が整ったらフルスクリーンモードに切り替える
        SwingUtilities.invokeLater(() -> {
            switchToFullScreen();
            setVisible(true);
        });
    }

    private void switchToFullScreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            setUndecorated(true); // ウィンドウの装飾を無効にする
            gd.setFullScreenWindow(this); // フルスクリーンモードに設定する
        } else {
            System.err.println("フルスクリーンモードはサポートされていません");
            setSize(1300, 1200); // フォールバックとしてサイズを設定
            setVisible(true);
        }

        // プログラム終了時にフルスクリーンモードを解除する処理を追加
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gd.setFullScreenWindow(null); // フルスクリーンモードを解除する
            }
        });
    }
}