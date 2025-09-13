package model;

public class User {
     private String name;
    private CalendarList calendarList;

    public User(String name) {
        this.name = name;
        this.calendarList = new CalendarList();
    }

    public String getName() { 
        return name; 
    }
    public CalendarList getCalendarList() { 
        return calendarList; 
    }
}