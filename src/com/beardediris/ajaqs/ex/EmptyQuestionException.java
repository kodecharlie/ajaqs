/**
 * EmptyQuestionException.java
 */

package com.beardediris.ajaqs.ex;

public class EmptyQuestionException extends Exception
{
    public EmptyQuestionException(String msg) {
        super(msg);
    }

    public EmptyQuestionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
