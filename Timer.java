import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Timer {
    private final JTextArea timerTextArea;
    private final List<Integer> flam;
    private final javax.swing.Timer swingTimer;

    public Timer(JTextArea timerTextArea) {
        this.timerTextArea = timerTextArea;
        this.flam = new ArrayList<>();
        this.flam.add(0); // 最初のカウンターを追加

        // javax.swing.Timerを使ってカウントを更新
        swingTimer = new javax.swing.Timer(17, e -> SwingUtilities.invokeLater(this::updateCounters)); // 0.017秒ごとに実行
        swingTimer.start();
    }

    private void updateCounters() {
        // 最後のカウンターを更新
        int lastIndex = flam.size() - 1;
        flam.set(lastIndex, flam.get(lastIndex) + 1);

        // 99に到達したら新しいカウンターを追加
        if (flam.get(lastIndex) > 99) {
            flam.set(lastIndex, 99); // カウンターを99で固定
            flam.add(1); // 新しいカウンターを1で開始
        } else if (System.currentTimeMillis() % 17 == 0) {
            flam.set(lastIndex, flam.get(lastIndex) + 1); // 0.017秒後にカウントを1増加
        }

        // カウンターを新しく作る
        StringBuilder sb = new StringBuilder();
        for (int counter : flam) {
            sb.append(counter).append("\n");
        }
        timerTextArea.setText(sb.toString());
    }
}