angular.module('CallForPaper').factory('RestrictedContact', ['$resource', function($resource) {
    return $resource('api/restricted/contacts/:id', {},
        {
            getByRowId: {
                url: 'api/restricted/contacts/row/:rowId',
                method: 'GET',
                isArray: true
            },
            update: {method: 'PUT'}
        });
}]);
