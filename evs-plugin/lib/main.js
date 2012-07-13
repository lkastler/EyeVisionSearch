var evs = evs || {}

// responsible for loading the config file
evs.Config = function(configFile) {
	try {
			console.log("loading json config: " + configFile);
				
			let xhr = require("xhr");
			let req = new xhr.XMLHttpRequest();
			req.open("GET", configFile, false);
			req.send(null);
				
			return JSON.parse(req.responseText);
		} catch (e) {
			throw "could not load json config: "+ configFile + ", Reason: " + e;
		}
			
		throw "something went wrong while loading json config: " + configFile;
};

// configurates evaluation page-mod
evs.Evaluation = function (config) {		
		let data = require("self").data;
		let pm = require("page-mod");

		this.eval = pm.PageMod({
			include: data.url(config.source),
			contentScriptFile: [
				data.url("jquery/jquery-1.6.2.min.js"), 
				data.url("jquery/jquery-ui-1.8.14.custom.min.js"), 
				data.url("evaluation.js")
			],
			contentScriptWhen: "ready",
			onAttach: function(worker) {
				config.workers.eval = worker;	
				
				worker.port.on("eval", function(data) {
					console.log("type: " + data.type);
					eventhandlers.eval[data.type](data);
				});
			}
		});
	};	
	


// initializes all with specified configFile
evs.Main =  function(configFile) {
	// saves an image specified by imageURI, window needed for save dialog
	function saveImage(window, imageURI) {
        let {Ci, Cc, Cu, components} = require("chrome");
        
        // file saver dialog
        function openSaveDialog(window) {
            let nsIFilePicker = Ci.nsIFilePicker;
			let fp = Cc["@mozilla.org/filepicker;1"].createInstance(nsIFilePicker);
			fp.appendFilters(nsIFilePicker.filterImages);
			fp.init(window, "Bild speichern", nsIFilePicker.modeSave);
			
			let res = fp.show();
			if (res == nsIFilePicker.returnOK) {
                console.log("selected file name: " + fp.file.path);
				return fp.file.path;
            }
            throw "something is wrong with file picker";
        };
        
        // loading image via xhr
        function loadImage(imageURI) {
            let xhr = require("xhr");
            let req = new xhr.XMLHttpRequest();
		
            req.overrideMimeType("text/plain; charset=x-user-defined");
            req.open("GET", imageURI, false);
            req.send(null);
            
            if (req.status == 200 || req.status == 0) {
                let ret = {};
                ret.contentType = "";
                
                if(req.status == 200) {
                    let contentType = req.getResponseHeader("Content-Type");
                    if(contentType.search("image") >= 0) {
                        ret.contentType = "." + contentType.substr(contentType.search("image") + 6);
                        console.log("found: " + ret.contentType);
                    }
                    else {
                        console.log("no content type found");
                    }
                }
                
                ret.image = req.responseText;                

                return ret;
            }
            throw "could not load image via XMLHttpRequest";
        };
		
		function saveTo(image, filePath) {
    		let file = Cc["@mozilla.org/file/local;1"]
                .createInstance(Ci.nsILocalFile);
						
            file.initWithPath(filePath);
            
            if(!file.exists()) {
                file.create(Ci.nsIFile.NORMAL_FILE_TYPE, 420);
            }
					
            stream = Cc["@mozilla.org/network/file-output-stream;1"]
                .createInstance(Ci.nsIFileOutputStream);
					
            // save
            stream.init(file, -1, -1, null); 
            stream.write(image, image.length); 
            stream.flush(); 
            stream.close();
		};
		
		try { 
            console.log("save-image");
            
            let filePath = openSaveDialog(window);
            
            let image = loadImage(imageURI);
            
            saveTo(image.image, filePath + image.contentType);
            
            config.eval.addEvent({
                type: "image.saved",
                time: (new Date).getTime(),
                id: imageURI,                
            });
            
            window.alert("image saved");
            
        } catch(e) {
            console.log(e);
			throw e;
        }
	};

	// opens windows
	function openWindow (url) {
		let windows = require("windows").browserWindows;
		
		windows.open({
			url: url,
			onClose: function(window) {
				console.log("window closed");
				config.eval.addEvent({
					time: (new Date).getTime(),
					type: "win-closed"
				});
				config.workers.eval.port.emit("next", null);
			}
		});
	};
	
	// functions for handling events from evaluation and image search
	var eventhandlers = {
		// gis events
		gis: function(data) {
			config.eval.addEvent(data);
			
			if(config.group) {
				config.clipboard.port.emit("data",
					config.filter.eval(data));
			}
				
		},
		// evaluation events
		eval: {
			// setting group
			"groupset": function(data) {
				console.log("group set: " + data.payload);
				
				config.eval.addEvent(data);
				
				config.group = data.payload;
				config.filter = new evs.Filter(config.group);
			},
			
			// variable call
			"$": function(data) {
				console.log("opening: " + config.vars[data.payload]);
				config.eval.addEvent(data);
				config.clipboard.port.emit("save", {});
				config.clipboard.port.emit("clear", {});
				openWindow(config.vars[data.payload]);
			},
			
			// end of eval
			"end": function(data) {
				console.log("evaluation ended");
				config.eval.addEvent(data);
				config.eval.save("output.json");
			},
			
			// next evaluations step
			"next": function(data) {
				console.log("next eval page");
				config.eval.addEvent(data);
			},
			
			// precision request
			"request_precision": function() {
			   config.clipboard.port.emit("backup", {});
			},
			
			"precision": function(data) {
				config.eval.addEvent(data);
				console.log("precision saved");
			}
		}
	};
	
	function configureGoogleImageSearch() {
		let data = require("self").data;
		let pm = require("page-mod");
		
		console.log("gis include: " + config.vars.$);
		
		
		this.gis = pm.PageMod({
			include: config.vars.$ + "*",
			contentScriptFile: [
				data.url("jquery/jquery-1.6.2.min.js"), 
				data.url("jquery/jquery.timers-1.2.js"),
				data.url("jquery/jquery.hoverIntent.minified.js"),
				data.url("text20/text20.js"),
				data.url("text20/text20-wrap.js"),
				data.url("googleimagesearch.css"),
				data.url("googleimagesearch.js")
			], 
			contentScriptWhen: "start",
			onAttach: function(worker) {
				config.workers.main = worker;
				
				console.log("worker attached");
				
				worker.port.emit("config", config.eyetrack);
				
				worker.port.on("gis", function(event){
					eventhandlers.gis(event);
				});
			}
		});
	};
	
	function observerNotification() {
		let obs = require("observer-service");
		let {Ci} = require("chrome");

		obs.add("http-on-examine-response", function(subject) {
			subject.QueryInterface(Ci.nsIHttpChannel);
			
			if(subject.originalURI.host.search("images.google.de") >= 0) {
				if(subject.contentType.search("javascript") >= 0) {
					if(subject.originalURI.path.search("imgevent") >= 0) {
						console.log("page loads new images");
						config.workers.main.port.emit("imgevent", {});
					}
				}
				else if(subject.contentType.search("json") >= 0) {
					console.log();
					if(subject.originalURI.path.indexOf("search") >= 0) {
						console.log("page loads new search or filters");
						config.workers.main.port.emit("reload",{});
					}
				}
			}
		});
	};
		
	// opens evaluation file
	function start() {	
		console.log("starting evaluaton");
		
		config.eval.addEvent({
			time: (new Date).getTime(),
			type: "start"
		});
		
		let data = require("self").data;
			
		let tabs = require("tabs");
			
		let oldtab = tabs.activeTab;
			
		console.log("open evaluation html: " + config.source);			
	
		tabs.open(data.url(config.source));
		
		oldtab.close();
	};
		
	// main
	// setting up config and starting the run
	var config = {};
	
	this.config = config;
	try {
		config = evs.Config(configFile);
		config.workers = {};
		
		config.saveImage = saveImage;
		
		config.eval = new evs.Eval();
		
		configureEvaluation();
		configureGoogleImageSearch();
		observerNotification()
			
		config.clipboard = new evs.Clipboard(config);
			
		start();
			
	} catch(e) {
		console.log("Fehler: " + e);
		throw e;
	}
};	

// filters events for panel depending on group
evs.Filter = function(group) {
	// saves the group number
	this.group = group;
	
	// specific filters for the groups
	let filters = {
		state : {},
		"a": function(event) {},
		
		// reference system based on top x of google image search
		"b": function(event) {
			if(event.type == "top") {
				return {
					id: event.id,
					ranking: (event.misc) ? Math.max((1000 / event.misc.number), 1) : 250
				}
			}
		},
		
		// eyetracking
		"c": function(event) {
			switch(event.type) {
				// gaze over
				case "gazeover":
					filters.state.lastGaze = event;
					return {
						id: event.id,
						ranking: 1
					};
					break;
				// gaze out
				case "gazeout": 
					if((filters.state) 
						&& (filters.state.lastGaze)
						&& (event.id == filters.state.lastGaze.id)) {
						
						var msg = {
							id: event.id,
							ranking: ((event.time - filters.state.lastGaze.time) / 10)
						};
						
						this.state.lastGaze = null;
					} else {
						var msg = {
							id: event.id,
							ranking: 1
						};
					}
					
					return msg;
					break;
			};
		}
	}
	
	// evaluates an event
	this.eval = function(event) {
		return filters[this.group](event);
	};
};

// event Storage part
evs.Storage = function() {
	// adds an event to the system.
	this.addEvent = function(data) {
		this.events.push(data);
	};
	
	// saves evaluation to specified fileName
	this.save = function(fileName) {
		console.log("saving evaluation events to: " + fileName);
		evs.FileHandler.write(fileName, JSON.stringify(this.events, "", "\t"));
	};
	
	this.events = [];
	
	console.log("evaluation created");
};

// clipboard part
evs.Clipboard = function(config) {
	let data = require("self").data;
		
	let panel = require("panel").Panel({
		contentURL: data.url("panel.html"),
		contentScriptFile: [
			data.url("jquery/jquery-1.6.2.min.js"), 
			data.url("panel.js")
		], 
		width: 1440,
		height: 900,
		onShow: function() {
			if(config.workers.main)
				config.workers.main.port.emit("eventlistener", "disable");
		},
		onHide: function() {
			if(config.workers.main)
				config.workers.main.port.emit("eventlistener", "enable");
		},
	});
	
	function show() {
		let tabs = require("tabs");
			
			if((config.group) && (config.group != "a") // TODO: hardcoded
				&& (tabs.activeTab.url.search(config.vars.$) >= 0)) {
				
				console.log("here will be a clipboard");
				panel.show();
				panel.port.emit("show", {});
				config.eval.addEvent({
					type: "panel.show",
					time: (new Date).getTime()
				});
			} else {
				console.log("here won't be a clipboard");
			}	
	};
	
	function hide() {
		panel.hide();
		
		config.eval.addEvent({
			type: "panel.hide",
			time: (new Date).getTime()
		});
	}
	
	let hk = require("hotkeys");
	
	let clipboardHotkey = hk.Hotkey({
		combo: "alt-F1",
		onPress: function() {
			show();
		}
	});
	
	// port for sending and recieving messages from the clipboard
	this.port = panel.port;
	
	// request backup
	panel.port.on("backup", function(data){
		config.eval.addEvent({
			type: "panel.backup",
			data: data
		});
		
		config.workers.eval.port.emit("precision", data);
	});
	
	// panel requests exit
	panel.port.on("exit", function() {
		hide();
	});
	
	/* panel requests image save with specified informations: 
	 * {
	 * 	window : window object from panel, needed for save dialog
	 *	imageURI : string based URI of image
	 * } 
	 */
	panel.port.on("save-image", function(data){
		console.log("save-image");
		config.saveImage(data.window, data.imageURI);
	});
		
	let widget = require("widget").Widget({
		id: "evs-clipboard",
		label: "EyeVisionSearch (Alt + F1)",
		contentURL:"http://www.uni-koblenz-landau.de/favicon.ico",
		onClick: show		
	});
	
	console.log("clipboard created");
};

// handles files
evs.FileHandler = {
	// writes data to file
	write: function(fileName,data) {
		console.log("write file");
		let {Ci, Cc, Cu, components} = require("chrome");
		
		try {
			var NetUtil = Cu.import("resource://gre/modules/NetUtil.jsm").NetUtil;
			var FileUtils = Cu.import("resource://gre/modules/FileUtils.jsm").FileUtils;
		
			// saves file to desktop
			var file = FileUtils.getFile("Desk", [fileName]);
	 
			var ostream = FileUtils.openSafeFileOutputStream(file)
		
			var converter = Cc["@mozilla.org/intl/scriptableunicodeconverter"].
				createInstance(Ci.nsIScriptableUnicodeConverter);
				converter.charset = "UTF-8";
			
			var istream = converter.convertToInputStream(data);
			
			// The last argument (the callback) is optional.
			NetUtil.asyncCopy(istream, ostream, function(status) {
				if (!components.isSuccessCode(status)) {
					console.log("error");
					return;
				}
	 
			});
		} catch(e) {
			console.log(e);
			throw e;
		}
	}
};

// main execution
evs.obj = new evs.Main(require("self").data.url("config.json"));
