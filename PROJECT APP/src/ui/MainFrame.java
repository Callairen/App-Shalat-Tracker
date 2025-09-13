package ui;

import java.awt.*;
import javax.swing.*;
import model.*;
import service.*;
import util.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;
    private User user;
    private PrayerList prayerList;
    private ReminderManager reminderManager;
    private CalendarService calendarService;
    private PrayerService prayerService;
    private FileHandler fileHandler;
    private CalendarPage calendarPage;
    private ProgressPage progressPage;
    private HistoryPage historyPage;

    public MainFrame() {
        super("Shalat Tracker - GUI");
        initDomain();
        initUI();
    }

    private void initDomain() {
        user = new User("Nana");
        String[] PRAYER_NAMES = {"Subuh", "Zuhur", "Asar", "Maghrib", "Isya"};

        java.time.LocalDate today = java.time.LocalDate.now();
        int m = today.getMonthValue();
        int y = today.getYear();
        int nextMonth = m == 12 ? 1 : m + 1;
        int nextYear  = m == 12 ? y + 1 : y;
        int prevMonth = m == 1  ? 12 : m - 1;
        int prevYear  = m == 1  ? y - 1 : y;

        user.getCalendarList().addMonth(prevMonth, prevYear, PRAYER_NAMES);
        user.getCalendarList().addMonth(m, y, PRAYER_NAMES);
        user.getCalendarList().addMonth(nextMonth, nextYear, PRAYER_NAMES);
        prayerList = new PrayerList();
        prayerList.addPrayer("Subuh", java.time.LocalTime.of(5, 0));
        prayerList.addPrayer("Zuhur", java.time.LocalTime.of(12, 0));
        prayerList.addPrayer("Asar", java.time.LocalTime.of(15, 0));
        prayerList.addPrayer("Maghrib", java.time.LocalTime.of(18, 0));
        prayerList.addPrayer("Isya", java.time.LocalTime.of(19, 30));

        reminderManager = new ReminderManager(prayerList);
        prayerService = new PrayerService();
        calendarService = new CalendarService();
        fileHandler = new FileHandler();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 640);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setBackground(Color.WHITE);

        calendarPage = new CalendarPage(this, user, reminderManager);
        progressPage = new ProgressPage(this, user, prayerList, prayerService);
        historyPage = new HistoryPage(this, user);

        cards.add(calendarPage, "calendar");
        cards.add(progressPage, "progress");
        cards.add(historyPage, "history");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createTopBar(), BorderLayout.NORTH);
        getContentPane().add(cards, BorderLayout.CENTER);

        showPage("calendar");
    }

    private JPanel createTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel title = new JLabel("Shalat Tracker");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);

        JButton btnCalendar = new JButton("Calendar");
        JButton btnProgress = new JButton("Progress");
        JButton btnHistory = new JButton("History");
        JButton btnReminder = new JButton("Reminder");

        MainFrame.styleRoundedButton(btnCalendar);
        MainFrame.addHoverEffect(btnCalendar);

        MainFrame.styleRoundedButton(btnProgress);
        MainFrame.addHoverEffect(btnProgress);

        MainFrame.styleRoundedButton(btnHistory);
        MainFrame.addHoverEffect(btnHistory);

        MainFrame.styleRoundedButton(btnReminder);
        MainFrame.addHoverEffect(btnReminder);

        btnCalendar.addActionListener(e -> showPage("calendar"));
        btnProgress.addActionListener(e -> showPage("progress"));
        btnHistory.addActionListener(e -> showPage("history"));
        btnReminder.addActionListener(e -> {
            ReminderPopup popup = new ReminderPopup(this, reminderManager);
            popup.setVisible(true);
        });

        btns.add(btnCalendar);
        btns.add(btnProgress);
        btns.add(btnHistory);
        btns.add(btnReminder);
        top.add(title, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);

        return top;
    }

    private void styleTopButton(AbstractButton b) {
        b.setBackground(new Color(255, 182, 193));
        b.setFocusPainted(false);
        b.setOpaque(true);
    }

    public void showPage(String name) {
        cardLayout.show(cards, name);
        if ("calendar".equals(name)) calendarPage.refreshView();
        if ("progress".equals(name)) progressPage.refreshView();
        if ("history".equals(name)) historyPage.refreshView();
    }

    public User getUser() { 
        return user; 
    }
    public PrayerList getPrayerList() { 
        return prayerList; 
    }
    public ReminderManager getReminderManager() { 
        return reminderManager; 
    }
    public PrayerService getPrayerService() { 
        return prayerService; 
    }
    public CalendarPage getCalendarPage() {
    return calendarPage;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }

    public static void styleRoundedButton(JButton b) {
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setBackground(new Color(255,182,193));
        b.setForeground(Color.BLACK);
        b.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {

            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (c.isEnabled()) {
                    g2.setColor(b.getBackground());
                } else {
                    g2.setColor(Color.GRAY);
                }
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);

                FontMetrics fm = g2.getFontMetrics();
                int x = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
                int y = (c.getHeight() + fm.getAscent()) / 2 - 2;
                g2.setColor(b.getForeground());
                g2.drawString(b.getText(), x, y);

                g2.dispose();
            }
        });
    }

    public static void addHoverEffect(JButton b) {
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setFont(b.getFont().deriveFont(b.getFont().getSize2D() + 2f));
                b.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));
                b.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setFont(b.getFont().deriveFont(14f));
                b.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
                b.repaint();
            }
        });
    }
}