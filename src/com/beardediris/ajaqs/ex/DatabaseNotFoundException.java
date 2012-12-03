/**
 * DatabaseNotFoundException.java
 */

package com.beardediris.ajaqs.ex;

public class DatabaseNotFoundException extends Exception
{
    public DatabaseNotFoundException(String msg) {
        super(msg);
    }

    public DatabaseNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
