package com.widowcrawler.exo;

import com.widowcrawler.exo.model.SiteMap;
import com.widowcrawler.exo.parse.Parser;

import java.io.InputStream;

/**
 * @author Scott Mansfield
 */
public class Exo {

    public static SiteMap parse(String data) throws Exception {
        return new Parser().parse(data);
    }

    public static SiteMap parse(InputStream inputStream) throws Exception {
        return new Parser().parse(inputStream);
    }
}
