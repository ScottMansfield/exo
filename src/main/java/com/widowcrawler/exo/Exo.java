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

package com.widowcrawler.exo;

import com.widowcrawler.exo.model.Sitemap;
import com.widowcrawler.exo.parse.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Scott Mansfield
 */
public class Exo {

    private static final Logger logger = LoggerFactory.getLogger(Exo.class);

    public static Sitemap parse(String url) throws SitemapParseException, IOException {
        try {
            Invocation invocation = ClientBuilder.newClient().target(url).request().buildGet();

            String data = invocation.invoke().readEntity(String.class);

            return new Parser().parse(data);

        } catch (XMLStreamException ex) {
            logger.error("Error reading XML stream", ex);
            throw new IOException("Error reading XML stream", ex);
        }
    }

    public static Sitemap parse(InputStream inputStream) throws SitemapParseException, IOException {
        try {
            return new Parser().parse(inputStream);

        } catch (XMLStreamException ex) {
            logger.error("Error reading XML stream", ex);
            throw new IOException("Error reading XML stream", ex);
        }
    }
}
