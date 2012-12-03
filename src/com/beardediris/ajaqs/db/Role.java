/**
 * Role.java
 */

package com.beardediris.ajaqs.db;

import java.io.Serializable;

/**
 * <p>This class implements the Java object mapped to records
 * in the back-end that contain roles, each associated with
 * zero or more FAQ users.  Roles are used in web.xml in order
 * to regulate access to web-resources in this application.</p>
 */
public class Role
    implements Serializable
{
    /*
     * Fields in Role table.
     */
    private String roleName;

    public Role() {
        roleName = null;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("roleName=");
        sb.append((null != roleName) ? roleName : "");
        return sb.toString();
    }

    public boolean equals(Role other) {
        return (roleName.equals(other.getRoleName()));
    }

    public int hashCode() {
        return roleName.hashCode();
    }

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
