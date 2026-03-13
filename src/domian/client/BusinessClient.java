package ConstruccionDeSoftwareII.src.domian.client;

import ConstruccionDeSoftwareII.src.domian.User.User;

/**
    Member develop: Juan Diego Millan
    Version 1.0
 */
public class BusinessClient extends User {
    private String companyName;
    private String taxID; // Unique
    private String legalRepresentative; // FK → IndividualClient.identificationNumber
    private final String role = "Business Client";

    public BusinessClient(int userID,
                          String relatedID,
                          String fullName,
                          String identificationID,
                          String email,
                          String phone,
                          String address,
                          String systemRole,
                          String userStatus,
                          String companyName,
                          String taxID,
                          String legalRepresentative) {
        super(userID, relatedID, fullName, identificationID, email, phone, null, address, systemRole, userStatus);
        this.companyName = companyName;
        this.taxID = taxID;
        this.legalRepresentative = legalRepresentative;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getRole() {
        return role;
    }

}
