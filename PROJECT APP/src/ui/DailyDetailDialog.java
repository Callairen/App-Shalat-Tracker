package ui;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import model.*;

public class DailyDetailDialog extends JDialog {
    private CalendarMonth month;
    private int day;
    private User dummyUserRefForAction;
    private JPanel content;
    private static final Color PINK = new Color(255, 182, 193);

    public DailyDetailDialog(Frame owner, CalendarMonth month, int day) {
        super(owner, "Detail Hari - " + day, true);
        this.month = month;
        this.day = day;

        initUI();
        setSize(420, 320);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel hdr = new JLabel("Hari " + day + " - " + month.getMonth() + "/" + month.getYear(), SwingConstants.CENTER);
        hdr.setFont(new Font("SansSerif", Font.BOLD, 16));
        content.add(hdr, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        DailyProgress[] dps = month.getDayProgress(day);
        if (dps != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
            for (DailyProgress dp : dps) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

                JLabel name = new JLabel(dp.getPrayerName());
                name.setFont(new Font("SansSerif", Font.BOLD, 15));
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

                JButton btnMark = new JButton(dp.isDone() ? "Done" : "Check-in");
                btnMark.setEnabled(!dp.isDone());
                MainFrame.styleRoundedButton(btnMark);
                MainFrame.addHoverEffect(btnMark);

                btnMark.addActionListener(e -> {
                    dp.markDone(java.time.LocalTime.now());
                    status.setText("✔ " + dp.getCheckInTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    btnMark.setEnabled(false);
                });

                row.add(btnMark, BorderLayout.EAST);

                listPanel.add(row);
            }
        }

        JScrollPane sp = new JScrollPane(listPanel);
        sp.setBorder(BorderFactory.createEmptyBorder());
        content.add(sp, BorderLayout.CENTER);

        JButton close = new JButton("Tutup");
        MainFrame.styleRoundedButton(close);
        MainFrame.addHoverEffect(close);

        close.addActionListener(e -> dispose());

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        foot.setBackground(Color.WHITE);
        foot.add(close);
        content.add(foot, BorderLayout.SOUTH);

        setContentPane(content);
    }
}