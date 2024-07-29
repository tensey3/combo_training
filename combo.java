import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Combo {
    private final LinkedList<Integer> keySequence;
    private final int inputCount;
    private ComboListener listener;
    private final Map<String, int[][]> commandMap;

    public Combo() {
        keySequence = new LinkedList<>();
        commandMap = initializeCommands();
        inputCount = calculateMaxSequenceLength();
    }

    public void setComboListener(ComboListener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        keySequence.add(keyCode);

        if (keySequence.size() > inputCount) {
            keySequence.removeFirst();
        }

        System.out.println("Key added: " + KeyEvent.getKeyText(keyCode));
        System.out.println("Current sequence: " + keySequence);

        detectAndNotify();
    }

    private void detectAndNotify() {
        for (Map.Entry<String, int[][]> entry : commandMap.entrySet()) {
            if (detectCommand(entry.getValue())) {
                if (listener != null) {
                    listener.onComboDetected(entry.getKey());
                }
                clearCommandSequence(entry.getValue()[0].length);
                return;
            }
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
                System.out.println("Command detected: " + commandSequenceToString(commandSequence));
                return true;
            }
        }
        return false;
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
        map.put("波動拳", new int[][]{
            {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_D, KeyEvent.VK_O},    // ↓ ↘︎ → 強P
            {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_D, KeyEvent.VK_U},    // ↓ ↘︎ → 弱P
            {KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_D, KeyEvent.VK_I}     // ↓ ↘︎ → 中P
        });
        map.put("昇龍拳", new int[][]{
            {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_O},    // → ↓ ↘︎ 強P
            {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_U},    // → ↓ ↘︎ 弱P
            {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_I}     // → ↓ ↘︎ 中P
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
}
