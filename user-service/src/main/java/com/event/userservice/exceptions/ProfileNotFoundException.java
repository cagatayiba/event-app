package com.event.userservice.exceptions;

public class ProfileNotFoundException extends Exception{
    private String username;
    public ProfileNotFoundException(){
        super("No profile information found");
    }
    public ProfileNotFoundException(String message){
        super(message);
    }

    public ProfileNotFoundException(String message, String username){
        super("Profile Information is Not Available For This User: " + username);
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }
}
