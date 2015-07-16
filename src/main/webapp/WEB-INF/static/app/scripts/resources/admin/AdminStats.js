angular.module('CallForPaper').factory('AdminStats', ['resourceRetries', function(resourceRetries) {
  return resourceRetries('api/admin/stats', null, 
  	{
  		meter :{
  			url : 'api/admin/stats/meter',
  			method: 'GET',
  			isArray: false
  		},
    });
}]);