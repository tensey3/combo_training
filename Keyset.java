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

public class Keyset extends JPanel implements KeyListener {
    private final JPanel directionPanel;
    private final JLabel currentDirectionLabel;
    private boolean isAKeyPressed = false;
    private boolean isWKeyPressed = false;
    private boolean isSKeyPressed = false;
    private boolean isDKeyPressed = false;
    private boolean isSpaceKeyPressed = false;
    private boolean isJKeyPressed = false;
    private boolean isKKeyPressed = false;
    private boolean isLKeyPressed = false;
    private boolean isUKeyPressed = false;
    private boolean isIKeyPressed = false;
    private boolean isOKeyPressed = false;
    private String lastDirection = "";
    public final Timer timer;
    private boolean isUpdatePending = false;
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
            case KeyEvent.VK_A -> isAKeyPressed = isPressed;
            case KeyEvent.VK_W -> isWKeyPressed = isPressed;
            case KeyEvent.VK_S -> isSKeyPressed = isPressed;
            case KeyEvent.VK_D -> isDKeyPressed = isPressed;
            case KeyEvent.VK_SPACE -> isSpaceKeyPressed = isPressed;
            case KeyEvent.VK_J -> isJKeyPressed = isPressed;
            case KeyEvent.VK_K -> isKKeyPressed = isPressed;
            case KeyEvent.VK_L -> isLKeyPressed = isPressed;
            case KeyEvent.VK_U -> isUKeyPressed = isPressed;
            case KeyEvent.VK_I -> isIKeyPressed = isPressed;
            case KeyEvent.VK_O -> isOKeyPressed = isPressed;
        }
        scheduleUpdate();
    }

    private void scheduleUpdate() {
        if (!isUpdatePending) {
            isUpdatePending = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateDirection();
                    isUpdatePending = false;
                }
            }, 10); // 10ミリ秒の遅延
        }
    }

    public void updateDirection() {
        String newcomen = getDirection();
        if (!newcomen.isEmpty() && !newcomen.equals(lastDirection)) {
            ImageIcon icon = getDirectionIcon(newcomen);
            JLabel newDirectionLabel = new JLabel(icon); // 新しい方向のラベルを作成
            directionPanel.add(newDirectionLabel, 0); // リストの先頭に追加する
            directionPanel.revalidate();
            lastDirection = newcomen;
        } else if (newcomen.isEmpty()) {
            currentDirectionLabel.setIcon(null);
            lastDirection = "";
        }
    }

    public String getDirection() {
        if ((isWKeyPressed || isSpaceKeyPressed) && isAKeyPressed) {
            return "↖️";
        } else if ((isWKeyPressed || isSpaceKeyPressed) && isDKeyPressed) {
            return "↗︎";
        } else if (isSKeyPressed && isAKeyPressed) {
            return "↙︎";
        } else if (isSKeyPressed && isDKeyPressed) {
            return "↘︎";
        } else if (isWKeyPressed || isSpaceKeyPressed) {
            return "↑";
        } else if (isSKeyPressed) {
            return "↓";
        } else if (isAKeyPressed) {
            return "←";
        } else if (isDKeyPressed) {
            return "→";
        } else if (isJKeyPressed) {
            return "弱K";
        } else if (isKKeyPressed) {
            return "中K";
        } else if (isLKeyPressed) {
            return "強K";
        } else if (isUKeyPressed) {
            return "弱P";
        } else if (isIKeyPressed) {
            return "中P";
        } else if (isOKeyPressed) {
            return "強P";
        }// else if (isUKeyPressed || isJKeyPressed) {
        //     return "掴み";
        // }
        return "";
    }

    public ImageIcon getDirectionIcon(String direction) {
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
