package com.gaoshin.stock.plugin;

import java.util.HashMap;

public class JavascriptLibrary {
    private static JavascriptLibrary instance;
    public static synchronized JavascriptLibrary getInstance() {
        if(instance == null) {
            instance = new JavascriptLibrary();
        }
        return instance;
    }
    
    private HashMap<String, String> library = new HashMap<String, String>();
    private String lineBreak = "";
    
    private JavascriptLibrary() {
        library.put("getElementByTagWithClass", funElementByTagWithClass(new StringBuilder(), lineBreak).toString());
        library.put("removeNodeChildren", funRemoveAllChildren(new StringBuilder()).toString());
        library.put("removeAllExceptId", funRemoveAllExceptId(new StringBuilder()).toString());
        library.put("removeAllExceptTagClass", funRemoveAllExceptTagClass(new StringBuilder()).toString());
    }
    
    public String getAllFunctions() {
        StringBuilder sb = new StringBuilder();
        for(String s : library.values()) {
            sb.append(s).append(" ");
        }
        return sb.toString();
    }
    
    public String getFunctions(String... names) {
        StringBuilder sb = new StringBuilder();
        for(String name : names) {
            String function = library.get(name);
            if(function == null) {
                throw new RuntimeException("cannot find javascript function " + name);
            }
            sb.append(function).append(" ");
        }
        return sb.toString();
    }
    
    private static StringBuilder funElementByTagWithClass(StringBuilder sb, String lineBreak) {
        sb.append(" function getElementByTagWithClass(tag, cls) {").append(lineBreak);
        sb.append(" var ret = new Array();").append(lineBreak);
        sb.append(" var eles = document.getElementsByTagName(tag);").append(lineBreak);
        sb.append(" for(var i=0; i<eles.length; i++) {").append(lineBreak);
        sb.append("  var ele = eles[i];").append(lineBreak);
        sb.append("  var attr = ele.getAttribute('class');").append(lineBreak);
        sb.append("  if(attr != cls)continue;").append(lineBreak);
        sb.append("  ret.push(ele);").append(lineBreak);
        sb.append(" }").append(lineBreak);
        sb.append(" return ret;").append(lineBreak);
        sb.append("};").append(lineBreak);
        return sb;
    }
    
    private static StringBuilder funRemoveAllChildren(StringBuilder sb) {
        sb.append(" function removeNodeChildren(node) {");
        sb.append(" if ( node.hasChildNodes() ) {");
        sb.append("    while ( node.childNodes.length >= 1 ) {");
        sb.append("        node.removeChild( node.firstChild );   }  } };");
        return sb;
    }
    
    private static StringBuilder funRemoveAllExceptId(StringBuilder sb) {
        sb.append(" function removeAllExceptId(id) {");
        sb.append("node = document.getElementById(id);removeNodeChildren(document.body);document.body.appendChild(node);");
        sb.append("};");
        return sb;
    }
    
    private static StringBuilder funRemoveAllExceptTagClass(StringBuilder sb) {
        sb.append(" function removeAllExceptTagClass(tag, cls) {");
        sb.append("node = getElementByTagWithClass(tag, cls);removeNodeChildren(document.body);document.body.appendChild(node[0]);");
        sb.append("};");
        return sb;
    }
}
