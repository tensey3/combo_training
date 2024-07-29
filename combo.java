import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Combo {
    private final LinkedList<KeyPress> keySequence;
    private final int inputCount;
    private ComboListener listener;
    private final Map<String, int[][]> commandMap;
    private static final int TIME_THRESHOLD = 200; // 200ミリ秒以内の入力を許容

    public Combo() {
        keySequence = new LinkedList<>();
        commandMap = initializeCommands();
        inputCount = calculateMaxSequenceLength();
    }

    public void setComboListener(ComboListener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        long currentTime = System.currentTimeMillis();
        keySequence.add(new KeyPress(keyCode, currentTime));

        if (keySequence.size() > inputCount) {
            keySequence.removeFirst();
        }

        System.out.println("Key added: " + KeyEvent.getKeyText(keyCode));
        System.out.println("Current sequence: " + keySequence);

        detectAndNotify();
    }

    private void detectAndNotify() {
        // 昇竜拳を優先的にチェック
        if (detectCommand(commandMap.get("昇龍拳"))) {
            if (listener != null) {
                listener.onComboDetected("昇龍拳");
            }
            clearCommandSequence(commandMap.get("昇龍拳")[0].length);
            return;
        }

        // 波動拳をチェック
        if (detectCommand(commandMap.get("波動拳"))) {
            if (listener != null) {
                listener.onComboDetected("波動拳");
            }
            clearCommandSequence(commandMap.get("波動拳")[0].length);
        }
    }

    private boolean detectCommand(int[][] commandSequences) {
        if (keySequence.size() < commandSequences[0].length) {
            return false;
        }

        for (int[] commandSequence : commandSequences) {
            if (detectWithTimeThreshold(commandSequence)) {
                System.out.println("Command detected: " + commandSequenceToString(commandSequence));
                return true;
            }
        }
        return false;
    }

    private boolean detectWithTimeThreshold(int[] commandSequence) {
        int matchCount = 0;
        long previousTime = 0;

        for (int i = 0; i < commandSequence.length; i++) {
            KeyPress keyPress = keySequence.get(keySequence.size() - commandSequence.length + i);

            if (keyPress.keyCode == commandSequence[i] || 
                (i == 1 && commandSequence[i] == (KeyEvent.VK_S | KeyEvent.VK_D) && keyPress.keyCode == KeyEvent.VK_D && 
                 keySequence.get(keySequence.size() - commandSequence.length + i - 1).keyCode == KeyEvent.VK_S)) {
                if (previousTime == 0 || (keyPress.time - previousTime <= TIME_THRESHOLD)) {
                    matchCount++;
                    previousTime = keyPress.time;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return matchCount == commandSequence.length;
    }

    private void clearCommandSequence(int length) {
        for (int i = 0; i < length; i++) {
            if (!keySequence.isEmpty()) {
                keySequence.removeFirst();
            }
        }
        System.out.println("Sequence cleared");
    }

    private String commandSequenceToString(int[] commandSequence) {
        StringBuilder sb = new StringBuilder();
        for (int key : commandSequence) {
            sb.append(KeyEvent.getKeyText(key)).append(" ");
        }
        return sb.toString().trim();
    }

    private Map<String, int[][]> initializeCommands() {
        Map<String, int[][]> map = new HashMap<>();
        map.put("昇龍拳", new int[][]{
            {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_O},    // → ↓ ↘︎ 強P
            {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_U},    // → ↓ ↘︎ 弱P
            {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_I}     // → ↓ ↘︎ 中P
        });
        map.put("波動拳", new int[][]{
            {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_O},    // ↓ ↘︎ → 強P
            {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_U},    // ↓ ↘︎ → 弱P
            {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_I}     // ↓ ↘︎ → 中P
        });
        return map;
    }

    private int calculateMaxSequenceLength() {
        int maxLength = 0;
        for (int[][] sequences : commandMap.values()) {
            for (int[] sequence : sequences) {
                if (sequence.length > maxLength) {
                    maxLength = sequence.length;
                }
            }
        }
        return maxLength;
    }

    public interface ComboListener {
        void onComboDetected(String combo);
    }

    private static class KeyPress {
        final int keyCode;
        final long time;

        KeyPress(int keyCode, long time) {
            this.keyCode = keyCode;
            this.time = time;
        }

        @Override
        public String toString() {
            return KeyEvent.getKeyText(keyCode) + "@" + time;
        }
    }
}
