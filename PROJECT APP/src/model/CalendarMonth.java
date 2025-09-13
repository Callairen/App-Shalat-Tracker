package model;

public class CalendarMonth {
   private int month;
    private int year;
    private DailyProgress[][] days;
    private CalendarMonth prev;
    private CalendarMonth next;

    public CalendarMonth(int month, int year, String[] prayerNames) {
        this.month = month;
        this.year = year;
        this.days = new DailyProgress[32][prayerNames.length];
        for (int d = 1; d <= 31; d++) {
            for (int i = 0; i < prayerNames.length; i++) {
                days[d][i] = new DailyProgress(prayerNames[i]);
            }
        }
    }

    public DailyProgress[] getDayProgress(int day) {
        if (day < 1 || day > 31) return null;
        return days[day];
    }

    public int getMonth() { 
        return month; 
    }
    public int getYear() { 
        return year; 
    }

    public CalendarMonth getPrev() { 
        return prev; 
    }
    public void setPrev(CalendarMonth prev) { 
        this.prev = prev; 
    }

    public CalendarMonth getNext() { 
        return next; 
    }
    public void setNext(CalendarMonth next) { 
        this.next = next; 
    }
}