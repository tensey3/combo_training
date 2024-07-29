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
    private final Combo combo; // Comboクラスのインスタンスを追加

    public Keyset(Combo combo) {
        setLayout(new BorderLayout());

        directionPanel = new JPanel();
        directionPanel.setLayout(new BoxLayout(directionPanel, BoxLayout.Y_AXIS)); // 縦並びに設定
        add(directionPanel, BorderLayout.CENTER);

        currentDirectionLabel = new JLabel();
        add(currentDirectionLabel, BorderLayout.NORTH);

        Font font = new Font("SansSerif", Font.BOLD, 34);
        imageSize = font.getSize();

        timer = new Timer();
        this.combo = combo; // Comboクラスのインスタンスを初期化
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // キーがタイプされたときの処理
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            handleKeyState(e, true);
            combo.addKey(e.getKeyCode()); // Comboクラスにキー入力を通知
        } catch (Exception ex) {
            ex.printStackTrace(); // エラーログを出力
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            handleKeyState(e, false);
        } catch (Exception ex) {
            ex.printStackTrace(); // エラーログを出力
        }
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
                        try {
                            updateDirection();
                        } catch (Exception e) {
                            e.printStackTrace(); // エラーログを出力
                        }
                        updatePending = false;
                    });
                }
            }, 10); // 10ミリ秒の遅延
        }
    }

    public void updateDirection() {
        try {
            String newDirection = getDirection();
            if (!newDirection.isEmpty() && !newDirection.equals(lastDirection)) {
                JPanel newPanel = new JPanel();
                newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.X_AXIS)); // 同時押しの場合横並び

                String[] directions = newDirection.split(",");
                for (String direction : directions) {
                    ImageIcon icon = getDirectionIcon(direction.trim());
                    if (icon != null) {
                        JLabel newDirectionLabel = new JLabel(icon);
                        newPanel.add(newDirectionLabel);
                    }
                }
                directionPanel.add(newPanel, 0); // 新しい出力を上に追加
                directionPanel.revalidate();
                directionPanel.repaint();
                lastDirection = newDirection;
            } else if (newDirection.isEmpty()) {
                currentDirectionLabel.setIcon(null);
                lastDirection = "";
            }
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
        }
    }

    public String getDirection() {
        try {
            StringBuilder directionBuilder = new StringBuilder();
            if ((wPressed || spacePressed) && aPressed) {
                directionBuilder.append("↖️,");
            } else if ((wPressed || spacePressed) && dPressed) {
                directionBuilder.append("↗︎,");
            } else if (sPressed && aPressed) {
                directionBuilder.append("↙︎,");
            } else if (sPressed && dPressed) {
                directionBuilder.append("↘︎,");
            } else if (wPressed || spacePressed) {
                directionBuilder.append("↑,");
            } else if (sPressed) {
                directionBuilder.append("↓,");
            } else if (aPressed) {
                directionBuilder.append("←,");
            } else if (dPressed) {
                directionBuilder.append("→,");
            }
            if (jPressed) {
                directionBuilder.append("弱K,");
            }
            if (kPressed) {
                directionBuilder.append("中K,");
            }
            if (lPressed) {
                directionBuilder.append("強K,");
            }
            if (uPressed) {
                directionBuilder.append("弱P,");
            }
            if (iPressed) {
                directionBuilder.append("中P,");
            }
            if (oPressed) {
                directionBuilder.append("強P,");
            }
            if (directionBuilder.length() > 0) {
                directionBuilder.setLength(directionBuilder.length() - 1); // 最後のカンマを削除
            }
            return directionBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace(); // エラーログを出力
            return "";
        }
    }

    // 画像パスを取得するメソッドです
    private ImageIcon getDirectionIcon(String direction) {
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
        try {
            ImageIcon icon = new ImageIcon(imageName);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        } catch (Exception e) {
            e.printStackTrace(); // ログにエラーを出力
            return new ImageIcon(); // デフォルトのアイコンを返す
        }
    }
}
