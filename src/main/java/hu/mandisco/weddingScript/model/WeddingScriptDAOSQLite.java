package hu.mandisco.weddingScript.model;

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

import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WeddingScriptDAOSQLite implements WeddingScriptDAO {

	private static final String JDBC_CONNECTION_PREFIX = "jdbc:sqlite:";
	private static final String DATABASE_FILE = "src\\main\\resources\\database.db";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String DATEFORMAT_DATETIME_FOR_INSERT = "yyyy-MM-dd HH:mm:ss";
	// private static final String DATEFORMAT_DATE_FOR_INSERT = "yyyy-MM-dd";

	Path currentWorkingFolder = Paths.get("").toAbsolutePath();
	Path pathToTheDatabaseFile = currentWorkingFolder.resolve(DATABASE_FILE);
	String databaseConnectionURL = JDBC_CONNECTION_PREFIX
			+ pathToTheDatabaseFile.toUri().toString();

	private static ObservableList<AttributeType> attributeTypeList = FXCollections
			.observableArrayList();

	public WeddingScriptDAOSQLite() {
		super();
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to load SQLite JDBC driver.");
			e.printStackTrace();
		}

		attributeTypeList = getAttributeTypes();
	}

	public LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

	}

	public ObservableList<Program> getPrograms() {
		Connection conn = null;
		Statement st = null;

		ObservableList<Program> programs = FXCollections.observableArrayList();
		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM programs");

			while (rs.next()) {
				int progId = rs.getInt("progId");
				String name = rs.getString("name");
				int defaultTime = rs.getInt("defaultTime");
				Boolean defaultProgram = rs.getInt("defaultProgram") != 0;

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
			System.out.println("Failed to execute listing programs.");
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when listing programs.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when listing programs.");
				e.printStackTrace();
			}
		}

		return programs;

	}

	public List<Attribute> getAttributes() {
		Connection conn = null;
		Statement st = null;

		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM attributes");

			while (rs.next()) {
				int attrId = rs.getInt("attributeId");
				String name = rs.getString("name");
				String defValue = rs.getString("defaultValue");
				Boolean mandatory = rs.getInt("mandatory") != 0;
				int attrTypeId = rs.getInt("attrTypeId");
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
			System.out.println("Failed to execute listing attributes.");
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when listing attributes.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when listing attributes.");
				e.printStackTrace();
			}
		}

		return attributes;

	}

	public ObservableList<AttributeType> getAttributeTypes() {
		Connection conn = null;
		Statement st = null;

		if (attributeTypeList != null) {
			attributeTypeList.clear();
		}

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM attributeTypes");

			while (rs.next()) {
				int attrTypeId = rs.getInt("attrTypeId");
				String name = rs.getString("name");

				AttributeType attrType = new AttributeType();
				attrType.setName(name);
				attrType.setAttrTypeId(attrTypeId);
				attributeTypeList.add(attrType);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute listing programs.");
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when listing programs.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when listing programs.");
				e.printStackTrace();
			}
		}

		return attributeTypeList;

	}

	public ObservableList<Script> getScripts() {
		Connection conn = null;
		Statement st = null;

		ObservableList<Script> scripts = FXCollections.observableArrayList();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM scripts");

			while (rs.next()) {
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
			System.out.println("Failed to execute listing programs.");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Failed to parse a date.");
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when listing programs.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when listing programs.");
				e.printStackTrace();
			}
		}

		return scripts;

	}

	public ObservableList<Service> getServices() {
		Connection conn = null;
		Statement st = null;

		ObservableList<Service> services = FXCollections.observableArrayList();
		services.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM services");

			while (rs.next()) {
				int serviceId = rs.getInt("serviceId");
				String name = rs.getString("name");

				Service service = new Service();
				service.setName(name);
				service.setServiceId(serviceId);

				services.add(service);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute listing services.");
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when listing services.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when listing services.");
				e.printStackTrace();
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

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn
					.prepareStatement("INSERT INTO scripts(name, date, comment) VALUES (?, ?, ?)");

			int index = 1;
			pst.setString(index++, script.getName());
			pst.setString(index++,
					script.getDate() == null ? ""
							: script.getDate().format(
									DateTimeFormatter.ofPattern(DATEFORMAT_DATETIME_FOR_INSERT)));
			pst.setString(index++, script.getComment());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}

			Statement st = null;
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT last_insert_rowid() AS lastId");

			int rowId = -1;
			while (rs.next()) {
				rowId = rs.getInt("lastId");
			}
			script.setScriptId(rowId);

		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeScript(Script script) {
		boolean rvSucceeded = false;
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
			System.out.println("Failed to execute removing script.");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when removing script.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when removing script.");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public ObservableList<Program> getProgramsOfScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int progId = rs.getInt("progId");
				String name = rs.getString("name");
				int defaultTime = rs.getInt("defaultTime");
				int time = rs.getInt("time");
				Boolean defaultProgram = rs.getInt("defaultProgram") != 0;

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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return programs;
	}

	@Override
	public ObservableList<Program> getProgramsNotInScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int progId = rs.getInt("progId");
				String name = rs.getString("name");
				int defaultTime = rs.getInt("defaultTime");
				Boolean defaultProgram = rs.getInt("defaultProgram") != 0;

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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return programs;
	}

	@Override
	public boolean addProgramToScript(Script script, Program program) {
		String errorName = "adding program to a script";
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
			Long seconds = 1000 * new Long(day + hour + min);
			pst.setLong(index++, program.getTime() == null ? 0 : seconds);

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorName + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorName + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorName + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public List<Attribute> getAttributesOfProgram(Program program) {
		String errorDesc = "getting program attributes";
		Connection conn = null;
		PreparedStatement pst = null;

		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(
					"SELECT * FROM attributes WHERE attributeId IN (SELECT attrId FROM progAttr WHERE progId = ?)");

			int index = 1;
			pst.setInt(index++, program.getProgId());
			conn.setAutoCommit(false);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt("attributeId");
				String name = rs.getString("name");

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);

				attributes.add(attribute);
			}

			conn.commit();

		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}
		return attributes;
	}

	@Override
	public boolean removeProgram(Program program) {
		boolean rvSucceeded = false;
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
			System.out.println("Failed to execute removing program.");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when removing program.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when removing program.");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean addProgram(Program program) {
		String errorName = "program";
		String errorType = "adding";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("INSERT INTO programs(name, defaultTime, defaultProgram) VALUES (?, ?, ?)");

			int index = 1;
			pst.setString(index++, program.getName());
			int defDay = (program.getDefaultTime().getDayOfMonth() - 1) * 24 * 60 * 60;
			int defHour = program.getDefaultTime().getHour() * 60 * 60;
			int defMin = program.getDefaultTime().getMinute() * 60;
			Long defaultSeconds = 1000 * new Long(defDay + defHour + defMin);
			pst.setLong(index++, program.getDefaultTime() == null ? 0 : defaultSeconds);
			pst.setInt(index++, program.isDefaultProgram() ? 1 : 0);

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorType + " " + errorName + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println(
						"Failed to close statement when " + errorType + " " + errorName + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println(
						"Failed to close connection when " + errorType + " " + errorName + ".");
				e.printStackTrace();
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
			Long defaultSeconds = 1000 * new Long(defDay + defHour + defMin);
			pst.setLong(index++, program.getDefaultTime() == null ? 0 : defaultSeconds);
			pst.setInt(index++, program.isDefaultProgram() ? 1 : 0);
			pst.setInt(index++, program.getProgId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeAttribute(Attribute attribute) {
		String errorName = "attribute";
		String errorType = "removing";
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
			System.out.println("Failed to execute " + errorType + " " + errorName + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println(
						"Failed to close statement when " + errorType + " " + errorName + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println(
						"Failed to close connection when " + errorType + " " + errorName + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean addAttribute(Attribute attribute) {

		String errorName = "attribute";
		String errorType = "adding";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(
					"INSERT INTO attributes(name, defaultValue, attrTypeId, serviceId, mandatory) VALUES (?, ?, ?, ?, ?)");

			int index = 1;

			pst.setString(index++, attribute.getName());
			pst.setString(index++, attribute.getDefaultValue());
			pst.setInt(index++, attribute.getAttrType().getAttrTypeId());
			pst.setInt(index++, attribute.getServiceId());
			pst.setInt(index++, attribute.isMandatory() ? 1 : 0);

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorType + " " + errorName + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println(
						"Failed to close statement when " + errorType + " " + errorName + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println(
						"Failed to close connection when " + errorType + " " + errorName + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;

	}

	@Override
	public ObservableList<Attribute> getAttributesOfScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt("attributeId");
				String name = rs.getString("name");
				String defValue = rs.getString("defaultValue");
				String value = rs.getString("value");
				Boolean mandatory = rs.getInt("mandatory") != 0;

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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return scriptAttrList;

	}

	@Override
	public ObservableList<Attribute> getAttributesNotInScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt("attributeId");
				String name = rs.getString("name");
				String defValue = rs.getString("defaultValue");
				// String value = rs.getString("value");
				Boolean mandatory = rs.getInt("mandatory") != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				// attribute.setValue(value);
				attribute.setMandatory(mandatory);

				scriptAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public List<Service> getServicesOfScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int serviceId = rs.getInt("serviceId");
				String name = rs.getString("name");

				Service service = new Service();
				service.setName(name);
				service.setServiceId(serviceId);

				services.add(service);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return services;
	}

	@Override
	public boolean addServiceToScript(Script script, Service service) {
		String errorName = "adding service to a script";
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
			System.out.println("Failed to execute " + errorName + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorName + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorName + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public List<Service> getServicesNotInScript(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int serviceId = rs.getInt("serviceId");
				String name = rs.getString("name");

				Service service = new Service();
				service.setName(name);
				service.setServiceId(serviceId);

				services.add(service);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return services;
	}

	@Override
	public List<Attribute> getAttributesNotInProgram(Program program) {
		Connection conn = null;
		PreparedStatement pst = null;
		String errorDesc = "reverse listing program's attributes";
		List<Attribute> programAttrList = new ArrayList<Attribute>();

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			String sql = "SELECT * FROM attributes WHERE "
					+ "attrTypeId = (SELECT attrTypeId FROM attributeTypes WHERE name = \"Program\") "
					+ "AND attributeId NOT IN (SELECT attrId FROM progAttr WHERE progId = ?) ";
			pst = conn.prepareStatement(sql);

			int index = 1;
			pst.setInt(index++, program.getProgId());

			conn.setAutoCommit(false);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt("attributeId");
				String name = rs.getString("name");
				String defValue = rs.getString("defaultValue");
				// String value = rs.getString("value");
				Boolean mandatory = rs.getInt("mandatory") != 0;

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);
				attribute.setDefaultValue(defValue);
				// attribute.setValue(value);
				attribute.setMandatory(mandatory);

				programAttrList.add(attribute);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			Long newTimeSeconds = 1000 * new Long(day + hour + min);

			pst.setLong(index++, newTimeSeconds);
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
					"UPDATE attributes SET name = ?, defaultValue = ?, attrTypeId = ?, serviceId = ?, mandatory = ?  "
							+ "WHERE attributeId = ?;");

			int index = 1;

			pst.setString(index++, attribute.getName());
			pst.setString(index++, attribute.getDefaultValue());
			pst.setInt(index++, attribute.getAttrType().getAttrTypeId());
			pst.setInt(index++, attribute.getServiceId());
			pst.setInt(index++, attribute.isMandatory() ? 1 : 0);
			pst.setInt(index++, attribute.getAttrId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			pst.setString(index++,
					script.getDate() == null ? ""
							: script.getDate().format(
									DateTimeFormatter.ofPattern(DATEFORMAT_DATETIME_FOR_INSERT)));
			pst.setString(index++, script.getComment());
			pst.setInt(index++, script.getScriptId());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public ObservableList<Attribute> getAttributesOfScriptProgram(Script script, Program program) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt("attributeId");
				String name = rs.getString("name");
				String defValue = rs.getString("defaultValue");
				String value = rs.getString("value");
				Boolean mandatory = rs.getInt("mandatory") != 0;

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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return scriptAttrList;

	}

	@Override
	public ObservableList<Attribute> getAttributesNotInScriptProgram(Script script,
			Program program) {
		Connection conn = null;
		PreparedStatement pst = null;
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
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int attrId = rs.getInt("attributeId");
				String name = rs.getString("name");
				String defValue = rs.getString("defaultValue");
				String value = rs.getString("value");
				Boolean mandatory = rs.getInt("mandatory") != 0;

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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
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
			System.out.println("Failed to execute " + errorDesc + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when " + errorDesc + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when " + errorDesc + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public boolean removeService(Service service) {
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
			System.out.println("Failed to execute removing script.");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when removing script.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when removing script.");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

	@Override
	public List<Program> getDefaultPrograms() {
		Connection conn = null;
		Statement st = null;

		ObservableList<Program> programs = FXCollections.observableArrayList();
		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM programs WHERE defaultProgram = 1");

			while (rs.next()) {
				int progId = rs.getInt("progId");
				String name = rs.getString("name");
				int defaultTime = rs.getInt("defaultTime");
				Boolean defaultProgram = rs.getInt("defaultProgram") != 0;

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
			System.out.println("Failed to execute listing programs.");
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when listing programs.");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when listing programs.");
				e.printStackTrace();
			}
		}

		return programs;
	}

}
