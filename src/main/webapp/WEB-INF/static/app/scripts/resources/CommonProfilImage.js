angular.module('CallForPaper').factory('CommonProfilImage', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('api/profil/image/user/url/:id', null, {});
}]);
