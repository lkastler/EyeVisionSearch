var evs = evs || {};
// calculates precision
evs.Precision = function(panel_data) {
	
	// fills precision list
	this.fillList = function(id) {
		if(this.data) {
			// copy & randomize array
			let randomed = [];
			let count = 0;
			
			for(let i in this.data) {
				// upper border
				if(count++ > 50)
					break;
					
				randomed.push(this.data[i]);
			}
			
			//Fisher–Yates / Knuth shuffle
			for(let i = (randomed.length - 1); i > 0; --i) {
				let j = Math.floor(Math.random() * i);
				
				let buffer = randomed[j];
				randomed[j] = randomed[i];
				randomed[i] = buffer;
			}
			
			console.log(randomed.length);
			
			for(var x in randomed) {
				jQuery(id).append('<li><img src="' 
					+ randomed[x].id + '"></img></li>'
				);
			}
			
			//selection
			jQuery(id).find("img").click(function() {
				console.log(jQuery(this).attr("marked"));
				
				if(jQuery(this).attr("marked") == "true") {
					jQuery(this).parent().css("border-color", "black");
					jQuery(this).attr("marked", false);
				}
				else {
					jQuery(this).parent().css("border-color", "red");
					jQuery(this).attr("marked", true);
				}
			});
		}
	};
	
	// evaluates checkboxes of precision section
	this.evaluate = function(id) {
		let checked = {};
		jQuery(id).find("img").each(function() {
			if(jQuery(this).attr("marked"))
				checked[jQuery(this).attr("src")] = true;
		});
		
		return checked;
	};
	
	// [{id, ranking}]
	this.data = panel_data;
	
	console.log("precision created");
};

// Evaluation part
evs.evaluation = {
	// loads the evaluation sequence from the html file
	eval_sequences: document.defaultView.evs_sequences,
	
	config: {
		group: null,
		step: 0
	},
	
	// sends an event to Main
	sendEvent: function(type, payload, time) {
		self.port.emit("eval", {
			type: type,
			time: time ? time : (new Date).getTime(),
			payload: payload
		});
	},
	
	// loads
	load: function() {
		console.log("evaluation loaded");
		console.log("group: " + evs.evaluation.config.group);
		
		if(evs.evaluation.config.group != null) {
			evs.evaluation.execute();
		}
		else {
			evs.evaluation.hide("div.section");
			
			// create group selection section
			let div = ["<div class='section' style='text-align: center;font-size:30px;'>", 
				"Gruppe:"];
			
			for(let g in evs.evaluation.eval_sequences) {
				div.push("<a id='group-" + g + "' href='#'>" + g.toUpperCase() + "</a>");
			}
			
			div.push("</div>");
			
			jQuery("body").append(div.join(" "));
		
			for(let g in evs.evaluation.eval_sequences) {
				// jQuery specific since g will be set as 'c' at the end of for-loop
				let x = g; 
				jQuery("#group-" + g).bind({
					click: function() {evs.evaluation.setGroup(x);},
				});
			}
		}
	},
	
	// sets the group to specified g
	setGroup: function(g) {
		console.log("set group = " + g);
		evs.evaluation.config.group = g; 
		
		evs.evaluation.sendEvent("groupset", g);
		
		evs.evaluation.execute();
	},
	
	// handles the progression div in the top right
	progression: function() {
		if(evs.evaluation.config.group != null) {
			jQuery("#progression").html("<b>Fortschritt</b>"
				+ (evs.evaluation.config.step + 1)
				+ " / "
				+ evs.evaluation.eval_sequences[evs.evaluation.config.group].length);
		}
	},

	// checks if section has flag	
	hasFlag: function(group, step) {
		if(evs.evaluation.eval_sequences[group][step] != undefined)
		if(!evs.evaluation.eval_sequences[group][step].match("^\\$"))
			return jQuery(evs.evaluation.eval_sequences[group][step] + " a").attr("flagged");
		return undefined;
	},

	// adds flag to section
	addFlag: function(group, step) {
		if(evs.evaluation.eval_sequences[group][step] != undefined)
		if(!evs.evaluation.eval_sequences[group][step].match("^\\$"))
			jQuery(evs.evaluation.eval_sequences[group][step] + " a").attr("flagged", "true");
	},
	
	// removes flag from section
	removeFlag: function (group, step) {
		if(evs.evaluation.eval_sequences[group][step] != undefined)
		if(!evs.evaluation.eval_sequences[group][step].match("^\\$"))
			jQuery(evs.evaluation.eval_sequences[group][step] + " a").removeAttr("flagged");
	},
	
	// handles selection
	makeSelection: function(sel) {
		jQuery(sel).change(function() {
			if(jQuery(sel).val() != "0")
				jQuery(sel).css("background-color", "white");
			else
				jQuery(sel).css("background-color", "red");
		});
	},
	
	// handles text boxes
	makeTextBox: function(text) {
		jQuery(text).change(function(){
		if(jQuery(text).val() != "")
			jQuery(text).css("background-color","white");
		});
	},
	
	// creates sliders
	makeSlider: function(slider) {
		//console.log("make sliders");
		
		try {
			slider.slider("destroy");
		} catch(e)
		{
			console.log("slider destroy: " + e);
		}
		
		// could be used for configuration in future
		// let settings = slider.text().split(",");
		
		slider.empty();
		
		slider.slider({
			min: 0,
			max: 300,
			start: function(event, ui) {
				jQuery(ui.handle).css("visibility", "visible");
				jQuery(ui.handle).parent().css("background-color", "gray");
			}
		});
	},
	
	// precison
	handlePrecision: function() {
		evs.evaluation.hide("div.section");
		
		jQuery("#precision ul").empty();
		
		evs.evaluation.config.precision = null;
		
		evs.evaluation.sendEvent("request_precision");
    
	
		// evaluate precision
		jQuery("#precision .link-next").click(function() {
			evs.evaluation.sendEvent("precision", 
				evs.evaluation.config.precision.evaluate("#precision")
			);
			
			jQuery("#precision .link-next").unbind("click");
			evs.evaluation.next();
		});
	},
	
	// checks if every element is correctly answered
	checkConstraints: function(id) {
	
		console.log("checking constraints");
		let check = {
			values: {
				"$": id // id of div element
			},
			invalid: []
		};		
		
		// selects
		jQuery(id).find("select").each(function(){
						
			if(jQuery(this).val() == "0") {
				check.invalid.push(jQuery(this));
			}
			check.values[jQuery(this).attr("name")] = jQuery(this).val();
		});
		
		// text boxes
		jQuery(id).find('input[type="text"]').each(function() {
			let text = jQuery(this).val();
		
			if(text == "") {
				check.invalid.push(jQuery(this));
			}
			check.values[jQuery(this).attr("name")] = text;
		});
		
		// text areas
		jQuery(id).find("textarea").each(function(){
			check.values[jQuery(this).attr("name")] = jQuery(this).val();
		});
		
		// check sliders
		jQuery(id).find(".slider").each(function() {
				let handle = jQuery(this).find(".ui-slider-handle");

				if(handle.css("visibility") == "hidden") {
					handle.parent().css("background-color", "red");
					check.invalid.push(handle.parent());
				}
				check.values[jQuery(this).attr("id")] = jQuery(this).slider("value");
		});
		
		console.log(JSON.stringify(check));
		return check;
	},
	
	
	
	// handles internal section
	handleInternalSection: function(id, group, step) {
		console.log("internal section");
			
		evs.evaluation.hide("div.section");
		
		// selections
		jQuery(id).find("select").each(function() {
			evs.evaluation.makeSelection(jQuery(this));
		});
		
		// text boxes
		jQuery(id).find('input[type="text"]').each(function() {
			evs.evaluation.makeTextBox(jQuery(this));
		});
			
		// slider
		jQuery(id).find(".slider").each(function() {
			evs.evaluation.makeSlider(jQuery(this));
		});
		
		evs.evaluation.show(id);

		jQuery(id + " .link-next").bind("click", function() {
			if(!evs.evaluation.hasFlag(group, step)) {
				console.log("link clicked");
				
				let check = {};

				check = evs.evaluation.checkConstraints(id);

				if(check.invalid.length > 0) {
					console.log("some elements are invalid");
					console.log(JSON.stringify(check.invalid));
					
					for(let i in check.invalid) {
						jQuery(check.invalid[i]).css("background-color","red");
					}
				}
				else {
					//console.log(JSON.stringify(check.values));
					evs.evaluation.sendEvent("next", check.values);
					evs.evaluation.next();
				}
			}
			else {
				console.log("already done");
			}
		});

	},
		
	// sends a message to main	
	handleVariable: function(id) {
		console.log("calling main");
		evs.evaluation.sendEvent("$", id);
	},
	
	// section handle	
	execute: function() {
		try {
			console.log("execute");
			evs.evaluation.progression();

			let group = evs.evaluation.config.group;
			let step = evs.evaluation.config.step;
		
			// list processing degree
			if(evs.evaluation.eval_sequences[group].length > step) {
				let id = evs.evaluation.eval_sequences[group][step];
			
				console.log("current step: " + id);

				// precision
				if(id.match("#precision")) {
					evs.evaluation.handlePrecision();
				}
				// internal link 
				if(id.match("^#")) { 
					evs.evaluation.handleInternalSection(id, group, step);
				}
				// variable
				if(id.match("^\\$")) {
					evs.evaluation.handleVariable(id);	
				}
			}
		}
		catch(e) {
			console.log("ERROR: " + e)
		}
	},
	
	// loads next section
	next: function() {
		console.log("next");
		
		try {
			
			evs.evaluation.removeFlag(evs.evaluation.config.group, evs.evaluation.config.step - 1);
		
			evs.evaluation.addFlag(evs.evaluation.config.group, evs.evaluation.config.step);
		
			evs.evaluation.config.step++;
	
			if((evs.evaluation.config.step+1) == evs.evaluation.eval_sequences[evs.evaluation.config.group].length) {
				console.log("the end!");
				evs.evaluation.sendEvent("end", {});
			}
		
			evs.evaluation.execute();
		} catch(e) {
			console.log(e);
		}
	},

	// hides id
	hide: function(id) {
		jQuery(id).css("visibility","hidden").css("display","none");
	},
	
	// shows id
	show: function(id) {
		console.log("showing " + id);
		jQuery(id).css("visibility","visible").css("display","block");
	},
};

// executed on ready
jQuery(document).ready(function(){
	evs.evaluation.load();
});

// messages from main
// show precision!
self.port.on("precision", function(data) {
	evs.evaluation.config.precision = new evs.Precision(data);
	evs.evaluation.config.precision.fillList("#precision ul");
	evs.evaluation.show("#precision");
});

// show next!
self.port.on("next", function() {
		evs.evaluation.sendEvent("next", {});
		evs.evaluation.next();
});
