package client;

import com.google.inject.Singleton;

@Singleton
public final class UserState {

    private String password;

    /**
     * Gets the inputted password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the inputted password
     * @param password inputted password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
