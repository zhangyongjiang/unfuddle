package com.gaoshin.coupon.db;

import common.geo.UnitedStatesNameAbbreviation;

public class MyContextWrapper {    
    
    private static void createDataSourcesForAllUnitedStates() {
        for(String state : UnitedStatesNameAbbreviation.short2Long.keySet()) {
            System.out.print("<bean id=\"dataSource" + state + "\" parent=\"parentDataSource\">");
            System.out.print("<property name=\"url\" value=\"${jdbc.url.prefix}" + state + "${jdbc.url.surfix}\"/>");
            System.out.println("</bean>");
        }
        
        System.out.println("<bean id=\"dataSource\" class=\"" + UserRoutingDataSource.class.getName() + "\">");
        System.out.println("\t<property name=\"targetDataSources\">");
        System.out.println("\t\t<map key-type=\"java.lang.String\">");
        
        for(String state : UnitedStatesNameAbbreviation.short2Long.keySet()) {
            System.out.println("\t\t\t<entry key=\"" + state + "\" value-ref=\"dataSource" + state + "\"/>");
        }
        
        System.out.println("\t\t</map>");
        System.out.println("\t</property>");
        System.out.println("</bean>");
    }
    
    public static void main(String[] args) {
        createDataSourcesForAllUnitedStates();
    }
}



