/**
 * FaqNotFoundException.java
 */

package com.beardediris.ajaqs.ex;

public class FaqNotFoundException extends Exception
{
    public FaqNotFoundException(String msg) {
        super(msg);
    }

    public FaqNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
