
var historyFile = "/hol/history_id_" + parameters.hid;

cuwyApp.controller('EpicriseCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {

	console.log("EpicriseCtrl");
	$scope.epicriseTemplate = epicriseTemplate;
	$scope.seekTag = "";
	$scope.epicrise = {};
	$scope.epicrise.departmentHistoryOut = new Date();
	$scope.dt = new Date();
	console.log($scope.epicriseTemplate);

	initDeclareController($scope, $http, $sce, $filter);

	$http({ method : 'GET', url : historyFile
	}).success(function(data, status, headers, config) {
		$scope.patientHistory = data;
		initEpicrise();
	}).error(function(data, status, headers, config) {
	});

	initEpicrise = function(){
		$scope.epicrise.epicriseGroups = [];
		var rsp = {name:"Рекомендовано/смерть/перевід"};
		$scope.patientHistory.historyTreatmentAnalysises.forEach(function(hol1Element) {
			var groupElement = createGroupElement(hol1Element.historyTreatmentAnalysisName);
			if( rsp.name == groupElement.name){
				rsp = groupElement;
			}else{
				var value = {};
				value.historyTreatmentAnalysisDatetime = new Date();
				initFromHol1(value, hol1Element.historyTreatmentAnalysisText);
				groupElement.value = value;
				$scope.epicrise.epicriseGroups.push(groupElement);
			}
		});
		console.log($scope.epicrise);
		var op = $scope.epicriseTemplate.head1s[1];
		$scope.epicrise.epicriseGroups.splice(0,0,{name:op.name});
		var dz = $scope.epicriseTemplate.head1s[0];
		$scope.epicrise.epicriseGroups.splice(0,0,{name:dz.name});
		var addGroup = $scope.epicriseTemplate.head1s[2];
		$scope.epicrise.epicriseGroups.push(rsp);
		var addGroup = {name:"Лікування, обстеження, аналізи, рекомендації ...", type : 'isOnDemand'};
		$scope.epicrise.epicriseGroups.splice(2,0,addGroup);
		
		
	}
	initEpicrise_old = function(){
	/*
		$scope.patientHistory.historyTreatmentAnalysises.forEach(function(hol1Element) {
			$scope.epicrise[hol1Element.historyTreatmentAnalysisName] = {};
			$scope.epicrise[hol1Element.historyTreatmentAnalysisName].historyTreatmentAnalysisDatetime = new Date();
			initFromHol1($scope.epicrise[hol1Element.historyTreatmentAnalysisName], hol1Element);
		});
	*/
		$scope.epicrise.groupsMap = {};
		$scope.epicrise.epicriseGroups = [];
		$scope.epicriseTemplate.head1s.forEach(function(h1, index) {
			$scope.epicrise.epicriseGroups.push(createGroupElement(h1));
			$scope.epicrise.groupsMap[h1.name] = index;
		});
		console.log($scope.patientHistory);
		$scope.patientHistory.historyTreatmentAnalysises.forEach(function(hol1Element) {
			epicriseElement = $scope.epicrise.epicriseGroups[$scope.epicrise.groupsMap[hol1Element.historyTreatmentAnalysisName]].value;
			initFromHol1(epicriseElement, hol1Element);
		});
		console.log($scope.epicrise);
	}

	createGroupElement = function(h1Name){
		var type="isTextHtml";
		if(epicriseTemplate.epicriseBlockConfig[h1Name]){
			if(epicriseTemplate.epicriseBlockConfig[h1Name].isLabor){
				var type="isLabor";
			}else
				if(epicriseTemplate.epicriseBlockConfig[h1Name].isOnDemand){
					var type="isOnDemand";
				}
		}
		return {name:h1Name, type:type};
	}

	initFromHol1 = function(epicriseElement, textHol1){
		var element = angular.element(textHol1);
		var trs = element.find("td.name");
		if(trs.length > 0){
			var laborValues = {};
			for(i = 0; i < trs.length; i++){
				var nameTd = angular.element(trs[i]);
				var valueTd = nameTd.next();
				var unitTd = valueTd.next();
				if(valueTd.text().trim().length > 0){
					laborValues[nameTd.text()] = {value:valueTd.text(), unit:unitTd.text()};
				}
			}
			epicriseElement.laborValues = laborValues;
		}else{
			epicriseElement.textHtml = textHol1;
			if(textHol1 == "") {
				epicriseElement.textHtml = " &nbsp; ";
			}
		}
	}

	$scope.openLaborToEdit = function(laborBlock, laborName){
		laborBlock.laborOpenToEdit=laborName;
		if(!laborBlock.laborValues[laborName])
		laborBlock.laborValues[laborName] = {value:""};
		console.log($('#'+laborName));
		//not work
		$('#'+laborName).focus();
		console.log(laborBlock);
	}

	$scope.setSeekTag = function(tag){
		$scope.seekTag = tag;
		if("все" === tag) {
			$scope.seekTag = "";
		}
	}

	$scope.beetDays = function(){
		var outDay = $scope.epicrise.departmentHistoryOut;
		if($scope.patientHistory){
			var inDay = $scope.patientHistory.patientDepartmentMovements[0].departmentHistoryIn;
			var beetDaysRaw = (outDay - inDay)/dayInMs;
			return Math.round(beetDaysRaw);
		}
	}
	var dayInMs = 1000*60*60*24;
	$scope.changeBlockDate = function(dt, h1Index){
		var epicriseBlockElement = $scope.epicrise[$scope.epicriseTemplate.head1s[h1Index].name];
		epicriseBlockElement.historyTreatmentAnalysisDatetime=dt;
	}

	$scope.addGroup = function(addGroup){
		var middlePosition = ($scope.epicrise.epicriseGroups.length + $scope.epicrise.epicriseGroups.length%2)/2;
		$scope.epicrise.epicriseGroups.splice(middlePosition,0,createGroupElement(addGroup));
	}

	$scope.saveWorkDoc = function(){
		console.log("-----------------");
		saveWorkDoc("/save/epicrise", $scope, $http);
		console.log("-----------------");
	}
	$scope.editOpenClose = function(h1Index){
		var head1s = $scope.epicriseTemplate.head1s;
		if(!$scope.epicrise[head1s[h1Index].name]) {
			$scope.epicrise[head1s[h1Index].name] = {textHtml : " &nbsp; "};
			var head = head1s[h1Index];
			if(head.isOnDemand){
				$scope.epicrise[head1s[h1Index].name].isOnDemand = true;
			}
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
			for(k in $scope.epicrise[head1s[h1Index].name].laborValues)
				if($scope.epicrise[head1s[h1Index].name].laborValues[k].value == "")
					delete $scope.epicrise[head1s[h1Index].name].laborValues[k];
		}
	}

} ]);
