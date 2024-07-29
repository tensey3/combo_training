import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Flam frame = new Flam();
                frame.setVisible(true);
            } catch (Exception e) {
                // Remove the line that prints the stack trace
            }
        });
    }
}
