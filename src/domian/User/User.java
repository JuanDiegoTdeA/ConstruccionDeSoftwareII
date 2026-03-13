package ConstruccionDeSoftwareII.src.domian.User;

import java.time.LocalDate;

/**
    Member develop: Juan Diego Millan
    Version 1.0
 */
public abstract class User {

    private int userID; // PK
    private String relatedID;
    private String fullName;
    private String identificationID; // Unique
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String systemRole;
    private String userStatus;

    public User(int userID,
                String relatedID,
                String fullName,
                String identificationID,
                String email,
                String phone,
                LocalDate birthDate,
                String address,
                String systemRole,
                String userStatus) {
        this.userID = userID;
        this.relatedID = relatedID;
        this.fullName = fullName;
        this.identificationID = identificationID;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.systemRole = systemRole;
        this.userStatus = userStatus;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getRelatedID() {
        return relatedID;
    }

    public void setRelatedID(String relatedID) {
        this.relatedID = relatedID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentificationID() {
        return identificationID;
    }

    public void setIdentificationID(String identificationID) {
        this.identificationID = identificationID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
