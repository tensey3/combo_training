import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class combo {
    private final List<Integer> keySequence;
    private final int maxSequenceSize;

    public combo() {
        keySequence = new ArrayList<>();
        maxSequenceSize = 10;
    }

    public void addKey(int keyCode) {
        if (keySequence.size() >= maxSequenceSize) {
            keySequence.remove(0); // Remove the oldest key
        }
        keySequence.add(keyCode);

        // Check for commands
        if (detectCommand()) {
            // 波動拳が入力された
            System.out.println("波動拳");
        }
    }

    private boolean detectCommand() {
        // Define the sequence for 波動拳: ↓, ↘︎, →, P
        int[] hadoukenSequence = {
            KeyEvent.VK_S,   // ↓
            KeyEvent.VK_S | KeyEvent.VK_D,   // ↘︎
            KeyEvent.VK_D,   // →
            KeyEvent.VK_O    // P
        };

        if (keySequence.size() < hadoukenSequence.length) {
            return false;
        }

        for (int i = 0; i < hadoukenSequence.length; i++) {
            if (keySequence.get(keySequence.size() - hadoukenSequence.length + i) != hadoukenSequence[i]) {
                return false;
            }
        }

        return true;
    }
}
