package model;

public class CalendarList {
     private CalendarMonth head;

    public void addMonth(int month, int year, String[] prayerNames) {
        CalendarMonth newMonth = new CalendarMonth(month, year, prayerNames);
        if (head == null) {
            head = newMonth;
        } else {
            CalendarMonth temp = head;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(newMonth);
            newMonth.setPrev(temp);
        }
    }

    public CalendarMonth getMonth(int month, int year) {
        CalendarMonth temp = head;
        while (temp != null) {
            if (temp.getMonth() == month && temp.getYear() == year) {
                return temp;
            }
            temp = temp.getNext();
        }
        return null;
    }

    public CalendarMonth getHead() { 
        return head; 
    }
}