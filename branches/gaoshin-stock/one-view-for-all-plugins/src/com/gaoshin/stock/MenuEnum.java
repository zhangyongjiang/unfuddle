package com.gaoshin.stock;

public enum MenuEnum {
    AddSymbol("Add Symbol")
    , Help("Help")
    , Group("Group")
    , NewGroup("New Group")
    , DelCurrentGroup("Delete Current Group")
    , SwitchGroup("Switch Group")
    , Info("Information")
    ;
    
    private String label;
    private MenuEnum(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
