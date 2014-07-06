package com.gaoshin.stock.BigCharts;

public enum EnumChartStyleBackground implements ChartParam {
    Default("320", "Default"),
    BlackGrids("330", "Black Grids"),
    BlueWhite("340", "Blue & White "),
    Black("350", "Black"),
    Blobe("360", "Globe"),
    GraphPaper("370", "Graph Paper"),
    Blue("380", "Blue"),
    ;
    
    private String index;
    private String label;
    private EnumChartStyleBackground(String index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public String getValue() {
        return index;
    }
    
    public String getLabel() {
        return label;
    }

    public static EnumChartStyleBackground fromIndex(String index) {
        for(EnumChartStyleBackground et : EnumChartStyleBackground.class.getEnumConstants()) {
            if(et.getValue() == index) {
                return et;
            }
        }
        return null;
    }
    
    public static String getDescription() {
        StringBuilder sb = new StringBuilder();
        for(EnumChartStyleBackground ema : EnumChartStyleBackground.class.getEnumConstants()) {
            sb.append("\t " + ema.getValue() + ": " + ema.getLabel() + "\n");
        }
        return sb.toString();
    }
}
