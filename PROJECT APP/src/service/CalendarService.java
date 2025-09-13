package service;

import model.*;

public class CalendarService {
      public void showMonth(User user, int month, int year) {
        CalendarMonth cm = user.getCalendarList().getMonth(month, year);
        if (cm == null) {
            System.out.println("Bulan tidak ditemukan.");
            return;
        }
        printCalendar(user, month, year);
    }

    public void printCalendar(User user, int month, int year) {
        CalendarMonth cm = user.getCalendarList().getMonth(month, year);
        if (cm == null) return;

        System.out.println("Kalender Progress " + month + "/" + year);
        for (int d = 1; d <= 31; d++) {
            DailyProgress[] dp = cm.getDayProgress(d);
            if (dp == null) continue;
            System.out.print("Hari " + d + ": ");
            for (DailyProgress p : dp) {
                if (p.isDone()) {
                    System.out.print("[" + p.getPrayerName() + "✔] ");
                } else {
                    System.out.print("[" + p.getPrayerName() + " ] ");
                }
            }
            System.out.println();
        }
    }
}