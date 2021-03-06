package com.ixpert.sb.userregistration1.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    @Column(name = "email",nullable = false, unique = true)
    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    private String email;


    @Column(name = "pasword")
    @Transient
    private String password;


    @Column(name = "first_name")
    @NotEmpty(message = "Please provide a first name")
    private String firstName;


    @Column(name = "last_name")
    @NotEmpty(message = "Please provide a last name")
    private String lastName;


    @Column(name = "enabled")
    private boolean enabled;


    @Column(name = "confirmation_token")
    private String confirmationToken;



}
