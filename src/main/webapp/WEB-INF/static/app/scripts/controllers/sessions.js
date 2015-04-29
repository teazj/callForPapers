'use strict';

angular.module('CallForPaper')
	.controller('SessionsCtrl', function($scope, Session, $filter, ngTableParams, $q) {
		var sessions = []
		Session.query(function(sessionsTmp) {
			sessions = sessionsTmp.map(function(session) {
				session.fullname = session.name + " " + session.firstname;
				session.realDifficulty = (['Débutant', 'Confirmé', 'Expert'])[session.difficulty - 1];
				return session;
			});
			updateTable();
		});
		$scope.difficulties = function(column) {
			var def = $q.defer();
			var difficulties = [{
				'id': '1',
				'title': 'Débutant'
			}, {
				'id': '2',
				'title': 'Confirmé'
			}, {
				'id': '3',
				'title': 'Expert'
			}];
			def.resolve(difficulties);
			return def;
		};

		$scope.tracks = function(column) {
			var def = $q.defer();
			var difficulties = [{
				'id': 'Web',
				'title': 'Web'
			}, {
				'id': 'Codelab',
				'title': 'Codelab'
			}, {
				'id': 'Discovery',
				'title': 'Discovery'
			}, {
				'id': 'Mobile',
				'title': 'Mobile'
			}, {
				'id': 'Cloud',
				'title': 'Cloud'
			}];
			def.resolve(difficulties);
			return def;
		};

		var updateTable = function() {
			$scope.tableParams = new ngTableParams({
				count: 10,
				filter: {
					nafullnameme: '', // initial filter
					description: '',
					difficulty: '',
					track: '',
					description: ''
				}
			}, {
				total: sessions.length, // length of data
				getData: function($defer, params) {
					// use build-in angular filter
					var orderedData = params.filter() ?
						$filter('filter')(sessions, params.filter()) : sessions;
					orderedData = params.sorting() ? $filter('orderBy')(orderedData, params.orderBy()) : orderedData;

					$scope.sessions = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

					params.total(orderedData.length); // set total for recalc pagination
					$defer.resolve($scope.sessions);
				}
			});
		}
	});