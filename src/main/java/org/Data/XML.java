package org.Data;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;

/**
 * The XML class allow to write and read DashboardSerializationModel to XML file.
 */
public class XML {
    /**
     * Method allows to write the DashboardSerializationModel to file where we gave the path.
     *
     * @param path                        the path to file
     * @param dashboardSerializationModel the dashboard serialization model
     * @throws IOException        the io exception
     * @throws XMLStreamException the xml stream exception
     */
    public void writeToXml(Path path, DashboardSerializationModel dashboardSerializationModel) throws IOException, XMLStreamException {
        try (OutputStream os = Files.newOutputStream(path)) {
            XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = null;
            try {
                writer = outputFactory.createXMLStreamWriter(os, "utf-8");
                writeDashboardElements(writer, dashboardSerializationModel);
            } finally {
                if (writer != null)
                    writer.close();
            }
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }


    private void writeDashboardElements(XMLStreamWriter writer, DashboardSerializationModel dashboardSerializationModel) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");
        writer.writeCharacters("\n");

        writer.writeStartElement("dashboard");
        writer.writeCharacters("\r\n\t");

        writeOnBoardComputer(writer, dashboardSerializationModel);
        writer.writeStartElement("counter");
        writer.writeCharacters(Integer.toString(dashboardSerializationModel.getCounter()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t");
        writer.writeStartElement("day-counter-1");
        writer.writeCharacters(Float.toString(dashboardSerializationModel.getDayCounter1()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t");
        writer.writeStartElement("day-counter-2");
        writer.writeCharacters(Float.toString(dashboardSerializationModel.getDayCounter2()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t");
        writer.writeStartElement("save-record-date");
        writer.writeCharacters(dashboardSerializationModel.getRecordDate().toString());
        writer.writeEndElement();


        writer.writeCharacters("\r\n");
        writer.writeEndElement();
        writer.writeCharacters("\r\n");
        writer.writeCharacters("\r\n");
        writer.writeEndDocument();
    }

    private void writeOnBoardComputer(XMLStreamWriter writer, DashboardSerializationModel dashboardSerializationModel) throws XMLStreamException {
        writer.writeStartElement("on-board-computer");

        writer.writeCharacters("\r\n\t\t");
        writer.writeStartElement("average-speed");
        writer.writeCharacters(Float.toString(dashboardSerializationModel.getAvgSpeed()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t\t");
        writer.writeStartElement("maximum-speed");
        writer.writeCharacters(Float.toString(dashboardSerializationModel.getMaxSpeed()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t\t");
        writer.writeStartElement("average-fuel-usage");
        writer.writeCharacters(Float.toString(dashboardSerializationModel.getAvgFuel()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t\t");
        writer.writeStartElement("maximum-fuel-usage");
        writer.writeCharacters(Float.toString(dashboardSerializationModel.getMaxFuel()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t\t");
        writer.writeStartElement("journey-distance");
        writer.writeCharacters(Float.toString(dashboardSerializationModel.getJourneyDistance()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t\t");
        writer.writeStartElement("journey-time-in-minutes");
        writer.writeCharacters(Integer.toString(dashboardSerializationModel.getJourneyMinutes()));
        writer.writeEndElement();

        writer.writeCharacters("\r\n\t");
        writer.writeEndElement();
        writer.writeCharacters("\r\n\t");
    }


    /**
     * Method allow to read from XML file DashboardSerializationModel object.
     *
     * @param path the path to the file
     * @return the DashboardSerializationModel
     * @throws XMLStreamException the xml stream exception
     */
    public DashboardSerializationModel readFromXML(Path path) throws XMLStreamException {
        InputStream is = null;
        try {
            is = new FileInputStream(String.valueOf(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = inputFactory.createXMLStreamReader(is);
            return readDashboard(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private DashboardSerializationModel readDashboard(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("dashboard"))
                        return readDashboardElements(reader);
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return readDashboardElements(reader);
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private DashboardSerializationModel readDashboardElements(XMLStreamReader reader) throws XMLStreamException {
        DashboardSerializationModel dashboardSerializationModel = new DashboardSerializationModel();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    switch (elementName) {
                        case "on-board-computer":
                            readOnBoardComputer(reader, dashboardSerializationModel);
                            break;
                        case "counter":
                            dashboardSerializationModel.setCounter(readInt(reader));
                            break;
                        case "day-counter-1":
                            dashboardSerializationModel.setDayCounter1(readFloat(reader));
                            break;
                        case "day-counter-2":
                            dashboardSerializationModel.setDayCounter2(readFloat(reader));
                            break;
                        case "save-record-date":
                            dashboardSerializationModel.setRecordDate(readDate(reader));
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return dashboardSerializationModel;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private Object readOnBoardComputer(XMLStreamReader reader, DashboardSerializationModel dashboardSerializationModel) throws XMLStreamException {
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    switch (elementName) {
                        case "average-speed":
                            dashboardSerializationModel.setAvgSpeed(readFloat(reader));
                            break;
                        case "maximum-speed":
                            dashboardSerializationModel.setMaxSpeed(readFloat(reader));
                            break;
                        case "average-fuel-usage":
                            dashboardSerializationModel.setAvgFuel(readFloat(reader));
                            break;
                        case "maximum-fuel-usage":
                            dashboardSerializationModel.setMaxFuel(readFloat(reader));
                            break;
                        case "journey-distance":
                            dashboardSerializationModel.setJourneyDistance(readFloat(reader));
                            break;
                        case "journey-time-in-minutes":
                            dashboardSerializationModel.setJourneyMinutes(readInt(reader));
                            break;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return null;
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }

    private int readInt(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Integer.parseInt(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid integer " + characters);
        }
    }

    private float readFloat(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Float.parseFloat(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid float " + characters);
        }
    }

    private Date readDate(XMLStreamReader reader) throws XMLStreamException {
        String characters = readCharacters(reader);
        try {
            return Date.valueOf(characters);
        } catch (NumberFormatException e) {
            throw new XMLStreamException("Invalid date " + characters);
        }
    }
}
