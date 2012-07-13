var evs = evs || {};

// data part
evs.data = {
	// {id: ranking]}
	ranking: {}
};

// control part
evs.control = {
	add: function(data) {
		// obj = {id: ranking}}
		function handle(obj) {
			if(!evs.data.ranking[obj.id]) {
				evs.data.ranking[obj.id] = {
					id: obj.id,
					ranking: 0
				};
			}
			evs.data.ranking[obj.id].ranking += obj.ranking;
		};
	
		handle(data);
	},
	
	// sorts list
	sort: function() {
		let a = [];
		
		for(let i in evs.data.ranking)
			a.push(evs.data.ranking[i]);
		
		a.sort(function(x,b) {
			if(x.ranking > b.ranking)
			 return -1;
			else if(x.ranking < b.ranking)
				return 1;
			return 0;
		});
		
		return a;
	},
	
	// shows the list
	show: function() {
		try {
			jQuery("#panel-list ul").empty();
			
			let ar = evs.control.sort();
			
			let topOption = jQuery('#panel-menu input[name="top-list"]:checked').val();
			
			let border = Math.min(ar.length, topOption ,50);
			
			console.log("show top" + border);
			
			for(let i = 0; i < border; ++i) {
				jQuery("#panel-list ul").append('<li><img src="' + ar[i].id + '" /></li>');
			}
			
			
			jQuery("#panel-list img").each(function() {
				jQuery(this).click(function(e) {
					
					evs.control.saveImage(e.currentTarget.src);
				});
				
				jQuery(this).mouseover(function(e) {
					console.log("ranking: " + evs.data.ranking[e.currentTarget.src].ranking);
				});
			});
			
		} catch(e) {
			console.log("problem: " + e);
			throw e;
		}
	},
	
	clear: function() {
		evs.data.ranking = {};
	},
	
	backup: function() {
		self.port.emit("backup", evs.data.ranking);
	},
	
	exit: function() {
		console.log("close");
		self.port.emit("exit", {});
	},
	
	saveImage: function(imageURI) {
		console.log("saving image");
		
		self.port.emit("save-image", {
			window: window,
			imageURI: imageURI
		});
	},
	
	// on ready
	ready: function() {
		evs.control.clear();
		
		jQuery('input[name="top-list"]').change(function(){
			evs.control.show();
		});
		
		jQuery('input[name="exit"]').click(function() {
			evs.control.exit();
		});
	}
};

// messages from main
// add data to your list!
self.port.on("data", function(data) {
	if(data != null) {
		evs.control.add(data);
	}
});

// show the panel!	
self.port.on("show", function() {
	console.log("panel show");
	evs.control.show();
	});

// clear yourself!
self.port.on("clear", function() {
	evs.control.clear();
});

// send me a backup!
self.port.on("backup", function() {
	evs.control.backup();
});

evs.control.ready();
console.log("panel created");
