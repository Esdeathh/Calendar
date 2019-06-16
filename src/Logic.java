import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Summary;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Logic {

    private DataBase dataBase;

    Logic(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public List<CalendarEvent> getAllEvents() {
        return dataBase.getEvents();
    }

    public List<CalendarEvent> checkEventsIn(int minutes) {
        Calendar calendar = Calendar.getInstance();
        return dataBase.getEvents().stream().filter(s -> s.getCalendar().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
                s.getCalendar().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                s.getCalendar().get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                (((s.getCalendar().get(Calendar.HOUR_OF_DAY))*60)+s.getCalendar().get(Calendar.MINUTE)) - (((calendar.get(Calendar.HOUR_OF_DAY))*60)+calendar.get(Calendar.MINUTE)) <= minutes &&
                (((s.getCalendar().get(Calendar.HOUR_OF_DAY))*60)+s.getCalendar().get(Calendar.MINUTE)) - (((calendar.get(Calendar.HOUR_OF_DAY))*60)+calendar.get(Calendar.MINUTE)) >= 0)
                .collect(Collectors.toList());
    }

    public List<CalendarEvent> checkEventsInSpecificDay(Calendar calendar) {
        return dataBase.getEvents().stream().filter(s -> s.getCalendar().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
                s.getCalendar().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                s.getCalendar().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)).collect(Collectors.toList());
    }

    public List<CalendarEvent> checkEventsInSpecificMonth(Calendar calendar) {
        return dataBase.getEvents().stream().filter(s -> s.getCalendar().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                s.getCalendar().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)).collect(Collectors.toList());
    }

    public void deleteEventsUnderDate(Calendar calendar) {
        List<CalendarEvent> toRemove = new ArrayList<>();
        for (CalendarEvent calendarEvent : dataBase.getEvents()) {
            if(calendarEvent.getCalendar().before(calendar)) toRemove.add(calendarEvent);
        }
        for (CalendarEvent calendarEvent : toRemove) {
            dataBase.deleteEvent(calendarEvent);
        }
    }

    public void createEvent(CalendarEvent calendarEvent) {
        dataBase.addEvent(calendarEvent);
    }

    public void importDataFromXML(File file, boolean override){
        try {
            dataBase.importDataFromXML(file, override);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void exportDataToXML() {
        try {
            dataBase.exportDataToXML();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void importDataFromDataBase(File file, boolean override){

    }

    public void exportDataToDataBase() {

    }

    public void createICSFile(CalendarEvent calendarEvent) {
        ICalendar ics = new ICalendar();
        VEvent event = new VEvent();

        System.out.println("File to write: " + calendarEvent);

        LocalDateTime localDateTime = LocalDateTime.of(
                calendarEvent.getCalendar().get(Calendar.YEAR),
                calendarEvent.getCalendar().get(Calendar.MONTH) + 1,
                calendarEvent.getCalendar().get(Calendar.DAY_OF_MONTH),
                calendarEvent.getCalendar().get(Calendar.HOUR_OF_DAY),
                calendarEvent.getCalendar().get(Calendar.MINUTE));
        Date eventTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        event.setDateStart(eventTime);
        event.setLocation(calendarEvent.getPlace());
        event.setDescription(calendarEvent.getDescription());

        ics.addEvent(event);

        try (FileWriter writer = new FileWriter("iCal.ics");
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(Biweekly.write(ics).go());

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
