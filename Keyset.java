import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
//ここは触んない
public class Keyset extends JPanel implements KeyListener {
    private final JPanel directionPanel;
    private final JLabel currentDirectionLabel;
    private boolean aPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private boolean spacePressed = false;
    private boolean jPressed = false;
    private boolean kPressed = false;
    private boolean lPressed = false;
    private boolean uPressed = false;
    private boolean iPressed = false;
    private boolean oPressed = false;
    private String lastDirection = "";
    private final Timer timer;
    private boolean updatePending = false;
    private final int imageSize;

    public Keyset() {
        setLayout(new BorderLayout());

        directionPanel = new JPanel();
        directionPanel.setLayout(new BoxLayout(directionPanel, BoxLayout.Y_AXIS));
        add(directionPanel, BorderLayout.CENTER);

        currentDirectionLabel = new JLabel();
        add(currentDirectionLabel, BorderLayout.NORTH);

        Font font = new Font("SansSerif", Font.BOLD, 34);
        imageSize = font.getSize();

        timer = new Timer();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // キーがタイプされたときの処理
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleKeyState(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleKeyState(e, false);
    }

    private void handleKeyState(KeyEvent e, boolean isPressed) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_A -> aPressed = isPressed;
            case KeyEvent.VK_W -> wPressed = isPressed;
            case KeyEvent.VK_S -> sPressed = isPressed;
            case KeyEvent.VK_D -> dPressed = isPressed;
            case KeyEvent.VK_SPACE -> spacePressed = isPressed;
            case KeyEvent.VK_J -> jPressed = isPressed;
            case KeyEvent.VK_K -> kPressed = isPressed;
            case KeyEvent.VK_L -> lPressed = isPressed;
            case KeyEvent.VK_U -> uPressed = isPressed;
            case KeyEvent.VK_I -> iPressed = isPressed;
            case KeyEvent.VK_O -> oPressed = isPressed;
        }
        scheduleUpdate();
    }

    private void scheduleUpdate() {
        if (!updatePending) {
            updatePending = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> {
                        updateDirection();
                        updatePending = false;
                    });
                }
            }, 10); // 10ミリ秒の遅延
        }
    }

    public void updateDirection() {
        String newDirection = getDirection();
        if (!newDirection.isEmpty() && !newDirection.equals(lastDirection)) {
            ImageIcon icon = getDirectionIcon(newDirection);
            JLabel newDirectionLabel = new JLabel(icon);
            directionPanel.add(newDirectionLabel, 0);
            directionPanel.revalidate();
            lastDirection = newDirection;
        } else if (newDirection.isEmpty()) {
            currentDirectionLabel.setIcon(null);
            lastDirection = "";
        }
    }

    public String getDirection() {
        if ((wPressed || spacePressed) && aPressed) {
            return "↖️";
        } else if ((wPressed || spacePressed) && dPressed) {
            return "↗︎";
        } else if (sPressed && aPressed) {
            return "↙︎";
        } else if (sPressed && dPressed) {
            return "↘︎";
        } else if (wPressed || spacePressed) {
            return "↑";
        } else if (sPressed) {
            return "↓";
        } else if (aPressed) {
            return "←";
        } else if (dPressed) {
            return "→";
        } else if (jPressed) {
            return "弱K";
        } else if (kPressed) {
            return "中K";
        } else if (lPressed) {
            return "強K";
        } else if (uPressed) {
            return "弱P";
        } else if (iPressed) {
            return "中P";
        } else if (oPressed) {
            return "強P";
        }
        return "";
    }

    private  ImageIcon getDirectionIcon(String direction) {
        String imageName = switch (direction) {
            case "↖️" -> "images/up_left.png";
            case "↗︎" -> "images/up_right.png";
            case "↙︎" -> "images/down_left.png";
            case "↘︎" -> "images/down_right.png";
            case "↑" -> "images/up.png";
            case "↓" -> "images/down.png";
            case "←" -> "images/left.png";
            case "→" -> "images/right.png";
            case "弱K" -> "images/JK.png";
            case "中K" -> "images/TK.png";
            case "強K" -> "images/KK.png";
            case "弱P" -> "images/JP.png";
            case "中P" -> "images/TP.png";
            case "強P" -> "images/KP.png";
            default -> null;
        };
        if (imageName == null) return null;

        ImageIcon icon = new ImageIcon(imageName);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
}