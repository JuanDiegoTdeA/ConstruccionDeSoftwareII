package app.domain.ports.in;

import app.domain.models.BusinessClient;

/**
 * Input port — registers a new business (legal entity) client.
 */
public interface RegisterBusinessClientUseCase {

    /**
     * Registers a new business client after validating uniqueness of
     * identification number and email.
     *
     * @param identificationId tax/registration number (e.g. NIT)
     * @param email               contact email address
     * @param phone               contact phone number
     * @param address             physical address
     * @param companyName         registered company name
     * @param legalRepresentative full name of the legal representative
     * @return the persisted {@link BusinessClient}
     */
    BusinessClient registerBusinessClient(String identificationId,
                                          String email,
                                          String phone,
                                          String address,
                                          String companyName,
                                          String legalRepresentative);
}