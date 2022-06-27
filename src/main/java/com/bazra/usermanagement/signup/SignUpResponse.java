package com.bazra.usermanagement.signup;

import java.util.Set;

import com.bazra.usermanagement.model.Role;
import com.bazra.usermanagement.model.Roles;

/**
 * SignUp Response
 * 
 * @author MOSS
 * @version 4/2022
 */
public class SignUpResponse {
    private String message;
    private String firstName;
    private String lastName;
    
    private String username;
    private String roles;
    private String country;
    private String gender;

    public SignUpResponse(String message) {
        this.message = message;
    }

    public SignUpResponse(String username, String string, String country, String gender, String message,String firstname,String lastname) {
        this.username = username;
        this.roles = string;
        this.country = country;
        this.gender = gender;
        this.message = message;
        this.firstName =firstname;
        this.lastName = lastname;
    }
    
    
    
    public SignUpResponse(String username, String roles, String string, String firstName, String lastName) {
		this.username=username;
		this.roles=roles;
		this.message=string;
		this.firstName=firstName;
		this.lastName=lastName;
		
	}

	public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
