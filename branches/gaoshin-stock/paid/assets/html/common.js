function getCurrentSymbol() {
	currentGroup = getCurrentGroup();
	currentItem = android.getGroupItem(currentGroup.defaultItem);
	currentItem = JSON.parse(currentItem);
	return currentItem.sym;
}

function getSystemMenu() {
	pluginList = android.listPlugins();
	pluginList = JSON.parse(pluginList).items;
	currentPlugin = JSON.parse(android.getCurrentPlugin());

	Refresh = 1000;
	Exit = 1001;
	var items = new Array();
	items.push({id: Refresh, label: 'Refresh'});
	
	for(var i=0; i<pluginList.length; i++) {
		var plugin = pluginList[i];
		items.push({id: plugin.id + 1000, label: plugin.name, disabled:plugin.id==currentPlugin.id});
	}
	
	items.push({id: Exit, label: 'Exit'});
	
    var menu = {
		items: items
    };
    
    return menu;
}

function onSysMenu(id, label) {
	if(label == currentPlugin.name) {
		android.toast(label);
		return true;
	}
	if(label == 'Refresh') {
		try {
			Refresh();
		}
		catch(e) {	
			android.log(e);
			window.location.reload();
		}
		return true;
	}
	if(label == 'Exit') {
		android.exit();
		return true;
	}
	for(var i=0; i<pluginList.length; i++) {
		var plugin = pluginList[i];
		if(plugin.name == label) {
			android.selectPluginByName(label);
			return true;
		}
	}
	return false;
}
