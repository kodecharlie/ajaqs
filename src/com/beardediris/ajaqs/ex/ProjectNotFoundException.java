/**
 * ProjectNotFoundException.java
 */

package com.beardediris.ajaqs.ex;

public class ProjectNotFoundException extends Exception
{
    public ProjectNotFoundException(String msg) {
        super(msg);
    }

    public ProjectNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
