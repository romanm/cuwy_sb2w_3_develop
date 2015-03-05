
cuwyApp.controller('addPatientCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	console.log("addPatientCtrl");
	$scope.configHol = configHol;
	console.log($scope.configHol);
	$scope.patientEditing = {};
	$scope.benefits = ["Чорнобилець І категорії"
	                   ,"Чорнобилець ІІ категорії"
	                   ,"Чорнобилець ІІІ категорії"
	                   ,"Чорнобилець ІV категорії"
	                   ,"Інвалід І групи"
	                   ,"Інвалід ІІ групи"
	                   ,"Інвалід ІІІ групи"
	                   ,"Інвалід ІV групи"
	                   ];
	$scope.newHistoryTemplate = {
		"groups":["pip","adress","diagnos","medinfo"],
		"pip":{"name":"ПІП"},
		"adress":{"name":"Адреса"},
		"diagnos":{"name":"Діагноз/Направлення"},
		"medinfo":{"name":"Медична інформація"}
	};
	$scope.newHistoryTemplate["adress"].open = true;

	$scope.patientHistory = {};
	
	console.log(parameters.hno);
	var historyFile = "/hol/history_id_"+parameters.hno;
	console.log(historyFile);

	if(parameters.hno){
		$http({
			method : 'GET',
			url : historyFile
		}).success(function(data, status, headers, config) {
			$scope.patientHistory = data;
			console.log($scope.patientHistory);
		}).error(function(data, status, headers, config) {
		});
	}
	//----------------adress---------------------------------------------------
	$scope.getCountryDistricts = function(){
		console.log("getCountryDistricts");
		if($scope.patientHistory.patientHolDb === undefined) {
			console.log("нема пацієнта");
		}else{
			$($scope.configHol.countries).each(function () {
				if(this.countryId == $scope.patientHistory.patientHolDb.countryId){
					console.log(this);
					$scope.districts = this.districtsHol;
					console.log($scope.districts);
				}
			});
		}
	}
	//----------------adress-------------------------------------------------END
	$scope.savePatientHistory = function(){
		$http({
			method : 'POST',
			data : $scope.patientHistory,
			url : '/savePatientHistory'
		}).success(function(data, status, headers, config){
			$scope.patientHistory = data;
		}).error(function(data, status, headers, config) {
			$scope.error = data;
		});
		return true;
	}
	

	
	$scope.editOpenClose = function(gr){
		var open = !gr.open;
		$scope.newHistoryTemplate.groups.forEach(function(g) {
			$scope.newHistoryTemplate[g].open = false;
		})
		gr.open = open;
		console.log(gr);
		console.log($scope.patientHistory);
		console.log("Autosave - "+JSON.stringify($scope.patientHistory).length);
	};
	
	$scope.open = function($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.opened = true;
	};

	$scope.dateOptions = {
		formatYear: 'yyyy',
		startingDay: 1
	};
	
}]);