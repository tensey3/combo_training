import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Flam frame = new Flam();//ここでインスタンスかをしているようだね
            frame.setVisible(true); //ここが大事だったんだんだね
        });
    }
}