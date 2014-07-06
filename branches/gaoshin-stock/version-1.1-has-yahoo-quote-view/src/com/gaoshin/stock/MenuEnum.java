package com.gaoshin.stock;

public enum MenuEnum {
    AddSymbol("Add Symbol")
    , Help("Help")
    , Groups("Groups")
    , NewGroup("New Portfolio")
    , DelCurrentGroup("Delete Current Group")
    , SwitchGroup("Switch Group")
    , Info("Information")
    , Exit("Exit")
    , FullScreen("Full Screen")
    ;
    
    private String label;
    private MenuEnum(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
