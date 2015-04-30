'use strict';

angular.module('CallForPaper')
	.controller('MainCtrl', function($scope) {
		$scope.init = function() {
			$('input.rating').rating();
			$('input.rating').on('change',function(){
				console.log();
				$('#label_difficulty').html((['Débutant','Confirmé','Expert'])[$(this).val()-1])
			})
			$('#social, #cospeaker').tokenfield();
			$('#social, #cospeaker').on('tokenfield:createtoken', function(event) {
				var existingTokens = $(this).tokenfield('getTokens');
				$.each(existingTokens, function(index, token) {
					if (token.value === event.attrs.value)
						event.preventDefault();
				});
			});
		};

		$scope.init();
	});