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
}
