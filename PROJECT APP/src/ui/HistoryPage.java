package ui;

import java.awt.*;
import javax.swing.*;
import model.*;

public class HistoryPage extends JPanel {
    private MainFrame frame;
    private User user;
    private JPanel listPanel;
    private static final Color PINK = new Color(255, 182, 193);

    public HistoryPage(MainFrame frame, User user) {
        this.frame = frame;
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initHeader();
        initList();
        refreshView();
    }

    private void initHeader() {
        JLabel hdr = new JLabel("History Kalender", SwingConstants.LEFT);
        hdr.setFont(new Font("SansSerif", Font.BOLD, 16));
        hdr.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        add(hdr, BorderLayout.NORTH);
    }

    private void initList() {
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
    }

    public void refreshView() {
        listPanel.removeAll();
        CalendarMonth cursor = user.getCalendarList().getHead();
        if (cursor == null) {
            listPanel.add(new JLabel("Belum ada data kalender."));
            revalidate(); repaint();
            return;
        }

        java.util.Set<CalendarMonth> visited = new java.util.HashSet<>();
        while (cursor != null && !visited.contains(cursor)) {
            visited.add(cursor);

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(8,8,8,8)
            ));

            JLabel title = new JLabel("Bulan " + cursor.getMonth() + " / " + cursor.getYear());
            title.setFont(new Font("SansSerif", Font.BOLD, 14));
            title.setOpaque(true);
            title.setBackground(new Color(250, 240, 240));
            title.setForeground(new Color(60, 60, 60)); 
            title.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            card.add(title, BorderLayout.NORTH);

            int completeDays = 0;
            int totalDays = 0;
            for (int d = 1; d <= 31; d++) {
                DailyProgress[] dps = cursor.getDayProgress(d);
                if (dps == null) continue;
                totalDays++;
                boolean all = true;
                for (DailyProgress dp : dps) {
                    if (!dp.isDone()) { all = false; break; }
                }
                if (all) completeDays++;
            }

            JLabel body = new JLabel(String.format("Hari lengkap: %d / %d", completeDays, totalDays));
            card.add(body, BorderLayout.CENTER);

            listPanel.add(card);

            cursor = cursor.getNext();
        }

        revalidate();
        repaint();
    }
}