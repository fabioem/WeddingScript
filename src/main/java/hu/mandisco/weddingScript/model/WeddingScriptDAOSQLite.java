package hu.mandisco.weddingScript.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;

public class WeddingScriptDAOSQLite implements WeddingScriptDAO {

	private static final String JDBC_CONNECTION_PREFIX = "jdbc:sqlite:";
	private static final String DATABASE_FILE = "src\\main\\resources\\database.db";

	private static final String SQL_SELECT_ALL_PROGRAMS = "SELECT * FROM programs";
	private static final String SQL_SELECT_ALL_SERVICES = "SELECT * FROM services";
	private static final String SQL_SELECT_ALL_SCRIPTS = "SELECT * FROM scripts";
	private static final String SQL_SELECT_ALL_ATTRIBUTETYPES = "SELECT * FROM attributeTypes";

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

	public List<Program> getPrograms() {
		Connection conn = null;
		Statement st = null;

		programs.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT_ALL_PROGRAMS);

			while (rs.next()) {
				int progId = rs.getInt("progId");
				String name = rs.getString("name");

				Program program = new Program();
				program.setName(name);
				program.setProgId(progId);

				programs.add(program);
				System.out.println(program + "\t" + name + "\t" + progId);
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

	public List<AttributeType> getAttributeTypes() {
		Connection conn = null;
		Statement st = null;

		attributeTypes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT_ALL_ATTRIBUTETYPES);

			while (rs.next()) {
				int attrTypeId = rs.getInt("attrTypeId");
				String name = rs.getString("name");

				AttributeType attrType = new AttributeType();
				attrType.setName(name);
				attrType.setAttrTypeId(attrTypeId);

				attributeTypes.add(attrType);
				System.out.println(attrType + "\t" + name + "\t" + attrTypeId);
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
			ResultSet rs = st.executeQuery(SQL_SELECT_ALL_SCRIPTS);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			while (rs.next()) {
				int scriptId = rs.getInt("scriptId");
				String name = rs.getString("name");
				Date date = rs.getDate("date");

				Script script = new Script();
				script.setName(name);
				script.setScriptId(scriptId);
				script.setDate(date);

				scripts.add(script);
				System.out.println(script + "\t" + name + "\t" + scriptId + "\t" + date);
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

		return scripts;

	}

}
