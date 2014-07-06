package com.gaoshin.stock.BigCharts;

public enum BigChartParameterName {
    TimeName(EnumTime.class, "time", "Time", EnumTime.SixMonths.getValue()),
    FreqName(EnumFreq.class, "freq", "Frequency", EnumFreq.Daily.getValue()),
    MovingAverageName(EnumMovingAverage.class, "ma", "Moving Average", null),
    MovingAverageValue(null, "maval", "Moving Average Period", "9"),
    UpperIndicatorName(EnumUpperIndicator.class, "uf", "Upper Indicator", null),
    LowIndecatorName(EnumLowerIndicator.class, "lf", "Low Indicator", EnumLowerIndicator.Volume.getValue()),
    LowIndecator1Name(EnumLowerIndicator.class, "lf2", "Low Indicator", null),
    LowIndecator2Name(EnumLowerIndicator.class, "lf3", "Low Indicator", null),
    ChartStylePriceName(EnumChartStylePrice.class, "type", "Chart Type", EnumChartStylePrice.Candlestick.getValue()),
    ChartStyleBackground(EnumChartStyleBackground.class, "style", "Background", EnumChartStyleBackground.GraphPaper.getValue()),
    ChartStyleSize(EnumChartStyleSize.class, "size", "Size", EnumChartStyleSize.Large.getValue()),
    ;
    
    private Class<? extends ChartParam> enumCls;
    private String name;
    private String display;
    private String defValue;
    private BigChartParameterName(Class<? extends ChartParam> enumCls, String name, String display, String defValue) {
        this.enumCls = enumCls;
        this.name = name;
        this.display = display;
        this.defValue = defValue;
    }
    
    public Class<? extends ChartParam> getEnumClass() {
        return enumCls;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDisplay() {
        return display;
    }
    
    public String getDefValue() {
        return defValue;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time Parameter 'time'\n").append(EnumTime.getDescription()).append("\n");
        sb.append("Frequency Parameter 'freq'\n").append(EnumFreq.getDescription()).append("\n");
        sb.append("Moving Average Parameter 'ma'\n").append(EnumMovingAverage.getDescription()).append("\n");
        sb.append("Upper Indicator Parameter 'uf'\n").append(EnumUpperIndicator.getDescription()).append("\n");
        sb.append("Lower Indicator 1 Parameter 'lf'\n").append(EnumLowerIndicator.getDescription()).append("\n");
        sb.append("Lower Indicator 2 Parameter 'lf2'\n").append(EnumLowerIndicator.getDescription()).append("\n");
        sb.append("Lower Indicator 3 Parameter 'lf3'\n").append(EnumLowerIndicator.getDescription()).append("\n");
        sb.append("Price Style Parameter 'type'\n").append(EnumChartStylePrice.getDescription()).append("\n");
        sb.append("Background Parameter 'style'\n").append(EnumChartStyleBackground.getDescription()).append("\n");
        sb.append("Chart Size Parameter 'size'\n").append(EnumChartStyleSize.getDescription()).append("\n");
        return sb.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(getDescription());
    }
}
