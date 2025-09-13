package model;

import java.time.LocalTime;

public class PrayerList {
     private PrayerNode head;

    public void addPrayer(String prayerName, LocalTime prayerTime) {
        PrayerNode newNode = new PrayerNode(prayerName, prayerTime);
        if (head == null) {
            head = newNode;
            head.setNext(head);
        } else {
            PrayerNode temp = head;
            while (temp.getNext() != head) {
                temp = temp.getNext();
            }
            temp.setNext(newNode);
            newNode.setNext(head);
        }
    }

    public PrayerNode getNextPrayer() {
        if (head == null) return null;
        PrayerNode temp = head;
        LocalTime now = LocalTime.now();
        do {
            if (temp.getPrayerTime().isAfter(now)) {
                return temp;
            }
            temp = temp.getNext();
        } while (temp != head);
        return head;
    }

    public void printPrayers() {
        if (head == null) return;
        PrayerNode temp = head;
        do {
            System.out.println(temp.getPrayerName() + " - " + temp.getPrayerTime());
            temp = temp.getNext();
        } while (temp != head);
    }
}