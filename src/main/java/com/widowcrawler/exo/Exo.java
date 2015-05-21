package com.widowcrawler.exo;

import com.widowcrawler.exo.model.Sitemap;
import com.widowcrawler.exo.parse.Parser;

import java.io.InputStream;

/**
 * @author Scott Mansfield
 */
public class Exo {

    public static Sitemap parse(String data) throws Exception {
        return new Parser().parse(data);
    }

    public static Sitemap parse(InputStream inputStream) throws Exception {
        return new Parser().parse(inputStream);
    }
}
