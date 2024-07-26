import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Combo {
    private final LinkedList<Integer> keySequence;
    private final int sequenceSize;
    private ComboListener listener;

    public Combo() {
        keySequence = new LinkedList<>();
        sequenceSize = 4; // シーケンス長は4
    }

    public void setComboListener(ComboListener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        if (keySequence.size() >= sequenceSize) {
            keySequence.removeFirst(); // 最も古いキーを削除
        }
        keySequence.add(keyCode);

        // コマンドの検出
        if (detectCommand("波動拳")) {
            if (listener != null) {
                listener.onComboDetected("波動拳");
            }
        } else if (detectCommand("昇龍拳")) {
            if (listener != null) {
                listener.onComboDetected("昇龍拳");
            }
        }
    }

    private boolean detectCommand(String command) {
        int[] commandSequence;
        switch (command) {
            case "波動拳":
                // 波動拳のシーケンス: ↓, ↘︎, →, P
                commandSequence = new int[]{
                    KeyEvent.VK_S,   // ↓
                    KeyEvent.VK_D,   // ↘︎ (下右方向)
                    KeyEvent.VK_D,   // →
                    KeyEvent.VK_O    // P
                };
                break;
            case "昇龍拳":
                // 昇龍拳のシーケンス: →, ↓, ↘︎, P
                commandSequence = new int[]{
                    KeyEvent.VK_D,   // →
                    KeyEvent.VK_S,   // ↓
                    KeyEvent.VK_D,   // ↘︎ (下右方向)
                    KeyEvent.VK_O    // P
                };
                break;
            default:
                return false;
        }

        if (keySequence.size() < commandSequence.length) {
            return false;
        }

        for (int i = 0; i < commandSequence.length; i++) {
            if (keySequence.get(keySequence.size() - commandSequence.length + i) != commandSequence[i]) {
                return false;
            }
        }

        return true;
    }

    public interface ComboListener {
        void onComboDetected(String combo);
    }
}
