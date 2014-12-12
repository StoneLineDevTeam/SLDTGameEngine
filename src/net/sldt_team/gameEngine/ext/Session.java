package net.sldt_team.gameEngine.ext;

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

    public String getUserName() {
        return userName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getPassword() {
        return password;
    }
}
