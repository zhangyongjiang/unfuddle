package com.gaoshin.dating;

public enum DimensionJob {
    Student("Student", 10),
    ArtisticMusicalWriter("Artistic / Musical / Writer", 20),
    BankingFinancialRealestate("Banking/ Financial / Real Estate", 30),
    ClericalAdministrative("Clerical / Administrative", 40),
    Computer("Computer", 50),
    ConstructionCraftsmanship("Construction", 60),
    EducationAcademia("Education", 70),
    Entertainment("Entertainment", 80),
    ExecutiveManagement("Executive Management", 90),
    Hospitality("Hospitality", 100),
    LawLegalServices("Law / Legal Services", 110),
    MedicineHealth("Medicine / Health", 120),
    Military("Military", 130),
    PoliticalGovernment("Political / Government", 140),
    SalesMarketingBizdev("Sales / Marketing / Biz Dev", 150),
    ScienceEngineering("Science / Engineering", 160),
    Transportation("Transportation", 170),
    Unemployed("Unemployed", 180),
    Retired("Retired", 190),
    Other("Other", 200), ;

    private String label;
    private int code;

    private DimensionJob(String label, int code) {
        this.label = label;
        this.code = code;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
