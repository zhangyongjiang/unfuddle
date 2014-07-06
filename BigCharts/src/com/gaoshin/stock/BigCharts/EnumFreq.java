package com.gaoshin.stock.BigCharts;

public enum EnumFreq implements ChartParam {
    OneMinute(true, "9", "1-Minute"),
    FiveMinutes(true, "6", "5-Minute"),
    FifteenMinutes(true, "7", "15-Minute"),
    Hourly(true, "8", "Hourly"),
    Daily(false, "1", "Daily"),       
    Weekly(false, "2", "Weekly"),
    Monthly(false, "3", "Monthly"),
    Quarterly(false, "4", "Quarterly"),
    Yearly(false, "5", "Yearly"),
    ;
    
    private boolean intraDay;
    private String index;
    private String label;
    private EnumFreq(boolean intraDay, String index, String label) {
        this.index = index;
        this.label = label;
        this.intraDay = intraDay;
    }
    
    public String getValue() {
        return index;
    }
    
    public String getLabel() {
        return label;
    }
    
    public boolean isIntraDay() {
        return intraDay;
    }

    public static EnumFreq fromIndex(String index) {
        for(EnumFreq et : EnumFreq.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumFreq ema : EnumFreq.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
