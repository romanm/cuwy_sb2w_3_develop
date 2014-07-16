package hello;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.cuwy1.hol.model.DepartmentHol;
import org.cuwy1.hol.model.PatientDiagnosisHol;
import org.cuwy1.icd10.Icd10UaClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

@Component("cuwyDbService1")
public class CuwyDbService1 {
	private JdbcTemplate jdbcTemplate;

	public CuwyDbService1(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/hol");
		dataSource.setUsername("hol");
		dataSource.setPassword("hol");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public List<Icd10UaClass> getIcd10UaChilds() {
		System.out.println("Querying for customer records where first_name = 'Josh':");
		List<Icd10UaClass> icd10Classes = jdbcTemplate.query(
				"SELECT * from icd WHERE icd_left_key = ?", new Object[] { 1 },
//				"select * from customers where first_name = ?", new Object[] { "Josh" },
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
		return jdbcTemplate.query(
				"SELECT concat(patient_surname,' ',patient_name,' ',patient_patronnymic) name"
				+ ",history_in,icd_code,icd_name,patient.patient_id,diagnos_id"
				+ " FROM ("
				+ " SELECT history_id,diagnos_id,max(diagnos_id),icd_id FROM history_diagnos group by history_id"
				+ ") lastds,history,icd,patient"
				+ " WHERE lastds.history_id=history.history_id"
				+ " AND history_out is null"
				+ " AND patient.patient_id=history.patient_id"
				+ " AND icd.icd_id=lastds.icd_id"
				+ " AND history_department_in=?", new Object[] { departmentId }, 
				new RowMapper<PatientDiagnosisHol>(){
					@Override
					public PatientDiagnosisHol mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return new PatientDiagnosisHol( 
								rs.getString("name"),
								rs.getDate("history_in"),
								rs.getString("icd_code"),
								rs.getString("icd_name"),
								rs.getInt("patient_id"),
								rs.getShort("diagnos_id")
								);
					}
				});
	}
}
