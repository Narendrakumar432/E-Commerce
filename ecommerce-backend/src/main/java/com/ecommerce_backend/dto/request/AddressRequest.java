package com.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AddressRequest {

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    private String landmark;
    private boolean isDefault = false;

    // Getters
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPincode() { return pincode; }
    public String getLandmark() { return landmark; }
    public boolean isDefault() { return isDefault; }

    // Setters
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}