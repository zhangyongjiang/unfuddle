package com.gaoshin.stock.plugin;

import java.util.ArrayList;
import java.util.List;

import com.gaoshin.stock.BigCharts.BigChartParameterName;
import com.gaoshin.stock.BigCharts.BigCharts;
import com.gaoshin.stock.BigCharts.ChartParameters;
import com.gaoshin.stock.BigCharts.EnumFreq;
import com.gaoshin.stock.BigCharts.EnumTime;

public class BuiltInPlugins {
    public static final String BuiltinPluginListHtml = "file:///android_asset/html/plugin.html";

    public List<Plugin> getBuildInPluginList() {
        List<Plugin> list = new ArrayList<Plugin>();
        list.add(getMarketWatchPlugin());
        list.add(getChartOneDayPlugin());
        list.add(getChartOneYearPlugin());
        list.add(getCnnMarketMove());
        list.add(getGoogleQuotePlugin());
        list.add(getNasdaqMostActive());
        list.add(getYahooMobileQuotePlugin());
        list.add(getYahooPortfolio());
        list.add(getYahooQuotePlugin());
        return list;
    }
    
    public static Plugin getMarketWatchPlugin() {
        Plugin plugin = new Plugin();
        plugin.setName("News");
        String url = "http://www.marketwatch.com/story/newsviewer";
        plugin.setUrl(url);
        String postAction = "node = document.getElementById('mktwheadlines');removeNodeChildren(document.body);document.body.appendChild(node);";
        plugin.setPostAction(postAction);
        return plugin;
    }
    
    public static Plugin getYahooPortfolio() {
        Plugin plugin = new Plugin();
        plugin.setName("Y!P");
        String url = "http://finance.yahoo.com/portfolio/pf_8/view/v1";
        plugin.setUrl(url);
        String postAction = "removeAllExceptId('sortableTable0');";
        plugin.setPostAction(postAction);
        return plugin;
    }
    
    public static Plugin getCnnMarketMove() {
        Plugin plugin = new Plugin();
        plugin.setName("CNN");
        String url = "http://money.cnn.com/data/hotstocks/";
        plugin.setUrl(url);
        String postAction = "removeAllExceptId('wsod_hotStocks');";
        plugin.setPostAction(postAction);
        return plugin;
    }
    
    public static Plugin getNasdaqMostActive() {
        Plugin plugin = new Plugin();
        plugin.setName("Most");
        String url = "http://dynamic.nasdaq.com/aspx/mostactive.aspx";
        plugin.setUrl(url);
        String postAction = "removeAllExceptId('_active');";
        plugin.setPostAction(postAction);
        return plugin;
    }
    
    public static Plugin getYahooQuotePlugin() {
        Plugin plugin = new Plugin();
        plugin.setName("Yahoo");
        String url = "http://finance.yahoo.com/q?s=__SYM__&ql=1";
        plugin.setUrl(url);
        String postAction = "removeAllExceptTagClass('div', 'yfi_quote_summary');";
        plugin.setPostAction(postAction);
        return plugin;
    }
    
    public static Plugin getPluginListPlugin() {
        Plugin plugin = new Plugin();
        plugin.setName("+");
        String url = BuiltinPluginListHtml;
        plugin.setUrl(url);
        return plugin;
    }
    
    public static Plugin getYahooMobileQuotePlugin() {
        Plugin plugin = new Plugin();
        plugin.setName("Yahoo");
        String url = "http://m.yahoo.com/w/yfinance/quote/__SYM__";
        plugin.setUrl(url);
        String postAction = "removeAllExceptTagClass('div', 'bd');";
        plugin.setPostAction(postAction);
        return plugin;
    }
    
    public static Plugin getGoogleQuotePlugin() {
        Plugin plugin = new Plugin();
        plugin.setName("Google");
        String url = "http://www.google.com/finance?q=__SYM__";
        plugin.setUrl(url);
        String postAction = "removeAllExceptId('market-data-div');";
        plugin.setPostAction(postAction);
        return plugin;
    }
    
    public static Plugin getChartOneDayPlugin() {
        Plugin plugin = new Plugin();
        plugin.setName("DayChart");
        ChartParameters cp = new ChartParameters();
        cp.time = EnumTime.OneDay;
        cp.freq = EnumFreq.OneMinute;
        cp.setSym("__SYM__");
        String url = BigCharts.getChartUrl(cp);
        plugin.setUrl(url);
        plugin.setDescription(BigChartParameterName.getDescription());
        return plugin;
    }
    
    public static Plugin getChartOneYearPlugin() {
        Plugin plugin = new Plugin();
        plugin.setName("YearChart");
        ChartParameters cp = new ChartParameters();
        cp.time = EnumTime.OneYear;
        cp.freq = EnumFreq.Daily;
        cp.setSym("__SYM__");
        String url = BigCharts.getChartUrl(cp);
        plugin.setUrl(url);
        return plugin;
    }
    
}
