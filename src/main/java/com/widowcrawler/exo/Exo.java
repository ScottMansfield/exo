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
