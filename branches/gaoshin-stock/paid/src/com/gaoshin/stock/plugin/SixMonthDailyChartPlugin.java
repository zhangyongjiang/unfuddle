package com.gaoshin.stock.plugin;

import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.BigCharts.BigChartParameterName;
import com.gaoshin.stock.BigCharts.EnumFreq;
import com.gaoshin.stock.BigCharts.EnumTime;

public class SixMonthDailyChartPlugin extends ChartPlugin {
    public SixMonthDailyChartPlugin() {
        super();
        setName("6 months daily chart");
        PluginParamList list = getPluginParamList();
        list.setDefValue(BigChartParameterName.TimeName.getName(), EnumTime.SixMonths.getValue());
        list.setDefValue(BigChartParameterName.FreqName.getName(), EnumFreq.Daily.getValue());
        setParamsJson(JsonUtil.toJsonString(list));
        setSampleUrl("http://realtime.bigcharts.com/big.chart?symb=IBM&&uf=0&type=2&size=4&style=320&freq=1&time=7&ma=0&maval=9&lf=1&lf2=256&lf3=2&height=981&width=1045&mocktick=1");
    }
}
