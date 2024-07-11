import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Flam extends JFrame {
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

        new Timer(timerTextArea); // なぜか警告出るけど動いてるからええか

        add(mainPanel, BorderLayout.CENTER); // メインパネルをフレームに追加

        addKeyListener(keyset); // KeysetをKeyListenerとして追加
        setFocusable(true);
        requestFocusInWindow();
    }

    public static void main(String[] args) {
        Flam frame = new Flam();

        // フルスクリーンモードに設定
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        } else {
            System.err.println("フルスクリーンモードはサポートされていません");
            frame.setSize(1300, 1200); // フォールバックとしてサイズを設定
            frame.setVisible(true);
        }

        // レイアウトを再検証して再描画
        frame.revalidate();
        frame.repaint();

        // プログラム終了時にフルスクリーンモードを解除
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gd.setFullScreenWindow(null);
            }
        });

        // フレームを可視化
        frame.setVisible(true);
    }
}