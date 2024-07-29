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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Flam extends JFrame implements Combo.ComboListener {
    public Timers timers; // Timersインスタンスをフィールドとして保持
    private final JTextArea comboTextArea; // コンボ表示用のテキストエリア
    private final Combo combo; // Comboインスタンスをフィールドとして保持
    private Timer clearTimer; // コンボテキストエリアをクリアするためのタイマー

    public Flam() {
        setTitle("Key Event Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // メインパネルを作成してBorderLayoutで配置
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        combo = new Combo();
        combo.setComboListener(this); // リスナーの設定を確実に行う

        Keyset keyset = new Keyset(combo); // Keysetクラスのインスタンスを生成
        mainPanel.add(keyset, BorderLayout.WEST); // Keysetクラスのインスタンスを左側に追加

        JTextArea timerTextArea = new JTextArea();
        timerTextArea.setEditable(false);
        timerTextArea.setFont(new Font("SansSerif", Font.PLAIN, 24));
        mainPanel.add(timerTextArea, BorderLayout.CENTER); // タイマー表示用のエリアを中央に追加

        comboTextArea = new JTextArea();
        comboTextArea.setEditable(false);
        comboTextArea.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainPanel.add(comboTextArea, BorderLayout.EAST); // コンボ表示用のエリアを右側に追加

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

        // コンストラクタの外でリスナーを設定
        initializeComboListener();
    }

    // コンボリスナーを初期化するメソッド
    private void initializeComboListener() {
        combo.setComboListener(this);
    }

    // 画面作る処理
    private void switchToFullScreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            setUndecorated(true); // ウィンドウの装飾を無効にする
            gd.setFullScreenWindow(this); // フルスクリーンモードに設定する
        } else {
            System.err.println("フルスクリーンモードはサポートされていません。");
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

    @Override
    public void onComboDetected(String combo) {
        System.out.println("Combo detected: " + combo); // デバッグ出力を追加
        SwingUtilities.invokeLater(() -> {
            comboTextArea.setText(combo);
            startClearTimer(); // コンボ表示をクリアするタイマーを開始
        });
    }

    // コンボテキストエリアを一定時間後にクリアするためのメソッド
    private void startClearTimer() {
        if (clearTimer != null) {
            clearTimer.stop(); // 既存のタイマーがある場合は停止
        }
        clearTimer = new Timer(2000, new ActionListener() { // 2秒後にクリア
            @Override
            public void actionPerformed(ActionEvent e) {
                comboTextArea.setText(""); // コンボ表示をクリア
            }
        });
        clearTimer.setRepeats(false); // タイマーを一度だけ実行するように設定
        clearTimer.start();
    }
}
