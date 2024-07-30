import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Combo {
    private final LinkedList<KeyPress> keyList;
    private final int maxKeys;
    private Listener listener;
    private final Map<String, int[][]> combos;
    private static final int TIME_LIMIT = 200; // 200ミリ秒以内の入力を許容

    public Combo() {
        keyList = new LinkedList<>();
        combos = initCombos();
        maxKeys = getMaxComboLength();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        long currentTime = System.currentTimeMillis();
        keyList.add(new KeyPress(keyCode, currentTime));

        if (keyList.size() > maxKeys) {
            keyList.removeFirst();
        }

        System.out.println("Key added: " + KeyEvent.getKeyText(keyCode));
        System.out.println("Current sequence: " + keyList);

        checkCombos();
    }

    private void checkCombos() {
        if (checkCombo(combos.get("昇龍拳"))) {
            if (listener != null) {
                listener.onComboDetected("昇龍拳");
            }
            clearKeys(combos.get("昇龍拳")[0].length);
            return;
        }

        if (checkCombo(combos.get("波動拳"))) {
            if (listener != null) {
                listener.onComboDetected("波動拳");
            }
            clearKeys(combos.get("波動拳")[0].length);
            return;
        }

        if (checkCombo(combos.get("竜巻旋風脚"))) {
            if (listener != null) {
                listener.onComboDetected("竜巻旋風脚");
            }
            clearKeys(combos.get("竜巻旋風脚")[0].length);
        }
    }

    private boolean checkCombo(int[][] sequences) {
        if (keyList.size() < sequences[0].length) {
            return false;
        }

        for (int[] sequence : sequences) {
            if (checkWithTimeLimit(sequence)) {
                System.out.println("Combo detected: " + seqToString(sequence));
                return true;
            }
        }
        return false;
    }

    private boolean checkWithTimeLimit(int[] sequence) {
        int matchCount = 0;
        long prevTime = 0;

        for (int i = 0; i < sequence.length; i++) {
            KeyPress keyPress = keyList.get(keyList.size() - sequence.length + i);

            if (keyPress.keyCode == sequence[i] ||
                (i == 1 && sequence[i] == (KeyEvent.VK_S | KeyEvent.VK_D) && keyPress.keyCode == KeyEvent.VK_D &&
                keyList.get(keyList.size() - sequence.length + i - 1).keyCode == KeyEvent.VK_S)) {
                if (prevTime == 0 || (keyPress.time - prevTime <= TIME_LIMIT)) {
                    matchCount++;
                    prevTime = keyPress.time;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return matchCount == sequence.length;
    }

    private void clearKeys(int length) {
        for (int i = 0; i < length; i++) {
            if (!keyList.isEmpty()) {
                keyList.removeFirst();
            }
        }
        System.out.println("Sequence cleared");
    }

    private String seqToString(int[] sequence) {
        StringBuilder sb = new StringBuilder();
        for (int key : sequence) {
            sb.append(KeyEvent.getKeyText(key)).append(" ");
        }
        return sb.toString().trim();
    }

    private Map<String, int[][]> initCombos() {
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
        map.put("竜巻旋風脚", new int[][]{
            {KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_J},    // ↓ ↙︎ ← 強P
            {KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_K},    // ↓ ↙︎ ← 弱P
            {KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_L}     // ↓ ↙︎ ← 中P
        });
        return map;
    }

    private int getMaxComboLength() {
        int maxLength = 0;
        for (int[][] sequences : combos.values()) {
            for (int[] sequence : sequences) {
                if (sequence.length > maxLength) {
                    maxLength = sequence.length;
                }
            }
        }
        return maxLength;
    }

    public interface Listener {
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