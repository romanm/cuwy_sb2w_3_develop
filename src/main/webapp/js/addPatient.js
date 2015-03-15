cuwyApp.controller('addPatientCtrl', [ '$scope', '$http', '$filter', '$sce', function ($scope, $http, $filter, $sce) {
	console.log("addPatientCtrl");
	$scope.configHol = configHol;
	console.log($scope.configHol);
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
		"adress":{"name":"Адреса, робота"},
		"diagnos":{"name":"Діагноз/Направлення"},
		"medinfo":{"name":"Медична інформація"}
	};
	$scope.newHistoryTemplate["adress"].open = true;
	$scope.newHistoryTemplate["diagnos"].open = true;

	$scope.patientHistory = {};
	$scope.patientEditing = {};
	$scope.requiredFields = {
		"patientSurname":{"group":"pip","name":"Прізвище"}
		,"patientPersonalName":{"group":"pip","name":"Ім’я"}
		,"patientPatronymic":{"group":"pip","name":"По батькові"}
		,"patientDob":{"group":"pip","name":"д.н."}
		,"countryId":{"group":"adress","name":"Країна"}
		,"districtId":{"group":"adress","name":"Область"}
		,"regionId":{"group":"adress","name":"Район"}
		,"localityId":{"group":"adress","name":"місто/село"}
		,"patientJob":{"group":"adress","name":"Місце роботи"}
		,"directId":{"group":"adress","name":"Направлення"}
		,"icd10":{"group":"adress","name":"Діагноз МКБ"}
	};
	
	console.log(parameters.hno);
	var historyFile = "/hol/history_id_"+parameters.hno;
	console.log(historyFile);

	
	seekIcd10Tree = function(){
		console.log("seekIcd10Tree");
		console.log($scope.patientHistory.diagnosisOnAdmission.icdName);
		var seerIcd10TreeUrl = "/seekIcd10Tree/"+$scope.patientHistory.diagnosisOnAdmission.icdName;
		$http({
			method : 'GET',
			url : seerIcd10TreeUrl
		}).success(function(data, status, headers, config) {
			$scope.icd10Root = data;
			console.log($scope.icd10Root);
		}).error(function(data, status, headers, config) {
		});
	};
	//----------------adress---------------------------------------------------
	$scope.changeIcd10Name = function(){
		console.log("changeIcd10Name");
		if($scope.patientHistory.diagnosisOnAdmission.icdName){
			$scope.collapseIcd10Liste = !($scope.patientHistory.diagnosisOnAdmission.icdName.length > 0);
		}
		if(!$scope.collapseIcd10Liste){
			seekIcd10Tree();
		}
	}
	$scope.changeLocalityName = function(){
		console.log("changeLocalityName");
		if($scope.patientEditing.localityName){
			$scope.collapseLocalityListe = !($scope.patientEditing.localityName.length > 0);
		}
	}
	$scope.changeDirectName = function(){
		if($scope.patientEditing.directName){
			$scope.collapseDirectListe = !($scope.patientEditing.directName.length > 0);
		}
	}
	$scope.changeRegionName = function(){
		if($scope.patientEditing.districtName){
			$scope.collapseRegionListe = !($scope.patientEditing.regionName.length > 0);
		}
	}
	$scope.changeDistrictName = function(){
		if($scope.patientEditing.districtName){
			$scope.collapseDistrictListe = !($scope.patientEditing.districtName.length > 0);
		}
	}
	$scope.setIcd10= function(icd10){
		console.log($scope.patientHistory.diagnosisOnAdmission);
		console.log($scope.patientHistory.diagnosisOnAdmission.icdName);
		console.log(icd10);
		$scope.patientHistory.diagnosisOnAdmission.icdCode = icd10.icdCode;
		$scope.patientHistory.diagnosisOnAdmission.icdEnd = icd10.icdEnd;
		$scope.patientHistory.diagnosisOnAdmission.icdId = icd10.icdId;
		$scope.patientHistory.diagnosisOnAdmission.icdName = icd10.icdName;
		$scope.patientHistory.diagnosisOnAdmission.icdStart = icd10.icdStart;
	}
	$scope.setDistrict= function(district){
		$scope.patientEditing.districtName = district.districtName;
		$scope.patientHistory.patientHolDb.districtId = district.districtId;
		$scope.collapseDistrictListe = true;
		$scope.regions = district.regionsHol;
	}
	$scope.setDirect = function(region){
		console.log(region);
		$scope.patientEditing.directName = region.direct_name;
		$scope.patientHistory.patientHolDb.directId = region.direct_id;
		$scope.collapseDirectListe = true;
	}
	$scope.setRegion = function(region){
		$scope.patientEditing.regionName = region.regionName;
		$scope.patientHistory.patientHolDb.regionId = region.regionId;
		$scope.collapseRegionListe = true;
		$scope.localitys = region.localitysHol;
	}
	$scope.setCountry = function(country){
		$scope.patientEditing.countryName = country.countryName;
		$scope.patientHistory.patientHolDb.countryId = country.countryId;
		$scope.collapseDistrictListe = true;
		$scope.districts = country.districtsHol;
	}
	$scope.getLocalityName = function(locality){
		return (locality.localityType == 1 ? "м.":"с.") + " " + locality.localityName;
	}
	$scope.setLocality = function(locality){
		$scope.patientEditing.localityName = locality.localityName;
		$scope.patientEditing.localityType = locality.localityType;
		$scope.patientHistory.patientHolDb.localityId = locality.localityId;
		$scope.collapseLocalityListe = true;
	}
	//----------------adress-------------------------------------------------END
	//----------------ds-------------------------------------------------
	$scope.openIcd10TreeDialog = function(){
		$scope.collapseIcd10Liste = !$scope.collapseIcd10Liste;
		console.log($scope.collapseIcd10Liste);
	}
	//----------------ds-------------------------------------------------END
	//----------------on start--------------------------------------------------
	initDeclareController($scope, $http, $sce, $filter);
	initPatientEdit = function(){
		$scope.collapseIcd10Liste = true;
		$($scope.configHol.countries).each(function (k1,country) {
			if(country.countryId == $scope.patientHistory.patientHolDb.countryId){
				$scope.setCountry(country);
				$($scope.districts).each(function (k2,district) {
					if(district.districtId == $scope.patientHistory.patientHolDb.districtId){
						$scope.setDistrict(district);
						$($scope.regions).each(function (k3,region) {
							if(region.regionId == $scope.patientHistory.patientHolDb.regionId){
								$scope.setRegion(region);
								$($scope.localitys).each(function (k3,locality) {
									if(locality.localityId == $scope.patientHistory.patientHolDb.localityId){
										$scope.setLocality(locality);
									}
								});
							}
						});
					}
				});
			}
		});
	};
	if(parameters.hno){
		$http({
			method : 'GET',
			url : historyFile
		}).success(function(data, status, headers, config) {
			$scope.patientHistory = data;
			console.log($scope.patientHistory);
			initPatientEdit();
		}).error(function(data, status, headers, config) {
		});
	}else{
		$scope.patientHistory.patientHolDb = {};
		console.log($scope.patientHistory.patientHolDb.countryId);
		$scope.setCountry($scope.configHol.countries[0]);
		$scope.patientHistory.patientHolDb.districtId = 1;
		$scope.patientHistory.patientHolDb.regionId = 1;
		initPatientEdit();
	}
	
	//----------------on start-----------------------------------------------END

	$scope.isRegion2of4 = function($index, regions){
		return $index > regions.length/4 && $index < regions.length/4*2;
	}

	$scope.isRegion3of4 = function($index, regions){
		return $index > regions.length/4*2 && $index < regions.length/4*3;
	}

	$scope.savePatientHistory = function(){
		if(!checkeRequiredFields())
			return true;
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
	
	checkeRequiredFields = function(){
		console.log("checkeRequiredFields");
		var r = true;
		if(typeof $scope.patientHistory.patientHolDb.patientDob === 'undefined')
		{
			$scope.patientHistory.patientHolDb.patientDob = new Date();
//			$scope.patientHistory.patientHolDb.patientDob
//			.setMonth($scope.patientHistory.patientHolDb.patientDob.getMonth()-1)
			$scope.patientHistory.patientHolDb.patientDob.setDate($scope.patientHistory.patientHolDb.patientDob.getDate()-5);
		}
		var pDob = $scope.patientHistory.patientHolDb.patientDob;
		var todayDate = new Date();
		var age = $scope.calculateAge(pDob, todayDate);
		console.log("year "+age);
		var pDob2 = pDob;
		pDob2.setFullYear(pDob.getFullYear()+age);
		var month = $scope.calculateMonth(pDob2, todayDate);
		console.log("month "+month);
		pDob2.setMonth(pDob2.getMonth()+month);
		var day = $scope.calculateDay(pDob2, todayDate);
		console.log("day "+day);
		Object.keys($scope.requiredFields).forEach(function(key) {
			if(typeof $scope.patientHistory.patientHolDb[key] === 'undefined' 
			&& typeof $scope.patientHistory[key] === 'undefined'){
				$scope.requiredFields[key].isFull = false;
				r = false;
			}else{
				$scope.requiredFields[key].isFull = true;
			}
		});
		console.log($scope.requiredFields);
		return r;
	}
	$scope.requiredFieldsNoFull = function(gr){
		var rfNames = [];
		Object.keys($scope.requiredFields).forEach(function(key) {
			if($scope.requiredFields[key].group === gr){
				if($scope.requiredFields[key].isFull === false){
					rfNames.push(key);
				}
			}
		});
		return rfNames;
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
