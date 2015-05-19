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
    public void parse_foo() throws Exception {
        parser.parse(new FileInputStream("src/test/resources/example_sitemap_tiny.xml"));
    }
}
