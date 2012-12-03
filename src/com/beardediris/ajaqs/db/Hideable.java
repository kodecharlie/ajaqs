/**
 * Hideable.java
 */

package com.beardediris.ajaqs.db;

public interface Hideable {
    public final static int ACTIVE = 1;
    public final static int INACTIVE = 0;
    public final static int UNDEFINED = -1;

    public abstract int getState();
}
