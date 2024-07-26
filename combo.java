import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Combo {
    private final LinkedList<Integer> keySequence;
    private final int sequenceSize;
    private ComboListener listener;
    private final Map<String, int[]> commandMap;

    public Combo() {
        keySequence = new LinkedList<>();
        sequenceSize = 4; // シーケンス長は4
        commandMap = initializeCommands();
    }

    public void setComboListener(ComboListener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        if (keySequence.size() >= sequenceSize) {
            keySequence.removeFirst(); // 最も古いキーを削除
        }
        keySequence.add(keyCode);

        detectAndNotify();
    }

    private void detectAndNotify() {
        for (Map.Entry<String, int[]> entry : commandMap.entrySet()) {
            if (detectCommand(entry.getValue())) {
                if (listener != null) {
                    listener.onComboDetected(entry.getKey());
                }
                return;
            }
        }
    }

    private boolean detectCommand(int[] commandSequence) {
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

    private Map<String, int[]> initializeCommands() {
        Map<String, int[]> map = new HashMap<>();
        map.put("波動拳", new int[]{
            KeyEvent.VK_S,   // ↓
            KeyEvent.VK_D,   // ↘︎ (下右方向)
            KeyEvent.VK_D,   // →
            KeyEvent.VK_O    // P
        });
        map.put("昇龍拳", new int[]{
            KeyEvent.VK_D,   // →
            KeyEvent.VK_S,   // ↓
            KeyEvent.VK_D,   // ↘︎ (下右方向)
            KeyEvent.VK_O    // P
        });
        return map;
    }

    public interface ComboListener {
        void onComboDetected(String combo);
    }
}
