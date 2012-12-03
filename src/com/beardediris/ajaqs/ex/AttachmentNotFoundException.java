/**
 * AttachmentNotFoundException.java
 */

package com.beardediris.ajaqs.ex;

public class AttachmentNotFoundException extends Exception
{
    public AttachmentNotFoundException(String msg) {
        super(msg);
    }

    public AttachmentNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
