package hu.mandisco.weddingscript.view;

public class Labels {

	// Date formats
	public static final String DATEFORMAT_DATETIME = "yyyy.MM.dd HH:mm:ss";
	public static final String DATEFORMAT_TIME = "HH:mm";
	public static final String DATEFORMAT_DATE = "yyyy.MM.dd";
	public static final String DATEFORMAT_DATETIME_FOR_INSERT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMAT_DATE_FOR_INSERT = "yyyy-MM-dd";

	// Error messages
	public static final String LOGGER_FORMAT_STRING = "{0} {1}";

	public static final String FAILED_TO_EXECUTE = "Failed to execute";
	public static final String FAILED_TO_CLOSE_RESULTSET = "Failed to close result set when";
	public static final String FAILED_TO_CLOSE_STATEMENT = "Failed to close statement when";
	public static final String FAILED_TO_CLOSE_CONNECTION = "Failed to close connection when";

	// DB
	public static final String DB_PROGRAM_PROGID = "progId";
	public static final String DB_PROGRAM_NAME = "name";
	public static final String DB_PROGRAM_TIME = "time";
	public static final String DB_PROGRAM_DEFAULT_TIME = "defaultTime";
	public static final String DB_PROGRAM_DEFAULT_PROGRAM = "defaultProgram";

	public static final String DB_SERVICE_SERVICEID = "serviceId";

	public static final String DB_ATTRIBUTE_ATTRIBUTEID = "attributeId";
	public static final String DB_ATTRIBUTE_NAME = "name";
	public static final String DB_ATTRIBUTE_DEFAULT_VALUE = "defaultValue";
	public static final String DB_ATTRIBUTE_VALUE = "value";
	public static final String DB_ATTRIBUTE_MANDATORY = "mandatory";

	public static final String DB_ATTRIBUTE_TYPE_ID = "attrTypeId";
	public static final String DB_ATTRIBUTE_TYPE_NAME = "name";


	private Labels() {
		super();

	}

}
