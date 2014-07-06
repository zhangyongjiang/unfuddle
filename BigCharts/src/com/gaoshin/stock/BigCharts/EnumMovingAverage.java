package com.gaoshin.stock.BigCharts;

public enum EnumMovingAverage implements ChartParam {
    None("0", "None"),
    SMA("1", "SMA"),
    SMA2("3", "SMA (2-line)"),
    SMA3("4", "SMA (3-line)"),
    EMA("2", "EMA"),
    EMA2("5", "EMA (2-line)"),
    EMA3("6", "EMA (3-line)"),
    ;
    
    private String index;
    private String label;
    private EnumMovingAverage(String index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public String getValue() {
        return index;
    }
    
    public String getLabel() {
        return label;
    }

    public static EnumMovingAverage fromIndex(String index) {
        for(EnumMovingAverage et : EnumMovingAverage.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumMovingAverage ema : EnumMovingAverage.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
