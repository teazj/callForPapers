'use strict';

angular.module('CallForPaper').factory('AdminRate', ['$resource', function($resource) {
    return $resource('api/admin/rates', null,
        {
            update: {method: 'PUT', url: 'api/admin/rates/:id'},
            getByRowIdAndUserId: {
                method: 'GET',
                url: 'api/admin/rates/session/:rowId/user/me'
            },
            getByRowId: {
                method: 'GET',
                url: 'api/admin/rates/session/:rowId',
                isArray: true
            }
        });
}]);
