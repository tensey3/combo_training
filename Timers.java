import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Timers {
    public final JTextArea timerArea;
    public final List<Integer> counts;
    public final List<String> dirs;
    public final javax.swing.Timer timer;
    public String lastDir = "";
    public final Keyset keyset; // Keysetインスタンスを保持

    public Timers(JTextArea timerArea, Keyset keyset) {
        this.timerArea = timerArea;
        this.counts = new ArrayList<>();
        this.dirs = new ArrayList<>();
        this.counts.add(0); // 最初のカウンターを追加
        this.dirs.add(""); // 最初の方向を追加
        this.keyset = keyset; // Keysetインスタンスを設定

        // javax.swing.Timerを使ってカウントを更新
        timer = new javax.swing.Timer(17, e -> SwingUtilities.invokeLater(() -> {
            try {
                updateCounts();
            } catch (Exception ex) {
                // ex.printStackTrace(); // エラーログを出力
            }
        })); // 0.017秒ごとに実行
        timer.start();
    }

    public void updateCounts() {
        try {
            // 最後のカウンターを更新
            int lastIndex = counts.size() - 1;
            String newDir = keyset.getDir(); // Keysetから方向を取得

            // キーイベントを受け取ったらその時点のカウント数でストップ
            if (!newDir.isEmpty() && !newDir.equals(lastDir)) {
                lastDir = newDir; // 新しい方向を更新
                counts.set(lastIndex, counts.get(lastIndex)); // 最後のカウンターを現在の値で固定する
                dirs.set(lastIndex, newDir); // 最後の方向を現在の値で固定する
                counts.add(1); // 新しいカウンターを1で開始
                dirs.add(""); // 新しい方向を追加
            } else {
                counts.set(lastIndex, counts.get(lastIndex) + 1);

                // 99に到達したら新しいカウンターを追加
                if (counts.get(lastIndex) > 99) {
                    counts.set(lastIndex, 99); // カウンターを99で固定
                    counts.add(1); // 新しいカウンターを1で開始
                    dirs.add(""); // 新しい方向を追加
                }
            }

            // カウンターを新しく作る
            StringBuilder sb = new StringBuilder();
            for (int i = counts.size() - 1; i >= 0; i--) { // リストの後ろから処理する
                sb.append(counts.get(i)).append(" ").append(dirs.get(i)).append("\n");
            }
            timerArea.setText(sb.toString());
        } catch (Exception e) {
            // e.printStackTrace(); // エラーログを出力
        }
    }
}