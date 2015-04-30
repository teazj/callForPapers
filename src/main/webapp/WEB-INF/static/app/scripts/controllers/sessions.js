'use strict';

angular.module('CallForPaper')
	.controller('SessionsCtrl', function($scope, Session, $filter, ngTableParams, $q, Notification) {
		var sessions = []
		Session.query(function(sessionsTmp) {
			sessions = sessionsTmp.map(function(session) {
				session.fullname = session.name + " " + session.firstname;
				session.realDifficulty = ([$filter('translate')('step2.beginner'), $filter('translate')('step2.confirmed'), $filter('translate')('step2.expert')])[session.difficulty - 1];
				return session;
			});
			updateTable();
		});
		$scope.difficulties = function(column) {
			var def = $q.defer();
			var difficulties = [{
				'id': '1',
				'title': $filter('translate')('step2.beginner')
			}, {
				'id': '2',
				'title': $filter('translate')('step2.confirmed')
			}, {
				'id': '3',
				'title': $filter('translate')('step2.expert')
			}];
			def.resolve(difficulties);
			return def;
		};

		$scope.tracks = function(column) {
			var def = $q.defer();
			var difficulties = [{
				'id': 'Web',
				'title': $filter('translate')('step2.tracks.web')
			}, {
				'id': 'Codelab',
				'title': $filter('translate')('step2.tracks.codelab')
			}, {
				'id': 'Discovery',
				'title': $filter('translate')('step2.tracks.discovery')
			}, {
				'id': 'Mobile',
				'title': $filter('translate')('step2.tracks.mobile')
			}, {
				'id': 'Cloud',
				'title': $filter('translate')('step2.tracks.cloud')
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