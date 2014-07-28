package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cuwy1.hol.model.CountryHol;
import org.cuwy1.hol.model.DepartmentHol;
import org.cuwy1.hol.model.DiagnosisOnAdmission;
import org.cuwy1.hol.model.DistrictHol;
import org.cuwy1.hol.model.HistoryTreatmentAnalysis;
import org.cuwy1.hol.model.PatientDepartmentMovement;
import org.cuwy1.hol.model.PatientDiagnosisHol;
import org.cuwy1.hol.model.PatientHistory;
import org.cuwy1.hol.model.RegionHol;
import org.cuwy1.holDb.model.PatientHolDb;
import org.cuwy1.icd10.Icd10UaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

@Component("cuwyDbService1")
public class CuwyDbService1 {
	
	private static final Logger logger = LoggerFactory.getLogger(CuwyDbService1.class);
	
	private JdbcTemplate jdbcTemplate;

	public CuwyDbService1(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/hol?useUnicode=true&characterEncoding=utf-8");
		dataSource.setUsername("hol");
		dataSource.setPassword("hol");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public List<Icd10UaClass> getIcd10UaChilds() {
		List<Icd10UaClass> icd10Classes = jdbcTemplate.query(
				"SELECT * from icd WHERE icd_left_key = ?", new Object[] { 1 },
				new RowMapper<Icd10UaClass>() {
					@Override
					public Icd10UaClass mapRow(ResultSet rs, int rowNum) throws SQLException {
						Icd10UaClass icd10UaClass = new Icd10UaClass(
								rs.getLong("icd_id"), rs.getLong("icd_start"), rs.getLong("icd_end"), 
								rs.getString("icd_code"), rs.getString("icd_name"));
						return icd10UaClass;
					}
				});

		showList(icd10Classes);
		return icd10Classes;
	}

	private void showList(List<Icd10UaClass> icd10Classes) {
		for (Icd10UaClass icd10Class : icd10Classes) {
			System.out.println(icd10Class);
		}
	}

	class DepartmentHolRowMapper implements RowMapper {
		@Override
		public DepartmentHol mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			return new DepartmentHol(rs.getInt("department_id"), 
					rs.getString("department_name"),
					rs.getBoolean("department_active"),
					rs.getShort("department_profile_id")
					);
		}
	}
	public DepartmentHol getDepartmentsHol(int id) {
		return jdbcTemplate.queryForObject(
				"SELECT * FROM department WHERE department_id = ?", 
				new Object[] { id }, new DepartmentHolRowMapper());
	}

	public List<DepartmentHol> getDepartmentsHol() {
		return jdbcTemplate.query(
				"SELECT * FROM department", 
				new DepartmentHolRowMapper());
	}

	public List<PatientDiagnosisHol> getDepartmentsHolPatientsDiagnose(Integer departmentId) {
		String sql = "SELECT concat(p.patient_surname,' ',p.patient_name,' ',p.patient_patronnymic) name"
		+ ", dh.department_history_in"
		+ ", h.history_in"
		+ ", h.history_no"
		+ ", h.history_id"
		+ ", h.patient_id"
		+ ", hd.max_diagnosis_id"
		+ ", icd_code, icd_name"
		+ " FROM department_history dh, history h, patient p, icd i,"
		+ "(SELECT history_id,diagnos_id,max(diagnos_id) max_diagnosis_id, icd_id FROM history_diagnos GROUP BY history_id) hd"
		+ " WHERE h.patient_id = p.patient_id AND h.history_id=hd.history_id "
		+ " AND i.icd_id = hd.icd_id "
		+ " AND h.history_id=dh.history_id AND dh.department_history_out IS NULL "
		+ " AND dh.department_id = ? ";
		logger.info("\n"+sql+departmentId);
		return jdbcTemplate.query(
				sql, new Object[] { departmentId }, 
				new RowMapper<PatientDiagnosisHol>(){
					@Override
					public PatientDiagnosisHol mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						PatientDiagnosisHol patientDiagnosisHol = new PatientDiagnosisHol();
						patientDiagnosisHol.setName(rs.getString("name"));
						patientDiagnosisHol.setHistory_in(rs.getTimestamp("history_in"));
						patientDiagnosisHol.setIcd_code(rs.getString("icd_code"));
						patientDiagnosisHol.setIcd_name(rs.getString("icd_name"));
						patientDiagnosisHol.setPatient_id(rs.getInt("patient_id"));
						patientDiagnosisHol.setHistory_no(rs.getInt("history_no"));
						patientDiagnosisHol.setHistory_id(rs.getInt("history_id"));
						patientDiagnosisHol.setDiagnos_id(rs.getShort("max_diagnosis_id"));
						return patientDiagnosisHol;
					}
				});
	}

	public PatientHistory getPatientHistory(int historyNo) {
		String sql ="SELECT * FROM patient p, history h"
		+ " WHERE h.history_out IS NULL AND h.patient_id=p.patient_id"
		+ " AND h.history_no= ? ";
		logger.info("\n"+sql+historyNo);
		return jdbcTemplate.queryForObject(
			sql, new Object[] { historyNo }, 
			new RowMapper<PatientHistory>(){
				@Override
				public PatientHistory mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					PatientHistory patientHistory = new PatientHistory();
					patientHistory.setHistory_id(rs.getInt("history_id"));
					patientHistory.setPatientId(rs.getInt("patient_id"));
					patientHistory.setPatient_gender(rs.getBoolean("patient_gender"));
					patientHistory.setPatient_dob(rs.getDate("patient_dob"));
					patientHistory.setPatient_blood(rs.getString("patient_blood"));
					patientHistory.setPatient_rh(rs.getBoolean("patient_rh"));
					patientHistory.setPatient_street(rs.getString("patient_street"));
					patientHistory.setPatient_house(rs.getString("patient_house"));
					patientHistory.setPatient_flat(rs.getString("patient_flat"));
					patientHistory.setPatient_job(rs.getString("patient_job"));
					patientHistory.setPatient_tbc(rs.getBoolean("patient_tbc"));
					patientHistory.setPatient_sc(rs.getBoolean("patient_sc"));
					patientHistory.setPatient_bj(rs.getBoolean("patient_bj"));
					patientHistory.setPatient_t(rs.getBoolean("patient_t"));
					patientHistory.setPatient_hbs(rs.getBoolean("patient_hbs"));
					patientHistory.setPatient_rw(rs.getBoolean("patient_rw"));
					patientHistory.setPatient_rw_date(rs.getDate("patient_rw_date"));
					return patientHistory;
				}
			});
	}

	public List<PatientDepartmentMovement> getPatientDepartmentMovements(int historyId) {
		String sql ="SELECT * FROM (SELECT "
				+ " d.department_id,"
				+ " d.department_name,"
				+ " dh.personal_department_id_in,"
				+ " dh.personal_department_id_out,"
				+ " dh.history_id,"
				+ " dh.department_history_in,"
				+ " dh.department_history_out"
				+ " FROM department d, department_history dh"
				+ " WHERE  d.department_id=dh.department_id) ddh LEFT JOIN (SELECT "
				+ " pd.personal_department_id,"
				+ " p.personal_id,"
				+ " p.personal_surname,"
				+ " p.personal_name ,"
				+ " p.personal_patronymic"
				+ " FROM personal p, personal_department pd "
				+ " WHERE p.personal_id=pd.personal_id ) ppd"
				+ " ON ddh.personal_department_id_out = ppd.personal_department_id"
				+ " WHERE ddh.history_id = ?";
		logger.info("\n"+sql+historyId);
		return jdbcTemplate.query(
				sql, new Object[] { historyId }, 
				new RowMapper<PatientDepartmentMovement>(){
					@Override
					public PatientDepartmentMovement mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						PatientDepartmentMovement patientDepartmentMovement = new PatientDepartmentMovement();
						patientDepartmentMovement.setDepartmentName(rs.getString("department_name"));
						patientDepartmentMovement.setPersonalSurname(rs.getString("personal_surname"));
						patientDepartmentMovement.setPersonalName(rs.getString("personal_name"));
						patientDepartmentMovement.setPersonalPatronymic(rs.getString("personal_patronymic"));
						patientDepartmentMovement.setDepartmentHistoryIn(rs.getTimestamp("department_history_in"));
						patientDepartmentMovement.setDepartmentHistoryOut(rs.getTimestamp("department_history_out"));
						return patientDepartmentMovement;
					}
				});
	}

	public List<HistoryTreatmentAnalysis> getHistoryTreatmentAnalysises(int historyId) {
		String sql ="SELECT * FROM history_treatment_analysis WHERE history_id=?";
		logger.info("\n"+sql+historyId);
		return jdbcTemplate.query(
			sql, new Object[] { historyId }, 
			new RowMapper<HistoryTreatmentAnalysis>(){
				@Override
				public HistoryTreatmentAnalysis mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					HistoryTreatmentAnalysis historyTreatmentAnalysis = new HistoryTreatmentAnalysis();
					historyTreatmentAnalysis.setHistoryTreatmentAnalysisText(rs.getString("history_treatment_analysis_text"));
					historyTreatmentAnalysis.setHistoryTreatmentAnalysisDatetime(rs.getTimestamp("history_treatment_analysis_datetime"));
					return historyTreatmentAnalysis;
				}
			});
	}

	public DiagnosisOnAdmission getDiagnosisOnAdmission(int historyId) {
		String sql = "SELECT * FROM history_diagnos hd, icd i"
				+ " WHERE 2 = hd.diagnos_id AND i.icd_id = hd.icd_id AND history_id=?";
		logger.info("\n"+sql+historyId);
		return jdbcTemplate.queryForObject(
				sql, new Object[] { historyId }, 
				new RowMapper<DiagnosisOnAdmission>(){
					@Override
					public DiagnosisOnAdmission mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						DiagnosisOnAdmission diagnosisOnAdmission = new DiagnosisOnAdmission();
						diagnosisOnAdmission.setHistoryDiagnosDate(rs.getTimestamp("history_diagnos_date"));
						diagnosisOnAdmission.setIcdCode(rs.getString("icd_code"));
						diagnosisOnAdmission.setIcdName(rs.getString("icd_name"));
						return diagnosisOnAdmission;
					}
				});
	}

	public List<RegionHol> getRegions(Integer districtId) {
		String like = "%хм%";
		String sql = "SELECT * FROM region where district_id = ? and region_name like ?";
		logger.info("\n"+sql+districtId);
		return jdbcTemplate.query(
			sql, new Object[] { districtId, like }, 
			new RowMapper<RegionHol>(){
				@Override
				public RegionHol mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					RegionHol regionHol = new RegionHol();
					regionHol.setRegionId(rs.getInt("region_id"));
					regionHol.setDistrictId(rs.getInt("district_id"));
					regionHol.setRegionName(rs.getString("region_name"));
					return regionHol;
				}
			});
	}

	public void setPatientHolDb(PatientHistory patientHistory) {
		int patientId = patientHistory.getPatientId();
		String sql = "SELECT * FROM patient p WHERE patient_id = ?";
		logger.info("\n"+sql+patientId);
		PatientHolDb patientHolDb = jdbcTemplate.queryForObject(
			sql, new Object[] { patientId }, 
			new RowMapper<PatientHolDb>(){
				@Override
				public PatientHolDb mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					PatientHolDb patientHolDb = new PatientHolDb();
					patientHolDb.setPatientSurname(rs.getString("patient_surname"));
					patientHolDb.setPatientPersonalName(rs.getString("patient_name"));
					patientHolDb.setPatientPatronymic(rs.getString("patient_patronnymic"));
					patientHolDb.setPatientGender(rs.getBoolean("patient_gender"));
					patientHolDb.setPatientId(rs.getInt("patient_id"));
					patientHolDb.setPatientStreet(rs.getString("patient_street"));
					patientHolDb.setPatientHouse(rs.getInt("patient_house"));
					patientHolDb.setPatientFlat(rs.getInt("patient_flat"));
					return patientHolDb;
				}
			});
		patientHistory.setPatientHolDb(patientHolDb);
	}
	public void setPatientName(PatientHistory patientHistory) {
		int patientId = patientHistory.getPatientId();
		String sql = "SELECT concat(p.patient_surname,' ',p.patient_name,' ',p.patient_patronnymic) name"
				+ " from patient p where patient_id= ?";
		logger.info("\n"+sql+patientId);
		PatientHistory patientHistoryDb = jdbcTemplate.queryForObject(
				sql, new Object[] { patientId }, 
				new RowMapper<PatientHistory>(){
					@Override
					public PatientHistory mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						PatientHistory patientHistoryDb = new PatientHistory();
						patientHistoryDb.setPatientName(rs.getString("name"));
						return patientHistoryDb;
					}
				});
		patientHistory.setPatientName(patientHistoryDb.getPatientName());
	}

	public List<CountryHol> readCountries() {
		logger.info("\n"+sqlCountry);
		Map<Integer, CountryHol> mapCountryHol = new HashMap<Integer, CountryHol>();
		List<CountryHol> countries = jdbcTemplate.query(
				sqlCountry, new Object[] {}, 
				new CountryRowMapper(mapCountryHol)
				);
		Map<Integer, DistrictHol> mapDistrictHol = new HashMap<Integer, DistrictHol>();
		jdbcTemplate.query(
				sqlDistrict, new Object[] {}, 
				new DestrictRowMapper(mapCountryHol, mapDistrictHol)
				);
		jdbcTemplate.query(
				sqlRegion, new Object[] {}, 
				new RegionRowMapper(mapDistrictHol)
				);
		logger.info("\n"+sqlDistrict);
		return countries;
	}

	String sqlCountry	= "SELECT * FROM country";
	String sqlDistrict	= "SELECT * FROM district";
	String sqlRegion	= "SELECT * FROM region WHERE region_active ORDER BY region_name";

	private class RegionRowMapper<T> implements RowMapper<T>{
		private Map<Integer, DistrictHol> mapDistrictHol;
		public RegionRowMapper(Map<Integer, DistrictHol> mapDistrictHol) {
			this.mapDistrictHol = mapDistrictHol;
		}
		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			RegionHol regionHol = new RegionHol();
			regionHol.setRegionId(rs.getInt("region_id"));
			regionHol.setDistrictId(rs.getInt("district_id"));
			regionHol.setRegionName(rs.getString("region_name"));
			DistrictHol districtHol = mapDistrictHol.get(regionHol.getDistrictId());
			if(districtHol.getRegionsHol() == null)
				districtHol.setRegionsHol(new ArrayList<RegionHol>());
			districtHol.getRegionsHol().add(regionHol);
			return (T) regionHol;
		}
	}
	private class DestrictRowMapper<T> implements RowMapper<T>{
		private Map<Integer, CountryHol>	mapCountryHol;
		private Map<Integer, DistrictHol>	mapDistrictHol;
		public DestrictRowMapper(Map<Integer, CountryHol> mapCountryHol, Map<Integer, DistrictHol> mapDistrictHol) {
			this.mapCountryHol	= mapCountryHol;
			this.mapDistrictHol	= mapDistrictHol;
		}
		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			DistrictHol districtHol = new DistrictHol();
			districtHol.setCountryId(rs.getInt("country_id"));
			districtHol.setDistrictId(rs.getInt("district_id"));
			districtHol.setDistrictName(rs.getString("district_name"));
			mapDistrictHol.put(districtHol.getDistrictId(), districtHol);
			CountryHol countryHol = mapCountryHol.get(districtHol.getCountryId());
			if(countryHol.getDistrictsHol() == null)
				countryHol.setDistrictsHol(new ArrayList<DistrictHol>());
			countryHol.getDistrictsHol().add(districtHol);
			return (T) districtHol;
		}
		
	}
	private class CountryRowMapper<T> implements RowMapper<T>{
		private Map<Integer, CountryHol> mapCountryHol;
		public CountryRowMapper(Map<Integer, CountryHol> mapCountryHol) {
			this.mapCountryHol=mapCountryHol;
		}
		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			CountryHol countryHol = new CountryHol();
			countryHol.setCountryId(rs.getInt("country_id"));
			countryHol.setCountryName(rs.getString("country_name"));
			mapCountryHol.put(countryHol.getCountryId(), countryHol);
			return (T) countryHol;
		}
		
	}
	
}
