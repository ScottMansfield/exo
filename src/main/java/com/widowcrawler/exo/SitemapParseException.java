package com.widowcrawler.exo;

/**
 * @author Scott Mansfield
 */
public class SitemapParseException extends Exception {
    public SitemapParseException() {
        super();
    }

    public SitemapParseException(String message) {
        super(message);
    }

    public SitemapParseException(Throwable cause) {
        super(cause);
    }

    public SitemapParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SitemapParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
