package util;

import model.PrayerList;
import model.PrayerNode;

public class ReminderManager {
    private PrayerList prayerList;

    public ReminderManager(PrayerList prayerList) {
        this.prayerList = prayerList;
    }
    public void startReminder() {
        System.out.println("Reminder Salat (simulasi):");
        prayerList.printPrayers();
    }
    public PrayerNode getNextReminder() {
        return prayerList.getNextPrayer();
    }
}