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

import com.widowcrawler.exo.model.Sitemap;
import com.widowcrawler.exo.model.SitemapURL;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Scott Mansfield
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void before() {
        parser = new Parser();
    }

    @Test
    public void parse_smallDocument_noErrorsThrown() throws Exception {
        long start = System.nanoTime();
        parser.parse(new FileInputStream("src/test/resources/example_sitemap_tiny.xml"));
        long duration = System.nanoTime() - start;

        System.out.println("Duration: " + duration + " nanos");
    }

    @Test
    public void parse_mediumDocument_noErrorsThrown() throws Exception {
        long start = System.nanoTime();
        parser.parse(new FileInputStream("src/test/resources/example_sitemap_small.xml"));
        long duration = System.nanoTime() - start;

        System.out.println("Duration: " + duration + " nanos");
    }

    @Test
    public void parse_largeDocument_noErrorsThrown() throws Exception {
        long start = System.nanoTime();
        parser.parse(new FileInputStream("src/test/resources/example_sitemap_huge.xml"));
        long duration = System.nanoTime() - start;

        System.out.println("Duration: " + duration + " nanos");
    }

    @Test
    public void parse_tinyDocument_parsesSuccessfully() throws Exception {
        // Arrange
        String doc =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"" +
                "        xmlns:mobile=\"http://www.google.com/schemas/sitemap-mobile/1.0\">" +
                " <url>" +
                "  <loc>http://www.google.com/</loc>" +
                "  <priority>1.000</priority>" +
                "  <mobile:mobile/>" +
                " </url>" +
                "</urlset>";

        InputStream inputStream = new ByteArrayInputStream(doc.getBytes());

        // Act
        Sitemap sitemap = parser.parse(inputStream);

        // Assert
        SitemapURL sitemapURL = new SitemapURL.Builder()
                .withLocation(new URL("http://www.google.com/"))
                .withPriority(1.0D)
                .withIsMobileContent(true)
                .build();

        sitemap.getUrls().forEach(System.out::println);
        sitemap.getUrls().forEach(url -> System.out.println(url.hashCode()));
        System.out.println();
        System.out.println(sitemapURL);
        System.out.println(sitemapURL.hashCode());
        System.out.println();

        assertTrue("Sitemap URLs collection did not contain the expected URL",
                sitemap.getUrls().contains(sitemapURL));

        assertThat(sitemap.getUrls(), hasItem(sitemapURL));
    }
}
