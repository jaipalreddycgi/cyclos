Behaviour.register({
	'#newButton': function(button) {
		button.onclick = function() {
			self.location = pathPrefix + "/editCurrency";
		}
	},
	
	'img.details': function(img) {
		setPointer(img);
		img.onclick = function() {
			self.location = pathPrefix + "/editCurrency?currencyId=" + img.getAttribute("currencyId");
		}
	},
	
	'img.remove': function(img) {
		setPointer(img);
		img.onclick = function() {
			if (confirm(removeConfirmation)) {
				self.location = pathPrefix + "/removeCurrency?currencyId=" + img.getAttribute("currencyId");
			}
		}
	}
});