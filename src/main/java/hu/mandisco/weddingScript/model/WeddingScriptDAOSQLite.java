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

public class WeddingScriptDAOSQLite implements WeddingScriptDAO {

	private static final String JDBC_CONNECTION_PREFIX = "jdbc:sqlite:";
	private static final String DATABASE_FILE = "src\\main\\resources\\database.db";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String DATEFORMAT_DATETIME_FOR_INSERT = "yyyy-MM-dd HH:mm:ss";
	// private static final String DATEFORMAT_DATE_FOR_INSERT = "yyyy-MM-dd";

	private static final String SQL_SELECT_ATTRIBUTES = "SELECT * FROM attributes WHERE 1 = 1 ";
	private static final String SQL_SELECT_SERVICES = "SELECT * FROM services WHERE 1 = 1 ";
	private static final String SQL_SELECT_SCRIPTS = "SELECT * FROM scripts WHERE 1 = 1 ";
	private static final String SQL_SELECT_ATTRIBUTETYPES = "SELECT * FROM attributeTypes WHERE 1 = 1 ";

	// private static final String SQL_SELECT_SCRIPTPROG = "SELECT * FROM
	// scriptProg WHERE 1 = 1 ";
	// private static final String SQL_SELECT_PROGATTR = "SELECT * FROM progAttr
	// WHERE 1 = 1 ";
	// private static final String SQL_SELECT_SCRIPTATTR = "SELECT * FROM
	// scriptAttr WHERE 1 = 1 ";

	private static final String SQL_INSERT_SCRIPT = "INSERT INTO scripts(name, date, comment) VALUES (?, ?, ?)";
	private static final String SQL_INSERT_PROGRAM = "INSERT INTO programs(name, defaultTime) VALUES (?, ?)";
	private static final String SQL_DELETE_SCRIPT = "DELETE FROM scripts where scriptId = ?";
	private static final String SQL_DELETE_PROGRAM = "DELETE FROM programs where progId = ?";

	Path currentWorkingFolder = Paths.get("").toAbsolutePath();
	Path pathToTheDatabaseFile = currentWorkingFolder.resolve(DATABASE_FILE);
	String databaseConnectionURL = JDBC_CONNECTION_PREFIX + pathToTheDatabaseFile.toUri().toString();

	List<Program> programs = new ArrayList<Program>();
	List<Script> scripts = new ArrayList<Script>();
	List<Service> services = new ArrayList<Service>();
	List<AttributeType> attributeTypes = new ArrayList<AttributeType>();
	List<Attribute> attributes = new ArrayList<Attribute>();

	public WeddingScriptDAOSQLite() {
		super();
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println("Failed to load SQLite JDBC driver.");
			e.printStackTrace();
		}
	}

	public LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

	}

	public List<Program> getPrograms() {
		Connection conn = null;
		Statement st = null;

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

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);
				program.setDefaultTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(defaultTime), ZoneId.of("+0")));

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

		attributes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT_ATTRIBUTES);

			while (rs.next()) {
				int attrId = rs.getInt("attrId");
				String name = rs.getString("name");

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);

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

	public List<AttributeType> getAttributeTypes() {
		Connection conn = null;
		Statement st = null;

		attributeTypes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT_ATTRIBUTETYPES);

			while (rs.next()) {
				int attrTypeId = rs.getInt("attrTypeId");
				String name = rs.getString("name");

				AttributeType attrType = new AttributeType();
				attrType.setName(name);
				attrType.setAttrTypeId(attrTypeId);

				attributeTypes.add(attrType);
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

		return attributeTypes;

	}

	public List<Script> getScripts() {
		Connection conn = null;
		Statement st = null;

		scripts.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT_SCRIPTS);

			while (rs.next()) {
				int scriptId = rs.getInt("scriptId");
				String name = rs.getString("name");
				LocalDateTime date = rs.getString("date").isEmpty() ? null
						: dateToLocalDateTime(dateFormat.parse(rs.getString("date")));
				String comment = rs.getString("comment");

				LocalDateTime lastEdited = dateToLocalDateTime(dateFormat.parse(rs.getString("lastEdited")));
				LocalDateTime created = dateToLocalDateTime(dateFormat.parse(rs.getString("created")));

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

	public List<Service> getServices() {
		Connection conn = null;
		Statement st = null;

		services.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT_SERVICES);

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
		String errorName = "script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(SQL_INSERT_SCRIPT);

			int index = 1;
			pst.setString(index++, script.getName());
			pst.setString(index++, script.getDate() == null ? ""
					: script.getDate().format(DateTimeFormatter.ofPattern(DATEFORMAT_DATETIME_FOR_INSERT)));
			pst.setString(index++, script.getComment());

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute adding " + errorName + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when adding " + errorName + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when adding " + errorName + ".");
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
			pst = conn.prepareStatement(SQL_DELETE_SCRIPT);

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
	public List<Attribute> getScriptAttributes(Script script) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Program> getScriptPrograms(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;

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

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);
				program.setDefaultTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(defaultTime), ZoneId.of("+0")));
				program.setTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("+0")));

				programs.add(program);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute listing programs.");
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
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

	@Override
	public List<Program> getScriptProgramsInverse(Script script) {
		Connection conn = null;
		PreparedStatement pst = null;

		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);

			String sqlScript = "SELECT * FROM programs "
					+ "WHERE progId not in (SELECT progId FROM scriptProg WHERE scriptId = ?);";
			pst = conn.prepareStatement(sqlScript);
			pst.setInt(1, script.getScriptId());

			conn.setAutoCommit(false);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int progId = rs.getInt("progId");
				String name = rs.getString("name");
				int defaultTime = rs.getInt("defaultTime");

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);
				program.setDefaultTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(defaultTime), ZoneId.of("+0")));

				programs.add(program);
			}

			conn.commit();
		} catch (SQLException e) {
			System.out.println("Failed to execute listing programs.");
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
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

	@Override
	public boolean addProgramToScript(Script script, Program program) {
		String errorName = "adding program to a script";
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement("INSERT INTO scriptProg(scriptId, progId) VALUES (?, ?)");

			int index = 1;
			pst.setInt(index++, script.getScriptId());
			pst.setInt(index++, program.getProgId());

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
	public List<Attribute> getProgramAttributes(Program program) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeProgram(Program program) {
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(SQL_DELETE_PROGRAM);

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
		boolean rvSucceeded = false;
		Connection conn = null;
		PreparedStatement pst = null;

		try {

			conn = DriverManager.getConnection(databaseConnectionURL);
			pst = conn.prepareStatement(SQL_INSERT_PROGRAM);

			int index = 1;
			pst.setString(index++, program.getName());
			pst.setString(index++, program.getDefaultTime() == null ? ""
					: program.getDefaultTime().format(DateTimeFormatter.ofPattern(DATEFORMAT_DATETIME_FOR_INSERT)));

			int rowsAffected = pst.executeUpdate();
			if (rowsAffected == 1) {
				rvSucceeded = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to execute adding " + errorName + ".");
			e.printStackTrace();
		} finally {

			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close statement when adding " + errorName + ".");
				e.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Failed to close connection when adding " + errorName + ".");
				e.printStackTrace();
			}
		}

		return rvSucceeded;
	}

}
