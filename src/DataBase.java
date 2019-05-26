import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataBase {

    List<CalendarEvent> calendarEvents;

    DataBase() {
        calendarEvents = new ArrayList<>();
        //////////////////////////// stub
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.MAY,25,14,30);
        calendarEvents.add(new CalendarEvent(calendar, "dupa", "dasd"));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2019,Calendar.MAY,25,14,30);
        calendarEvents.add(new CalendarEvent(calendar2, "dupa2", "dasd"));
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2019,Calendar.MAY,26,14,30);
        calendarEvents.add(new CalendarEvent(calendar3, "dupa3", "dasd"));
        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(2019,4,25,19,5);
        calendarEvents.add(new CalendarEvent(calendar4, "dupa4", "dasd"));
    }

    public final List<CalendarEvent> getEvents() {
        return calendarEvents;
    }

    public void deleteEvent(CalendarEvent calendarEvent) {
        calendarEvents.remove(calendarEvent);
    }

    public void addEvent(CalendarEvent calendarEvent) {
        calendarEvents.add(calendarEvent);
    }

    public void exportDataToXML() throws ParserConfigurationException, TransformerException {
        System.out.println("dupa");
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element root = document.createElement("events");
        document.appendChild(root);

        for (CalendarEvent calendarEvent : calendarEvents) {
            Element event = document.createElement("event");
            root.appendChild(event);

            Element calendar = document.createElement("calendar");
            event.appendChild(calendar);

            Element year = document.createElement("year");
            calendar.appendChild(year);
            year.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.YEAR))));
            Element month = document.createElement("month");
            calendar.appendChild(month);
            month.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.MONTH)+1)));
            Element day = document.createElement("day");
            calendar.appendChild(day);
            day.appendChild(document.createTextNode(String.valueOf(calendarEvent.getCalendar().get(Calendar.DAY_OF_MONTH))));

            Element description = document.createElement("description");
            event.appendChild(description);
            description.appendChild(document.createTextNode(calendarEvent.getDescription()));

            Element place = document.createElement("place");
            event.appendChild(place);
            place.appendChild(document.createTextNode(calendarEvent.getPlace()));
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("cos.xml"));

        transformer.transform(domSource, streamResult);

        System.out.println("Done creating XML File");
    }

    public void importDataFromXML(File file, boolean override) throws ParserConfigurationException, IOException, SAXException {
        if (override) calendarEvents.clear();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        for (int i = 0; i < document.getElementsByTagName("event").getLength(); i++) {
            Element e = (Element) document.getElementsByTagName("event").item(i);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(e.getElementsByTagName("year").item(0).getTextContent()),Integer.valueOf(e.getElementsByTagName("month").item(0).getTextContent())-1,Integer.valueOf(e.getElementsByTagName("day").item(0).getTextContent()),0,0);
            CalendarEvent calendarEvent = new CalendarEvent(calendar, e.getElementsByTagName("description").item(0).getTextContent(),e.getElementsByTagName("place").item(0).getTextContent());
            System.out.println(calendarEvent);
            calendarEvents.add(calendarEvent);
        }
    }
}
