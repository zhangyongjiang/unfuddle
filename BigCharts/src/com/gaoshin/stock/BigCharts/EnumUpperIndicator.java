package com.gaoshin.stock.BigCharts;

public enum EnumUpperIndicator implements ChartParam {
    None("0", "None"),
    MAEnvelopes("4", "MA Envelopes"),        
    BollingerBands("8", "Bollinger Bands"),  
    ParabolicSAR("16", "Parabolic SAR"),  
    VolumeByPrice("32", "Volume by Price"),  
    PriceChannel("128", "Price Channel"),  
    ShowSplits("1024", "Show Splits"),  
    ShowEarnings("2048", "Show Earnings"),
    ShowDividends("4096", "Show Dividends"),
    ShowAllEvents("7168", "Show All Events"),
    ADLine("512", "A/D Line"),  
    ;
    
    private String index;
    private String label;
    private EnumUpperIndicator(String index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public String getValue() {
        return index;
    }
    
    public String getLabel() {
        return label;
    }

    public static EnumUpperIndicator fromIndex(String index) {
        for(EnumUpperIndicator et : EnumUpperIndicator.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumUpperIndicator ema : EnumUpperIndicator.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
