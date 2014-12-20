
var historyFile = "/hol/history_id_" + parameters.hid;

cuwyApp.controller('EpicriseCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {

	console.log("EpicriseCtrl");
	$scope.epicriseTemplate = epicriseTemplate;
	$scope.epicrise = {};
	console.log($scope.epicriseTemplate);

	$http({ method : 'GET', url : historyFile
	}).success(function(data, status, headers, config) {
		$scope.epicrise.patientHistory = data;
		initEpicrise();
	}).error(function(data, status, headers, config) {
	});

	initEpicrise = function(){
		$scope.epicrise.patientHistory.historyTreatmentAnalysises.forEach(function(o) {
			$scope.epicrise[o.historyTreatmentAnalysisName] = {hol1:o};
			initFromHol1($scope.epicrise[o.historyTreatmentAnalysisName]);
		});
		console.log($scope.epicrise);
	}

	initFromHol1 = function(epicriseElement){
		var textHol1 = epicriseElement.hol1.historyTreatmentAnalysisText;
		var element = angular.element(textHol1);
		var trs = element.find("td.name");
		if(trs.length > 0){
			var hol1LaborValues = {};
			for(i = 0; i < trs.length; i++){
				var nameTd = angular.element(trs[i]);
				var valueTd = nameTd.next();
				var unitTd = valueTd.next();
				if(valueTd.text().trim().length > 0){
					hol1LaborValues[nameTd.text()] = {value:valueTd.text(), unit:unitTd.text()};
				}
			}
			epicriseElement.hol1LaborValues = hol1LaborValues;
		}
	}

	$scope.openLaborToEdit = function(laborBlock, laborName){
		laborBlock.laborOpenToEdit=laborName;
		if(!laborBlock.hol1LaborValues[laborName])
		laborBlock.hol1LaborValues[laborName] = {value:""};
		console.log($('#'+laborName));
		//not work
		$('#'+laborName).focus();
		console.log(laborBlock);
	}

	$scope.editOpenClose = function(head1s, h1Index){
		if(!$scope.epicrise[head1s[h1Index].name])
		{
			$scope.epicrise[head1s[h1Index].name] = {text:""};
		}

		var element = angular.element($scope.epicrise[head1s[h1Index].name].textHol1);
		var trs = element.find("td.name");
		var oldOpen = head1s[h1Index].open;
		epicriseTemplate.head1s.forEach(function(o) {
			o.open = false;
		});
		head1s[h1Index].open = !oldOpen;
		if(!head1s[h1Index].open )
		{
			for(k in $scope.epicrise[head1s[h1Index].name].hol1LaborValues)
				if($scope.epicrise[head1s[h1Index].name].hol1LaborValues[k].value == "")
					delete $scope.epicrise[head1s[h1Index].name].hol1LaborValues[k];
		}
	}

} ]);
