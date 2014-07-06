package com.gaoshin.cj.api;

public enum LinkType {
    Banner("1", "Banner"),
    AdvancedLink("2", "Advanced Link"),
    TextLink("3", "Text Link"),
    ContentLink("7", "Content Link"),
    SmartLink("9", "SmartLink"),
    Catalog("10", "Product Catalog"),
    AdvertiserSmartZone("11", "Advertiser SmartZone"),
    FlashLink("15", "Flash Link"),
    LeadForm("14", "Lead Form"),
    ;
    
    private String id;
    private String label;

    private LinkType(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

}
