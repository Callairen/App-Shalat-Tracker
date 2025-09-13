package ui;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import model.*;
import service.*;

public class ProgressPage extends JPanel {
    private MainFrame frame;
    private User user;
    private PrayerList prayerList;
    private PrayerService prayerService;
    private JLabel lblTitle;
    private JPanel listPanel;
    private static final Color PINK = new Color(255, 182, 193);

    public ProgressPage(MainFrame frame, User user, PrayerList prayerList, PrayerService prayerService) {
        this.frame = frame;
        this.user = user;
        this.prayerList = prayerList;
        this.prayerService = prayerService;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initHeader();
        initList();
        refreshView();
    }

    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        lblTitle = new JLabel("Progress Hari Ini", SwingConstants.LEFT);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

        header.add(lblTitle, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
    }

    private void initList() {
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
    }

    public void refreshView() {
        listPanel.removeAll();

        java.time.LocalDate today = java.time.LocalDate.now();

        CalendarMonth month = user.getCalendarList().getMonth(today.getMonthValue(), today.getYear());
        if (month == null) {
            listPanel.add(new JLabel("Tidak ada data kalender untuk bulan ini."));
            revalidate(); repaint();
            return;
        }
        DailyProgress[] dps = month.getDayProgress(today.getDayOfMonth());
        if (dps == null) {
            listPanel.add(new JLabel("Tidak ada progress untuk hari ini."));
            revalidate(); repaint();
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        for (DailyProgress dp : dps) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Color.WHITE);
            row.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

            JLabel name = new JLabel(dp.getPrayerName());
            name.setFont(new Font("SansSerif", Font.BOLD, 16));         
            name.setOpaque(true);
            name.setBackground(new Color(250, 240, 240));               
            name.setForeground(new Color(60, 60, 60));                  
            name.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10)); 

            JLabel status = new JLabel(dp.isDone() ? "✔ " + dp.getCheckInTime().format(fmt) : "Belum");
            status.setFont(new Font("SansSerif", Font.PLAIN, 12));     
            status.setForeground(Color.GRAY);
            status.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); 

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            leftPanel.setBackground(Color.WHITE);
            leftPanel.add(name);
            leftPanel.add(status);

            row.add(leftPanel, BorderLayout.CENTER);

            JButton btn = new JButton(dp.isDone() ? "Done" : "Check-in");
            MainFrame.styleRoundedButton(btn);
            MainFrame.addHoverEffect(btn);

            btn.setEnabled(!dp.isDone());


            btn.addActionListener(e -> {
                dp.markDone(java.time.LocalTime.now());
                status.setText("✔ " + dp.getCheckInTime().format(fmt));
                btn.setEnabled(false);

                frame.getCalendarPage().refreshView();

            });

            row.add(btn, BorderLayout.EAST);
            listPanel.add(row);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}