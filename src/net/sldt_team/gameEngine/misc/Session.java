package net.sldt_team.gameEngine.misc;

public class Session {

    private String userName;
    private String sessionID;
    private String password;

    /**
     * Creates a player session
     *
     * @param user Username
     * @param pass Password/Wathever you want as string
     * @param id   Normaly session id, but after all, wathever you want
     */
    public Session(String user, String pass, String id) {
        userName = user;
        sessionID = id;
        password = pass;
    }

    /**
     * Returns the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the session ID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Returns the password
     */
    public String getPassword() {
        return password;
    }
}
