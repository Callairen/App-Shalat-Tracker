package model;

import java.time.LocalTime;


public class PrayerNode {
     private String prayerName;
    private LocalTime prayerTime;
    private PrayerNode next;

    public PrayerNode(String prayerName, LocalTime prayerTime) {
        this.prayerName = prayerName;
        this.prayerTime = prayerTime;
    }

    public String getPrayerName() { 
        return prayerName; 
    }
    public void setPrayerName(String prayerName) { 
        this.prayerName = prayerName; 
    }

    public LocalTime getPrayerTime() { 
        return prayerTime; 
    }
    public void setPrayerTime(LocalTime prayerTime) { 
        this.prayerTime = prayerTime; 
    }

    public PrayerNode getNext() { 
        return next;
    }
    public void setNext(PrayerNode next) { 
        this.next = next;
    }
}