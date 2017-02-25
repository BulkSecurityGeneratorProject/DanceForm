(function() {
	'use strict';
	angular
		.module('danceFormApp')
		.factory('CompetitionReport', CompetitionReport);

	CompetitionReport.$inject = ['$resource'];

	function CompetitionReport($resource) {
		var resourceUrl = 'api/reports/competition/:id/registered-couples';

		return $resource(resourceUrl, {}, {
			'get': {
				method: 'GET',
				responseType: 'arraybuffer',
				transformResponse: function(data, headers) {
					return {
						data: data,
						filename: function(headers) {
							var header = headers('content-disposition');
							var result = header.split(';')[1].trim().split('=')[1];
							return result.replace(/"/g, '');
						}
					}
				}
			}
		});
	}
})();
