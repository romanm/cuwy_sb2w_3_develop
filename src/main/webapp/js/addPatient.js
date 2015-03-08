
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
		"adress":{"name":"Адреса"},
		"diagnos":{"name":"Діагноз/Направлення"},
		"medinfo":{"name":"Медична інформація"}
	};
	$scope.newHistoryTemplate["adress"].open = true;

	$scope.patientHistory = {};
	$scope.patientEditing = {};
	
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
			initPatientEdit();
		}).error(function(data, status, headers, config) {
		});
	}
	initPatientEdit = function(){
		$scope.patientHistory.patientHolDb.countryId;
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
	//----------------adress---------------------------------------------------
	$scope.changeLocalityName = function(){
		console.log("changeLocalityName");
		if($scope.patientEditing.localityName){
			$scope.collapseLocalityListe = !($scope.patientEditing.localityName.length > 0);
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
	$scope.setDistrict= function(district){
		$scope.patientEditing.districtName = district.districtName;
		$scope.patientHistory.patientHolDb.districtId = district.districtId;
		$scope.collapseDistrictListe = true;
		$scope.regions = district.regionsHol;
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
	$scope.supportDistrictField = function(){
		var collapseDistrictListe = true;
		if($scope.patientEditing.district){
			collapseDistrictListe = !($scope.patientEditing.district.length > 0);
			if(!$scope.districts){
				//seek all districts
			}else{
				$($scope.districts).each(function(){
					if(this.districtName == $scope.patientEditing.district){
						collapseDistrictListe = true;
					}
				});
			}
		}
		return collapseDistrictListe;
	}
	//----------------adress-------------------------------------------------END
	

	$scope.isRegion2of4 = function($index, regions){
		return $index > regions.length/4 && $index < regions.length/4*2;
	}

	$scope.isRegion3of4 = function($index, regions){
		return $index > regions.length/4*2 && $index < regions.length/4*3;
	}

	
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