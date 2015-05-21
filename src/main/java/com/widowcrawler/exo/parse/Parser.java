package com.widowcrawler.exo.parse;

import com.widowcrawler.exo.SitemapParseException;
import com.widowcrawler.exo.model.ChangeFreq;
import com.widowcrawler.exo.model.Sitemap;
import com.widowcrawler.exo.model.SitemapURL;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Scott Mansfield
 *
 * urlset
 *   url*
 *     loc - URI (required)
 *     lastmod - Date/Time
 *     changefreq - ChangeFreq
 *     priority - Double
 */
public class Parser {

    private static String getEventTypeString(int eventType) {
        switch (eventType) {
            case XMLStreamConstants.START_ELEMENT: return "START_ELEMENT";
            case XMLStreamConstants.END_ELEMENT: return "END_ELEMENT";
            case XMLStreamConstants.PROCESSING_INSTRUCTION: return "PROCESSING_INSTRUCTION";
            case XMLStreamConstants.CHARACTERS: return "CHARACTERS";
            case XMLStreamConstants.COMMENT: return "COMMENT";
            case XMLStreamConstants.START_DOCUMENT: return "START_DOCUMENT";
            case XMLStreamConstants.END_DOCUMENT: return "END_DOCUMENT";
            case XMLStreamConstants.ENTITY_REFERENCE: return "ENTITY_REFERENCE";
            case XMLStreamConstants.ATTRIBUTE: return "ATTRIBUTE";
            case XMLStreamConstants.DTD: return "DTD";
            case XMLStreamConstants.CDATA: return "CDATA";
            case XMLStreamConstants.SPACE: return "SPACE";
            case XMLStreamConstants.ENTITY_DECLARATION: return "ENTITY_DECLARATION";
            case XMLStreamConstants.NAMESPACE: return "NAMESPACE";
            case XMLStreamConstants.NOTATION_DECLARATION: return "NOTATION_DECLARATION";
        }
        return "UNKNOWN_EVENT_TYPE: " + eventType;
    }

    private enum State {
        START,
        END,
        URLSET,
        URL,
        URL_PROP_LOC,
        URL_PROP_LASTMOD,
        URL_PROP_CHANGEFREQ,
        URL_PROP_PRIORITY
    }

    private State state = State.START;

    public Sitemap parse(String data) throws IOException, XMLStreamException, SitemapParseException {
        return parse(new ByteArrayInputStream(data.getBytes("utf-8")));
    }

    public Sitemap parse(InputStream inputStream) throws XMLStreamException, SitemapParseException {

        final XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream, "utf-8");
        final Set<SitemapURL> sitemapURLs = new HashSet<>();
        SitemapURL.Builder urlBuilder = null;

        reader.getEventType();

        while (reader.hasNext()) {
            switch (state) {
                case START:
                    reader.nextTag();
                    state = State.URLSET;
                    break;

                case URLSET:
                    // If we're done with the URLs, we're done overall
                    if (reader.nextTag() == XMLStreamConstants.END_ELEMENT) {
                        state = State.END;
                        break;
                    }

                    urlBuilder = new SitemapURL.Builder();
                    state = State.URL;
                    break;

                case URL:
                    reader.nextTag();

                    if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                        switch (StringUtils.lowerCase(reader.getLocalName())) {
                            case "loc": state = State.URL_PROP_LOC; break;
                            case "lastmod": state = State.URL_PROP_LASTMOD; break;
                            case "changefreq": state = State.URL_PROP_CHANGEFREQ; break;
                            case "priority": state = State.URL_PROP_PRIORITY; break;
                            default:
                                // on any other tag, ignore for now
                                // throw new SitemapParseException("Unexpected tag in url: " + reader.getLocalName());
                                // emulate other methods
                                reader.getElementText();
                        }
                    } else if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                        // we're done collecting the data for this URL
                        assert urlBuilder != null;
                        sitemapURLs.add(urlBuilder.build());
                        urlBuilder = new SitemapURL.Builder();
                        state = State.URLSET;
                    }
                    break;

                case URL_PROP_LOC:
                    try {
                        assert urlBuilder != null;
                        urlBuilder.withLocation(new URL(StringUtils.trimToNull(reader.getElementText())));
                    } catch (MalformedURLException ex) {
                        System.out.println(ex.getMessage());
                        // nothing for now?
                    }
                    state = State.URL;
                    break;

                case URL_PROP_LASTMOD:
                    assert urlBuilder != null;
                    urlBuilder.withLastModified(DateTime.parse(reader.getElementText()));
                    state = State.URL;
                    break;

                case URL_PROP_CHANGEFREQ:
                    assert urlBuilder != null;
                    urlBuilder.withChangeFrequency(ChangeFreq.valueOf(StringUtils.upperCase(reader.getElementText())));
                    state = State.URL;
                    break;

                case URL_PROP_PRIORITY:
                    assert urlBuilder != null;
                    urlBuilder.withPriority(Double.valueOf(reader.getElementText()));
                    state = State.URL;
                    break;

                case END:
                    // consume all end tags
                    reader.next();
                    break;
            }

            //System.out.println(state);
        }

        return new Sitemap(sitemapURLs);
    }
}
