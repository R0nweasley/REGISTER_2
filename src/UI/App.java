package UI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public App() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            new LoginFrame().setVisible(true);
        });
    }
}
