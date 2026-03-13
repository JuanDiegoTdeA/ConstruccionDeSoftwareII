package ConstruccionDeSoftwareII.src.domian.client;

import ConstruccionDeSoftwareII.src.domian.User.User;

/**
    Member develop: Juan Diego Millan
    Version 1.0
 */

public class IndividualClient extends User {
     private String identificationNumber; // Unique
    private final String role = "Individual Client";

    public IndividualClient(int userID,
                            String relatedID,
                            String fullName,
                            String identificationID,
                            String email,
                            String phone,
                            java.time.LocalDate birthDate,
                            String address,
                            String systemRole,
                            String userStatus,
                            String identificationNumber) {
        super(userID, relatedID, fullName, identificationID, email, phone, birthDate, address, systemRole, userStatus);
        this.identificationNumber = identificationNumber;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getRole() {
        return role;
    }
}
