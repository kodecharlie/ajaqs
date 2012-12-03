/**
 * RoleNotFoundException.java
 */

package com.beardediris.ajaqs.ex;

public class RoleNotFoundException extends Exception
{
    public RoleNotFoundException(String msg) {
        super(msg);
    }

    public RoleNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
