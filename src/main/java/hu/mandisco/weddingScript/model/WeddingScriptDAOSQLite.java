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

	private static final String SQL_SELECT_ALL_ATTRIBUTES = "SELECT * FROM attributes WHERE 1 = 1";
	private static final String SQL_SELECT_ALL_PROGRAMS = "SELECT * FROM programs WHERE 1 = 1";
	private static final String SQL_SELECT_ALL_SERVICES = "SELECT * FROM services WHERE 1 = 1";
	private static final String SQL_SELECT_ALL_SCRIPTS = "SELECT * FROM scripts WHERE 1 = 1";
	private static final String SQL_SELECT_ALL_ATTRIBUTETYPES = "SELECT * FROM attributeTypes WHERE 1 = 1";
	private static final String SQL_INSERT_SCRIPT = "INSERT INTO scripts(name, date, comment) VALUES (?, ?, ?)";

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

	public List<Attribute> getAttributes() {
		Connection conn = null;
		Statement st = null;

		attributes.clear();

		try {
			conn = DriverManager.getConnection(databaseConnectionURL);
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT_ALL_ATTRIBUTES);

			while (rs.next()) {
				int attrId = rs.getInt("attrId");
				String name = rs.getString("name");

				Attribute attribute = new Attribute();
				attribute.setName(name);
				attribute.setAttrId(attrId);

				attributes.add(attribute);
				System.out.println(attribute + "\t" + name + "\t" + attrId);
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

			while (rs.next()) {
				int scriptId = rs.getInt("scriptId");
				String name = rs.getString("name");
				Date date = rs.getDate("date");
				String comment = rs.getString("comment");

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				Date lastEdited = format.parse(rs.getString("lastEdited"));
				Date created = format.parse(rs.getString("created"));

				Script script = new Script();
				script.setName(name);
				script.setScriptId(scriptId);
				script.setDate(date);
				script.setComment(comment);
				script.setLastEdited(lastEdited);
				script.setCreated(created);

				scripts.add(script);
				System.out.println(script + "\t" + name + "\t" + scriptId + "\t" + date + "\t" + created);
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
			ResultSet rs = st.executeQuery(SQL_SELECT_ALL_SERVICES);

			while (rs.next()) {
				int serviceId = rs.getInt("serviceId");
				String name = rs.getString("name");

				Service service = new Service();
				service.setName(name);
				service.setServiceId(serviceId);

				services.add(service);
				System.out.println(service + "\t" + name + "\t" + serviceId);
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
		boolean rvSucceeded = false;
        Connection conn = null;
        PreparedStatement pst = null;

        try {

            conn = DriverManager.getConnection(databaseConnectionURL);
            pst = conn.prepareStatement(SQL_INSERT_SCRIPT);
/*
 * 	scriptId INTEGER PRIMARY KEY AUTOINCREMENT,
	name varchar(255) NOT NULL,
	date date,
	comment varchar(255) NOT NULL,
	lastEdited datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	created datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
 */
            int index = 1;
            pst.setString(index++, script.getName());
            pst.setString(index++, script.getDate().toString());
            pst.setString(index++, script.getComment());

            // Az ExecuteUpdate paranccsal végrehajtjuk az utasítást
            // Az executeUpdate visszaadja, hogy hány sort érintett az SQL ha
            // DML-t hajtunk végre (DDL esetén 0-t ad vissza)
            int rowsAffected = pst.executeUpdate();

            // csak akkor sikeres, ha valóban volt érintett sor
            if (rowsAffected == 1) {
                rvSucceeded = true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to execute adding book.");
            e.printStackTrace();
        } finally {
            // NAGYON FONTOS!
            // Minden adatbázis objektumot le kell zárni, mivel ha ezt nem
            // tesszük meg, akkor előfordulhat, hogy nyitott kapcsolatok
            // maradnak az adatbázis felé. Az adatbázis pedig korlátozott
            // számban tart fenn kapcsolatokat, ezért egy idő után akar ez be is
            // telhet!
            // Minden egyes objektumot külön try-catch ágban kell megpróbálni
            // bezárni!
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.out.println("Failed to close statement when adding book.");
                e.printStackTrace();
            }

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Failed to close connection when adding book.");
                e.printStackTrace();
            }
        }

        return rvSucceeded;
	}

}
