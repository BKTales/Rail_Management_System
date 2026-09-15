package org.dei.Authentication;



import org.dei.Repository.AuthenticationRepository;
import org.dei.Authentication.User.UserRoleDTO;

import java.util.List;

/**
 * Controller class responsible for managing user authentication,
 * user roles, scenarios, maps, and simulation lifecycle.
 */
public class AuthenticationController {


    private final AuthenticationRepository authenticationRepository;

    /**
     * Constructs an AuthenticationController instance
     * initializing the AuthenticationRepository.
     */
    public AuthenticationController() {
        this.authenticationRepository =  new AuthenticationRepository();
    }

    /**
     * Attempts to log in a user with the given ID and password.
     *
     * @param id  the user identifier (e.g., email)
     * @param pwd the user's password
     * @return true if login was successful; false otherwise
     */
    public boolean doLogin(String id, String pwd) {
        try {
            return authenticationRepository.doLogin(id, pwd);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Returns the list of roles for the currently logged-in user.
     *
     * @return list of UserRoleDTO for the current user or null if no user is logged in
     */
    public List<UserRoleDTO> getUserRoles() {
        if (authenticationRepository.getCurrentUserSession().isLoggedIn()) {
            return authenticationRepository.getCurrentUserSession().getUserRoles();
        }
        return null;
    }

    /**
     * Returns the primary role of the currently logged-in user.
     *
     * @return the UserRoleDTO of the current user or null if no user is logged in
     */
    public UserRoleDTO getCurrentUserRole() {
        if (authenticationRepository.getCurrentUserSession().isLoggedIn()) {
            return authenticationRepository.getCurrentUserSession().getUser().getUserRole();
        }
        return null;
    }

    /**
     * Adds a new user with the specified email, password, role ID and description.
     * If the role ID is "EDITOR", the user is added with the Editor role,
     * otherwise with the Player role.
     *
     * @param email           the email of the new user
     * @param password        the password for the new user
     * @param roleId          the role ID to assign
     * @param roleDescription the description of the role
     */
    public void addUser(String email, String password, String roleId, String roleDescription) {
        authenticationRepository.addUser(email, password, roleId, roleDescription);
    }

    /**
     * Logs out the currently logged-in user.
     */
    public void doLogout() {
        authenticationRepository.doLogout();
    }


}

