package service;

import java.time.LocalTime;
import model.*;

public class PrayerService {
     public void checkIn(User user, int day, String prayerName) {
        CalendarMonth month = user.getCalendarList().getHead();
        if (month == null) return;
        DailyProgress[] progress = month.getDayProgress(day);
        if (progress == null) return;

        for (DailyProgress dp : progress) {
            if (dp.getPrayerName().equalsIgnoreCase(prayerName)) {
                dp.markDone(LocalTime.now());
                System.out.println("Check-in berhasil: " + prayerName);
                return;
            }
        }
        System.out.println("Salat tidak ditemukan.");
    }
}
