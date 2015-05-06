angular.module('CallForPaper').factory('Rate', function($resource) {
  return $resource('rate/:id', null, 
  	{
        update: { method:'PUT', url: 'rate/:id' },
        getByRowIdAndUserId: {
        	method:'GET',
        	url: 'rate/row/:rowId/user/me'
        },
        getByRowId: {
        	method:'GET',
        	url: 'rate/row/:rowId',
        	isArray: true
        }
    });
});