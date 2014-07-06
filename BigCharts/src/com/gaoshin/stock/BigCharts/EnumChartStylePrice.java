package com.gaoshin.stock.BigCharts;

public enum EnumChartStylePrice implements ChartParam {
    HidePrice("0", "Hide Price"),
    HLC("1", "HLC"),
    OHLC("2", "OHLC"),
    Candlestick("4", "Candlestick"),
    Mountain("8", "Mountain"),
    BarCharts("16", "Bar Charts"),
    Dot("32", "Dot"),
    Close("64", "Close"),
    Logarithmic("128", "Logarithmic"),
    ;
    
    private String index;
    private String label;
    private EnumChartStylePrice(String index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public String getValue() {
        return index;
    }
    
    public String getLabel() {
        return label;
    }

    public static EnumChartStylePrice fromIndex(String index) {
        for(EnumChartStylePrice et : EnumChartStylePrice.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumChartStylePrice ema : EnumChartStylePrice.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
