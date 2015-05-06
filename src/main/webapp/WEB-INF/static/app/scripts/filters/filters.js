angular.module('customFilters', [])
	.filter('truncate', function() {
		return function(input) {
			if (input !== undefined) {
				if (input.length > 255)
					return input.substring(0, 255) + " ...";
				else
					return input;
			}
			return "";
		};
	});