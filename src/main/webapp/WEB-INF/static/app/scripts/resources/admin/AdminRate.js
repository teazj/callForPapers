angular.module('CallForPaper').factory('AdminRate', ['$resource', function($resource) {
    return $resource('api/admin/rates/:id', null,
        {
            update: {method: 'PUT', url: 'api/admin/rates/:id'},
            getByRowIdAndUserId: {
                method: 'GET',
                url: 'api/admin/rates/row/:rowId/user/me'
            },
            getByRowId: {
                method: 'GET',
                url: 'api/admin/rates/row/:rowId',
                isArray: true
            }
        });
}]);
