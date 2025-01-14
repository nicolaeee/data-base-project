package ClientLogic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryLogic {

    private List<String> history;

    public HistoryLogic() {
        history = new ArrayList<>();
    }

    // Metodă pentru a adăuga un eveniment în istoric
    public void addAction(String action) {
        history.add(action);
    }

    // Metodă pentru afișarea istoricului într-o fereastră
    public void showHistory(JFrame parentFrame) {
        JFrame historyFrame = new JFrame("Istoric Utilizare");
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setSize(600, 400);
        historyFrame.setLocationRelativeTo(parentFrame);

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);

        // Construim conținutul istoricului
        StringBuilder historyContent = new StringBuilder();
        for (String action : history) {
            historyContent.append(action).append("\n");
        }
        historyArea.setText(historyContent.toString());

        JScrollPane scrollPane = new JScrollPane(historyArea);
        historyFrame.add(scrollPane);

        historyFrame.setVisible(true);
    }
}
