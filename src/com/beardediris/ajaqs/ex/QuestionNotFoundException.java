/**
 * QuestionNotFoundException.java
 */

package com.beardediris.ajaqs.ex;

public class QuestionNotFoundException extends Exception
{
    public QuestionNotFoundException(String msg) {
        super(msg);
    }

    public QuestionNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
