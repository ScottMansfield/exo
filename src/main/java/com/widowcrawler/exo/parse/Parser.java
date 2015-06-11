/**
 * Copyright 2015 Scott Mansfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.widowcrawler.exo.parse;

import com.widowcrawler.exo.Exo;
import com.widowcrawler.exo.SitemapParseException;
import com.widowcrawler.exo.model.ChangeFreq;
import com.widowcrawler.exo.model.Sitemap;
import com.widowcrawler.exo.model.SitemapURL;
import com.widowcrawler.exo.retry.Retry;
import com.widowcrawler.exo.retry.RetryFailedException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
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
 *
 * sitemapindex
 *   sitemap
 *     loc - URI (required)
 *     lastmod - Date/Time
 */
public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private static final String URLSET_TAG_NAME = "urlset";
    private static final String SITEMAPINDEX_TAG_NAME = "sitemapindex";

    private static final String URL_TAG_NAME = "url";
    private static final String SITEMAP_TAG_NAME = "sitemap";

    private static final String LOC_TAG_NAME = "loc";
    private static final String LASTMOD_TAG_NAME = "lastmod";
    private static final String CHANGEFREQ_TAG_NAME = "changefreq";
    private static final String PRIORITY_TAG_NAME = "priority";

    private static final String MOBILE_TAG_NAME = "mobile";

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
        URL_PROP_PRIORITY,
        URL_PROP_MOBILE,
        SITEMAPINDEX,
        SITEMAP,
        SITEMAP_PROP_LOC,
        SITEMAP_PROP_LASTMOD
    }

    private String decorate(String message, Location location) {
        message += " | Line " + location.getLineNumber();
        message += " | Column " + location.getColumnNumber();

        return message;
    }

    private State state = State.START;

    public Sitemap parse(String data) throws IOException, XMLStreamException, SitemapParseException {
        return parse(new ByteArrayInputStream(data.getBytes("utf-8")));
    }

    public Sitemap parse(InputStream inputStream) throws XMLStreamException, SitemapParseException {

        final XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream, "utf-8");

        final Sitemap retval = new Sitemap(new HashSet<>());

        final Set<SitemapURL> sitemapURLs = new HashSet<>();
        SitemapURL.Builder urlBuilder = null;
        String urlContent = null;

        reader.getEventType();

        while (reader.hasNext()) {
            switch (state) {
                case START:
                    reader.nextTag();

                    if (StringUtils.equalsIgnoreCase(reader.getLocalName(), URLSET_TAG_NAME)) {
                        state = State.URLSET;
                    } else if (StringUtils.equalsIgnoreCase(reader.getLocalName(), SITEMAPINDEX_TAG_NAME)) {
                        state = State.SITEMAPINDEX;
                    } else {
                        String message = "Invalid root element. Must be either urlset or sitemapindex";
                        logger.error(message);
                        throw new SitemapParseException(message);
                    }

                    break;

                case END:
                    // consume all end tags
                    if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
                        String message = decorate("There should be only one root element in each sitemap.xml", reader.getLocation());
                        logger.error(message);
                        throw new SitemapParseException(message);
                    }

                    reader.next();
                    break;

                /////////////////////
                // URLSET Hierarchy
                /////////////////////
                case URLSET:
                    // If we're done with the URLs, we're done overall
                    if (reader.nextTag() == XMLStreamConstants.END_ELEMENT) {
                        state = State.END;
                        break;
                    }

                    // Check that we're entering into a <url> element
                    if (!StringUtils.equalsIgnoreCase(reader.getLocalName(), URL_TAG_NAME)) {
                        String message = "A <urlset> element can only contain <url> elements. Found: " + reader.getLocalName();
                        logger.error(message);
                        throw new SitemapParseException(message);
                    }

                    urlBuilder = new SitemapURL.Builder();
                    state = State.URL;
                    break;

                case URL:
                    reader.nextTag();

                    if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                        switch (StringUtils.lowerCase(reader.getLocalName())) {
                            case LOC_TAG_NAME:        state = State.URL_PROP_LOC;        break;
                            case LASTMOD_TAG_NAME:    state = State.URL_PROP_LASTMOD;    break;
                            case CHANGEFREQ_TAG_NAME: state = State.URL_PROP_CHANGEFREQ; break;
                            case PRIORITY_TAG_NAME:   state = State.URL_PROP_PRIORITY;   break;
                            default:
                                String message = "Unexpected tag in url: " + reader.getLocalName();
                                logger.error(message);
                                //throw new SitemapParseException(message);
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
                    urlContent = reader.getElementText();

                    try {
                        assert urlBuilder != null;
                        urlBuilder.withLocation(new URL(StringUtils.trimToNull(urlContent)));

                    } catch (MalformedURLException ex) {
                        String message = String.format("Malformed URL found: %s", urlContent);
                        logger.error(message);
                        throw new SitemapParseException(message);
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

                case URL_PROP_MOBILE:
                    assert urlBuilder != null;
                    urlBuilder.withIsMobileContent(true);

                ///////////////////////////
                // SITEMAPINDEX Hierarchy
                ///////////////////////////
                case SITEMAPINDEX:
                    // If we're done with all the Sitemaps, we're done overall
                    if (reader.nextTag() == XMLStreamConstants.END_ELEMENT) {
                        state = State.END;
                        break;
                    }

                    state = State.SITEMAP;
                    break;

                case SITEMAP:
                    if (!StringUtils.equalsIgnoreCase(reader.getLocalName(), SITEMAP_TAG_NAME)) {
                        throw new SitemapParseException("A <sitemapindex> element can only contain <sitemap> elements");
                    }

                    reader.nextTag();

                    if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                        switch (StringUtils.lowerCase(reader.getLocalName())) {
                            case LOC_TAG_NAME:        state = State.URL_PROP_LOC;        break;
                            case LASTMOD_TAG_NAME:    state = State.URL_PROP_LASTMOD;    break;
                            default:
                                throw new SitemapParseException("Unexpected tag in sitemap: " + reader.getLocalName());
                        }
                    } else if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                        // we're done collecting the data for this URL
                        assert urlBuilder != null;
                        sitemapURLs.add(urlBuilder.build());
                        urlBuilder = new SitemapURL.Builder();
                        state = State.URLSET;
                    }

                case SITEMAP_PROP_LOC:
                    urlContent = reader.getElementText();

                    try {
                        URL sitemapURL = new URL(StringUtils.trimToNull(urlContent));

                        Sitemap temp = Retry.retry(() -> {
                            try {
                                return Exo.parse(sitemapURL.toString());
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                        retval.merge(temp);

                    } catch (MalformedURLException ex) {
                        String message = String.format("Malformed URL found: %s", urlContent);
                        logger.error(message);
                        throw new SitemapParseException(message);

                    } catch (InterruptedException e) {
                        logger.warn("Thread interrupted while (re)trying");
                        Thread.currentThread().interrupt();

                    } catch (RetryFailedException e) {
                        String message = String.format("Failed to retrieve sitemap of sitemap index at %s", urlContent);
                        logger.error(message);
                        throw new SitemapParseException(message);
                    }

                    state = State.URL;
                    break;

                case SITEMAP_PROP_LASTMOD:
                    // Do nothing with this data for now
                    reader.getElementText();
                    break;
            }

            //System.out.println(state);
        }

        return retval.merge(new Sitemap(sitemapURLs));
    }
}
