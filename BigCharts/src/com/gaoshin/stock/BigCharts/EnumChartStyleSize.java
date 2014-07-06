package com.gaoshin.stock.BigCharts;

public enum EnumChartStyleSize implements ChartParam {
    Small("1", "Small"),
    Medium("2", "Medium"),
    Large("3", "Large"),
    Big("4", "Big"),
    ;
    
    private String value;
    private String label;
    private EnumChartStyleSize(String index, String label) {
        this.value = index;
        this.label = label;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getLabel() {
        return label;
    }

    public static EnumChartStyleSize fromIndex(String index) {
        for(EnumChartStyleSize et : EnumChartStyleSize.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumChartStyleSize ema : EnumChartStyleSize.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
