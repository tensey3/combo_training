import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Timers {
    public final JTextArea timerTextArea;
    public final List<Integer> flam;
    public final List<String> directions;
    public final javax.swing.Timer swingTimer;
    public String lastDirection = "";
    public final Keyset keyset; // Keysetインスタンスを保持

    public Timers(JTextArea timerTextArea, Keyset keyset) {
        this.timerTextArea = timerTextArea;
        this.flam = new ArrayList<>();
        this.directions = new ArrayList<>();
        this.flam.add(0); // 最初のカウンターを追加
        this.directions.add(""); // 最初の方向を追加
        this.keyset = keyset; // Keysetインスタンスを設定

        // javax.swing.Timerを使ってカウントを更新
        swingTimer = new javax.swing.Timer(17, e -> SwingUtilities.invokeLater(() -> {
            try {
                updateCounters();
            } catch (Exception ex) {
                ex.printStackTrace(); // エラーログを出力
            }
        })); // 0.017秒ごとに実行
        swingTimer.start();
    }

    public void updateCounters() {
        try {
            // 最後のカウンターを更新
            int lastIndex = flam.size() - 1;
            String newcomen = keyset.getDirection(); // Keysetから方向を取得

            // キーイベントを受け取ったらその時点のカウント数でストップ
            if (!newcomen.isEmpty() && !newcomen.equals(lastDirection)) {
                lastDirection = newcomen; // 新しい方向を更新
                flam.set(lastIndex, flam.get(lastIndex)); // 最後のカウンターを現在の値で固定する
                directions.set(lastIndex, newcomen); // 最後の方向を現在の値で固定する
                flam.add(1); // 新しいカウンターを1で開始
                directions.add(""); // 新しい方向を追加
            } else {
                flam.set(lastIndex, flam.get(lastIndex) + 1);

                // 99に到達したら新しいカウンターを追加
                if (flam.get(lastIndex) > 99) {
                    flam.set(lastIndex, 99); // カウンターを99で固定
                    flam.add(1); // 新しいカウンターを1で開始
                    directions.add(""); // 新しい方向を追加
                }
            }

            // カウンターを新しく作る
            StringBuilder sb = new StringBuilder();
            for (int i = flam.size() - 1; i >= 0; i--) { // リストの後ろから処理する
                sb.append(flam.get(i)).append(" ").append(directions.get(i)).append("\n");
            }
            timerTextArea.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
        }
    }
}
