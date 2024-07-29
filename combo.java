import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Combo {
    private final LinkedList<Integer> keySequence;
    private final int inputCount;
    private ComboListener listener;
    private final Map<String, int[][]> commandMap; // int[]からint[][]に変更

    public Combo() {
        keySequence = new LinkedList<>();
        commandMap = initializeCommands();
        inputCount = calculateMaxSequenceLength(); // 最大シーケンス長を動的に設定
    }

    public void setComboListener(ComboListener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        try {
            keySequence.add(keyCode);

            if (keySequence.size() > inputCount) {
                keySequence.removeFirst(); // 古いキーを削除
            }

            detectAndNotify();
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
        }
    }

    private void detectAndNotify() {
        try {
            for (Map.Entry<String, int[][]> entry : commandMap.entrySet()) {
                if (detectCommand(entry.getValue())) {
                    if (listener != null) {
                        listener.onComboDetected(entry.getKey());
                    }
                    clearCommandSequence(entry.getValue()[0].length); // コマンドのシーケンスを削除
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
        }
    }

    private boolean detectCommand(int[][] commandSequences) {
        if (keySequence.size() < commandSequences[0].length) {
            return false;
        }

        for (int[] commandSequence : commandSequences) {
            boolean match = true;
            for (int i = 0; i < commandSequence.length; i++) {
                if (keySequence.get(keySequence.size() - commandSequence.length + i) != commandSequence[i]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return true;
            }
        }
        return false;
    }

    private void clearCommandSequence(int length) {
        try {
            for (int i = 0; i < length; i++) {
                if (!keySequence.isEmpty()) {
                    keySequence.removeFirst();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
        }
    }

    private Map<String, int[][]> initializeCommands() {
        try {
            Map<String, int[][]> map = new HashMap<>();
            map.put("波動拳", new int[][]{
                {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_D, KeyEvent.VK_O},    // ↓ ↘︎ → P
                {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_D, KeyEvent.VK_U},    // ↓ ↘︎ → 弱P
                {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_D, KeyEvent.VK_I}     // ↓ ↘︎ → 中P
            });
            map.put("昇龍拳", new int[][]{
                {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_O},    // → ↓ ↘︎ P
                {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_U},    // → ↓ ↘︎ 弱P
                {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_I}     // → ↓ ↘︎ 中P
            });
            // ここで追加のコマンドを定義可能
            return map;
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
            return new HashMap<>();
        }
    }

    private int calculateMaxSequenceLength() {
        try {
            int maxLength = 0;
            for (int[][] sequences : commandMap.values()) {
                for (int[] sequence : sequences) {
                    if (sequence.length > maxLength) {
                        maxLength = sequence.length;
                    }
                }
            }
            return maxLength;
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
            return 0;
        }
    }

    public interface ComboListener {
        void onComboDetected(String combo);
    }
}
