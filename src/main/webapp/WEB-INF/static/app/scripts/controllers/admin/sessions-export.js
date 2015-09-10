'use strict';

angular.module('CallForPaper')
	.controller('AdminSessionsExportCtrl', ['$scope', 'AdminSession', function($scope, AdminSession) {
		$scope.sessions = [];
		
		/**
		 * Get all sessions (talks)
		 * @param  {void}
		 * @return {[AdminSession]}
		 */
		AdminSession.query().$promise.then(function(sessionsTmp) {
			$scope.sessions = sessionsTmp;
		});
}]);