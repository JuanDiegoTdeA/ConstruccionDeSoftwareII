package app.domain.models;

import java.util.UUID;

public final class BusinessClient extends Client {

    private final String companyName;
    private String legalRepresentative;

    private BusinessClient(UUID clientId, String identificationId,
                           String email, String phone, String address,
                           String companyName, String legalRepresentative) {
        super(clientId, identificationId, email, phone, address);
        this.companyName           = companyName;
        this.legalRepresentative   = legalRepresentative;
    }


    public static BusinessClient create(String identificationId,
                                        String email,
                                        String phone,
                                        String address,
                                        String companyName,
                                        String legalRepresentative) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("companyName must not be blank.");
        }
        if (legalRepresentative == null || legalRepresentative.isBlank()) {
            throw new IllegalArgumentException("legalRepresentative must not be blank.");
        }

        return new BusinessClient(
                UUID.randomUUID(),
                identificationId,
                email,
                phone,
                address,
                companyName.trim(),
                legalRepresentative.trim()
        );
    }

    public void updateLegalRepresentative(String legalRepresentative) {
        if (legalRepresentative == null || legalRepresentative.isBlank()) {
            throw new IllegalArgumentException("legalRepresentative must not be blank.");
        }
        this.legalRepresentative = legalRepresentative.trim();
    }

    public String getCompanyName()           { return companyName; }
    public String getLegalRepresentative()   { return legalRepresentative; }

    @Override
    public String toString() {
        return "BusinessClient{clientId=" + getClientId() + ", companyName='" + companyName
                + "', representative='" + legalRepresentative + "', email='" + getEmail() + "'}";
    }
}
