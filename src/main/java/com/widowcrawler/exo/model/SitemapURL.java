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
