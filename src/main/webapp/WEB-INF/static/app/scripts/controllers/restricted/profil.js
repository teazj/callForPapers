'use strict';

angular.module('CallForPaper')
	.controller('ProfilCtrl', ['$scope', 'RestrictedUser', 'Upload', 'RestrictedProfilImage', '$auth', "$q", function($scope, RestrictedUser, Upload, RestrictedProfilImage, $auth, $q) {
		$scope.formData = {};
		$scope.formData.phone = "";
		$scope.formData.imageProfilKey = null;
		$scope.$watch(function() {
			return $scope.form.name.$valid && $scope.form.firstname.$valid && ($scope.form.phone.$valid || $scope.formData.phone == "") && $scope.form.company.$valid && $scope.form.bio.$valid && $scope.form.social.$valid;
		}, function(isValid) {
			$scope.formData.isValid = isValid;
		})

		$scope.$watch(function() {
			if ($scope.formData.socialArray !== undefined)
				return $scope.formData.socialArray.length;
		}, function() {
			if ($scope.formData.socialArray !== undefined) {
				$scope.formData.social = $scope.formData.socialArray.map(function(elem) {
					return elem.text;
				}).join(", ");
			}
		})

		RestrictedUser.get(function(profil) {
			if (profil !== undefined) {
				for (var key in profil) {
					if (profil.hasOwnProperty(key)) {
						switch (key) {
							case "bio":
								if (profil[key] !== null) $scope.formData.bio = profil[key];
								break;
							case "company":
								if (profil[key] !== null) $scope.formData.company = profil[key];
								break;
							case "firstname":
								if (profil[key] !== null) $scope.formData.firstname = profil[key];
								break;
							case "name":
								if (profil[key] !== null) $scope.formData.name = profil[key];
								break;
							case "phone":
								if (profil[key] !== null) $scope.formData.phone = profil[key];
								break;
							case "imageProfilKey":
								if (profil[key] !== undefined) $scope.formData.imageProfilKey = profil[key];
								break;
							case "socialProfilImageUrl":
								if (profil[key] !== undefined) $scope.formData.socialProfilImageUrl = profil[key];
								break;
							case "social":
								if (profil[key] !== null && profil[key] != "") $scope.formData.socialArray = profil[key].split(", ").map(function(value) {
									return {
										text: value
									};
								});
								break;
						}
					}
				}
			}
		})

		/**
		 * update profil
		 * @type {string} profile img blob key 
		 */
		$scope.verify = false;
		$scope.sendError = false;
		$scope.sendSuccess = false;
		$scope.update = function(profilImageKey) {
			$scope.verify = true;
			if ($scope.formData.isValid) {
				// put
				$scope.sending = true;
				$scope.formData.imageProfilKey = profilImageKey.key;
				RestrictedUser.update({}, $scope.formData, function(success) {
					$scope.sendSuccess = true;
					$scope.sendError = false;
					$scope.sending = false;
					$scope.files = [];
				}, function(error) {
					$scope.sendSuccess = false;
					$scope.sendError = true;
					$scope.sending = false;
				});
			}
		}

		/**
		 * upload img then call update profil
		 * @param  {files} array containing the profil picture
		 */
		$scope.upload = function(files) {
			if (files && files.length) {
				$scope.verify = true;
				if ($scope.formData.isValid) {
					// put
					$scope.sending = true;
					RestrictedProfilImage.getUploadUri().$promise.then(function(respUrl) {
						var url = respUrl.uri;
						for (var i = 0; i < files.length; i++) {
							var file = files[i];
							Upload.upload({
								url: url,
								file: file,
								fileName: 'profil-' + $auth.getPayload().sub,
								sendFieldsAs: "form"
							}).progress(function(evt) {
								var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
							}).success(function(data, status, headers, config) {
								$scope.update(data);
							}).error(function(data, status, headers, config) {
								$scope.sendSuccess = false;
								$scope.sendError = true;
								$scope.sending = false;
							});
						}
					}, function() {})
				}
			} else {
				$scope.update({
					key: $scope.formData.imageProfilKey
				})
			}
		};

		/**
		 * remove selected img, then current img, then social pimg
		 * @return {[type]} [description]
		 */
		$scope.removeImage = function() {
			if ($scope.files && $scope.files.length) {
				$scope.files = [];
			} else if ($scope.formData.imageProfilKey) {
				$scope.formData.imageProfilKey = null;
			} else if($scope.formData.socialProfilImageUrl) {
				$scope.formData.socialProfilImageUrl = null;
			}
		}
	}]);