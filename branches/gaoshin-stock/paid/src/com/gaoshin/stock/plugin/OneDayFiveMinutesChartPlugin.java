package com.gaoshin.stock.plugin;

import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.BigCharts.BigChartParameterName;
import com.gaoshin.stock.BigCharts.EnumFreq;
import com.gaoshin.stock.BigCharts.EnumTime;

public class OneDayFiveMinutesChartPlugin extends ChartPlugin {
    public OneDayFiveMinutesChartPlugin() {
        super();
        setName("1 day 5 minutes chart");
        PluginParamList list = getPluginParamList();
        list.setDefValue(BigChartParameterName.TimeName.getName(), EnumTime.OneDay.getValue());
        list.setDefValue(BigChartParameterName.FreqName.getName(), EnumFreq.FiveMinutes.getValue());
        setParamsJson(JsonUtil.toJsonString(list));
        setSampleUrl("http://realtime.bigcharts.com/big.chart?symb=IBM&time=1&freq=6&lf=1&type=4&style=320&size=4");
    }
}
