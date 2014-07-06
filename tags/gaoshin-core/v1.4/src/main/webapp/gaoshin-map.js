<script type="text/javascript" >
    var mapKeys = new Array();
    mapKeys["xava.org"]         = "ABQIAAAAXJhUS5niXEXgEm0LZS219hRAYKnl5KH5KkIuBPVhxQdTCPJIIxR9JC0UBQ6NFWlda05eIZM0yGpEiQ";
    mapKeys["inonemile.com"]    = "ABQIAAAAXJhUS5niXEXgEm0LZS219hS7xYvQ5EFlx7Dh09wFQIEshDGGUxRASWjYdeJBssB8Y-Km1MWl15kSvw";
    mapKeys["25kmiles.com"]     = "ABQIAAAAXJhUS5niXEXgEm0LZS219hQMP3uD8ghGPdwLcT86JZJnarm9yxRXO1Y7cxOAGCXUm40WkXmXW3yTxg";
    mapKeys["192.168.1.69"] = "ABQIAAAAXJhUS5niXEXgEm0LZS219hRYNrkc-vwrl2-qBPlm_c6TWfkZRxRH65f6sYWZDwKKmyjbfPUbgpL4Pw";
    mapKeys["10.0.0.44"] = "ABQIAAAAXJhUS5niXEXgEm0LZS219hQ6wQ_RBoaY5Ra5_xxjY0lgC2NRBhSXhFSSnc5dg7k2NHeXceLH-0Ihgg";
    mapKeys["108.65.77.85"] =   "ABQIAAAAXJhUS5niXEXgEm0LZS219hQ0KmqIjvp0-_coMNWlCzl3p6ssQRT-n6HBMcTHqYE-XSeJa9I4vLsUvw";
    for (var key in mapKeys) {
        if (window.location.href.indexOf(key) != -1) {
            var mapKey = mapKeys[key];
            break;
        }
    }
    document.write("<script type=\"text/javascript\" src=\"http://maps.google.com/maps?file=api&amp;v=2&amp;key=" + mapKey + "\" ><\/script>");
</script>   
<script type="text/javascript" src='<c:url value="/m/map/PlaceManager.js"/>'></script>
<script type="text/javascript" src='<c:url value="/m/map/gogomap.js"/>'></script>
