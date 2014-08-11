package net.sldt_team.gameEngine.ext;

public class Session {

    private String userName;
    private String sessionID;
    private String password;

    public Session(String user, String pass, String id){
        userName = user;
        sessionID = id;
        password = pass;
    }

    public String getUserName(){
        return userName;
    }

    public String getSessionID(){
        return sessionID;
    }

    public String getPassword(){
        return password;
    }
}
