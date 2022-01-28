package com.iam.user.constants;

/**
 * @author Anaghesh Muruli
 */
public final class Constant {

    private Constant(){

    }
    public static final String INVALID_REQUEST = "invalid";
    public static final String INVALID_IDENTIFIER = "Incorrect email/phone number";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String FAILED_TO_CREATE_RESOURCE = "Failed to Register due to Internal server error";
    public static final String ROOT_CAUSE = "%s Handler. Root cause: %s ";
    public static final String EMAIL_FIELD = "email";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";
    public static final String EMAIL_ALREADY_PRESENT = "User already registered with the email id: %s";
    public static final String PHONE_NUMBER_ALREADY_PRESENT = "User already registered with the phone number: %s";
    public static final String USER_FAILED_TO_SAVE = "User save failed %s";
    public static final String GET_USER_FROM_REPOSITORY = "Retrieving user from repository";

}
