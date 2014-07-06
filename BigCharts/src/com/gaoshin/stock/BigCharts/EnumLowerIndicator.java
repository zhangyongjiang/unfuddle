package com.gaoshin.stock.BigCharts;

public enum EnumLowerIndicator implements ChartParam {
    None("0", "None"),
    Volume("1", "Volume"),                     
    VolumePlus("268435456", "Volume+"),
    RSI("2", "RSI"),
    MACD("4", "MACD"),
    OBV("8", "OBV"),
    FastStochastic("16", "Fast Stochastic"),
    SlowStochastic("32", "Slow Stochastic"),
    ROC("128", "ROC"),
    Williams("256", "Williams %R"),
    MoneyFlow("512", "Money Flow"),
    DMI("1024", "DMI"),
    VolAccumulation("4096", "Vol Accumulation"),
    VolatilityFast("8192", "Volatility Fast"),
    VolatilitySlow("16384", "Volatility Slow"),
    Momentum("65536", "Momentum"),
    UltOscillator("131072", "Ult Oscillator"),
    ShortInterest("1073741824", "% Short Interest"),
    RollingEPS("67108864", "Rolling EPS"),
    PERatio("16777216", "P/E Ratio"),
    PERanges("8388608", "P/E Ranges"),
    RollingDividend("134217728", "Rolling Dividend"),
    Yield("33554432", "Yield"),
    UpDownRatio("262144", "Up/Down Ratio"),
    ArmsIndexTRIN("524288", "Arms Index (TRIN)"),
    ADLineBreadth("1048576", "A/D Line (Breadth)"),
    ADLineDaily("2097152", "A/D Line (Daily)"),
    Compare("4194304", "% Compare"), 
    ;
    
    private String index;
    private String label;
    private EnumLowerIndicator(String index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public String getValue() {
        return index;
    }
    
    public String getLabel() {
        return label;
    }

    public static EnumLowerIndicator fromIndex(String index) {
        for(EnumLowerIndicator et : EnumLowerIndicator.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumLowerIndicator ema : EnumLowerIndicator.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
