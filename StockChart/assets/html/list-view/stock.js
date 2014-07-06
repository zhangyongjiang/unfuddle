function getCurrentGroup() {
	var group = android.getCurrentGroup();
	group = JSON.parse(group);
	return group;
}

function getGroups() {
	var groups = android.getGroups();
	groups = JSON.parse(groups).items;
	return groups;
}

function getSymbolsInGroup(groupId) {
	var list = android.getSymbolsInGroup(groupId);
	list = JSON.parse(list).items;
	return list;
}

function formatYahooQuote(sym, result) {
	var info = result;
	try {
	    var items = result.split("\n");
	    if(items.length > 3 && items[2].indexOf(sym) != -1) {
	        info = items[3];
	        var pos = info.indexOf(" EST");
	        if(pos != -1) {
	//        info = info.substring(0,  pos);
	        }
	        pos = info.indexOf("Real Time");
	        if(pos != -1) {
	            info = info.substring(9);
	        }
	        if(info.indexOf("0.00")!=-1 && items.length>4) {
	            info = items[4];
	        }
	        info = info.trim();
	        if(info.indexOf("Change:") == (info.length - 7)) {
	            info = info.substring(0, info.length - 7);
	        }
	        pos = info.indexOf("Prev");
	        if(pos != -1) {
	            info = info.substring(0,  pos);
	        }
	    }
	}
	catch (e) {
		android.log(e);
	}
    return info.replace(/\+/g, ' ');
}
