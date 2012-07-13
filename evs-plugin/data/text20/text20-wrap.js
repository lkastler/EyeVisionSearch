var evs = evs || {};
evs.text20 = {
	observers : [],
	buffer: null,
	singleton: null,
	
	addListener:function(listener){
		console.log("add listener");
		
		if(!evs.text20.contains(listener)) {
			evs.text20.observers.push(listener);
		}
	},
	
	contains: function(listener) {
		for(let i in evs.text20.observers)
			if(listener == evs.text20.observers[i])
				return true;
		return false;
		
	},
	// starts tracker
	start: function(jar, classpath) {
		if(text20) {
			if(evs.text20.singleton == null) {
				console.log("text20: jar: " +  jar + ", classpath: " + classpath);
		
				text20.connector.config.archive = jar;
				text20.connector.config.classPath = classpath;
				text20.connector.config.recordingEnabled = true;
			
				// called multiple times with every run through
				text20.connector.listener("INITIALIZED", function() {
					console.log("leng: " + evs.text20.observers.length);
					for(let cur in evs.text20.observers) {
						evs.text20.observers[cur].notify.init();
					}
				});
				text20.core.init();
			}
		}
		else {
			console.log("text20 not found");
		}
	},

	end: function() {		
		jQuery('applet[name="Text20Engine"], object[name="Text20Engine"]').remove();
		
		observers = [];
		
		console.log("text 20 ended/purged");
	},

	// add events
	addEvents: function(id) {
		console.log("add tracker events");
		
		jQuery(id).each(function() {
			jQuery(this).attr("onGazeOut", "evs.text20.handleEvent('gazeout', this)");
			jQuery(this).attr("onGazeOver", "evs.text20.handleEvent('gazeover', this)");
		});
	},
			
	// removes events
	removeEvents: function(id) {
		console.log("remove tracker events");
		
		jQuery(id).each(function() {
			jQuery(this).removeAttr("onGazeOut");
			jQuery(this).removeAttr("onGazeOver");
		});
	},
	
	handleEvent: function(type, sender) {
		console.log("eyetracker event: " + type);
		var msg = {
			subj: XPCNativeWrapper.unwrap(sender),
			type: type,
			time: new Date().getTime()
		};
		
		for(let cur in evs.text20.observers) {
			evs.text20.observers[cur].notify.event(type, msg);
		}	
	}
};