import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Combo {
    private final LinkedList<Integer> keySequence;
    private final int sequenceSize;
    private ComboListener listener;

    public Combo() {
        keySequence = new LinkedList<>();
        sequenceSize = 4; // 波動拳のシーケンス長は4
    }

    public void setComboListener(ComboListener listener) {
        this.listener = listener;
    }

    public void addKey(int keyCode) {
        if (keySequence.size() >= sequenceSize) {
            keySequence.removeFirst(); // Remove the oldest key
        }
        keySequence.add(keyCode);

        // Check for commands
        if (detectCommand()) {
            if (listener != null) {
                listener.onComboDetected("波動拳");
            }
        }
    }

    private boolean detectCommand() {
        // Define the sequence for 波動拳: ↓, ↘︎, →, P
        int[] hadoukenSequence = {
            KeyEvent.VK_S,   // ↓
            KeyEvent.VK_S, KeyEvent.VK_D,   // ↘︎ (for demonstration, consider simultaneous S and D as ↘︎)
            KeyEvent.VK_D,   // →
            KeyEvent.VK_O    // P
        };

        if (keySequence.size() < hadoukenSequence.length) {
            return false;
        }

        // Create a simple representation of ↘︎ as a simultaneous press of ↓ and →
        LinkedList<Integer> transformedSequence = new LinkedList<>(keySequence);
        for (int i = 0; i < transformedSequence.size() - 1; i++) {
            if (transformedSequence.get(i) == KeyEvent.VK_S && transformedSequence.get(i + 1) == KeyEvent.VK_D) {
                transformedSequence.set(i, KeyEvent.VK_S | KeyEvent.VK_D); // Treat as ↘︎
                transformedSequence.remove(i + 1);
            }
        }

        for (int i = 0; i < hadoukenSequence.length; i++) {
            if (transformedSequence.get(transformedSequence.size() - hadoukenSequence.length + i) != hadoukenSequence[i]) {
                return false;
            }
        }

        return true;
    }

    public interface ComboListener {
        void onComboDetected(String combo);
    }
}
