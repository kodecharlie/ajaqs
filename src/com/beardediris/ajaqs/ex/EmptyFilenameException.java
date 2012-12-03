/**
 * EmptyFilenameException.java
 */

package com.beardediris.ajaqs.ex;

public class EmptyFilenameException extends Exception
{
    public EmptyFilenameException(String msg) {
        super(msg);
    }

    public EmptyFilenameException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
