package de.fhdw.util;

import de.fhdw.models.ShopUser;
import org.junit.jupiter.api.Test;

import static de.fhdw.models.ShopUser.Role.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionUtilTest {

    private PermissionUtil permissionUtil;


    @Test
    void checkIfAdminOrEmployee() {
        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.email = "admin@admin.de";

        ShopUser User = new ShopUser();
        User.role = USER;
        User.email = "user@user.de";

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.email = "employee@employee.de";


        permissionUtil = new PermissionUtil(adminUser, User);
        assertTrue(permissionUtil.checkIfAdminOrEmployee());

        permissionUtil = new PermissionUtil(employeeUser, User);
        assertTrue(permissionUtil.checkIfAdminOrEmployee());

        permissionUtil = new PermissionUtil(User, User);
        assertFalse(permissionUtil.checkIfAdminOrEmployee());


    }

    @Test
    void checkIfAdmin() {
        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.email = "admin@admin.de";

        ShopUser User = new ShopUser();
        User.role = USER;
        User.email = "user@user.de";

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.email = "employee@employee.de";

        permissionUtil = new PermissionUtil(adminUser, User);
        assertTrue(permissionUtil.checkIfAdmin());
        permissionUtil = new PermissionUtil(employeeUser, User);
        assertFalse(permissionUtil.checkIfAdmin());
        permissionUtil = new PermissionUtil(User, User);
        assertFalse(permissionUtil.checkIfAdmin());

    }

    @Test
    void checkIfEmployee() {
        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.email = "admin@admin.de";

        ShopUser User = new ShopUser();
        User.role = USER;
        User.email = "user@user.de";

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.email = "employee@employee.de";

        permissionUtil = new PermissionUtil(adminUser, User);
        assertFalse(permissionUtil.checkIfEmployee());
        permissionUtil = new PermissionUtil(employeeUser, User);
        assertTrue(permissionUtil.checkIfEmployee());
        permissionUtil = new PermissionUtil(adminUser, User);
        assertFalse(permissionUtil.checkIfEmployee());

    }

    @Test
    void checkIfUserIsSelf() {
        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.id = 1L;

        ShopUser User = new ShopUser();
        User.role = USER;
        User.id = 2L;

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.id = 3L;

        permissionUtil = new PermissionUtil(adminUser, adminUser);
        assertTrue(permissionUtil.checkIfUserIsSelf());
        permissionUtil = new PermissionUtil(employeeUser, User);
        assertFalse(permissionUtil.checkIfUserIsSelf());

    }

    @Test
    void checkIfPropertiesAreChangeable() {

        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.id = 1L;

        ShopUser User = new ShopUser();
        User.role = USER;
        User.id = 2L;

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.id = 3L;

        permissionUtil = new PermissionUtil(adminUser, User);
        assertTrue(permissionUtil.checkIfPropertiesAreChangeable());
        permissionUtil = new PermissionUtil(employeeUser, User);
        assertFalse(permissionUtil.checkIfPropertiesAreChangeable());

        permissionUtil = new PermissionUtil(User, User);
        assertTrue(permissionUtil.checkIfPropertiesAreChangeable());

        permissionUtil = new PermissionUtil(User,adminUser);
        assertFalse(permissionUtil.checkIfPropertiesAreChangeable());


    }

    @Test
    void checkIfRolesAreTheSame(){
        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.email = "admin@admin.de";

        ShopUser User = new ShopUser();
        User.role = USER;
        User.email = "user@user.de";

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.email = "employee@employee.de";

        permissionUtil = new PermissionUtil(adminUser, User);
        assertFalse(permissionUtil.checkIfRolesAreTheSame());

        permissionUtil = new PermissionUtil(employeeUser, employeeUser);
        assertTrue(permissionUtil.checkIfRolesAreTheSame());

    }

    @Test
    void CheckIfNewRoleIsEmployee(){
        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.email = "admin@admin.de";

        ShopUser User = new ShopUser();
        User.role = USER;
        User.email = "user@user.de";

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.email = "employee@employee.de";

        permissionUtil = new PermissionUtil(adminUser, User);
        assertFalse(permissionUtil.checkIfNewRoleIsEmployee());

        permissionUtil = new PermissionUtil(employeeUser, employeeUser);
        assertTrue(permissionUtil.checkIfNewRoleIsEmployee());

    }

    @Test
    void checkIfNewRoleIsAdminAndRightsAreSufficient(){
        ShopUser adminUser = new ShopUser();
        adminUser.role = ADMIN;
        adminUser.email = "admin@admin.de";

        ShopUser user = new ShopUser();
        user.role = USER;
        user.email = "user@user.de";

        ShopUser employeeUser = new ShopUser();
        employeeUser.role = EMPLOYEE;
        employeeUser.email = "employee@employee.de";

        permissionUtil = new PermissionUtil(adminUser, user);
        assertFalse(permissionUtil.checkIfNewRoleIsAdminAndRightsAreSufficient());
        permissionUtil = new PermissionUtil(employeeUser, user);
        assertFalse(permissionUtil.checkIfNewRoleIsAdminAndRightsAreSufficient());
        permissionUtil = new PermissionUtil(user, user);
        assertFalse(permissionUtil.checkIfNewRoleIsAdminAndRightsAreSufficient());

        permissionUtil = new PermissionUtil(adminUser, adminUser);
        assertTrue(permissionUtil.checkIfNewRoleIsAdminAndRightsAreSufficient());



    }
}