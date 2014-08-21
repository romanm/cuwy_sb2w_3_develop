package hello;

import java.sql.PreparedStatement;
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
import org.cuwy1.holDb.model.HistoryHolDb;
import org.cuwy1.holDb.model.PatientHolDb;
import org.cuwy1.icd10.Icd10UaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

@Component("cuwyDbService1")
public class CuwyDbService1 {
	
	private static final Logger logger = LoggerFactory.getLogger(CuwyDbService1.class);
	
	private JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() { return jdbcTemplate; }

	public CuwyDbService1(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/hol?useUnicode=true&characterEncoding=utf-8");
		dataSource.setUsername("hol");
		dataSource.setPassword("hol");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Icd10UaClass getIcd10UaChilds(Icd10UaClass icd10Class) {
		String sql = "SELECT * FROM icd i1 WHERE icd_start >= ? AND icd_end <= ? AND icd_id != ? ";
		String sql2 = sql.replaceFirst("\\?", "" + icd10Class.getIcdStart())
				.replaceFirst("\\?", "" + icd10Class.getIcdEnd())
				.replaceFirst("\\?", "" + icd10Class.getIcdId());
		logger.info("\n "+sql2);
		List<Icd10UaClass> icd10Classes = jdbcTemplate.query(
				sql2,
				new Icd2TreeMapper(icd10Class));
		return icd10Class;
	}
	public Icd10UaClass getIcd10UaGroups() {
		Icd10UaClass icd10UaRoot = new Icd10UaClass();
		String sql = "SELECT * FROM icd i1 WHERE i1.icd_code not like '%.%'";
//		String sql = "SELECT * FROM icd i1 WHERE i1.icd_code like '%-%' ";
		logger.info("\n "+sql);
		List<Icd10UaClass> icd10Classes = jdbcTemplate.query(
				sql,
				new Icd2TreeMapper(icd10UaRoot));
		return icd10UaRoot;
	}
	class Icd2TreeMapper<T> implements RowMapper<T>{

		private Icd10UaClass icd10UaRoot;
		private List<Icd10UaClass> icd10UaPfad;
		public Icd2TreeMapper(Icd10UaClass icd10UaRoot) {
			this.icd10UaRoot = icd10UaRoot;
			this.icd10UaPfad = new ArrayList<Icd10UaClass>();
			icd10UaPfad.add(icd10UaRoot);
		}

		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			Icd10UaClass icd10UaClass = new Icd10UaClass();
			icd10UaClass.setIcdId(rs.getInt("icd_id"));
			icd10UaClass.setIcdLeftKey(rs.getInt("icd_left_key"));
			icd10UaClass.setIcdRightKey(rs.getInt("icd_right_key"));
			icd10UaClass.setIcdStart(rs.getInt("icd_start"));
			icd10UaClass.setIcdEnd(rs.getInt("icd_end"));
			icd10UaClass.setIcdCode(rs.getString("icd_code"));
			icd10UaClass.setIcdName(rs.getString("icd_name"));
			int i = icd10UaPfad.size()-1;
			for (; i>0; i--) 
				if(icd10UaClass.getIcdEnd() <= icd10UaPfad.get(i).getIcdEnd())
					break;
			icd10UaPfad.get(i).addChild(icd10UaClass);
			if(icd10UaPfad.size() > i+1)
				icd10UaPfad = icd10UaPfad.subList(0, i+1);
			icd10UaPfad.add(icd10UaClass);
			return (T) icd10UaClass;
		}
		
	}
	public List<Icd10UaClass> getIcd10UaChilds() {
		int icdLeftKey = 1;
		String sql = "SELECT * FROM icd WHERE icd_left_key = ?";
		logger.info("\n "+sql.replaceFirst("\\?", ""+icdLeftKey));
		List<Icd10UaClass> icd10Classes = jdbcTemplate.query(
				sql, new Object[] { icdLeftKey },
				new RowMapper<Icd10UaClass>() {
					@Override
					public Icd10UaClass mapRow(ResultSet rs, int rowNum) throws SQLException {
						Icd10UaClass icd10UaClass = new Icd10UaClass();
						icd10UaClass.setIcdId(rs.getInt("icd_id"));
						icd10UaClass.setIcdStart(rs.getInt("icd_start"));
						icd10UaClass.setIcdEnd(rs.getInt("icd_end"));
						icd10UaClass.setIcdCode(rs.getString("icd_code"));
						icd10UaClass.setIcdName(rs.getString("icd_name"));
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

	String sql_WHERE_YearWeek = " YEAR(h.history_in)= ? AND WEEKOFYEAR(h.history_in) = ? ";
	String sqlPatientsYearWeek = "SELECT * FROM history h WHERE " + sql_WHERE_YearWeek;

	public List<HistoryHolDb> getHistorysYearWeek(Integer year, Integer week) {
		return getHistory(year, week, sqlPatientsYearWeek);
	}

	public List<HistoryHolDb> getHistorysYearWeek(Integer year, Integer week,
			Integer departmentId) {
		String andDeparment = " AND history_department_id = ? ".replaceFirst("\\?", "" + departmentId);
		return getHistory(year, week, sqlPatientsYearWeek + andDeparment);
	}

	private List<HistoryHolDb> getHistory(Integer year, Integer week, String sqlPatientsYearWeek) {
		logger.info("\n"+sqlPatientsYearWeek.replaceFirst("\\?", ""+year).replaceFirst("\\?", ""+week));
		Map<Integer, HistoryHolDb> mapHistoryOfPatient = new HashMap<Integer, HistoryHolDb>();
		List patientsYearWeek = jdbcTemplate.query(
				sqlPatientsYearWeek, new Object[] { year, week }, 
				new HistoryHolDbRowMapper(mapHistoryOfPatient)
				);
		String sql2 = "SELECT p.* FROM patient p, (" + sqlPatientsYearWeek
		+ ") h WHERE p.patient_id = h.patient_id";
		logger.info("\n"+sql2.replaceFirst("\\?", ""+year).replaceFirst("\\?", ""+week));
		jdbcTemplate.query(
				sql2, new Object[] { year, week }, 
				new PatientHolDbRowMapper(mapHistoryOfPatient)
				);
		String sql3 = sqlPatientDepartmentMovement.replaceFirst("\\?", 
			"SELECT h.history_id FROM (" + sqlPatientsYearWeek + ") h");
		logger.info("\n"+sql3.replaceFirst("\\?", ""+year).replaceFirst("\\?", ""+week));
		jdbcTemplate.query(
				sql3, new Object[] { year, week }, 
				new PatientDepartmentMovementRowMapper(mapHistoryOfPatient)
				);
		return patientsYearWeek;
	}

	public List<Map<String, Object>> getHistorysDepartmentYearWeek(Integer year, Integer week) {
		String sql = "SELECT department_name, department_id, cnt "
				+ " FROM department d, ( "
				+ " SELECT history_department_id, COUNT(history_department_id) cnt "
				+ " FROM history h WHERE "
				+ sql_WHERE_YearWeek
				+ " GROUP BY history_department_id ) hd "
				+ " WHERE hd.history_department_id = d.department_id";
		logger.info("\n"+sql.replaceFirst("\\?", ""+year).replaceFirst("\\?", ""+week));
		List<Map<String, Object>> countPatientsProMonth 
		= jdbcTemplate.queryForList(sql, new Object[] { year, week });
		return countPatientsProMonth;
	}

	class HistoryHolDbRowMapper<T> implements RowMapper<T>{
		private Map<Integer, HistoryHolDb> mapHistoryOfPatient;

		public HistoryHolDbRowMapper() { }

		public HistoryHolDbRowMapper(
				Map<Integer, HistoryHolDb> mapHistoryOfPatient) {
			this.mapHistoryOfPatient = mapHistoryOfPatient;
		}

		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			HistoryHolDb historyHolDb = new HistoryHolDb();
			historyHolDb.setHistoryId(rs.getInt("history_id"));
			historyHolDb.setHistoryNo(rs.getInt("history_no"));
			historyHolDb.setPatientId(rs.getInt("patient_id"));
			historyHolDb.setHistoryIn(rs.getTimestamp("history_in"));
			if(mapHistoryOfPatient != null)
				mapHistoryOfPatient.put(historyHolDb.getPatientId(), historyHolDb);
			return (T) historyHolDb;
		}
		
	}

	public HistoryHolDb getHistoryHolDbByNo(int historyNo) {
		String sql = "SELECT * FROM history WHERE history_out IS NULL AND history_no = ? ";
		logger.info("\n"+sql+historyNo);
		return jdbcTemplate.queryForObject(
				sql, new Object[] { historyNo }, 
				new HistoryHolDbRowMapper()
				);
	}
	public PatientHistory getPatientHistoryByNo(int historyNo) {
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

	String sqlPatientDepartmentMovement ="SELECT * FROM "
			+ "( SELECT "
			+ " h.patient_id, dh.history_id, d.department_id, d.department_name,"
			+ " dh.personal_department_id_in, dh.personal_department_id_out,"
			+ " dh.department_history_in, dh.department_history_out"
			+ " FROM department d, department_history dh, history h"
			+ " WHERE h.history_id = dh.history_id AND d.department_id=dh.department_id"
			+ ") ddh LEFT JOIN (SELECT "
			+ " pd.personal_department_id,"
			+ " p.personal_id,"
			+ " p.personal_surname,"
			+ " p.personal_name ,"
			+ " p.personal_patronymic"
			+ " FROM personal p, personal_department pd "
			+ " WHERE p.personal_id=pd.personal_id ) ppd"
			+ " ON ddh.personal_department_id_out = ppd.personal_department_id"
			+ " WHERE ddh.history_id IN ( ? )";
	public List<PatientDepartmentMovement> getPatientDepartmentMovements(int historyId) {
		logger.info("\n"+sqlPatientDepartmentMovement.replaceFirst("\\?", ""+historyId));
		return jdbcTemplate.query(
				sqlPatientDepartmentMovement, new Object[] { historyId }, 
				new PatientDepartmentMovementRowMapper()
				);
	}
	private class PatientDepartmentMovementRowMapper<T> implements RowMapper<T>{
		private Map<Integer, HistoryHolDb> mapHistoryOfPatient;
		public PatientDepartmentMovementRowMapper(
				Map<Integer, HistoryHolDb> mapHistoryOfPatient) {
			this.mapHistoryOfPatient = mapHistoryOfPatient;
		}

		public PatientDepartmentMovementRowMapper() { }

		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			PatientDepartmentMovement patientDepartmentMovement = new PatientDepartmentMovement();
			patientDepartmentMovement.setHistoryId(rs.getInt("history_id"));
			patientDepartmentMovement.setPatientId(rs.getInt("patient_id"));
			patientDepartmentMovement.setDepartmentId(rs.getInt("department_id"));
			patientDepartmentMovement.setDepartmentName(rs.getString("department_name"));
			patientDepartmentMovement.setPersonalSurname(rs.getString("personal_surname"));
			patientDepartmentMovement.setPersonalName(rs.getString("personal_name"));
			patientDepartmentMovement.setPersonalPatronymic(rs.getString("personal_patronymic"));
			patientDepartmentMovement.setDepartmentHistoryIn(rs.getTimestamp("department_history_in"));
			patientDepartmentMovement.setDepartmentHistoryOut(rs.getTimestamp("department_history_out"));
			if(mapHistoryOfPatient != null){
				int patientId = patientDepartmentMovement.getPatientId();
				HistoryHolDb historyHolDb = mapHistoryOfPatient.get(patientId);
				if(historyHolDb.getPatientDepartmentMovements() == null)
					historyHolDb.setPatientDepartmentMovements(new ArrayList<PatientDepartmentMovement>());
				historyHolDb.getPatientDepartmentMovements().add(patientDepartmentMovement);
			}
			return (T) patientDepartmentMovement;
		}
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
		String sql = "SELECT * FROM region where district_id = ? AND region_name like ?";
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

	public void savePatientHolDb(final PatientHolDb patientHolDb) {
		String sql = "UPDATE patient "
				+ "SET patient_phone_1 = ?"
				+ ", patient_phone_2 = ? "
				+ ", patient_surname = ? "
				+ ", patient_name = ? "
				+ ", patient_patronnymic = ? "
				+ ", patient_gender = ? "
				+ ", patient_street = ? "
				+ ", patient_house = ? "
				+ ", patient_flat = ? "
				+ ", patient_job = ? "
				+ ", patient_blood = ? "
				+ ", patient_rh = ? "
				+ ", patient_bj = ? "
				+ " WHERE patient_id = ?";
		logger.info("\n"+sql+patientHolDb.getPatientId());
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, patientHolDb.getPatientPhoneHome());
				ps.setString(2, patientHolDb.getPatientPhoneMobil());
				ps.setString(3, patientHolDb.getPatientSurname());
				ps.setString(4, patientHolDb.getPatientPersonalName());
				ps.setString(5, patientHolDb.getPatientPatronymic());
				ps.setBoolean(6, patientHolDb.getPatientGender()==1?true:false);
				ps.setString(7, patientHolDb.getPatientStreet());
				ps.setString(8, patientHolDb.getPatientHouse());
				ps.setString(9, patientHolDb.getPatientFlat());
				ps.setString(10, patientHolDb.getPatientJob());
				ps.setString(11, patientHolDb.getPatientBlood());
				patientHolDb.setPatientRh(patientHolDb.getPatientRh2() == 1 ? true : false);
				ps.setBoolean(12, patientHolDb.getPatientRh());
				String patientBj = patientHolDb.getPatientBj();
				ps.setString(13, patientBj.equals("")?null:patientBj);
				ps.setInt(14, patientHolDb.getPatientId());
			}
		});
	}

	private class PatientHolDbRowMapper<T> implements RowMapper<T>{
		private Map<Integer, HistoryHolDb> mapHistoryOfPatient;
		public PatientHolDbRowMapper(){ }
		public PatientHolDbRowMapper(
				Map<Integer, HistoryHolDb> mapHistoryOfPatient) {
			this.mapHistoryOfPatient = mapHistoryOfPatient;
		}
		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			PatientHolDb patientHolDb = new PatientHolDb();
			patientHolDb.setPatientId(rs.getInt("patient_id"));
			patientHolDb.setPatientSurname(rs.getString("patient_surname"));
			patientHolDb.setPatientPersonalName(rs.getString("patient_name"));
			patientHolDb.setPatientGender(rs.getBoolean("patient_gender")?1:0);
			patientHolDb.setPatientPatronymic(rs.getString("patient_patronnymic"));
			patientHolDb.setPatientDob(rs.getDate("patient_dob"));
			patientHolDb.setPatientStreet(rs.getString("patient_street"));
			patientHolDb.setPatientHouse(rs.getString("patient_house"));
			patientHolDb.setPatientFlat(rs.getString("patient_flat"));
			patientHolDb.setPatientJob(rs.getString("patient_job"));
			patientHolDb.setPatientPhoneHome(rs.getString("patient_phone_1"));
			patientHolDb.setPatientPhoneMobil(rs.getString("patient_phone_2"));
			patientHolDb.setPatientBlood(rs.getString("patient_blood"));
			patientHolDb.setPatientRh(rs.getBoolean("patient_rh"));
			patientHolDb.setPatientRh2FromBoolean(patientHolDb.getPatientRh());
			String patient_bj = rs.getString("patient_bj");
			patientHolDb.setPatientBj(patient_bj==null?"":patient_bj);
			patientHolDb.setPatientHivFromBoolean(rs.getBoolean("patient_aid"));
			patientHolDb.setPatientHbsFromBoolean(rs.getBoolean("patient_hbs"));
			patientHolDb.setPatientRwFromBoolean(rs.getBoolean("patient_rw"));
			patientHolDb.setPatientRwDate(rs.getDate("patient_rw_date"));
			if(mapHistoryOfPatient != null){
				HistoryHolDb historyHolDb = mapHistoryOfPatient.get(patientHolDb.getPatientId());
				historyHolDb.setPatientHolDb(patientHolDb);
			}
			return (T) patientHolDb;
		}
	}

	public PatientHolDb getPatientHolDb(int patientId) {
		String sql = "SELECT * FROM patient p WHERE patient_id = ?";
		logger.info("\n"+sql+patientId);
		return jdbcTemplate.queryForObject(
			sql, new Object[] { patientId }, 
			new PatientHolDbRowMapper()
			);
	}

	public void setPatientName(PatientHistory patientHistory) {
		int patientId = patientHistory.getPatientId();
		String sql = "SELECT CONCAT(p.patient_surname,' ',p.patient_name,' ',p.patient_patronnymic) name"
				+ " FROM patient p WHERE patient_id= ?";
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

	public List<Map<String, Object>> countPatientsProYear() {
		String sql = "SELECT YEAR(h.history_in) year, COUNT(year(h.history_in)) yearPatientCount "
				+ " FROM history h GROUP BY YEAR(h.history_in) "
				+ " ORDER BY YEAR(h.history_in) DESC";
		logger.info("\n"+sql);
		List<Map<String, Object>> countPatientProYear = jdbcTemplate.queryForList(sql);
		return countPatientProYear;
	}

	public List<Map<String, Object>> countPatientsProWeeks(Integer year,
			Integer minWeek, Integer maxWeek) {
		String sql = "SELECT WEEKOFYEAR(h.history_in) weekNr, COUNT(WEEKOFYEAR(h.history_in)) weekPatientCount "
				+ " FROM history h "
				+ " WHERE YEAR(h.history_in) = ? AND (WEEKOFYEAR(h.history_in) = ? OR WEEKOFYEAR(h.history_in) = ?) "
				+ " GROUP BY WEEKOFYEAR(h.history_in) ORDER BY WEEKOFYEAR(h.history_in) DESC";
		logger.info("\n"+sql+" "+year+" "+minWeek+" "+maxWeek);
		List<Map<String, Object>> countPatientsProWeek 
		= jdbcTemplate.queryForList(sql, new Object[] { year, minWeek, maxWeek });
		return countPatientsProWeek;
	}

	public List<Map<String, Object>> countPatientsProWeeks(Integer year, Integer monthNr) {
		String sql = "SELECT WEEKOFYEAR(h.history_in) weekNr"
				+ ", COUNT(WEEKOFYEAR(h.history_in)) weekPatientCount "
				+ " FROM history h "
				+ " WHERE YEAR(h.history_in) = ? AND MONTH(h.history_in) = ? "
				+ " GROUP BY WEEKOFYEAR(h.history_in) ORDER BY WEEKOFYEAR(h.history_in) DESC";
		logger.info("\n"+sql+" "+year+" "+monthNr);
		List<Map<String, Object>> countPatientsProWeek 
		= jdbcTemplate.queryForList(sql, new Object[] { year, monthNr });
		return countPatientsProWeek;
	}

	public List<Map<String, Object>> countPatientsProMonth(Integer year) {
		String sql = "SELECT MONTH(h.history_in) monthNr, COUNT(MONTH(h.history_in)) monthPatientCount "
				+ " FROM history h "
				+ " WHERE YEAR(h.history_in)= ? GROUP BY MONTH(h.history_in) ORDER BY MONTH(h.history_in) DESC";
		logger.info("\n"+sql);
		List<Map<String, Object>> countPatientsProMonth 
		= jdbcTemplate.queryForList(sql, new Object[] { year });
		return countPatientsProMonth;
	}

	public List<Map<String, Object>> patientsYearWeekRsList(Integer year, Integer week) {
		logger.info("\n"+sqlPatientsYearWeek+" "+year+" "+week);
		List<Map<String, Object>> countPatientsProWeek 
		= jdbcTemplate.queryForList(sqlPatientsYearWeek, new Object[] { year, week });
		return countPatientsProWeek;
	}

	public List<Map<String, Object>> getArchiveOperationOrder() {
		String sql = "SELECT o_saved.operation_name op_name_saved, oo.*, o.*, p.* "
				+ " FROM order_operation oo, operation o_saved,"
				+ " (SELECT operation_id, operation_code, operation_name, operation_subgroup_name, operation_group_name "
				+ " FROM operation o, operation_subgroup osg, operation_group og "
				+ "WHERE o.operation_subgroup_id=osg.operation_subgroup_id "
				+ " AND osg.operation_group_id=og.operation_group_id) o,"
				+ " (SELECT d.department_name, p.personal_name, p.personal_surname, p.personal_patronymic , pd.personal_department_id, po.position_name"
				+ " FROM personal_department pd, personal p, department d, position po"
				+ " WHERE p.personal_id=pd.personal_id AND d.department_id=pd.department_id AND po.position_id=pd.position_id) p"
				+ " WHERE p.personal_department_id=oo.personal_department_id AND o.operation_id=oo.operation_id "
				+ " AND o_saved.operation_id=oo.new_operation_id";
		logger.info("\n"+sql);
		List<Map<String, Object>> countPatientsProMonth 
		= jdbcTemplate.queryForList(sql);
		return countPatientsProMonth;
	}

	public List<Map<String,Object>> getActiveOperationOrder() {
		String sql = "SELECT * FROM order_operation oo, "
				+ " (SELECT operation_id, operation_code, operation_name, operation_subgroup_name "
				+ ", operation_group_name "
				+ " FROM operation o, operation_subgroup osg, operation_group og "
				+ " WHERE o.operation_subgroup_id=osg.operation_subgroup_id "
				+ " AND osg.operation_group_id=og.operation_group_id) o "
				+ ",(SELECT d.department_name, p.personal_name, p.personal_surname, p.personal_patronymic "
				+ ", pd.personal_department_id, po.position_name "
				+ " FROM "
				+ " personal_department pd, personal p, department d, position po "
				+ " WHERE p.personal_id=pd.personal_id AND d.department_id=pd.department_id "
				+ " AND po.position_id=pd.position_id) p "
				+ " WHERE oo.new_operation_id IS NULL and p.personal_department_id=oo.personal_department_id "
				+ " AND o.operation_id=oo.operation_id ";
		logger.info("\n"+sql);
		List<Map<String, Object>> countPatientsProMonth 
		= jdbcTemplate.queryForList(sql);
		return countPatientsProMonth;
	}
	public List<Map<String,Object>> getPersonalListe() {
		String sql = "SELECT p.personal_id, p.personal_surname, p.personal_name, p.personal_patronymic,"
				+ " pdd.department_name FROM personal p LEFT JOIN ("
				+ "SELECT pd.personal_id, d.* FROM personal_department pd, department d"
				+ " WHERE d.department_id = pd.department_id"
				+ ") pdd ON p.personal_id = pdd.personal_id";
		logger.info("\n"+sql);
		List<Map<String, Object>> countPatientsProMonth 
		= jdbcTemplate.queryForList(sql);
		return countPatientsProMonth;
	}

	public List<Map<String, Object>> getOperationListe() {
		String sql = "SELECT o.*,osg.operation_subgroup_name,og.* "
				+ " FROM operation o, operation_subgroup osg, operation_group og "
				+ " WHERE o.operation_subgroup_id = osg.operation_subgroup_id AND og.operation_group_id = osg.operation_group_id "
				+ " ORDER BY operation_group_sort ";
//		+ " ORDER BY operation_code ";
		logger.info("\n"+sql);
		List<Map<String, Object>> countPatientsProMonth 
		= jdbcTemplate.queryForList(sql);
		return countPatientsProMonth;
	}

	public List<Map<String, Object>> getComplicationListe() {
		String sql = "SELECT * FROM operation_complication";
		logger.info("\n"+sql);
		List<Map<String, Object>> countPatientsProMonth 
		= jdbcTemplate.queryForList(sql);
		return countPatientsProMonth;
	}

	

}
