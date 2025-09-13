package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.*;
import model.*;
import util.*;

public class CalendarPage extends JPanel {
    private MainFrame frame;
    private User user;
    private ReminderManager reminderManager;
    private CalendarMonth currentMonth;
    private JLabel lblTitle;
    private JPanel calendarGrid;

    private static final Color PINK = new Color(255, 182, 193);

    public CalendarPage(MainFrame frame, User user, ReminderManager reminderManager) {
        this.frame = frame;
        this.user = user;
        this.reminderManager = reminderManager;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initHeader();
        initGrid();
        
        java.time.LocalDate today = java.time.LocalDate.now();
        currentMonth = user.getCalendarList().getMonth(today.getMonthValue(), today.getYear());
        if (currentMonth == null) {
            currentMonth = user.getCalendarList().getHead();
        }
        if (currentMonth == null) {
            lblTitle.setText("Kalender - Tidak ada data");
        } else {
            refreshView();
        }
    }

    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton btnPrev = new JButton("←");
        JButton btnNext = new JButton("→");
        MainFrame.styleRoundedButton(btnPrev);
        MainFrame.addHoverEffect(btnPrev);

        MainFrame.styleRoundedButton(btnNext);
        MainFrame.addHoverEffect(btnNext);

        lblTitle = new JLabel("", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(Color.WHITE);
        left.add(btnPrev);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(btnNext);

        header.add(left, BorderLayout.WEST);
        header.add(lblTitle, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        btnPrev.addActionListener(e -> {
            if (currentMonth != null && currentMonth.getPrev() != null) {
                currentMonth = currentMonth.getPrev();
                refreshView();
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentMonth != null && currentMonth.getNext() != null) {
                currentMonth = currentMonth.getNext();
                refreshView();
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    private void initGrid() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        JPanel weekdayHeader = new JPanel(new GridLayout(1, 7, 6, 6));
        weekdayHeader.setBackground(Color.WHITE);
        weekdayHeader.setBorder(BorderFactory.createEmptyBorder(6, 12, 0, 12));

        String[] days = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            weekdayHeader.add(lbl);
        }

        container.add(weekdayHeader, BorderLayout.NORTH);

        calendarGrid = new JPanel(new GridLayout(0, 7, 6, 6));
        calendarGrid.setBackground(Color.WHITE);
        calendarGrid.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));

        JScrollPane sp = new JScrollPane(calendarGrid);
        sp.setBorder(BorderFactory.createEmptyBorder());
        container.add(sp, BorderLayout.CENTER);

        add(container, BorderLayout.CENTER);
    }


    public void refreshView() {
        if (currentMonth == null) return;

        YearMonth ym = YearMonth.of(currentMonth.getYear(), currentMonth.getMonth());
        String monthName = ym.getMonth().getDisplayName(TextStyle.FULL, new Locale("id"));
        lblTitle.setText(String.format("Kalender - %s %d", capitalize(monthName), currentMonth.getYear()));

        calendarGrid.removeAll();
        int daysInMonth = ym.lengthOfMonth();
        LocalDate firstOfMonth = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), 1);
        int startOffset = firstOfMonth.getDayOfWeek().getValue() - 1;

        for (int i = 0; i < startOffset; i++) {
            calendarGrid.add(createEmptyCell());
        }
        for (int d = 1; d <= daysInMonth; d++) {
            DailyProgress[] dayProgress = currentMonth.getDayProgress(d);

        boolean allDone = true;
        if (dayProgress == null) {
            allDone = false;
        } else {
            for (DailyProgress dp : dayProgress) {
                if (!dp.isDone()) { allDone = false; break; }
            }
        }

        JPanel dayCell = new JPanel(new BorderLayout());
        dayCell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        dayCell.setBackground(Color.WHITE);

        JLabel dayLabel = new JLabel(String.valueOf(d), SwingConstants.CENTER);
        dayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        dayCell.add(dayLabel, BorderLayout.NORTH);

        JLabel summary = new JLabel(allDone ? "✓ semua" : "belum lengkap", SwingConstants.CENTER);
        summary.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dayCell.add(summary, BorderLayout.CENTER);

        if (allDone) dayCell.setBackground(PINK);

        final int dayClicked = d;
        final Font origDayFont = dayLabel.getFont();
        final Font hoverFont = origDayFont.deriveFont(origDayFont.getSize2D() + 6f);
        final Font pressFont = origDayFont.deriveFont(origDayFont.getSize2D() + 10f);

        dayCell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        MouseAdapter dayAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                dayLabel.setFont(hoverFont);
                dayLabel.revalidate(); dayLabel.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                dayLabel.setFont(origDayFont);
                dayLabel.revalidate(); dayLabel.repaint();
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                dayLabel.setFont(pressFont);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                java.awt.Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), dayCell);
                if (dayCell.contains(p)) {
                    dayLabel.setFont(hoverFont);
                } else {
                    dayLabel.setFont(origDayFont);
                }
            }
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    DailyDetailDialog dlg = new DailyDetailDialog(frame, currentMonth, dayClicked);
                    dlg.setVisible(true);
                    refreshView();
                });
            }
        };

        dayCell.addMouseListener(dayAdapter);
        dayLabel.addMouseListener(dayAdapter);

        calendarGrid.add(dayCell);
        }

        int totalCells = startOffset + daysInMonth;
        int remainder = totalCells % 7;
        if (remainder != 0) {
            int toAdd = 7 - remainder;
            for (int i = 0; i < toAdd; i++) calendarGrid.add(createEmptyCell());
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private void styleArrow(JButton b) {
        b.setBackground(PINK);
        b.setOpaque(true);
        b.setFocusPainted(false);
    }

    private Component createEmptyCell() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return p;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}