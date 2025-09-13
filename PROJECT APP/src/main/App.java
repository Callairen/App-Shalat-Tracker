package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import model.*;
import service.*;
import util.*;

public class App {
     private static final String[] PRAYER_NAMES = {"Subuh", "Zuhur", "Asar", "Maghrib", "Isya"};

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        User user = new User("Adena");
        LocalDate today = LocalDate.now();
        user.getCalendarList().addMonth(today.getMonthValue(), today.getYear(), PRAYER_NAMES);

        PrayerList prayerList = new PrayerList();
        prayerList.addPrayer("Subuh", LocalTime.of(5, 0));
        prayerList.addPrayer("Zuhur", LocalTime.of(12, 0));
        prayerList.addPrayer("Asar", LocalTime.of(15, 0));
        prayerList.addPrayer("Maghrib", LocalTime.of(18, 0));
        prayerList.addPrayer("Isya", LocalTime.of(19, 30));

        ReminderManager reminderManager = new ReminderManager(prayerList);
        PrayerService prayerService = new PrayerService();
        CalendarService calendarService = new CalendarService();

        int choice;
        do {
            System.out.println("\n=== Salat Tracker ===");
            System.out.println("1. Lihat progress hari ini");
            System.out.println("2. Check-in salat");
            System.out.println("3. Lihat kalender bulan");
            System.out.println("4. Lihat reminder berikutnya");
            System.out.println("5. Keluar");
            System.out.print("Pilih: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    DailyProgress[] progress = user.getCalendarList().getHead()
                            .getDayProgress(today.getDayOfMonth());
                    for (DailyProgress dp : progress) {
                        dp.printProgress();
                    }
                    break;
                case 2:
                    System.out.print("Masukkan nama salat: ");
                    String prayerName = input.nextLine();
                    prayerService.checkIn(user, today.getDayOfMonth(), prayerName);
                    break;
                case 3:
                    calendarService.showMonth(user, today.getMonthValue(), today.getYear());
                    break;
                case 4:
                    PrayerNode nextPrayer = reminderManager.getNextReminder();
                    if (nextPrayer != null) {
                        System.out.println("Reminder berikutnya: " + nextPrayer.getPrayerName() +
                                " pukul " + nextPrayer.getPrayerTime());
                    } else {
                        System.out.println("Tidak ada reminder.");
                    }
                    break;
                case 5:
                    System.out.println("Keluar...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 5);

        input.close();
    }
}