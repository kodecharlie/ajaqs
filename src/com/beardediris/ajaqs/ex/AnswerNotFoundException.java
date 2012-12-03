/**
 * AnswerNotFoundException.java
 */

package com.beardediris.ajaqs.ex;

public class AnswerNotFoundException extends Exception
{
    public AnswerNotFoundException(String msg) {
        super(msg);
    }

    public AnswerNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
