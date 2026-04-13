package app.domain.models;

import app.domain.enums.ProductCategory;

import java.util.Objects;


public final class BankingProduct {

    private final String productCode;
    private final String productName;
    private final ProductCategory category;
    private final boolean requiresApproval;

    private BankingProduct(String productCode, String productName,
                           ProductCategory category, boolean requiresApproval) {
        this.productCode      = productCode;
        this.productName      = productName;
        this.category         = category;
        this.requiresApproval = requiresApproval;
    }


    public static BankingProduct create(String productCode,
                                        String productName,
                                        ProductCategory category,
                                        boolean requiresApproval) {
        if (productCode == null || productCode.isBlank()) {
            throw new IllegalArgumentException("productCode must not be blank.");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be blank.");
        }
        Objects.requireNonNull(category, "category must not be null.");

        return new BankingProduct(
                productCode.trim().toUpperCase(),
                productName.trim(),
                category,
                requiresApproval
        );
    }

    public String getProductCode()       { return productCode; }
    public String getProductName()       { return productName; }
    public ProductCategory getCategory() { return category; }
    public boolean isRequiresApproval()  { return requiresApproval; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankingProduct that)) return false;
        return Objects.equals(productCode, that.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode);
    }

    @Override
    public String toString() {
        return "BankingProduct{code='" + productCode + "', name='" + productName
                + "', category=" + category + ", requiresApproval=" + requiresApproval + '}';
    }
}
