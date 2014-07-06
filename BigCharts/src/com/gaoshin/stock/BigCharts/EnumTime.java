package com.gaoshin.stock.BigCharts;

public enum EnumTime implements ChartParam{
    OneDay(true, "1", "1 day"),
    TwoDays(true, "2", "2 days"),
    FiveDays(true, "3", "5 days"),
    TenDays(true, "18", "10 days"),
    OneMonth(false, "4", "1 month"),
    TwoMonths(false, "5", "2 months"),
    ThreeMonths(false, "6", "3 months"),
    SixMonths(false, "7", "6 months"),
    Ytd(false, "19", "YTD"),
    OneYear(false, "8", "1 year"),
    TwoYears(false, "9", "2 years"),
    ThreeYears(false, "10", "3 years"),
    FourYears(false, "11", "4 years"),
    FiveYears(false, "12", "5 years"),
    OneDecade(false, "13", "1 decade"),
    AllData(false, "20", "All Data"),
    ;

    private String index;
    private String label;
    private boolean intraDay;
    private EnumTime(boolean intraDay, String index, String label) {
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

    public static EnumTime fromIndex(String index) {
        for(EnumTime et : EnumTime.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumTime ema : EnumTime.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
