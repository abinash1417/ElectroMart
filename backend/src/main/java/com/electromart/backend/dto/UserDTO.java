package com.electromart.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String phoneNumber;

    private String address;

    private String city;

    public String getName()                { return name; }
    public void setName(String name)       { this.name = name; }

    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }

    public String getPassword()            { return password; }
    public void setPassword(String pw)     { this.password = pw; }

    public String getPhoneNumber()         { return phoneNumber; }
    public void setPhoneNumber(String p)   { this.phoneNumber = p; }

    public String getAddress()             { return address; }
    public void setAddress(String a)       { this.address = a; }

    public String getCity()                { return city; }
    public void setCity(String c)          { this.city = c; }
}