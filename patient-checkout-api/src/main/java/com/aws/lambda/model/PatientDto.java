package com.aws.lambda.model;

public class PatientDto {
    
    private String firstName;
    private String lastName;
    private String adharNumber;
    private Long contactNumber;
    public PatientDto() {
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
    public String getAdharNumber() {
        return adharNumber;
    }
    public void setAdharNumber(String adharNumber) {
        this.adharNumber = adharNumber;
    }
    public Long getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }
    @Override
    public String toString() {
        return "PatientDto [firstName=" + firstName + ", lastName=" + lastName + ", adharNumber=" + adharNumber
                + ", contactNumber=" + contactNumber + "]";
    }

    
}
