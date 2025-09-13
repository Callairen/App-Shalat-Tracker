package model;

import java.time.LocalTime;

public class DailyProgress {
     private String prayerName;
    private boolean isDone;
    private LocalTime checkInTime;
    private DailyProgress next;

    public DailyProgress(String prayerName) {
        this.prayerName = prayerName;
        this.isDone = false;
    }

    public void markDone(LocalTime checkInTime) {
        this.isDone = true;
        this.checkInTime = checkInTime;
    }

    public void printProgress() {
        String status = isDone ? "[✔] " + prayerName + " " + checkInTime : "[ ] " + prayerName;
        System.out.println(status);
    }

    public String getPrayerName() { 
        return prayerName; 
    }
    public boolean isDone() { 
        return isDone; 
    }
    public LocalTime getCheckInTime() { 
        return checkInTime; 
    }

    public DailyProgress getNext() { 
        return next; 
    }
    public void setNext(DailyProgress next) { 
        this.next = next; 
    }
}