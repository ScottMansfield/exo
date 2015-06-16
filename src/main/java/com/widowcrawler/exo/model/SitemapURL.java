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
 *   isMobile - has mobile content
 */
public class SitemapURL {
    private URL location;
    private DateTime lastModified;
    private ChangeFreq changeFrequency;
    private Double priority;
    private Boolean isMobileContent;

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

        public Builder withIsMobileContent(Boolean isMobileContent) {
            building.isMobileContent = isMobileContent;
            return this;
        }

        public SitemapURL build() {
            return building;
        }
    }

    private SitemapURL() { }

    @Override
    public int hashCode() {
        int hash = 1;

        if (location != null) {
            hash *= 31 * location.hashCode();
        }

        if (lastModified != null) {
            hash *= 23 * lastModified.hashCode();
        }

        if (changeFrequency != null) {
            hash *= 67 * changeFrequency.hashCode();
        }

        if (priority != null) {
            hash *= 47 * priority.hashCode();
        }

        if (isMobileContent != null) {
            hash *= 13 * isMobileContent.hashCode();
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof SitemapURL)) {
            return false;
        }

        SitemapURL other = (SitemapURL) obj;

        if ((this.location != null && !this.location.equals(other.getLocation())) ||
                this.location == null && other.getLocation() != null) {
            return false;
        }

        if ((this.lastModified != null && !this.lastModified.equals(other.getLastModified())) ||
                this.lastModified == null && other.getLastModified() != null) {
            return false;
        }

        if ((this.changeFrequency != null && !this.changeFrequency.equals(other.getChangeFrequency())) ||
                (this.changeFrequency == null && other.getChangeFrequency() != null)) {
            return false;
        }

        if ((this.priority != null && !this.priority.equals(other.getPriority())) ||
                (this.priority == null && other.getPriority() != null)) {
            return false;
        }

        if ((this.isMobileContent != null && !this.isMobileContent.equals(other.isMobileContent())) ||
                (this.isMobileContent == null && other.isMobileContent() != null)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return  "Location: " + this.location +
                "\nLastModified: " + this.lastModified +
                "\nChangeFrequency: " + this.changeFrequency +
                "\nPriority: " + this.priority +
                "\nIsMobileContent: " + this.isMobileContent;
    }

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

    public Boolean isMobileContent() {
        return isMobileContent;
    }
}
