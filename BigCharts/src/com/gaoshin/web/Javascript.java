package com.gaoshin.web;

public class Javascript {
    public static StringBuilder funElementByTagWithClass(StringBuilder sb) {
        return funElementByTagWithClass(sb, "");
    }
    
    public static StringBuilder funElementByTagWithClass(StringBuilder sb, String lineBreak) {
        sb.append("function getElementByTagWithClass(tag, cls) {").append(lineBreak);
        sb.append("\tvar ret = new Array();").append(lineBreak);
        sb.append("\tvar eles = document.getElementsByTagName(tag);").append(lineBreak);
        sb.append("\tfor(var i=0; i<eles.length; i++) {").append(lineBreak);
        sb.append("\t\tvar ele = eles[i];").append(lineBreak);
        sb.append("\t\tvar attr = ele.getAttribute('class');").append(lineBreak);
        sb.append("\t\tif(attr != cls)continue;").append(lineBreak);
        sb.append("\t\tret.push(ele);").append(lineBreak);
        sb.append("\t}").append(lineBreak);
        sb.append("\treturn ret;").append(lineBreak);
        sb.append("};").append(lineBreak);
        return sb;
    }
    
    public static void main(String[] args) {
        System.out.println(funElementByTagWithClass(new StringBuilder(), "\n"));
    }
}
