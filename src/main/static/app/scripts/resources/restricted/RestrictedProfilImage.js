'use strict';

angular.module('CallForPaper').factory('RestrictedProfilImage', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('api/restricted/upload/:id', null, {
        getUploadUri: {method: 'GET', url: '/api/profil/image/user/url/:id'},
    });
}]);
