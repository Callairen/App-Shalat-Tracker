package ui;

import java.awt.*;
import javax.swing.*;
import util.*;

public class ReminderPopup extends JDialog {
    private ReminderManager reminderManager;
    private static final Color PINK = new Color(255, 182, 193);

    public ReminderPopup(Frame owner, ReminderManager reminderManager) {
        super(owner, "Reminder Berikutnya", true);
        this.reminderManager = reminderManager;
        initUI();
        setSize(360, 160);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(2,1));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        model.PrayerNode next = reminderManager.getNextReminder();
        if (next == null) {
            center.add(new JLabel("Tidak ada reminder.", SwingConstants.CENTER));
        } else {
            JLabel l1 = new JLabel("Reminder berikutnya:", SwingConstants.CENTER);
            l1.setFont(new Font("SansSerif", Font.BOLD, 14));
            JLabel l2 = new JLabel(next.getPrayerName() + " — " + next.getPrayerTime().toString(), SwingConstants.CENTER);
            l2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            center.add(l1);
            center.add(l2);
        }

        add(center, BorderLayout.CENTER);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        foot.setBackground(Color.WHITE);
        JButton ok = new JButton("OK");
        MainFrame.styleRoundedButton(ok);
        MainFrame.addHoverEffect(ok);

        ok.addActionListener(e -> dispose());
        foot.add(ok);
        add(foot, BorderLayout.SOUTH);
    }
}