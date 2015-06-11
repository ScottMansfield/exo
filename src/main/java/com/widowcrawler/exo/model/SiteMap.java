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

import java.util.Set;

/**
 * @author Scott Mansfield
 */
public class Sitemap {
    private Set<SitemapURL> urls;

    public Sitemap(Set<SitemapURL> urls) {
        this.urls = urls;
    }

    public Set<SitemapURL> getUrls() {
        return urls;
    }

    public Sitemap merge(Sitemap other) {
        if (other == null || other.getUrls() == null) {
            return this;
        }

        urls.addAll(other.getUrls());

        return this;
    }
}
