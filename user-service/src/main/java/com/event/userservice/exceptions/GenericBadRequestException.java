package com.event.userservice.exceptions;

public class GenericBadRequestException extends Exception{
    public GenericBadRequestException(){
        super("You Have Made a Bad Request. Please reconsider your inputs and check the api documentation");
    }
    public GenericBadRequestException(String message){
        super(message);
    }
}
