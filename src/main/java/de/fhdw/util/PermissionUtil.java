package de.fhdw.util;

import de.fhdw.models.ShopUser;

import static de.fhdw.models.ShopUser.Role.*;

public class PermissionUtil {

    private final ShopUser requestingUser;
    private final ShopUser newUserInformation;

    public PermissionUtil(ShopUser requestingUser, ShopUser newUserInformation) {
        this.requestingUser = requestingUser;
        this.newUserInformation = newUserInformation;
    }

    public boolean checkIfAdminOrEmployee() {
        return (requestingUser.role == ADMIN || requestingUser.role == EMPLOYEE);
    }

    public boolean checkIfAdmin() {
        return requestingUser.role == ADMIN;
    }

    public boolean checkIfEmployee() {
        return requestingUser.role == EMPLOYEE;
    }

    public boolean checkIfUserIsSelf() {
        return requestingUser.id.equals(newUserInformation.id);
    }

    public boolean checkIfPropertiesAreChangeable() {
        return checkIfAdmin() || checkIfUserIsSelf();
    }

    public boolean checkIfNewRoleIsEmployee() {
        return (newUserInformation.role == EMPLOYEE);
    }

    public boolean checkIfNewRoleIsAdminAndRightsAreSufficient() {
        return (newUserInformation.role == ADMIN && checkIfAdmin());
    }

    public boolean checkIfRolesAreTheSame() {
        return requestingUser.role == newUserInformation.role;
    }

    public boolean checkIfNewRoleIsAdmin() {
    return newUserInformation.role == ADMIN;
    }

    public boolean checkIfNewRoleIsEmployeeOrUser() {
        return checkIfEmployee() || newUserInformation.role == USER;
    }
}
