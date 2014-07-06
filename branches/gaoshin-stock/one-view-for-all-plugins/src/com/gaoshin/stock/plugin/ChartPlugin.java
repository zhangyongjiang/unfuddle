package com.gaoshin.stock.plugin;

import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.BigCharts.BigChartParameterName;
import com.gaoshin.stock.BigCharts.BigCharts;
import com.gaoshin.stock.BigCharts.ChartParam;

public class ChartPlugin extends Plugin {
    public static final String NAME = "Chart";
    
    public ChartPlugin() {
        setName(NAME);
        setCountry("US");
        setDescription("Real time stock chart");
        setUrl(BigCharts.getChartUrlPattern());
        setEnabled(true);
        
        PluginParamList paramList = new PluginParamList();
        for(BigChartParameterName chartParam : BigChartParameterName.class.getEnumConstants()) {
            PluginParameter param = new PluginParameter();
            paramList.getItems().add(param);
            
            param.setName(chartParam.getName());
            param.setDisplay(chartParam.getDisplay());
            param.setDefValue(chartParam.getDefValue());
            
            Class<? extends ChartParam> enumClass = chartParam.getEnumClass();
            if(enumClass != null) {
                for(ChartParam cp : enumClass.getEnumConstants()) {
                    PluginParamOption option = new PluginParamOption();
                    option.setDisplay(cp.getLabel());
                    option.setValue(String.valueOf(cp.getValue()));
                    param.getOptions().add(option);
                }
            }
        }
        
        setParamsJson(JsonUtil.toJsonString(paramList));
    }
}
