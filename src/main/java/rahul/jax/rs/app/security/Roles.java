/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rahul.jax.rs.app.security;

/**
 *
 * @author rahul.kh
 */
public enum Roles {

    NORMAL("normal"),
    ADMIN("admin");

    private final String role;

    private Roles(String _role) {
        this.role = _role;
    }

    public String getRole() {
        return role;
    }

    public static Roles getRole(String _role) {
        for (Roles role : Roles.values()) {
            if (role.role.equals(_role)) {
                return role;
            }
        }

        return null;
    }
}
