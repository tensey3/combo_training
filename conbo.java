import java.util.HashMap;
import java.util.Map;

public class conbo {
    public static void main(String[] args) {
        Map<Integer, String> buttonMap = new HashMap<>();
        buttonMap.put(1, "↓");
        buttonMap.put(2, "↘︎");
        buttonMap.put(3, "→");

        // int[] combo = {1, 2, 3}; // Remove this line
        String[] strength = {"弱", "中", "強"};

        // Simulating button presses
        int[] buttonPresses = {1, 2, 3, 2, 3, 3}; // Replace with actual button presses

        StringBuilder comboString = new StringBuilder();
        for (int buttonPress : buttonPresses) {
            comboString.append(buttonMap.get(buttonPress));
        }

        if (comboString.toString().equals("↓↘︎→")) {
            for (int pButtonPress : buttonMap.keySet()) {
                if (pButtonPress >= 0 && pButtonPress < strength.length) {
                    System.out.println("波動拳");
                }
            }
        }
    }
}
