import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Flam frame = new Flam();
                frame.setVisible(true);
            } catch (Exception e) {
                // e.printStackTrace(); // 例外が発生した場合にスタックトレースを出力
            }
        });
    }
}
