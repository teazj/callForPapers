'use strict';

angular.module('CallForPaper')
	.controller('AdminSessionsCtrl', ['$scope', 'AdminSession', '$filter', 'ngTableParams', '$q', 'Notification', 'screenSize', 'AdminDraft', 'localStorageService', function($scope, AdminSession, $filter, ngTableParams, $q, Notification, screenSize, AdminDraft, localStorageService) {
		var sessions = []
		$scope.sessions = [];
		$scope.sessionsAll = [];
		$scope.screenSize = screenSize;
		$scope.realDifficulty = [$filter('translate')('step2.beginner'), $filter('translate')('step2.confirmed'), $filter('translate')('step2.expert')];
		AdminSession.query().$promise.then(function(sessionsTmp) {
			sessions = sessionsTmp.map(function(session) {
				session.fullname = session.name + " " + session.firstname;
				session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[session.difficulty - 1];
				return session;
			});

			var getUnique = function(array) {
				var counts = {};
				for (var i = 0; i < array.length; i++) {
					var name = $filter('removeAccents')(angular.lowercase(array[i].fullname))
					counts[name] = 1 + (counts[name] || 0);
				}
				return Object.keys(counts).length;
			}

			$scope.uniqueUserCount = getUnique(sessions);
			$scope.sessionsAll = sessions;
			updateTable();
		});

		$scope.drafts = [];
		AdminDraft.query().$promise.then(function(draftsTmp) {
			$scope.drafts = draftsTmp;
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
				'id': 'web',
				'title': $filter('translate')('step2.tracks.web')
			}, {
				'id': 'discovery',
				'title': $filter('translate')('step2.tracks.discovery')
			}, {
				'id': 'mobile',
				'title': $filter('translate')('step2.tracks.mobile')
			}, {
				'id': 'cloud',
				'title': $filter('translate')('step2.tracks.cloud')
			}];
			def.resolve(difficulties);
			return def;
		};

		var updateTable = function() {
			var tableParamsString = localStorageService.get('tableParams');
			var initialParams;
			if (tableParamsString !== null && localStorageService.isSupported) {
				try{
					initialParams = angular.fromJson(tableParamsString);
				}catch(e) {
					initialParams = {
						count: 10,
						filter: {
							fullname: '', // initial filter
							description: '',
							difficulty: '',
							track: '',
							reviewed: ''
						},
						sorting: {
							added: 'desc'
						}
					};
				}
			} else {
				initialParams = {
					count: 10,
					filter: {
						fullname: '', // initial filter
						description: '',
						difficulty: '',
						track: '',
						reviewed: ''
					},
					sorting: {
						added: 'desc'
					}
				};
			}
			$scope.tableParams = new ngTableParams(
				initialParams
				, {
				filterDelay: 0,
				total: sessions.length, // length of data
				getData: function($defer, params) {
					// use build-in angular filter
					var orderedData = params.filter() ?
						$filter('filter')(sessions, params.filter()) : sessions;

					orderedData = $filter('filter')(orderedData, {'type' : ''});

					orderedData = params.sorting() ? $filter('orderBy')(orderedData, params.orderBy()) : orderedData;

					$scope.sessions = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

					params.total(orderedData.length); // set total for recalc pagination
					if(localStorageService.isSupported) {
						localStorageService.set('tableParams', params.$params);
					}
					$defer.resolve($scope.sessions);
				}
			});
			$scope.tableParamsConference = new ngTableParams(
				initialParams
				, {
				filterDelay: 0,
				total: sessions.length, // length of data
				getData: function($defer, params) {
					// use build-in angular filter
					var orderedData = params.filter() ?
						$filter('filter')(sessions, params.filter()) : sessions;

					orderedData = $filter('filter')(orderedData, {'type' : 'conference'});

					orderedData = params.sorting() ? $filter('orderBy')(orderedData, params.orderBy()) : orderedData;

					$scope.sessions = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

					params.total(orderedData.length); // set total for recalc pagination
					if(localStorageService.isSupported) {
						localStorageService.set('tableParams', params.$params);
					}
					$defer.resolve($scope.sessions);
				}
			});
			$scope.tableParamsCodelab = new ngTableParams(
				initialParams
				, {
				filterDelay: 0,
				total: sessions.length, // length of data
				getData: function($defer, params) {
					// use build-in angular filter
					var orderedData = params.filter() ?
						$filter('filter')(sessions, params.filter()) : sessions;

					orderedData = $filter('filter')(orderedData, {'type' : 'codelab'});

					orderedData = params.sorting() ? $filter('orderBy')(orderedData, params.orderBy()) : orderedData;

					$scope.sessions = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

					params.total(orderedData.length); // set total for recalc pagination
					if(localStorageService.isSupported) {
						localStorageService.set('tableParams', params.$params);
					}
					$defer.resolve($scope.sessions);
				}
			});
		}


		//Test Changes
		$scope.update = function(val) {
			$scope.property = val;
			$timeout(function() {
				alert("localStorage value: " + localStorageService.get('property'));
			});
		}

		$scope.handleNotReviewed = function() {
			if ($scope.notReviewed === true) {
				$scope.tableParams.filter()['reviewed'] = false;
			} else {
				$scope.tableParams.filter()['reviewed'] = '';
			}
		}
	}]);