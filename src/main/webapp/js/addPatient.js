
cuwyApp.controller('addPatientCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	console.log("addPatientCtrl");
	$scope.newHistoryTemplate = {
		"groups":["fib","adress","diagnos","medinfo"],
		"fib":{"name":"ФІБ"},
		"adress":{"name":"Адреса"},
		"diagnos":{"name":"Діагноз/Направлення"},
		"medinfo":{"name":"Медична інформація"}
	};
	$scope.patientHolDb = {
			"patientSurname":"",
			"patientPersonalName":""
	};
	$scope.editOpenClose = function(gr){
		var open = !gr.open;
		$scope.newHistoryTemplate.groups.forEach(function(g) {
			$scope.newHistoryTemplate[g].open = false;
		})
		gr.open = open;
		console.log(gr);
	}
}]);