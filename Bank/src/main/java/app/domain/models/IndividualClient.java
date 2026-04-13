package app.domain.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.UUID;


public final class IndividualClient extends Client {

    private static final int MINIMUM_AGE = 18;

    private final String fullName;
    private final LocalDate birthDate;

    private IndividualClient(UUID clientId, String identificationId,
                             String email, String phone, String address,
                             String fullName, LocalDate birthDate) {
        super(clientId, identificationId, email, phone, address);
        this.fullName  = fullName;
        this.birthDate = birthDate;
    }


    public static IndividualClient create(String identificationId,
                                          String email,
                                          String phone,
                                          String address,
                                          String fullName,
                                          LocalDate birthDate) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("fullName must not be blank.");
        }
        Objects.requireNonNull(birthDate, "birthDate must not be null.");

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("birthDate must be a past date.");
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException(
                    "Client must be at least " + MINIMUM_AGE + " years old. Provided age: " + age);
        }

        return new IndividualClient(
                UUID.randomUUID(),
                identificationId,
                email,
                phone,
                address,
                fullName.trim(),
                birthDate
        );
    }

    public String getFullName()     { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "IndividualClient{clientId=" + getClientId() + ", fullName='" + fullName
                + "', age=" + getAge() + ", email='" + getEmail() + "'}";
    }
}
