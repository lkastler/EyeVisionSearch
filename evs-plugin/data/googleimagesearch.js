var evs = evs || {}

// system state
evs.state = evs.state || {
	eyetrack: null,
	debug: true,
	listening: true
};
// disable functions for elements, links, etc.
evs.disable = evs.disable || {};

// disables an element
evs.disable.element = function(id) {
	function rm(id) {
		//console.log("remove element: " + id);
		jQuery(id).remove();
	};
		
	if(typeof(id) == "object") {
		for(let i in id) {
			rm(id[i]);
		}
	}	
	if(typeof(id) == "string") {
		rm(id);
	}
};

// disables an anchor
evs.disable.anchor = function(id) {
	function rm(id) {
		//console.log("disable links in: " + id);
		jQuery(id).click(function(e) {
			e.preventDefault();
		});
	};
	
	if(typeof(id) == "object") {
		for(let i in id) {
			rm(id[i]);
		}
	}		
	if(typeof(id) == "string") {
		rm(id);
	}
}

// functions to handle certain events
evs.control = evs.control || {};

// creates an event and sends it to main
evs.control.event = function(msg) {
	if(evs.state.listening)
		self.port.emit(
			"gis",
			{
				type: msg.type ? msg.type : null,
				id: msg.id ? msg.id : null,
				time: msg.time ? msg.time : (new Date).getTime(),
				misc: msg.misc ? msg.misc : null
			}
		);
	else 
		console.log("propagation blocked");
};

// eyetracker event part
evs.control.eyetracker = {
	// starts eyetracking
	start: function() {
		evs.text20.end();
	
		evs.text20.addListener(evs.control.eyetracker);

		if(evs.state.eyetrack && evs.state.eyetrack.jar && evs.state.eyetrack.classpath) {
			console.log("eyetracker config: " + JSON.stringify(evs.state.eyetrack));
			evs.text20.start(
				evs.stat.eyetrack.jar,
				evs.stat.eyetrack.classpath
			);

		}
		else {
			console.log("eyetracker config: default");
			evs.text20.start(
				"http://uni-koblenz.de/~lkastler/text20.jar",
				"http://uni-koblenz.de/~lkastler/"
			);
		}
	},
	// notification listener for text20-wrapper
	notify: {
		// notified when initiation
		init: function() {
			console.log("init");
			evs.overlay.hide();
			evs.control.listeners.register();
			evs.control.disable();
		},
		
		// notified when event happens
		event: function(type, data) {
			evs.control.event({
				type: data.type,
				id: data.subj.src,
				time: data.time
			});
		}
	}
};

// overlay
evs.control.Overlay = function() {
	var overlay = document.createElement("div");
	
	// hide overlay
	this.hide = function() {
		console.log("hide overlay");
		jQuery(".tracker-overlay").remove();
	};
	
	// show overlay
	this.show = function() {
		jQuery("body").prepend(overlay);
		console.log("show overlay");
	}

	jQuery(overlay).attr("class","tracker-overlay")
	.css("background-color","gray")
		.css("width","100%")
		.css("height","100%")
		.css("position","fixed")
		.css("z-index","20")
		.css("top","0")
		.css("left","0")
		.css("opacity","1")
		.css("text-align","center")
		.css("vertical-align","middle")
		.text("tracker wird geladen");
	
	
};	

// listener related stuff
evs.control.listeners = {
	// register listeners for events
	register: function() {
		console.log("register listeners");
		// eyetracker
		evs.text20.addEvents("#search img");
		
		// mouse over
		jQuery("#search img").each(function() {
			jQuery(this).mouseover(function(e) {
				if(!evs.state.mousebind || evs.state.mousebind != e.timeStamp) {
					evs.control.event({
						type: "over",
						id: e.currentTarget.src,
						time: e.timeStamp
					});
					evs.state.mousebind = e.timeStamp;
				}
			});
			
			// intent
			jQuery(this).hoverIntent({
				over: function(e) {
					evs.control.event({
						type: "intent",
						id: e.currentTarget.src,
						time: e.timeStamp
					});
				},
				// out
				out: function(e) {
					evs.control.event({
						type: "out",
						id: e.currentTarget.src,
						time: e.timeStamp
					});
				}
			});
		});
	},
	
	// disable listeners
	"enable": function() {
		console.log("propergate events");
		evs.state.listening = true;
	},
	
	// enable listeners
	"disable": function() {
		console.log("ignore events");
		evs.state.listening = false;
	}
};

// disables all elements and anchors
evs.control.disable = function() {
	console.log("disable elements");
	evs.disable.element(["#qbi", ".qhppd"]);
	evs.disable.anchor([
		".gbzt", ".gbgt", ".gbmt", "a.kl", "a#rg_hta", "a#rg_hl", "a#rg_hpl",
		"td.fl.sblc a", "div#fll a", "a#logo", "a#fehl.gl.nobr",
		"p#bfl a", "#search img"
	]);
	
};

// sends top x images to main
evs.control.sendTop = function(x) {
	let top = Math.min(jQuery("#search").find("img").length, x);
	console.log("sending top " + top + "test:" + jQuery("#search").find("img").length);
	for(let i = 1; i < top ; ++i) {
		evs.control.event({
			type: "top",
			id: jQuery(jQuery("#search").find("img")[i]).attr("src"),
			misc: {number : i, top: top}
		});
		
		if(jQuery(jQuery("#search").find("img")[i+1]).attr("src") == null)
			break;
	}
},

// executed on start of page-mod
evs.control.start = function() {
	if(document.location.href.search("search") >= 0) {
		evs.control.eyetracker.start();
		
		console.log("gis start");
	}
	else {
		console.log("site is skipped: " + document.location.href);
	}
};

// executed when DOM is ready
jQuery(document).ready(function() {
	if(document.location.href.search("search") >= 0) {
		evs.overlay = new evs.control.Overlay();
		evs.overlay.show();
		
		evs.control.sendTop(50);
	}
	else {
		console.log("site is skipped: " + document.location.href);
	}

	evs.control.disable();	
	console.log("gis ready");
});

// configures eyetracker
self.port.on("config", function(data) {
	if(data) {
		evs.state.eyetrack = data;
	}
});

// imgevent
self.port.on("imgevent", function(data) {
	console.log("load new images");
	evs.control.listeners.register();
	evs.control.disable();
});

// reload
self.port.on("reload", function(data) {
	console.log("recieved reload msg");
	
	evs.overlay = new evs.control.Overlay();
	evs.overlay.show();
	
	evs.control.event({
		type: "url",
		id: document.location.href
	});
	
	jQuery(document).oneTime(3000, function() {
		evs.control.sendTop(50);
		evs.control.listeners.register();
		evs.overlay.hide();		
	});
	
	
	
});

// enables/disables eventlistener
self.port.on("eventlistener", function(mode) {
	
	evs.control.listeners[mode]();
});

// start
evs.control.start();