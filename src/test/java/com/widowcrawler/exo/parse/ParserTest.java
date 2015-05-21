package com.widowcrawler.exo.parse;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

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
    public void parse_tinyDocument() throws Exception {
        long start = System.nanoTime();
        parser.parse(new FileInputStream("src/test/resources/example_sitemap_tiny.xml"));
        long duration = System.nanoTime() - start;

        System.out.println("Duration: " + duration + " nanos");
    }

    @Test
    public void parse_smallDocument() throws Exception {
        long start = System.nanoTime();
        parser.parse(new FileInputStream("src/test/resources/example_sitemap_small.xml"));
        long duration = System.nanoTime() - start;

        System.out.println("Duration: " + duration + " nanos");
    }

    @Test
    public void parse_largeDocument() throws Exception {
        long start = System.nanoTime();
        parser.parse(new FileInputStream("src/test/resources/example_sitemap_huge.xml"));
        long duration = System.nanoTime() - start;

        System.out.println("Duration: " + duration + " nanos");
    }
}
