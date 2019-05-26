import java.util.Calendar;

public class CalendarEvent implements Comparable<CalendarEvent>{
    public CalendarEvent(Calendar calendar, String description, String place) {
        this.calendar = calendar;
        this.description = description;
        this.place = place;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "Date: " + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH)+1) + "/" +
                calendar.get(Calendar.YEAR) + " " +
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) +
                ", Place: " + place +
                ", description='" + description + '\'' +
                '}';
    }
    private Calendar calendar;

    private String description;

    private String place;

    @Override
    public int compareTo(CalendarEvent o) {
        int tmp = this.calendar.compareTo(o.calendar);
        tmp += this.description.compareTo(o.description);
        return tmp;
    }
}
