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
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Scott Mansfield
 */
public class SitemapURLTest {

    @Test
    public void equals_twoEqualSitemapURLs_returnsTrue() throws Exception {
        // Arrange
        DateTime now = DateTime.now();

        SitemapURL a = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withChangeFrequency(ChangeFreq.DAILY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        SitemapURL b = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withChangeFrequency(ChangeFreq.DAILY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        // Act
        boolean equal = a.equals(b) && b.equals(a);

        // Assert
        assertTrue(equal);
    }

    @Test
    public void equals_equalOneInHashSet_containsReturnsTrue() throws Exception {
        // Arrange
        DateTime now = DateTime.now();

        SitemapURL a = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withChangeFrequency(ChangeFreq.DAILY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        SitemapURL b = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withChangeFrequency(ChangeFreq.DAILY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        Set<SitemapURL> foo = new HashSet<>(Arrays.asList(b));

        // Act
        boolean contains = foo.contains(a);

        // Assert
        assertTrue(contains);
    }

    @Test
    public void equals_twoEqualSitemapURLsSomeFieldsBlank_returnsTrue() throws Exception {
        // Arrange
        DateTime now = DateTime.now();

        SitemapURL a = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withIsMobileContent(false)
                .build();

        SitemapURL b = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withChangeFrequency(null)
                .withPriority(null)
                .withIsMobileContent(false)
                .build();

        // Act
        boolean equal = a.equals(b) && b.equals(a);

        // Assert
        assertTrue(equal);
    }

    @Test
    public void equals_twoEqualSitemapURLsLastModifiedNull_returnsTrue() throws Exception {
        // Arrange
        SitemapURL a = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(null)
                .withChangeFrequency(ChangeFreq.HOURLY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        SitemapURL b = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(null)
                .withChangeFrequency(ChangeFreq.HOURLY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        // Act
        boolean equal = a.equals(b) && b.equals(a);

        // Assert
        assertTrue(equal);
    }

    @Test
    public void equals_unequalSitemapURLs_returnsTrue() throws Exception {
        // Arrange
        DateTime now = DateTime.now();

        SitemapURL a = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withChangeFrequency(ChangeFreq.HOURLY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        SitemapURL b = new SitemapURL.Builder()
                .withLocation(new URL("https://www.google.com/"))
                .withLastModified(now)
                .withChangeFrequency(ChangeFreq.DAILY)
                .withPriority(0.5D)
                .withIsMobileContent(false)
                .build();

        // Act
        boolean equal = a.equals(b) && b.equals(a);

        // Assert
        assertFalse(equal);
    }
}
