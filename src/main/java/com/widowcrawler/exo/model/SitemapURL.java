package com.widowcrawler.exo.model;

import org.joda.time.DateTime;

import java.net.URL;

/**
 * @author Scott Mansfield
 *
 * url
 *   loc - URI (required)
 *   lastmod - Date/Time
 *   changefreq - ChangeFreq
 *   priority - Double
 */
public class SitemapURL {
    private URL location;
    private DateTime lastModified;
    private ChangeFreq changeFrequency;
    private Double priority;

    public static class Builder {
        private SitemapURL building;

        public Builder() {
            this.building = new SitemapURL();
        }

        public Builder withLocation(URL location) {
            building.location = location;
            return this;
        }

        public Builder withLastModified(DateTime lastModified) {
            building.lastModified = lastModified;
            return this;
        }

        public Builder withChangeFrequency(ChangeFreq changeFrequency) {
            building.changeFrequency = changeFrequency;
            return this;
        }

        public Builder withPriority(Double priority) {
            building.priority = priority;
            return this;
        }

        public SitemapURL build() {
            return building;
        }
    }

    private SitemapURL() { }

    public URL getLocation() {
        return location;
    }

    public DateTime getLastModified() {
        return lastModified;
    }

    public ChangeFreq getChangeFrequency() {
        return changeFrequency;
    }

    public Double getPriority() {
        return priority;
    }
}
