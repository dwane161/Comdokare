package com.example.comdokare.data;

import com.example.comdokare.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            if(username.equals("admin@gmail.com") && password.equals("123456")){
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Admin");
                return new Result.Success<>(fakeUser);
            }
            else {
                return new Result.Error(new IOException("Error logging in"));
            }
            // TODO: handle loggedInUser authentication

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}