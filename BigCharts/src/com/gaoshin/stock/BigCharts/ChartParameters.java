package com.gaoshin.stock.BigCharts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ChartParameters {
    public String sym = "PHYS";
    public EnumTime time = com.gaoshin.stock.BigCharts.EnumTime.OneDay;
    public EnumFreq freq = EnumFreq.FiveMinutes;
    public EnumMovingAverage ma = null;
    public String maval = null;
    public EnumUpperIndicator uf = null;//EnumUpperIndicator.BollingerBands;
    public EnumLowerIndicator lf = EnumLowerIndicator.Volume;
    public EnumLowerIndicator lf1 = null; //EnumLowerIndicator.Williams;
    public EnumLowerIndicator lf2 = null; //EnumLowerIndicator.RSI;
    public EnumChartStylePrice type = EnumChartStylePrice.Candlestick;
    public EnumChartStyleBackground style = EnumChartStyleBackground.Default;
    public EnumChartStyleSize size = EnumChartStyleSize.Big;

    public static ChartParameters fromMap(Map<String, Object> map) {
        ChartParameters param = new ChartParameters();
        for(Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value == null || "null".equals(value) || value.toString().trim().length() == 0) {
                continue;
            }
            
            if("maval".equals(key)) {
                param.maval = value.toString();
            }
            
            if(BigChartParameterName.ChartStyleBackground.getName().equals(key)) {
                param.style = EnumChartStyleBackground.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.ChartStylePriceName.getName().equals(key)) {
                param.type = EnumChartStylePrice.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.ChartStyleSize.getName().equals(key)) {
                param.size = EnumChartStyleSize.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.FreqName.getName().equals(key)) {
                param.freq = EnumFreq.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.LowIndecatorName.getName().equals(key)) {
                param.lf = EnumLowerIndicator.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.LowIndecator1Name.getName().equals(key)) {
                param.lf1 = EnumLowerIndicator.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.LowIndecator2Name.getName().equals(key)) {
                param.lf2 = EnumLowerIndicator.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.MovingAverageName.getName().equals(key)) {
                param.ma = EnumMovingAverage.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.TimeName.getName().equals(key)) {
                param.time = EnumTime.fromIndex(value.toString());
            }
            
            if(BigChartParameterName.UpperIndicatorName.getName().equals(key)) {
                param.uf = EnumUpperIndicator.fromIndex(value.toString());
            }
        }
        return param;
    }

    public String toString() {
        return sym + "-" + time + "-" + freq + "-" + ma + "-" + maval + "-" + uf + "-" + lf + "-" + lf1 + "-" + lf2 + "-" + type + "-" + style + "-" + size;
    }
    
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("sym", sym);
        values.put("time", time==null?null:time.getValue());
        values.put("freq", freq==null?null:freq.getValue());
        values.put("ma", ma==null?null:ma.getValue());
        values.put("maval", maval);
        values.put("uf", uf==null?null:uf.getValue());
        values.put("lf", lf==null?null:lf.getValue());
        values.put("lf2", lf1==null?null:lf1.getValue());
        values.put("lf3", lf2==null?null:lf2.getValue());
        values.put("type", type==null?null:type.getValue());
        values.put("style", style==null?null:style.getValue());
        values.put("size", size==null?null:size.getValue());
        return values;
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof ChartParameters)) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public EnumTime getTime() {
        return time;
    }

    public void setTime(EnumTime time) {
        this.time = time;
    }

    public EnumFreq getFreq() {
        return freq;
    }

    public void setFreq(EnumFreq freq) {
        this.freq = freq;
    }

    public EnumMovingAverage getMa() {
        return ma;
    }

    public void setMa(EnumMovingAverage ma) {
        this.ma = ma;
    }

    public String getMaval() {
        return maval;
    }

    public void setMaval(String maval) {
        this.maval = maval;
    }

    public EnumUpperIndicator getUf() {
        return uf;
    }

    public void setUf(EnumUpperIndicator uf) {
        this.uf = uf;
    }

    public EnumLowerIndicator getLf() {
        return lf;
    }

    public void setLf(EnumLowerIndicator lf) {
        this.lf = lf;
    }

    public EnumLowerIndicator getLf2() {
        return lf1;
    }

    public void setLf2(EnumLowerIndicator lf2) {
        this.lf1 = lf2;
    }

    public EnumLowerIndicator getLf3() {
        return lf2;
    }

    public void setLf3(EnumLowerIndicator lf3) {
        this.lf2 = lf3;
    }

    public EnumChartStylePrice getType() {
        return type;
    }

    public void setType(EnumChartStylePrice type) {
        this.type = type;
    }

    public EnumChartStyleBackground getStyle() {
        return style;
    }

    public void setStyle(EnumChartStyleBackground style) {
        this.style = style;
    }

    public EnumChartStyleSize getSize() {
        return size;
    }

    public void setSize(EnumChartStyleSize size) {
        this.size = size;
    }

    
}
