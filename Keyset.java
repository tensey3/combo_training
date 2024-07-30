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
    private final JPanel dirPanel;
    private final JLabel dirLabel;
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
    private String lastDir = "";
    private final Timer timer;
    private boolean updatePending = false;
    private final int imgSize;
    private final Combo combo; // Comboクラスのインスタンスを追加

    public Keyset(Combo combo) {
        setLayout(new BorderLayout());

        dirPanel = new JPanel();
        dirPanel.setLayout(new BoxLayout(dirPanel, BoxLayout.Y_AXIS)); // 縦並びに設定
        add(dirPanel, BorderLayout.CENTER);

        dirLabel = new JLabel();
        add(dirLabel, BorderLayout.NORTH);

        Font font = new Font("SansSerif", Font.BOLD, 34);
        imgSize = font.getSize();

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
            handleKey(e, true);
            combo.addKey(e.getKeyCode()); // Comboクラスにキー入力を通知
        } catch (Exception ex) {
            // ex.printStackTrace(); // エラーログを出力
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            handleKey(e, false);
        } catch (Exception ex) {
            // ex.printStackTrace(); // エラーログを出力
        }
    }

    private void handleKey(KeyEvent e, boolean isPressed) {
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
                            updateDir();
                        } catch (Exception e) {
                            // e.printStackTrace(); // エラーログを出力
                        }
                        updatePending = false;
                    });
                }
            }, 10); // 10ミリ秒の遅延
        }
    }

    public void updateDir() {
        try {
            String newDir = getDir();
            if (!newDir.isEmpty() && !newDir.equals(lastDir)) {
                JPanel newPanel = new JPanel();
                newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.X_AXIS)); // 同時押しの場合横並び

                String[] dirs = newDir.split(",");
                for (String dir : dirs) {
                    ImageIcon icon = getIcon(dir.trim());
                    if (icon != null) {
                        JLabel newDirLabel = new JLabel(icon);
                        newPanel.add(newDirLabel);
                    }
                }
                dirPanel.add(newPanel, 0); // 新しい出力を上に追加
                dirPanel.revalidate();
                dirPanel.repaint();
                lastDir = newDir;
            } else if (newDir.isEmpty()) {
                dirLabel.setIcon(null);
                lastDir = "";
            }
        } catch (Exception e) {
            // e.printStackTrace(); // エラーログを出力
        }
    }

    public String getDir() {
        try {
            StringBuilder dirBuilder = new StringBuilder();
            if ((wPressed || spacePressed) && aPressed) {
                dirBuilder.append("↖️,");
            } else if ((wPressed || spacePressed) && dPressed) {
                dirBuilder.append("↗︎,");
            } else if (sPressed && aPressed) {
                dirBuilder.append("↙︎,");
            } else if (sPressed && dPressed) {
                dirBuilder.append("↘︎,");
            } else if (wPressed || spacePressed) {
                dirBuilder.append("↑,");
            } else if (sPressed) {
                dirBuilder.append("↓,");
            } else if (aPressed) {
                dirBuilder.append("←,");
            } else if (dPressed) {
                dirBuilder.append("→,");
            }
            if (jPressed) {
                dirBuilder.append("弱K,");
            }
            if (kPressed) {
                dirBuilder.append("中K,");
            }
            if (lPressed) {
                dirBuilder.append("強K,");
            }
            if (uPressed) {
                dirBuilder.append("弱P,");
            }
            if (iPressed) {
                dirBuilder.append("中P,");
            }
            if (oPressed) {
                dirBuilder.append("強P,");
            }
            if (dirBuilder.length() > 0) {
                dirBuilder.setLength(dirBuilder.length() - 1); // 最後のカンマを削除
            }
            return dirBuilder.toString();
        } catch (Exception e) {
            // e.printStackTrace(); // エラーログを出力
            return "";
        }
    }

    // 画像パスを取得するメソッドです
    private ImageIcon getIcon(String dir) {
        String imageName = switch (dir) {
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
            Image newImg = img.getScaledInstance(imgSize, imgSize, Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        } catch (Exception e) {
            // e.printStackTrace(); // ログにエラーを出力
            return new ImageIcon(); // デフォルトのアイコンを返す
        }
    }
}