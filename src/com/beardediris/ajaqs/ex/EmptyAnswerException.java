/**
 * EmptyAnswerException.java
 */

package com.beardediris.ajaqs.ex;

public class EmptyAnswerException extends Exception
{
    public EmptyAnswerException(String msg) {
        super(msg);
    }

    public EmptyAnswerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
