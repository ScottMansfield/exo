package com.widowcrawler.exo.parse;

import com.widowcrawler.exo.model.SiteMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Scott Mansfield
 */
public class Parser {
    private static Set<Integer> textEvents;

    static {
        Set<Integer> temp = new HashSet<Integer>();
        temp.add(XMLEvent.CHARACTERS);
        temp.add(XMLEvent.COMMENT);
        temp.add(XMLEvent.CDATA);
        temp.add(XMLEvent.SPACE);
        temp.add(XMLEvent.ENTITY_REFERENCE);
        temp.add(XMLEvent.DTD);

        textEvents = Collections.unmodifiableSet(temp);
    }

    public SiteMap parse(String data) throws IOException, XMLStreamException {
        return parse(new ByteArrayInputStream(data.getBytes("utf-8")));
    }

    public SiteMap parse(InputStream inputStream) throws XMLStreamException {

        final XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream, "utf-8");

        while (reader.hasNext()) {
            int eventType = reader.next();

            System.out.println(getEventTypeString(eventType));

            if (eventType == XMLEvent.START_ELEMENT) {
                System.out.println(reader.getLocalName());
            }

            if (textEvents.contains(eventType)) {
                System.out.println(reader.getText());
            }
        }

        return null;
    }

    public final static String getEventTypeString(int eventType) {
        switch (eventType) {
            case XMLEvent.START_ELEMENT:
                return "START_ELEMENT";

            case XMLEvent.END_ELEMENT:
                return "END_ELEMENT";

            case XMLEvent.PROCESSING_INSTRUCTION:
                return "PROCESSING_INSTRUCTION";

            case XMLEvent.CHARACTERS:
                return "CHARACTERS";

            case XMLEvent.COMMENT:
                return "COMMENT";

            case XMLEvent.START_DOCUMENT:
                return "START_DOCUMENT";

            case XMLEvent.END_DOCUMENT:
                return "END_DOCUMENT";

            case XMLEvent.ENTITY_REFERENCE:
                return "ENTITY_REFERENCE";

            case XMLEvent.ATTRIBUTE:
                return "ATTRIBUTE";

            case XMLEvent.DTD:
                return "DTD";

            case XMLEvent.CDATA:
                return "CDATA";

            case XMLEvent.SPACE:
                return "SPACE";
        }
        return "UNKNOWN_EVENT_TYPE , " + eventType;
    }


}
