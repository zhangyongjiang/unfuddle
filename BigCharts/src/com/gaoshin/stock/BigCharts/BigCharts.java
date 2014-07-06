package com.gaoshin.stock.BigCharts;


public class BigCharts {
    private static final String chartUrl = "http://bigcharts.marketwatch.com/kaavio.Webhost/charts/big.chart?";
    private static final String realTimeChartUrl = "http://realtime.bigcharts.com/big.chart?";
    private static final String url2 = "http://bigcharts.marketwatch.com/quickchart/quickchart.asp?";
    
    public static String getQuickChartUrl(ChartParameters param) {
        return url2 + getParameters(param);
    }
    
    public static String getParameters(ChartParameters param) 
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("symb=").append(param.sym);
        
        if(param.time!=null)sb.append("&time=").append(param.time.getValue());
        
        if(param.freq!=null)sb.append("&freq=").append(param.freq.getValue());
        
        if(param.ma!=null) {
            sb.append("&ma=").append(param.ma.getValue());
            if(param.maval == null)
                param.maval = "9";
            sb.append("&maval=").append(param.maval);
        }
        
        if(param.uf != null)
            sb.append("&uf=").append(param.uf.getValue());
        
        if(param.lf != null)
            sb.append("&lf=").append(param.lf.getValue());
        
        if(param.lf1 != null)
            sb.append("&lf2=").append(param.lf1.getValue());
        
        if(param.lf2 != null)
            sb.append("&lf3=").append(param.lf2.getValue());

        if(param.type!=null)
            sb.append("&type=").append(param.type.getValue());
        
        if(param.style != null)
            sb.append("&style=").append(param.style.getValue());
        
        if(param.size != null)
            sb.append("&size=").append(param.size.getValue());
        
        return sb.toString();
    }
    
    public static String getPattern() 
    {
        StringBuffer sb = new StringBuffer();
//        for(BigChartParameterName chartParam : BigChartParameterName.class.getEnumConstants()) {
//            sb.append(chartParam.getName()).append("=__").append(chartParam.getName()).append("__&");
//        }
        sb.append("symb=__SYM__");
        return sb.toString();
    }
    
    public static String getChartUrl(ChartParameters param) {
        return realTimeChartUrl + getParameters(param);
    }
    
    public static String getChartUrlPattern() {
        return realTimeChartUrl + getPattern();
    }
    
    public static void main(String[] args) {
        ChartParameters param = new ChartParameters();
        param.sym = "gld";
        System.out.println(getQuickChartUrl(param));
        System.out.println(getChartUrl(param));
    }
}
