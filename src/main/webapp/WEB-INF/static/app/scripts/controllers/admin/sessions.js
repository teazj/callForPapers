'use strict';

angular.module('CallForPaper')
	.controller('AdminSessionsCtrl', ['$scope', 'AdminSession', '$filter', 'ngTableParams', '$q', 'Notification', 'screenSize', 'AdminDraft', 'localStorageService', 'lodash', 'NextPreviousSessionService', function($scope, AdminSession, $filter, ngTableParams, $q, Notification, screenSize, AdminDraft, localStorageService, lodash, NextPreviousSessionService) {
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

			var getUnique = function(sessions) {
				for (var i = 0; i < sessions.length; i++) {
					var name = $filter('removeAccents')(angular.lowercase(sessions[i].fullname))
				}
				return lodash.uniq(sessions, 'fullname').length
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

		var getData = function($defer, params, type) {
			// use build-in angular filter
			var orderedData = params.filter() ?
				$filter('filter')(sessions, params.filter()) : sessions;

			orderedData = $filter('filter')(orderedData, {
				'type': type
			});

			orderedData = params.sorting() ? $filter('orderBy')(orderedData, params.orderBy()) : orderedData;

			$scope.sessions = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

			params.total(orderedData.length); // set total for recalc pagination
			NextPreviousSessionService.setSessions(orderedData, type);
			if (localStorageService.isSupported) {
				localStorageService.set('tableParams', params.$params);
			}
			$defer.resolve($scope.sessions);
		}

		var updateTable = function() {
			var tableParamsString = localStorageService.get('tableParams');
			var defaultParams = {
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
			var initialParams;
			if (tableParamsString !== null && localStorageService.isSupported) {
				try {
					initialParams = angular.fromJson(tableParamsString);
				} catch (e) {
					initialParams = defaultParams;
				}
			} else {
				initialParams = defaultParams;
			}

			$scope.tableParamsCodelab = new ngTableParams(
				initialParams, {
					filterDelay: 0,
					total: sessions.length, // length of data
					getData: function($defer, params) {
						getData($defer, params, 'codelab');
					}
				});

			$scope.tableParamsConference = new ngTableParams(
				initialParams, {
					filterDelay: 0,
					total: sessions.length, // length of data
					getData: function($defer, params) {
						getData($defer, params, 'conference');
					}
				});

			$scope.tableParams = new ngTableParams(
				initialParams, {
					filterDelay: 0,
					total: sessions.length, // length of data
					getData: function($defer, params) {
						getData($defer, params, '');
					}
				});
			NextPreviousSessionService.setType('');
		}

		$scope.setActiveTab = function(type) {
			NextPreviousSessionService.setType(type);
		}

		$scope.handleNotReviewed = function() {
			if ($scope.notReviewed === true) {
				$scope.tableParams.filter()['reviewed'] = false;
			} else {
				$scope.tableParams.filter()['reviewed'] = '';
			}
		}
	}]);