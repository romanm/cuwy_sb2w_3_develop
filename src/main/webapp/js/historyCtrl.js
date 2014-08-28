var historyFile = "/hol/history_no_"+parameters.hno;

cuwyApp.controller('HistoryCtrl', [ '$scope', '$http', function ($scope, $http) {

	$scope.departmentsHol = configHol.departments;
	$scope.patientEditing = {}
	$scope.patient = {
		name: 'Patient name',
		history_no: parameters.hno,
		patientHistory: null
	};

	if(parameters.hno){
		$http({
			method : 'GET',
			url : historyFile
		}).success(function(data, status, headers, config) {
			$scope.patientHistory = data;
		}).error(function(data, status, headers, config) {
		});
	}else if(parameters.hnoold){
		$http({
			method : 'GET',
			url : "/hol/history_old_no_"+parameters.hnoold
		}).success(function(data, status, headers, config) {
			$scope.patient.patientHistory = data;
			$scope.patientHistory = data;
			$scope.patient.name = data.patientName;
		}).error(function(data, status, headers, config) {
		});
		
	}

	$scope.calculateAge = function(birthDateStr) {
		if(!birthDateStr)
			return 0;
		var birthDate = new Date(birthDateStr)
			birthYear = birthDate.getFullYear(),
			birthMonth = birthDate.getMonth(),
			birthDay = birthDate.getDate();
		var todayDate = new Date(),
			todayYear = todayDate.getFullYear(),
			todayMonth = todayDate.getMonth(),
			todayDay = todayDate.getDate(),
			age = todayYear - birthYear; 

		if (todayMonth < birthMonth)
		{
			age--;
		}else if (birthMonth === todayMonth && todayDay < birthDay)
		{
			age--;
		}
		return age;
	};

	$scope.menuMovePatient = [
		['<span class="glyphicon glyphicon-transfer"></span> Переведеня', function ($itemScope) {
			console.debug('Move');
			console.debug($itemScope);
			$scope.patientHistory.collapseMovePatient = !$scope.patientHistory.collapseMovePatient
		}]
	];

} ] );
