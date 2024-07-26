import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Combo {
    private final LinkedList<Integer> keySequence;
    private final int inputCount;
    private ComboListener listener;
    private final Map<String, int[]> commandMap;

    public Combo() {
        keySequence = new LinkedList<>();
        commandMap = initializeCommands();
        inputCount = calculateMaxSequenceLength(); // 最大シーケンス長を動的に設定
    }

    public void setComboListener(ComboListener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        if (keySequence.size() >= inputCount) {
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

        // キーシーケンスが正しい順序で入力されているか確認
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
        // ここで追加のコマンドを定義可能
        return map;
    }

    private int calculateMaxSequenceLength() {
        int maxLength = 0;
        for (int[] sequence : commandMap.values()) {
            if (sequence.length > maxLength) {
                maxLength = sequence.length;
            }
        }
        return maxLength;
    }

    public interface ComboListener {
        void onComboDetected(String combo);
    }
}
