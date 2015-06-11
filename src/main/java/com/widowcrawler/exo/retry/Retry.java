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
package com.widowcrawler.exo.retry;

import org.glassfish.jersey.internal.util.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Scott Mansfield
 */
public class Retry {

    private static final Logger logger = LoggerFactory.getLogger(Retry.class);

    private static final Integer DEFAULT_RETRY_COUNT = 3;

    public static <T> T retry(Producer<T> func) throws InterruptedException, RetryFailedException {
        return retry(DEFAULT_RETRY_COUNT, func);
    }

    public static <T> T retry(int times, Producer<T> func) throws InterruptedException, RetryFailedException {
        Throwable latestError = null;

        for (int i = 1; i <= times; i++) {
            try {
                logger.debug("Trying time " + i);
                return func.call();
            } catch (Throwable t) {
                logger.warn("Error while (re)trying ", t);
                latestError = t;
            }

            if (i < times) {
                int sleepTime = (i * i * 100);
                logger.debug("Sleeping for " + sleepTime + "ms");
                Thread.sleep(sleepTime);
            }
        }

        throw new RetryFailedException("Retry could not complete the operation", latestError);
    }
}
