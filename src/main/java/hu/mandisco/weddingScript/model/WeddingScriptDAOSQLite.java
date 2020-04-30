package hu.mandisco.weddingscript.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.AttributeType;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.model.bean.Script;
import hu.mandisco.weddingscript.model.bean.Service;
import hu.mandisco.weddingscript.view.Labels;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WeddingScriptDAOSQLite implements WeddingScriptDAO {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String JDBC_CONNECTION_PREFIX = "jdbc:sqlite:";
	private static final String DATABASE_FILE = "src\\main\\resources\\database.db";

	Path currentWorkingFolder = Paths.get("").toAbsolutePath();
	Path pathToTheDatabaseFile = currentWorkingFolder.resolve(DATABASE_FILE);
	String databaseConnectionURL = JDBC_CONNECTION_PREFIX
			+ pathToTheDatabaseFile.toUri().toString();

	public WeddingScriptDAOSQLite() {
		super();
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			LOGGER.error("Failed to load SQLite JDBC driver.");
			LOGGER.error(e);
		}
	}

	public LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

	}

	public ObservableList<Program> getPrograms() {
		String errorDesc = "listing programs";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		ObservableList<Program> programs = FXCollections.observableArrayList();
		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM programs");

			while (rs.next()) {
				int progId = rs.getInt(Labels.DB_PROGRAM_PROGID);
				String name = rs.getString(Labels.DB_PROGRAM_NAME);
				int defaultTime = rs.getInt(Labels.DB_PROGRAM_DEFAULT_TIME);
				Boolean defaultProgram = rs.getInt(Labels.DB_PROGRAM_DEFAULT_PROGRAM) != 0;

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);
				program.setDefaultTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(defaultTime),
						ZoneId.of("+0")));
				program.setDefaultProgram(defaultProgram);

				programs.add(program);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return programs;

	}

	public ObservableList<Attribute> getAttributes() {
		String errorDesc = "listing attributes";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		ObservableList<AttributeType> attributeTypeList = getAttributeTypes();
		ObservableList<Attribute> attributes = FXCollections.observableArrayList();
		attributes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM attributes");

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;
				int attrTypeId = rs.getInt(Labels.DB_ATTRIBUTE_TYPE_ID);
				AttributeType attrType = attributeTypeList.get(attrTypeId);

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setValue(defValue);
				attribute.setMandatory(mandatory);
				attribute.setAttrType(attrType);

				attributes.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return attributes;

	}

	public ObservableList<AttributeType> getAttributeTypes() {
		String errorDesc = "listing attribute types";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ObservableList<AttributeType> attributeTypeList = FXCollections.observableArrayList();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM attributeTypes");

			while (rs.next()) {
				int attrTypeId = rs.getInt(Labels.DB_ATTRIBUTE_TYPE_ID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_TYPE_NAME);

				AttributeType attrType = new AttributeType();
				attrType.setName(name);
				attrType.setAttrTypeId(attrTypeId);
				attributeTypeList.add(attrType);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return attributeTypeList;

	}

	public ObservableList<Script> getScripts() {
		String errorDesc = "listing scripts";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		ObservableList<Script> scripts = FXCollections.observableArrayList();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM scripts");

			while (rs.next()) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						Labels.DATEFORMAT_DATETIME_FOR_INSERT);
				int scriptId = rs.getInt("scriptId");
				String name = rs.getString("name");
				LocalDateTime date = rs.getString("date").isEmpty() ? null
						: dateToLocalDateTime(dateFormat.parse(rs.getString("date")));
				String comment = rs.getString("comment");

				LocalDateTime lastEdited = dateToLocalDateTime(
						dateFormat.parse(rs.getString("lastEdited")));
				LocalDateTime created = dateToLocalDateTime(
						dateFormat.parse(rs.getString("created")));

				Script script = new Script();
				script.setName(name);
				script.setScriptId(scriptId);
				script.setDate(date);
				script.setComment(comment);
				script.setLastEdited(lastEdited);
				script.setCreated(created);

				scripts.add(script);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} catch (ParseException e) {
			LOGGER.error("Failed to parse a date.");
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return scripts;

	}

	public ObservableList<Service> getServices() {
		String errorDesc = "listing services";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		ObservableList<Service> services = FXCollections.observableArrayList();
		services.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM services");

			while (rs.next()) {
				int serviceId = rs.getInt(Labels.DB_SERVICE_SERVICEID);
				String name = rs.getString("name");

				Service service = new Service();
				service.setName(name);
				service.setServiceId(serviceId);

				services.add(service);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return services;

	}

	@Override
	public boolean addScript(Script script) {
		String errorDesc = "adding script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Statement st = null;
		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn
					.prepareStatement("INSERT INTO scripts(name, date, comment) VALUES (?, ?, ?)");

			int index = 1;
			pst.setString(index++, script.getName());
			pst.setString(index++, script.getDate() == null ? ""
					: script.getDate().format(
							DateTimeFormatter.ofPattern(Labels.DATEFORMAT_DATETIME_FOR_INSERT)));
			pst.setString(index++, script.getComment());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}

			st = conn.createStatement();
			rs = st.executeQuery("SELECT last_insert_rowid() AS lastId");

			int rowId = -1;
			while (rs.next()) {
				rowId = rs.getInt("lastId");
			}
			script.setScriptId(rowId);

		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeScript(Script script) {
		boolean rvSucceeded = false;
		String errorDesc = "removing script";
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("DELETE FROM scripts where scriptId = ?");

			pst.setInt(1, script.getScriptId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public ObservableList<Program> getProgramsOfScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing programs of script";

		ObservableList<Program> programs = FXCollections.observableArrayList();
		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);

			String sqlScript = "SELECT * FROM programs, scriptProg WHERE "
					+ " programs.progId = scriptProg.progId AND scriptProg.scriptId = ? ORDER BY time";
			pst = conn.prepareStatement(sqlScript);
			pst.setInt(1, script.getScriptId());

			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int progId = rs.getInt(Labels.DB_PROGRAM_PROGID);
				String name = rs.getString(Labels.DB_PROGRAM_NAME);
				int defaultTime = rs.getInt(Labels.DB_PROGRAM_DEFAULT_TIME);
				int time = rs.getInt(Labels.DB_PROGRAM_TIME);
				Boolean defaultProgram = rs.getInt(Labels.DB_PROGRAM_DEFAULT_PROGRAM) != 0;

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);
				program.setDefaultTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(defaultTime),
						ZoneId.of("+0")));
				program.setTime(
						LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("+0")));
				program.setDefaultProgram(defaultProgram);
				programs.add(program);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return programs;
	}

	@Override
	public ObservableList<Program> getProgramsNotInScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing reverse programs of script";

		ObservableList<Program> programs = FXCollections.observableArrayList();
		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);

			String sqlScript = "SELECT * FROM programs "
					+ "WHERE progId NOT IN (SELECT progId FROM scriptProg WHERE scriptId = ?)";

			pst = conn.prepareStatement(sqlScript);
			pst.setInt(1, script.getScriptId());

			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int progId = rs.getInt(Labels.DB_PROGRAM_PROGID);
				String name = rs.getString(Labels.DB_PROGRAM_NAME);
				int defaultTime = rs.getInt(Labels.DB_PROGRAM_DEFAULT_TIME);
				Boolean defaultProgram = rs.getInt(Labels.DB_PROGRAM_DEFAULT_PROGRAM) != 0;

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);
				program.setDefaultTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(defaultTime),
						ZoneId.of("+0")));
				program.setDefaultProgram(defaultProgram);

				programs.add(program);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return programs;
	}

	@Override
	public boolean addProgramToScript(Script script, Program program) {
		String errorDesc = "adding program to a script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO scriptProg(scriptId, progId, time) VALUES (?, ?, ?)");

			int index = 1;
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());

			int day = (program.getDefaultTime().getDayOfMonth() - 1) * 24 * 60 * 60;
			int hour = program.getDefaultTime().getHour() * 60 * 60;
			int min = program.getDefaultTime().getMinute() * 60;
			Long seconds = 1000 * ((long) day + hour + min);
			pst.setLong(index++, program.getTime() == null ? 0 : seconds);

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public List<Attribute> getAttributesOfProgram(Program program) {
		String errorDesc = "getting program attributes";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		List<Attribute> attributes = new ArrayList<>();
		attributes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(
					"SELECT * FROM attributes WHERE attributeId IN (SELECT attrId FROM progAttr WHERE progId = ?)");

			int index = 1;
			pst.setInt(index++, program.getProgId());
			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);

				attributes.add(attribute);
			}

			conn.commit();

		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}
		return attributes;
	}

	@Override
	public boolean removeProgram(Program program) {
		boolean rvSucceeded = false;
		String errorDesc = "removing program";
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("DELETE FROM programs where progId = ?");

			pst.setInt(1, program.getProgId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean addProgram(Program program) {
		String errorDesc = "adding program";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO programs(name, defaultTime, defaultProgram) VALUES (?, ?, ?)");

			int index = 1;
			pst.setString(index++, program.getName());
			int defDay = (program.getDefaultTime().getDayOfMonth() - 1) * 24 * 60 * 60;
			int defHour = program.getDefaultTime().getHour() * 60 * 60;
			int defMin = program.getDefaultTime().getMinute() * 60;
			Long defaultSeconds = 1000 * ((long) defDay + defHour + defMin);
			pst.setLong(index++, program.getDefaultTime() == null ? 0 : defaultSeconds);
			if (Boolean.TRUE.equals(program.isDefaultProgram())) {
				pst.setInt(index++, 1);
			} else {
				pst.setInt(index++, 0);
			}

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setProgram(Program program) {
		String errorDesc = "editing program";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE programs SET name = ?, defaultTime = ?, defaultProgram = ? WHERE progId = ?;");

			int index = 1;
			pst.setString(index++, program.getName());
			int defDay = (program.getDefaultTime().getDayOfMonth() - 1) * 24 * 60 * 60;
			int defHour = program.getDefaultTime().getHour() * 60 * 60;
			int defMin = program.getDefaultTime().getMinute() * 60;
			Long defaultSeconds = 1000 * ((long) defDay + defHour + defMin);
			pst.setLong(index++, program.getDefaultTime() == null ? 0 : defaultSeconds);
			if (Boolean.TRUE.equals(program.isDefaultProgram())) {
				pst.setInt(index++, 1);
			} else {
				pst.setInt(index++, 0);
			}
			pst.setInt(index++, program.getProgId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeAttribute(Attribute attribute) {
		String errorDesc = "removing attribute";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("DELETE FROM attributes where attributeId = ?");

			pst.setInt(1, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean addAttribute(Attribute attribute) {

		String errorDesc = "adding attribute";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO attributes(name, defaultValue, attrTypeId, mandatory) VALUES (?, ?, ?, ?)");

			int index = 1;

			pst.setString(index++, attribute.getName());
			pst.setString(index++, attribute.getDefaultValue());
			pst.setInt(index++, attribute.getAttrType().getAttrTypeId());
			pst.setInt(index++, attribute.isMandatory() ? 1 : 0);

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;

	}

	@Override
	public ObservableList<Attribute> getAttributesOfScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing script's attributes";
		ObservableList<Attribute> scriptAttrList = FXCollections.observableArrayList();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("SELECT * FROM attributes, scriptAttr WHERE scriptId = ? "
					+ "AND attributes.attributeId = scriptAttr.attrId "
					+ "AND attributeId IN (SELECT attrId FROM scriptAttr WHERE scriptId = ?)");

			int index = 1;
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, script.getScriptId());
			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				String value = rs.getString(Labels.DB_ATTRIBUTE_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setValue(value);
				attribute.setMandatory(mandatory);

				scriptAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return scriptAttrList;

	}

	@Override
	public ObservableList<Attribute> getAttributesNotInScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "reverse listing script's attributes";
		ObservableList<Attribute> scriptAttrList = FXCollections.observableArrayList();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			String sql = "SELECT * FROM attributes WHERE "
					+ "attrTypeId = (SELECT attrTypeId FROM attributeTypes WHERE name = \"Script\") "
					+ "AND attributeId NOT IN (SELECT attrId FROM scriptAttr WHERE scriptId = ?) ";
			pst = conn.prepareStatement(sql);

			int index = 1;
			pst.setInt(index++, script.getScriptId());

			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setMandatory(mandatory);

				scriptAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return scriptAttrList;

	}

	@Override
	public boolean addAttributeToScript(Script script, Attribute attribute) {

		String errorDesc = "adding attribute to script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO scriptAttr(scriptId, attrId, value) VALUES (?, ?, ?)");

			int index = 1;

			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, attribute.getAttrId());
			pst.setString(index++, attribute.getDefaultValue());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public ObservableList<Service> getServicesOfScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing services of script";

		ObservableList<Service> services = FXCollections.observableArrayList();
		services.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);

			String sqlScript = "SELECT * FROM services WHERE "
					+ "serviceId IN (SELECT serviceId FROM scriptService WHERE scriptId = ?)";
			pst = conn.prepareStatement(sqlScript);
			pst.setInt(1, script.getScriptId());

			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int serviceId = rs.getInt(Labels.DB_SERVICE_SERVICEID);
				String name = rs.getString("name");

				Service service = new Service();
				service.setName(name);
				service.setServiceId(serviceId);

				services.add(service);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return services;
	}

	@Override
	public boolean addServiceToScript(Script script, Service service) {
		String errorDesc = "adding service to a script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO scriptService(scriptId, serviceId) VALUES (?, ?)");

			int index = 1;
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, service.getServiceId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public ObservableList<Service> getServicesNotInScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing reverse services of script";

		ObservableList<Service> services = FXCollections.observableArrayList();
		services.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);

			String sqlScript = "SELECT * FROM services WHERE "
					+ "serviceId NOT IN (SELECT serviceId FROM scriptService WHERE scriptId = ?)";

			pst = conn.prepareStatement(sqlScript);
			pst.setInt(1, script.getScriptId());

			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int serviceId = rs.getInt(Labels.DB_SERVICE_SERVICEID);
				String name = rs.getString("name");

				Service service = new Service();
				service.setName(name);
				service.setServiceId(serviceId);

				services.add(service);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return services;
	}

	@Override
	public List<Attribute> getAttributesNotInProgram(Program program) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "reverse listing program's attributes";
		List<Attribute> programAttrList = new ArrayList<>();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			String sql = "SELECT * FROM attributes WHERE "
					+ "attrTypeId = (SELECT attrTypeId FROM attributeTypes WHERE name = \"Program\") "
					+ "AND attributeId NOT IN (SELECT attrId FROM progAttr WHERE progId = ?) ";
			pst = conn.prepareStatement(sql);

			int index = 1;
			pst.setInt(index++, program.getProgId());

			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setMandatory(mandatory);

				programAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return programAttrList;
	}

	@Override
	public boolean addAttributeToProgram(Program program, Attribute attribute) {

		String errorDesc = "adding attribute to program";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO progAttr(progId, attrId, value) VALUES (?, ?, ?)");

			int index = 1;

			pst.setInt(index++, program.getProgId());
			pst.setInt(index++, attribute.getAttrId());
			pst.setString(index++, attribute.getDefaultValue());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setScriptAttributeValue(Script script, Attribute attribute,
			String newAttrValue) {
		String errorDesc = "editing value of script attribute";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE scriptAttr SET value = ? WHERE scriptId = ? AND attrId = ?;");

			int index = 1;
			pst.setString(index++, newAttrValue);
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setScriptProgramTime(Script script, Program program, LocalDateTime newTime) {
		String errorDesc = "editing time of script program";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE scriptProg SET time = ? WHERE scriptId = ? AND progId = ?;");

			int index = 1;

			int day = (newTime.getDayOfMonth() - 1) * 24 * 60 * 60;
			int hour = newTime.getHour() * 60 * 60;
			int min = newTime.getMinute() * 60;
			Long newTimeSeconds = 1000 * ((long) day + hour + min);

			pst.setLong(index++, newTimeSeconds);
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeAttributeFromProgram(Program program, Attribute attribute) {

		String errorDesc = "removing attribute from program";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("DELETE FROM progAttr WHERE progId = ? AND attrId = ?");

			int index = 1;

			pst.setInt(index++, program.getProgId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setAttribute(Attribute attribute) {
		String errorDesc = "editing attribute";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE attributes SET name = ?, defaultValue = ?, attrTypeId = ?, mandatory = ?  "
							+ "WHERE attributeId = ?;");

			int index = 1;

			pst.setString(index++, attribute.getName());
			pst.setString(index++, attribute.getDefaultValue());
			pst.setInt(index++, attribute.getAttrType().getAttrTypeId());
			pst.setInt(index++, attribute.isMandatory() ? 1 : 0);
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setScript(Script script) {
		String errorDesc = "editing script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE scripts SET name = ?, date = ?, comment = ?  " + "WHERE scriptId = ?;");

			int index = 1;
			pst.setString(index++, script.getName());
			pst.setString(index++, script.getDate() == null ? ""
					: script.getDate().format(
							DateTimeFormatter.ofPattern(Labels.DATEFORMAT_DATETIME_FOR_INSERT)));
			pst.setString(index++, script.getComment());
			pst.setInt(index++, script.getScriptId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeAttributeFromScript(Script script, Attribute attribute) {

		String errorDesc = "removing attribute from script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("DELETE FROM scriptAttr WHERE scriptId = ? AND attrId = ?");

			int index = 1;

			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public ObservableList<Attribute> getAttributesOfScriptProgram(Script script, Program program) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing script's program's attributes";
		ObservableList<Attribute> scriptAttrList = FXCollections.observableArrayList();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"SELECT * FROM attributes, scriptProgAttr WHERE scriptId = ? AND progId = ? "
							+ "AND attributes.attributeId = scriptProgAttr.attrId");

			int index = 1;
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());
			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				String value = rs.getString(Labels.DB_ATTRIBUTE_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setValue(value);
				attribute.setMandatory(mandatory);

				scriptAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return scriptAttrList;

	}

	@Override
	public ObservableList<Attribute> getAttributesNotInScriptProgram(Script script,
			Program program) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing script's program's attributes";
		ObservableList<Attribute> scriptAttrList = FXCollections.observableArrayList();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);

			pst = conn.prepareStatement("SELECT * FROM attributes, progAttr WHERE progId = ? "
					+ "AND attributes.attributeId = progAttr.attrId "
					+ "AND attributes.attributeId NOT IN (SELECT attrId FROM scriptProgAttr WHERE scriptId = ? AND progId = ?)");

			int index = 1;

			pst.setInt(index++, program.getProgId());
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());
			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				String value = rs.getString(Labels.DB_ATTRIBUTE_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setValue(value);
				attribute.setMandatory(mandatory);

				scriptAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return scriptAttrList;
	}

	@Override
	public boolean removeAttributeFromScriptProgram(Script script, Program program,
			Attribute attribute) {

		String errorDesc = "removing attribute from script's program";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"DELETE FROM scriptProgAttr WHERE scriptId = ? AND progId = ? AND attrId = ?");

			int index = 1;

			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean addAttributeToScriptProgram(Script script, Program program,
			Attribute attribute) {
		String errorDesc = "adding attribute to script's program";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO scriptProgAttr(scriptId, progId, attrId, value) VALUES (?, ?, ?, ?)");

			int index = 1;

			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());
			pst.setInt(index++, attribute.getAttrId());
			pst.setString(index++, attribute.getDefaultValue());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setScriptProgramAttributeValue(Script script, Program program,
			Attribute attribute, String newAttributeValue) {
		String errorDesc = "editing value of script's program attribute";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE scriptProgAttr SET value = ? WHERE scriptId = ? AND progId = ? AND attrId = ?;");

			int index = 1;
			pst.setString(index++, newAttributeValue);
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	public boolean addService(Service service) {
		String errorDesc = "adding service";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("INSERT INTO services(name) VALUES (?)");

			int index = 1;
			pst.setString(index++, service.getName());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setService(Service service) {
		String errorDesc = "editing service";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("UPDATE services SET name = ? WHERE serviceId = ?;");

			int index = 1;
			pst.setString(index++, service.getName());
			pst.setInt(index++, service.getServiceId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeService(Service service) {
		String errorDesc = "removing service";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("DELETE FROM services where serviceId = ?");

			pst.setInt(1, service.getServiceId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public List<Program> getDefaultPrograms() {
		String errorDesc = "listing default programs";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		ObservableList<Program> programs = FXCollections.observableArrayList();
		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM programs WHERE defaultProgram = 1");

			while (rs.next()) {
				int progId = rs.getInt(Labels.DB_PROGRAM_PROGID);
				String name = rs.getString(Labels.DB_PROGRAM_NAME);
				int defaultTime = rs.getInt(Labels.DB_PROGRAM_DEFAULT_TIME);
				Boolean defaultProgram = rs.getInt(Labels.DB_PROGRAM_DEFAULT_PROGRAM) != 0;

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);
				program.setDefaultTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(defaultTime),
						ZoneId.of("+0")));
				program.setDefaultProgram(defaultProgram);

				programs.add(program);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return programs;
	}

	@Override
	public boolean addAttributeToService(Service service, Attribute attribute) {

		String errorDesc = "adding attribute to service";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO serviceAttr(serviceId, attrId, value) VALUES (?, ?, ?)");

			int index = 1;

			pst.setInt(index++, service.getServiceId());
			pst.setInt(index++, attribute.getAttrId());
			pst.setString(index++, attribute.getDefaultValue());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeAttributeFromService(Service service, Attribute attribute) {

		String errorDesc = "removing attribute from service";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn
					.prepareStatement("DELETE FROM serviceAttr WHERE serviceId = ? AND attrId = ?");

			int index = 1;

			pst.setInt(index++, service.getServiceId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public List<Attribute> getAttributesNotInService(Service service) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "reverse listing service's attributes";
		ObservableList<Attribute> serviceAttrList = FXCollections.observableArrayList();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			String sql = "SELECT * FROM attributes WHERE "
					+ "attrTypeId = (SELECT attrTypeId FROM attributeTypes WHERE name = \"Service\") "
					+ "AND attributeId NOT IN (SELECT attrId FROM serviceAttr WHERE serviceId = ?) ";
			pst = conn.prepareStatement(sql);

			int index = 1;
			pst.setInt(index++, service.getServiceId());

			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setValue(defValue);
				attribute.setMandatory(mandatory);

				serviceAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return serviceAttrList;
	}

	@Override
	public List<Attribute> getAttributesOfService(Service service) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String errorDesc = "listing service's attributes";
		ObservableList<Attribute> serviceAttrList = FXCollections.observableArrayList();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("SELECT * FROM attributes, serviceAttr WHERE serviceId = ? "
					+ "AND attributes.attributeId = serviceAttr.attrId "
					+ "AND attributeId IN (SELECT attrId FROM serviceAttr WHERE serviceId = ?)");

			int index = 1;
			pst.setInt(index++, service.getServiceId());
			pst.setInt(index++, service.getServiceId());
			conn.setAutoCommit(false);
			rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt(Labels.DB_ATTRIBUTE_ATTRIBUTEID);
				String name = rs.getString(Labels.DB_ATTRIBUTE_NAME);
				String defValue = rs.getString(Labels.DB_ATTRIBUTE_DEFAULT_VALUE);
				String value = rs.getString(Labels.DB_ATTRIBUTE_VALUE);
				Boolean mandatory = rs.getInt(Labels.DB_ATTRIBUTE_MANDATORY) != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				attribute.setValue(value);
				attribute.setMandatory(mandatory);

				serviceAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_RESULTSET, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return serviceAttrList;
	}

	@Override
	public boolean setServiceAttributeValue(Service service, Attribute attribute,
			String newAttributeValue) {
		String errorDesc = "editing value of service attribute";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE serviceAttr SET value = ? WHERE serviceId = ? AND attrId = ?;");

			int index = 1;
			pst.setString(index++, newAttributeValue);
			pst.setInt(index++, service.getServiceId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean setProgramAttributeValue(Program program, Attribute attribute,
			String newAttributeValue) {
		String errorDesc = "editing value of program attribute";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"UPDATE progAttr SET value = ? WHERE progId = ? AND attrId = ?;");

			int index = 1;
			pst.setString(index++, newAttributeValue);
			pst.setInt(index++, program.getProgId());
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;

	}

	@Override
	public boolean removeServiceFromScript(Script script, Service service) {
		String errorDesc = "removing service from script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("DELETE FROM scriptService WHERE scriptId = ? AND serviceId = ?");

			int index = 1;

			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, service.getServiceId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING, Labels.FAILED_TO_EXECUTE,
					errorDesc));
			LOGGER.error(e);
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_STATEMENT, errorDesc));
				LOGGER.error(e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error(String.format(Labels.LOGGER_FORMAT_STRING,
						Labels.FAILED_TO_CLOSE_CONNECTION, errorDesc));
				LOGGER.error(e);
			}
		}

		return rvSucceeded;
	}

}
