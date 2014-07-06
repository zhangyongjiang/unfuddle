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
    }
}
