//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kingbase8.jdbc;

import com.kingbase8.Driver;
import com.kingbase8.KBRefCursorResultSet;
import com.kingbase8.KBResultSetMetaData;
import com.kingbase8.core.BaseConnection;
import com.kingbase8.core.BaseStatement;
import com.kingbase8.core.Encoding;
import com.kingbase8.core.Field;
import com.kingbase8.core.Oid;
import com.kingbase8.core.ParameterList;
import com.kingbase8.core.Query;
import com.kingbase8.core.ResultCursor;
import com.kingbase8.core.ResultHandlerBase;
import com.kingbase8.core.TypeInfo;
import com.kingbase8.core.Utils;
import com.kingbase8.util.ByteConverter;
import com.kingbase8.util.GT;
import com.kingbase8.util.HStoreConverter;
import com.kingbase8.util.KBbytea;
import com.kingbase8.util.KBobject;
import com.kingbase8.util.KBtokenizer;
import com.kingbase8.util.KSQLException;
import com.kingbase8.util.KSQLState;
import com.kingbase8.util.LOGGER;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;

public class KbResultSet implements ResultSet, KBRefCursorResultSet {
    private boolean updateable = false;
    private boolean doingUpdates = false;
    private HashMap<String, Object> updateValues = null;
    private boolean usingOID = false;
    private List<KbResultSet.PrimaryKey> primaryKeys;
    private boolean singleTable = false;
    private String onlyTable = "";
    private String tableName = null;
    private PreparedStatement updateStatement = null;
    private PreparedStatement insertStatement = null;
    private PreparedStatement deleteStatement = null;
    private PreparedStatement selectStatement = null;
    private final int resultsettype;
    private final int resultsetconcurrency;
    private int fetchdirection = 1002;
    private TimeZone defaultTimeZone;
    protected final BaseConnection connection;
    protected final BaseStatement statement;
    protected final Field[] fields;
    protected final Query originalQuery;
    protected final int maxRows;
    protected final int maxFieldSize;
    protected List<byte[][]> rows;
    protected int current_row = -1;
    protected int row_offset;
    protected byte[][] this_row;
    protected SQLWarning warnings = null;
    protected boolean wasNullFlag = false;
    protected boolean onInsertRow = false;
    private byte[][] rowBuffer = (byte[][])((byte[][])null);
    protected int fetchSize;
    protected ResultCursor cursor;
    private Map<String, Integer> columnNameIndexMap;
    private ResultSetMetaData rsMetaData;
    private String refCursorName;
    private static final BigInteger BYTEMAX = new BigInteger(Byte.toString((byte)127));
    private static final BigInteger BYTEMIN = new BigInteger(Byte.toString((byte)-128));
    private static final BigInteger SHORTMAX = new BigInteger(Short.toString((short)32767));
    private static final BigInteger SHORTMIN = new BigInteger(Short.toString((short)-32768));
    private static final NumberFormatException FAST_NUMBER_FAILED = new NumberFormatException() {
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    };
    private static final BigInteger INTMAX = new BigInteger(Integer.toString(2147483647));
    private static final BigInteger INTMIN = new BigInteger(Integer.toString(-2147483648));
    private static final BigInteger LONGMAX = new BigInteger(Long.toString(9223372036854775807L));
    private static final BigInteger LONGMIN = new BigInteger(Long.toString(-9223372036854775808L));

    protected ResultSetMetaData createMetaData() throws SQLException {
        return new KbResultSetMetaData(this.connection, this.fields);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        this.checkClosed();
        if (this.rsMetaData == null) {
            this.rsMetaData = this.createMetaData();
        }

        return this.rsMetaData;
    }

    KbResultSet(Query originalQuery, BaseStatement statement, Field[] fields, List<byte[][]> tuples, ResultCursor cursor, int maxRows, int maxFieldSize, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
        if (tuples == null) {
            throw new NullPointerException("tuples must be non-null");
        } else if (fields == null) {
            throw new NullPointerException("fields must be non-null");
        } else {
            this.originalQuery = originalQuery;
            this.connection = (BaseConnection)statement.getConnection();
            this.statement = statement;
            this.fields = fields;
            this.rows = tuples;
            this.cursor = cursor;
            this.maxRows = maxRows;
            this.maxFieldSize = maxFieldSize;
            this.resultsettype = rsType;
            this.resultsetconcurrency = rsConcurrency;
        }
    }

    public URL getURL(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getURL columnIndex: {0}", columnIndex);
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "getURL(int)");
    }

    public URL getURL(String columnName) throws SQLException {
        return this.getURL(this.findColumn(columnName));
    }

    protected Object internalGetObject(int columnIndex, Field field) throws SQLException {
        switch(this.getSQLType(columnIndex)) {
            case -7:
                return field.getMod() != 1 && field.getMod() != -1 ? this.getString(columnIndex) : this.getBoolean(columnIndex);
            case -6:
                return this.getByte(columnIndex);
            case -5:
                return this.getLong(columnIndex);
            case -4:
            case -3:
            case -2:
                return this.getBytes(columnIndex);
            case -1:
            case 1:
            case 12:
                return this.getString(columnIndex);
            case 2:
            case 3:
                return this.getBigDecimal(columnIndex, field.getMod() == -1 ? -1 : field.getMod() - 4 & '\uffff');
            case 4:
            case 5:
                return this.getInt(columnIndex);
            case 6:
            case 8:
                return this.getDouble(columnIndex);
            case 7:
                return this.getFloat(columnIndex);
            case 16:
                return this.getBoolean(columnIndex);
            case 91:
                return this.getDate(columnIndex);
            case 92:
                return this.getTime(columnIndex);
            case 93:
                return this.getTimestamp(columnIndex, (Calendar)null);
            case 2003:
                return this.getArray(columnIndex);
            case 2004:
                return this.getBlob(columnIndex);
            case 2005:
                return this.getClob(columnIndex);
            case 2009:
                return this.getSQLXML(columnIndex);
            default:
                String type = this.getKBType(columnIndex);
                if (type.equalsIgnoreCase("unknown")) {
                    return this.getString(columnIndex);
                } else if (type.equalsIgnoreCase("UUID")) {
                    if (this.isBinary(columnIndex)) {
                        return this.getUUID(this.this_row[columnIndex - 1]);
                    } else {
                        return this.getUUID(this.getString(columnIndex));
                    }
                } else if (type.equalsIgnoreCase("REFCURSOR")) {
                    String cursorName = this.getString(columnIndex);
                    StringBuilder sb = new StringBuilder("FETCH ALL IN ");
                    Utils.escapeIdentifier(sb, cursorName);
                    ResultSet rs = this.connection.execSQLQuery(sb.toString(), this.resultsettype, 1007);
                    sb.setLength(0);
                    sb.append("CLOSE ");
                    Utils.escapeIdentifier(sb, cursorName);
                    this.connection.execSQLUpdate(sb.toString());
                    ((KbResultSet)rs).setRefCursor(cursorName);
                    return rs;
                } else if ("HSTORE".equals(type)) {
                    if (this.isBinary(columnIndex)) {
                        return HStoreConverter.fromBytes(this.this_row[columnIndex - 1], this.connection.getEncoding());
                    } else {
                        return HStoreConverter.fromString(this.getString(columnIndex));
                    }
                } else {
                    return null;
                }
        }
    }

    private void checkScrollable() throws SQLException {
        this.checkClosed();
        if (this.resultsettype == 1003) {
            throw new KSQLException(GT.tr("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        }
    }

    public boolean absolute(int index) throws SQLException {
        this.checkScrollable();
        if (index == 0) {
            this.beforeFirst();
            return false;
        } else {
            int rows_size = this.rows.size();
            int internalIndex;
            if (index < 0) {
                if (index < -rows_size) {
                    this.beforeFirst();
                    return false;
                }

                internalIndex = rows_size + index;
            } else {
                if (index > rows_size) {
                    this.afterLast();
                    return false;
                }

                internalIndex = index - 1;
            }

            this.current_row = internalIndex;
            this.initRowBuffer();
            this.onInsertRow = false;
            return true;
        }
    }

    public void afterLast() throws SQLException {
        this.checkScrollable();
        int rows_size = this.rows.size();
        if (rows_size > 0) {
            this.current_row = rows_size;
        }

        this.onInsertRow = false;
        this.this_row = (byte[][])((byte[][])null);
        this.rowBuffer = (byte[][])((byte[][])null);
    }

    public void beforeFirst() throws SQLException {
        this.checkScrollable();
        if (!this.rows.isEmpty()) {
            this.current_row = -1;
        }

        this.onInsertRow = false;
        this.this_row = (byte[][])((byte[][])null);
        this.rowBuffer = (byte[][])((byte[][])null);
    }

    public boolean first() throws SQLException {
        this.checkScrollable();
        if (this.rows.size() <= 0) {
            return false;
        } else {
            this.current_row = 0;
            this.initRowBuffer();
            this.onInsertRow = false;
            return true;
        }
    }

    public Array getArray(String colName) throws SQLException {
        return this.getArray(this.findColumn(colName));
    }

    protected Array makeArray(int oid, byte[] value) throws SQLException {
        return new KbArray(this.connection, oid, value);
    }

    protected Array makeArray(int oid, String value) throws SQLException {
        return new KbArray(this.connection, oid, value);
    }

    public Array getArray(int i) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        } else {
            int oid = this.fields[i - 1].getOID();
            return this.isBinary(i) ? this.makeArray(oid, this.this_row[i - 1]) : this.makeArray(oid, this.getFixedString(i));
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return this.getBigDecimal(columnIndex, -1);
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnName));
    }

    public Blob getBlob(String columnName) throws SQLException {
        return this.getBlob(this.findColumn(columnName));
    }

    protected Blob makeBlob(byte[] buffer, KbResultSet rs, int columnIndex) throws SQLException {
        return new KbBlob(buffer, rs, columnIndex);
    }

    public Blob getBlob(int i) throws SQLException {
        this.checkResultSet(i);
        return this.makeBlob(this.getBytes(i), this, i);
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        return this.getCharacterStream(this.findColumn(columnName));
    }

    public Reader getCharacterStream(int i) throws SQLException {
        this.checkResultSet(i);
        return this.wasNullFlag ? null : new CharArrayReader(this.getString(i).toCharArray());
    }

    public Clob getClob(String columnName) throws SQLException {
        return this.getClob(this.findColumn(columnName));
    }

    protected Clob makeClob(char[] buffer, KbResultSet rs, int columnIndex) throws SQLException {
        return new KbClob(buffer, rs, columnIndex);
    }

    public Clob getClob(int i) throws SQLException {
        this.checkResultSet(i);
        return this.getString(i) == null ? this.makeClob((char[])null, this, i) : this.makeClob(this.getString(i).toCharArray(), this, i);
    }

    public int getConcurrency() throws SQLException {
        this.checkClosed();
        return this.resultsetconcurrency;
    }

    public Date getDate(int i, Calendar cal) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        } else {
            if (cal == null) {
                cal = this.getDefaultCalendar();
            }

            if (this.isBinary(i)) {
                int col = i - 1;
                int oid = this.fields[col].getOID();
                TimeZone tz = cal.getTimeZone();
                if (oid == 1082) {
                    return this.connection.getTimestampUtils().toDateBin(tz, this.this_row[col]);
                } else if (oid != 1114 && oid != 1184) {
                    throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[]{Oid.toString(oid), "DATE"}), KSQLState.DATA_TYPE_MISMATCH);
                } else {
                    Timestamp timestamp = this.getTimestamp(i, cal);
                    return this.connection.getTimestampUtils().convertToDate(timestamp.getTime(), tz);
                }
            } else {
                return this.connection.getTimestampUtils().toDate(cal, this.getString(i));
            }
        }
    }

    public Time getTime(int i, Calendar cal) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        } else {
            if (cal == null) {
                cal = this.getDefaultCalendar();
            }

            if (this.isBinary(i)) {
                int col = i - 1;
                int oid = this.fields[col].getOID();
                TimeZone tz = cal.getTimeZone();
                if (oid != 1083 && oid != 1266) {
                    if (oid != 1114 && oid != 1184) {
                        throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[]{Oid.toString(oid), "TIME"}), KSQLState.DATA_TYPE_MISMATCH);
                    } else {
                        Timestamp timestamp = this.getTimestamp(i, cal);
                        return this.connection.getTimestampUtils().convertToTime(timestamp.getTime(), tz);
                    }
                } else {
                    return this.connection.getTimestampUtils().toTimeBin(tz, this.this_row[col]);
                }
            } else {
                String string = this.getString(i);
                return this.connection.getTimestampUtils().toTime(cal, string);
            }
        }
    }

    private LocalTime getLocalTime(int i) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        } else if (this.isBinary(i)) {
            int col = i - 1;
            int oid = this.fields[col].getOID();
            if (oid == 1083) {
                return this.connection.getTimestampUtils().toLocalTimeBin(this.this_row[col]);
            } else {
                throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[]{Oid.toString(oid), "TIME"}), KSQLState.DATA_TYPE_MISMATCH);
            }
        } else {
            String string = this.getString(i);
            return this.connection.getTimestampUtils().toLocalTime(string);
        }
    }

    public Timestamp getTimestamp(int i, Calendar cal) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        } else {
            if (cal == null) {
                cal = this.getDefaultCalendar();
            }

            int col = i - 1;
            int oid = this.fields[col].getOID();
            if (!this.isBinary(i)) {
                String string = this.getString(i);
                return oid != 1083 && oid != 1266 ? this.connection.getTimestampUtils().toTimestamp(cal, string) : new Timestamp(this.connection.getTimestampUtils().toTime(cal, string).getTime());
            } else if (oid != 1184 && oid != 1114) {
                long millis;
                if (oid != 1083 && oid != 1266) {
                    if (oid != 1082) {
                        throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[]{Oid.toString(oid), "TIMESTAMP"}), KSQLState.DATA_TYPE_MISMATCH);
                    }

                    millis = this.getDate(i, cal).getTime();
                } else {
                    millis = this.getTime(i, cal).getTime();
                }

                return new Timestamp(millis);
            } else {
                boolean hasTimeZone = oid == 1184;
                TimeZone tz = cal.getTimeZone();
                return this.connection.getTimestampUtils().toTimestampBin(tz, this.this_row[col], hasTimeZone);
            }
        }
    }

    private LocalDateTime getLocalDateTime(int i) throws SQLException {
        this.checkResultSet(i);
        if (this.wasNullFlag) {
            return null;
        } else {
            int col = i - 1;
            int oid = this.fields[col].getOID();
            if (oid != 1114) {
                throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[]{Oid.toString(oid), "TIMESTAMP"}), KSQLState.DATA_TYPE_MISMATCH);
            } else if (this.isBinary(i)) {
                TimeZone timeZone = this.getDefaultCalendar().getTimeZone();
                return this.connection.getTimestampUtils().toLocalDateTimeBin(timeZone, this.this_row[col]);
            } else {
                String string = this.getString(i);
                return this.connection.getTimestampUtils().toLocalDateTime(string);
            }
        }
    }

    public Date getDate(String c, Calendar cal) throws SQLException {
        return this.getDate(this.findColumn(c), cal);
    }

    public Time getTime(String c, Calendar cal) throws SQLException {
        return this.getTime(this.findColumn(c), cal);
    }

    public Timestamp getTimestamp(String c, Calendar cal) throws SQLException {
        return this.getTimestamp(this.findColumn(c), cal);
    }

    public int getFetchDirection() throws SQLException {
        this.checkClosed();
        return this.fetchdirection;
    }

    public Object getObjectImpl(String columnName, Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(this.findColumn(columnName), map);
    }

    public Object getObjectImpl(int i, Map<String, Class<?>> map) throws SQLException {
        this.checkClosed();
        if (map != null && !map.isEmpty()) {
            throw Driver.notImplemented(this.getClass(), "getObjectImpl(int,Map)");
        } else {
            return this.getObject(i);
        }
    }

    public Ref getRef(String columnName) throws SQLException {
        return this.getRef(this.findColumn(columnName));
    }

    public Ref getRef(int i) throws SQLException {
        this.checkClosed();
        throw Driver.notImplemented(this.getClass(), "getRef(int)");
    }

    public int getRow() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return 0;
        } else {
            int rows_size = this.rows.size();
            return this.current_row >= 0 && this.current_row < rows_size ? this.row_offset + this.current_row + 1 : 0;
        }
    }

    public Statement getStatement() throws SQLException {
        this.checkClosed();
        return this.statement;
    }

    public int getType() throws SQLException {
        this.checkClosed();
        return this.resultsettype;
    }

    public boolean isAfterLast() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return false;
        } else {
            int rows_size = this.rows.size();
            if (this.row_offset + rows_size == 0) {
                return false;
            } else {
                return this.current_row >= rows_size;
            }
        }
    }

    public boolean isBeforeFirst() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return false;
        } else {
            return this.row_offset + this.current_row < 0 && !this.rows.isEmpty();
        }
    }

    public boolean isFirst() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return false;
        } else {
            int rows_size = this.rows.size();
            if (this.row_offset + rows_size == 0) {
                return false;
            } else {
                return this.row_offset + this.current_row == 0;
            }
        }
    }

    public boolean isLast() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            return false;
        } else {
            int rows_size = this.rows.size();
            if (rows_size == 0) {
                return false;
            } else if (this.current_row != rows_size - 1) {
                return false;
            } else if (this.cursor == null) {
                return true;
            } else if (this.maxRows > 0 && this.row_offset + this.current_row == this.maxRows) {
                return true;
            } else {
                this.row_offset += rows_size - 1;
                int fetchRows = this.fetchSize;
                if (this.maxRows != 0 && (fetchRows == 0 || this.row_offset + fetchRows > this.maxRows)) {
                    fetchRows = this.maxRows - this.row_offset;
                }

                this.connection.getQueryExecutor().fetch(this.cursor, new KbResultSet.CursorResultHandler(), fetchRows);
                this.rows.add(0, this.this_row);
                this.current_row = 0;
                return this.rows.size() == 1;
            }
        }
    }

    public boolean last() throws SQLException {
        this.checkScrollable();
        int rows_size = this.rows.size();
        if (rows_size <= 0) {
            return false;
        } else {
            this.current_row = rows_size - 1;
            this.initRowBuffer();
            this.onInsertRow = false;
            return true;
        }
    }

    public boolean previous() throws SQLException {
        this.checkScrollable();
        if (this.onInsertRow) {
            throw new KSQLException(GT.tr("Can''t use relative move methods while on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else if (this.current_row - 1 < 0) {
            this.current_row = -1;
            this.this_row = (byte[][])((byte[][])null);
            this.rowBuffer = (byte[][])((byte[][])null);
            return false;
        } else {
            --this.current_row;
            this.initRowBuffer();
            return true;
        }
    }

    public boolean relative(int rows) throws SQLException {
        this.checkScrollable();
        if (this.onInsertRow) {
            throw new KSQLException(GT.tr("Can''t use relative move methods while on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            return this.absolute(this.current_row + 1 + rows);
        }
    }

    public void setFetchDirection(int direction) throws SQLException {
        this.checkClosed();
        switch(direction) {
            case 1001:
            case 1002:
                this.checkScrollable();
            case 1000:
                this.fetchdirection = direction;
                return;
            default:
                throw new KSQLException(GT.tr("Invalid fetch direction constant: {0}.", new Object[]{direction}), KSQLState.INVALID_PARAMETER_VALUE);
        }
    }

    public synchronized void cancelRowUpdates() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            throw new KSQLException(GT.tr("Cannot call cancelRowUpdates() when on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            if (this.doingUpdates) {
                this.doingUpdates = false;
                this.clearRowBuffer(true);
            }

        }
    }

    public synchronized void deleteRow() throws SQLException {
        this.checkUpdateable();
        if (this.onInsertRow) {
            throw new KSQLException(GT.tr("Cannot call deleteRow() when on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else if (this.isBeforeFirst()) {
            throw new KSQLException(GT.tr("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else if (this.isAfterLast()) {
            throw new KSQLException(GT.tr("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else if (this.rows.isEmpty()) {
            throw new KSQLException(GT.tr("There are no rows in this ResultSet.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            int numKeys = this.primaryKeys.size();
            if (this.deleteStatement == null) {
                StringBuilder deleteSQL = (new StringBuilder("DELETE FROM ")).append(this.onlyTable).append(this.tableName).append(" where ");

                for(int i = 0; i < numKeys; ++i) {
                    Utils.escapeIdentifier(deleteSQL, ((KbResultSet.PrimaryKey)this.primaryKeys.get(i)).name);
                    deleteSQL.append(" = ?");
                    if (i < numKeys - 1) {
                        deleteSQL.append(" and ");
                    }
                }

                this.deleteStatement = this.connection.prepareStatement(deleteSQL.toString());
            }

            this.deleteStatement.clearParameters();

            for(int i = 0; i < numKeys; ++i) {
                this.deleteStatement.setObject(i + 1, ((KbResultSet.PrimaryKey)this.primaryKeys.get(i)).getValue());
            }

            this.deleteStatement.executeUpdate();
            this.rows.remove(this.current_row);
            --this.current_row;
            this.moveToCurrentRow();
        }
    }

    public synchronized void insertRow() throws SQLException {
        this.checkUpdateable();
        if (!this.onInsertRow) {
            throw new KSQLException(GT.tr("Not on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else if (this.updateValues.isEmpty()) {
            throw new KSQLException(GT.tr("You must specify at least one column value to insert a row.", new Object[0]), KSQLState.INVALID_PARAMETER_VALUE);
        } else {
            StringBuilder insertSQL = (new StringBuilder("INSERT INTO ")).append(this.tableName).append(" (");
            StringBuilder paramSQL = new StringBuilder(") values (");
            Iterator<String> columnNames = this.updateValues.keySet().iterator();
            int numColumns = this.updateValues.size();

            for(int i = 0; columnNames.hasNext(); ++i) {
                String columnName = (String)columnNames.next();
                Utils.escapeIdentifier(insertSQL, columnName);
                if (i < numColumns - 1) {
                    insertSQL.append(", ");
                    paramSQL.append("?,");
                } else {
                    paramSQL.append("?)");
                }
            }

            insertSQL.append(paramSQL.toString());
            this.insertStatement = this.connection.prepareStatement(insertSQL.toString());
            Iterator<Object> values = this.updateValues.values().iterator();

            for(int i = 1; values.hasNext(); ++i) {
                this.insertStatement.setObject(i, values.next());
            }

            this.insertStatement.executeUpdate();
            if (this.usingOID) {
                long insertedOID = ((KbStatement)this.insertStatement).getLastOID();
                this.updateValues.put("OID", insertedOID);
            }

            this.updateRowBuffer();
            this.rows.add(this.rowBuffer);
            this.this_row = this.rowBuffer;
            this.clearRowBuffer(false);
        }
    }

    public synchronized void moveToCurrentRow() throws SQLException {
        this.checkUpdateable();
        if (this.current_row >= 0 && this.current_row < this.rows.size()) {
            this.initRowBuffer();
        } else {
            this.this_row = (byte[][])((byte[][])null);
            this.rowBuffer = (byte[][])((byte[][])null);
        }

        this.onInsertRow = false;
        this.doingUpdates = false;
    }

    public synchronized void moveToInsertRow() throws SQLException {
        this.checkUpdateable();
        if (this.insertStatement != null) {
            this.insertStatement = null;
        }

        this.clearRowBuffer(false);
        this.onInsertRow = true;
        this.doingUpdates = false;
    }

    private synchronized void clearRowBuffer(boolean copyCurrentRow) throws SQLException {
        this.rowBuffer = new byte[this.fields.length][];
        if (copyCurrentRow) {
            System.arraycopy(this.this_row, 0, this.rowBuffer, 0, this.this_row.length);
        }

        this.updateValues.clear();
    }

    public boolean rowDeleted() throws SQLException {
        this.checkClosed();
        return false;
    }

    public boolean rowInserted() throws SQLException {
        this.checkClosed();
        return false;
    }

    public boolean rowUpdated() throws SQLException {
        this.checkClosed();
        return false;
    }

    public synchronized void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        if (x == null) {
            this.updateNull(columnIndex);
        } else {
            try {
                InputStreamReader reader = new InputStreamReader(x, "ASCII");
                char[] data = new char[length];
                int numRead = 0;

                do {
                    int n = reader.read(data, numRead, length - numRead);
                    if (n == -1) {
                        break;
                    }

                    numRead += n;
                } while(numRead != length);

                this.updateString(columnIndex, new String(data, 0, numRead));
            } catch (UnsupportedEncodingException var8) {
                throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[]{"ASCII"}), KSQLState.UNEXPECTED_ERROR, var8);
            } catch (IOException var9) {
                throw new KSQLException(GT.tr("Provided InputStream failed.", new Object[0]), (KSQLState)null, var9);
            }
        }

    }

    public synchronized void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        if (x == null) {
            this.updateNull(columnIndex);
        } else {
            byte[] data = new byte[length];
            int numRead = 0;

            try {
                do {
                    int n = x.read(data, numRead, length - numRead);
                    if (n == -1) {
                        break;
                    }

                    numRead += n;
                } while(numRead != length);
            } catch (IOException var7) {
                throw new KSQLException(GT.tr("Provided InputStream failed.", new Object[0]), (KSQLState)null, var7);
            }

            if (numRead == length) {
                this.updateBytes(columnIndex, data);
            } else {
                byte[] data2 = new byte[numRead];
                System.arraycopy(data, 0, data2, 0, numRead);
                this.updateBytes(columnIndex, data2);
            }
        }

    }

    public synchronized void updateBoolean(int columnIndex, boolean x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateByte(int columnIndex, byte x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateBytes(int columnIndex, byte[] x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        if (x == null) {
            this.updateNull(columnIndex);
        } else {
            try {
                char[] data = new char[length];
                int numRead = 0;

                do {
                    int n = x.read(data, numRead, length - numRead);
                    if (n == -1) {
                        break;
                    }

                    numRead += n;
                } while(numRead != length);

                this.updateString(columnIndex, new String(data, 0, numRead));
            } catch (IOException var7) {
                throw new KSQLException(GT.tr("Provided Reader failed.", new Object[0]), (KSQLState)null, var7);
            }
        }

    }

    public synchronized void updateDate(int columnIndex, Date x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateDouble(int columnIndex, double x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateFloat(int columnIndex, float x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateInt(int columnIndex, int x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateLong(int columnIndex, long x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateNull(int columnIndex) throws SQLException {
        this.checkColumnIndex(columnIndex);
        String columnTypeName = this.getKBType(columnIndex);
        this.updateValue(columnIndex, new KbResultSet.NullObject(columnTypeName));
    }

    public synchronized void updateObject(int columnIndex, Object x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        this.updateObject(columnIndex, x);
    }

    public void refreshRow() throws SQLException {
        this.checkUpdateable();
        if (this.onInsertRow) {
            throw new KSQLException(GT.tr("Can''t refresh the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            if (!this.isBeforeFirst() && !this.isAfterLast() && !this.rows.isEmpty()) {
                StringBuilder selectSQL = new StringBuilder("select ");
                ResultSetMetaData rsmd = this.getMetaData();
                KBResultSetMetaData pgmd = (KBResultSetMetaData)rsmd;

                int numKeys;
                for(numKeys = 1; numKeys <= rsmd.getColumnCount(); ++numKeys) {
                    if (numKeys > 1) {
                        selectSQL.append(", ");
                    }

                    selectSQL.append(pgmd.getBaseColumnName(numKeys));
                }

                selectSQL.append(" from ").append(this.onlyTable).append(this.tableName).append(" where ");
                numKeys = this.primaryKeys.size();

                for(int i = 0; i < numKeys; ++i) {
                    KbResultSet.PrimaryKey primaryKey = (KbResultSet.PrimaryKey)this.primaryKeys.get(i);
                    selectSQL.append(primaryKey.name).append("= ?");
                    if (i < numKeys - 1) {
                        selectSQL.append(" and ");
                    }
                }

                String sqlText = selectSQL.toString();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "selecting {0}", sqlText);
                }

                this.selectStatement = this.connection.prepareStatement(sqlText, 1004, 1008);
                int j = 0;

                for(int i = 1; j < numKeys; ++i) {
                    this.selectStatement.setObject(i, ((KbResultSet.PrimaryKey)this.primaryKeys.get(j)).getValue());
                    ++j;
                }

                KbResultSet rs = (KbResultSet)this.selectStatement.executeQuery();
                if (rs.next()) {
                    this.rowBuffer = rs.this_row;
                }

                this.rows.set(this.current_row, this.rowBuffer);
                this.this_row = this.rowBuffer;
                LOGGER.log(Level.FINE, "done updates");
                rs.close();
                this.selectStatement.close();
                this.selectStatement = null;
            }

        }
    }

    public synchronized void insertLob(Object obj) throws SQLException {
        this.init();
        StringBuilder updateSQL = new StringBuilder("UPDATE " + this.onlyTable + this.tableName + " SET  ");
        KBResultSetMetaData md = (KBResultSetMetaData)this.getMetaData();
        String column = null;
        if (obj instanceof KbClob) {
            column = md.getBaseColumnName(((KbClob)obj).getColumnIndex());
        } else if (obj instanceof KbBlob) {
            column = md.getBaseColumnName(((KbBlob)obj).getColumnIndex());
        }

        Utils.escapeIdentifier(updateSQL, column);
        updateSQL.append(" = ?");
        updateSQL.append(" WHERE ");
        int numKeys = this.primaryKeys.size();

        for(int i = 0; i < numKeys; ++i) {
            KbResultSet.PrimaryKey primaryKey = (KbResultSet.PrimaryKey)this.primaryKeys.get(i);
            Utils.escapeIdentifier(updateSQL, primaryKey.name);
            updateSQL.append(" = ?");
            if (i < numKeys - 1) {
                updateSQL.append(" and ");
            }
        }

        updateSQL.append(" returning CTID,XMIN");
        String sqlText = updateSQL.toString();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "updating {0}", sqlText);
        }

        this.updateStatement = this.connection.prepareStatement(sqlText);
        int i = 1;
        this.updateStatement.setObject(i, obj);

        for(int j = 0; j < numKeys; ++i) {
            this.updateStatement.setObject(i + 1, ((KbResultSet.PrimaryKey)this.primaryKeys.get(j)).getValue());
            ++j;
        }

        this.updateStatement.execute();
        ResultSet rs = this.updateStatement.getResultSet();
        if (rs.next()) {
            this.this_row[this.this_row.length - 2] = rs.getBytes(1);
            this.this_row[this.this_row.length - 1] = rs.getBytes(2);
        }

        this.updateStatement.close();
        this.updateStatement = null;
    }

    void init() throws SQLException {
        this.checkClosed();
        if (!this.originalQuery.hasForUpdate()) {
            throw new KSQLException(GT.tr("The query \"{0}\" is not locked.", new Object[]{this.originalQuery.toString((ParameterList)null)}), (KSQLState)null);
        } else {
            this.parseQuery();
            if (!this.singleTable) {
                LOGGER.log(Level.FINE, "not a single table");
            } else {
                LOGGER.log(Level.FINE, "getting primary keys");
                this.primaryKeys = new ArrayList();
                int ctidIndex = this.findColumnIndex("CTID");
                int xminIndex = this.findColumnIndex("XMIN");
                boolean existInternalRID = false;
                if (ctidIndex > 0 && xminIndex > 0) {
                    existInternalRID = true;
                }

                int i = 0;
                if (existInternalRID) {
                    ++i;
                    this.primaryKeys.add(new KbResultSet.PrimaryKey(ctidIndex, "CTID"));
                    ++i;
                    this.primaryKeys.add(new KbResultSet.PrimaryKey(xminIndex, "XMIN"));
                }

                LOGGER.log(Level.FINE, "no of keys={0}", i);
            }

        }
    }

    public synchronized void updateRow() throws SQLException {
        this.checkUpdateable();
        if (this.onInsertRow) {
            throw new KSQLException(GT.tr("Cannot call updateRow() when on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else if (!this.isBeforeFirst() && !this.isAfterLast() && !this.rows.isEmpty()) {
            if (this.doingUpdates) {
                StringBuilder updateSQL = new StringBuilder("UPDATE " + this.onlyTable + this.tableName + " SET  ");
                int numColumns = this.updateValues.size();
                Iterator<String> columns = this.updateValues.keySet().iterator();

                int numKeys;
                String sqlText;
                for(numKeys = 0; columns.hasNext(); ++numKeys) {
                    sqlText = (String)columns.next();
                    Utils.escapeIdentifier(updateSQL, sqlText);
                    updateSQL.append(" = ?");
                    if (numKeys < numColumns - 1) {
                        updateSQL.append(", ");
                    }
                }

                updateSQL.append(" WHERE ");
                numKeys = this.primaryKeys.size();

                int i;
                for(i = 0; i < numKeys; ++i) {
                    KbResultSet.PrimaryKey primaryKey = (KbResultSet.PrimaryKey)this.primaryKeys.get(i);
                    Utils.escapeIdentifier(updateSQL, primaryKey.name);
                    updateSQL.append(" = ?");
                    if (i < numKeys - 1) {
                        updateSQL.append(" and ");
                    }
                }

                sqlText = updateSQL.toString();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "updating {0}", sqlText);
                }

                this.updateStatement = this.connection.prepareStatement(sqlText);
                i = 0;

                for(Iterator iterator = this.updateValues.values().iterator(); iterator.hasNext(); ++i) {
                    Object o = iterator.next();
                    this.updateStatement.setObject(i + 1, o);
                }

                for(int j = 0; j < numKeys; ++i) {
                    this.updateStatement.setObject(i + 1, ((KbResultSet.PrimaryKey)this.primaryKeys.get(j)).getValue());
                    ++j;
                }

                this.updateStatement.executeUpdate();
                this.updateStatement.close();
                this.updateStatement = null;
                this.updateRowBuffer();
                LOGGER.log(Level.FINE, "copying data");
                System.arraycopy(this.rowBuffer, 0, this.this_row, 0, this.rowBuffer.length);
                this.rows.set(this.current_row, this.rowBuffer);
                LOGGER.log(Level.FINE, "done updates");
                this.updateValues.clear();
                this.doingUpdates = false;
            }

        } else {
            throw new KSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        }
    }

    public synchronized void updateShort(int columnIndex, short x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateString(int columnIndex, String x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateTime(int columnIndex, Time x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        this.updateValue(columnIndex, x);
    }

    public synchronized void updateNull(String columnName) throws SQLException {
        this.updateNull(this.findColumn(columnName));
    }

    public synchronized void updateBoolean(String columnName, boolean x) throws SQLException {
        this.updateBoolean(this.findColumn(columnName), x);
    }

    public synchronized void updateByte(String columnName, byte x) throws SQLException {
        this.updateByte(this.findColumn(columnName), x);
    }

    public synchronized void updateShort(String columnName, short x) throws SQLException {
        this.updateShort(this.findColumn(columnName), x);
    }

    public synchronized void updateInt(String columnName, int x) throws SQLException {
        this.updateInt(this.findColumn(columnName), x);
    }

    public synchronized void updateLong(String columnName, long x) throws SQLException {
        this.updateLong(this.findColumn(columnName), x);
    }

    public synchronized void updateFloat(String columnName, float x) throws SQLException {
        this.updateFloat(this.findColumn(columnName), x);
    }

    public synchronized void updateDouble(String columnName, double x) throws SQLException {
        this.updateDouble(this.findColumn(columnName), x);
    }

    public synchronized void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        this.updateBigDecimal(this.findColumn(columnName), x);
    }

    public synchronized void updateString(String columnName, String x) throws SQLException {
        this.updateString(this.findColumn(columnName), x);
    }

    public synchronized void updateBytes(String columnName, byte[] x) throws SQLException {
        this.updateBytes(this.findColumn(columnName), x);
    }

    public synchronized void updateDate(String columnName, Date x) throws SQLException {
        this.updateDate(this.findColumn(columnName), x);
    }

    public synchronized void updateTime(String columnName, Time x) throws SQLException {
        this.updateTime(this.findColumn(columnName), x);
    }

    public synchronized void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        this.updateTimestamp(this.findColumn(columnName), x);
    }

    public synchronized void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnName), x, length);
    }

    public synchronized void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnName), x, length);
    }

    public synchronized void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnName), reader, length);
    }

    public synchronized void updateObject(String columnName, Object x, int scale) throws SQLException {
        this.updateObject(this.findColumn(columnName), x);
    }

    public synchronized void updateObject(String columnName, Object x) throws SQLException {
        this.updateObject(this.findColumn(columnName), x);
    }

    boolean isUpdateable() throws SQLException {
        this.checkClosed();
        if (this.resultsetconcurrency == 1007) {
            throw new KSQLException(GT.tr("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else if (this.updateable) {
            return true;
        } else {
            LOGGER.log(Level.FINE, "checking if rs is updateable");
            this.parseQuery();
            if (!this.singleTable) {
                LOGGER.log(Level.FINE, "not a single table");
                return false;
            } else {
                LOGGER.log(Level.FINE, "getting primary keys");
                this.primaryKeys = new ArrayList();
                this.usingOID = false;
                int oidIndex = this.findColumnIndex("OID");
                boolean existInternalRID = false;
                int ctidIndex = this.findColumnIndex("CTID");
                int xminIndex = this.findColumnIndex("XMIN");
                if (ctidIndex > 0 && xminIndex > 0) {
                    existInternalRID = true;
                }

                int i = 0;
                int numPKcolumns = 0;
                if (oidIndex > 0) {
                    ++i;
                    ++numPKcolumns;
                    this.primaryKeys.add(new KbResultSet.PrimaryKey(oidIndex, "OID"));
                    this.usingOID = true;
                } else if (existInternalRID) {
                    ++i;
                    ++numPKcolumns;
                    this.primaryKeys.add(new KbResultSet.PrimaryKey(ctidIndex, "CTID"));
                    ++i;
                    ++numPKcolumns;
                    this.primaryKeys.add(new KbResultSet.PrimaryKey(xminIndex, "XMIN"));
                } else {
                    String[] s = quotelessTableName(this.tableName);
                    String quotelessTableName = s[0];
                    String quotelessSchemaName = s[1];
                    ResultSet rs = this.connection.getMetaData().getPrimaryKeys("", quotelessSchemaName, quotelessTableName);

                    while(rs.next()) {
                        ++numPKcolumns;
                        String columnName = rs.getString(4);
                        int index = this.findColumnIndex(columnName);
                        if (index > 0) {
                            ++i;
                            this.primaryKeys.add(new KbResultSet.PrimaryKey(index, columnName));
                        }
                    }

                    rs.close();
                }

                LOGGER.log(Level.FINE, "no of keys={0}", i);
                if (i < 1) {
                    throw new KSQLException(GT.tr("No primary key found for table {0}.", new Object[]{this.tableName}), KSQLState.DATA_ERROR);
                } else {
                    this.updateable = i == numPKcolumns;
                    LOGGER.log(Level.FINE, "checking primary key {0}", this.updateable);
                    return this.updateable;
                }
            }
        }
    }

    public static String[] quotelessTableName(String fullname) {
        String[] parts = new String[]{null, ""};
        StringBuilder acc = new StringBuilder();
        boolean betweenQuotes = false;

        for(int i = 0; i < fullname.length(); ++i) {
            char c = fullname.charAt(i);
            switch(c) {
                case '"':
                    if (i < fullname.length() - 1 && fullname.charAt(i + 1) == '"') {
                        ++i;
                        acc.append(c);
                        break;
                    }

                    betweenQuotes = !betweenQuotes;
                    break;
                case '.':
                    if (betweenQuotes) {
                        acc.append(c);
                    } else {
                        parts[1] = acc.toString();
                        acc = new StringBuilder();
                    }
                    break;
                default:
                    acc.append(betweenQuotes ? c : Character.toUpperCase(c));
            }
        }

        parts[0] = acc.toString();
        return parts;
    }

    private void parseQuery() {
        String l_sql = this.originalQuery.toString((ParameterList)null);
        StringTokenizer st = new StringTokenizer(l_sql, " \r\t\n");
        boolean tableFound = false;
        boolean tablesChecked = false;
        String name = "";
        this.singleTable = true;

        while(!tableFound && !tablesChecked && st.hasMoreTokens()) {
            name = st.nextToken();
            if ("from".equalsIgnoreCase(name)) {
                this.tableName = st.nextToken();
                if ("only".equalsIgnoreCase(this.tableName)) {
                    this.tableName = st.nextToken();
                    this.onlyTable = "ONLY ";
                }

                tableFound = true;
            }
        }

    }

    private void updateRowBuffer() throws SQLException {
        Iterator var1 = this.updateValues.entrySet().iterator();

        while(var1.hasNext()) {
            Entry<String, Object> entry = (Entry)var1.next();
            int columnIndex = this.findColumn((String)entry.getKey()) - 1;
            Object valueObject = entry.getValue();
            if (valueObject instanceof KBobject) {
                String value = ((KBobject)valueObject).getValue();
                this.rowBuffer[columnIndex] = value == null ? null : this.connection.encodeString(value);
            } else {
                switch(this.getSQLType(columnIndex + 1)) {
                    case -4:
                    case -3:
                    case -2:
                        if (this.isBinary(columnIndex + 1)) {
                            this.rowBuffer[columnIndex] = (byte[])((byte[])((byte[])valueObject));
                        } else {
                            try {
                                this.rowBuffer[columnIndex] = KBbytea.toKBString((byte[])((byte[])((byte[])valueObject))).getBytes("ISO-8859-1");
                            } catch (UnsupportedEncodingException var6) {
                                throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[]{"ISO-8859-1"}), KSQLState.UNEXPECTED_ERROR, var6);
                            }
                        }
                    case 0:
                        break;
                    case 91:
                        this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(this.getDefaultCalendar(), (Date)valueObject));
                        break;
                    case 92:
                        this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(this.getDefaultCalendar(), (Time)valueObject));
                        break;
                    case 93:
                        this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(this.getDefaultCalendar(), (Timestamp)valueObject));
                        break;
                    default:
                        this.rowBuffer[columnIndex] = this.connection.encodeString(String.valueOf(valueObject));
                }
            }
        }

    }

    public BaseStatement getKBStatement() {
        return this.statement;
    }

    public String getRefCursor() {
        return this.refCursorName;
    }

    private void setRefCursor(String refCursorName) {
        this.refCursorName = refCursorName;
    }

    public void setFetchSize(int rows) throws SQLException {
        this.checkClosed();
        if (rows < 0) {
            throw new KSQLException(GT.tr("Fetch size must be a value greater to or equal to 0.", new Object[0]), KSQLState.INVALID_PARAMETER_VALUE);
        } else {
            this.fetchSize = rows;
        }
    }

    public int getFetchSize() throws SQLException {
        this.checkClosed();
        return this.fetchSize;
    }

    public boolean next() throws SQLException {
        this.checkClosed();
        if (this.onInsertRow) {
            throw new KSQLException(GT.tr("Can''t use relative move methods while on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            if (this.current_row + 1 < this.rows.size()) {
                ++this.current_row;
            } else {
                if (this.cursor == null || this.maxRows > 0 && this.row_offset + this.rows.size() >= this.maxRows) {
                    this.current_row = this.rows.size();
                    this.this_row = (byte[][])((byte[][])null);
                    this.rowBuffer = (byte[][])((byte[][])null);
                    return false;
                }

                this.row_offset += this.rows.size();
                int fetchRows = this.fetchSize;
                if (this.maxRows != 0 && (fetchRows == 0 || this.row_offset + fetchRows > this.maxRows)) {
                    fetchRows = this.maxRows - this.row_offset;
                }

                this.connection.getQueryExecutor().fetch(this.cursor, new KbResultSet.CursorResultHandler(), fetchRows);
                this.current_row = 0;
                if (this.rows.isEmpty()) {
                    this.this_row = (byte[][])((byte[][])null);
                    this.rowBuffer = (byte[][])((byte[][])null);
                    return false;
                }
            }

            this.initRowBuffer();
            return true;
        }
    }

    public void close() throws SQLException {
        try {
            this.rows = null;
            if (this.cursor != null) {
                this.cursor.close();
                this.cursor = null;
            }
        } finally {
            ((KbStatement)this.statement).checkCompletion();
        }

    }

    public boolean wasNull() throws SQLException {
        this.checkClosed();
        return this.wasNullFlag;
    }

    public String getString(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getString columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        } else if (this.isBinary(columnIndex) && this.getSQLType(columnIndex) != 12 && this.getSQLType(columnIndex) != -7 && this.getSQLType(columnIndex) != 2005) {
            Field field = this.fields[columnIndex - 1];
            Object obj = this.internalGetObject(columnIndex, field);
            if (obj == null) {
                return null;
            } else if (!(obj instanceof java.util.Date)) {
                return "HSTORE".equals(this.getKBType(columnIndex)) ? HStoreConverter.toString((Map)obj) : this.trimString(columnIndex, obj.toString());
            } else {
                int oid = field.getOID();
                return this.connection.getTimestampUtils().timeToString((java.util.Date)obj, oid == 1184 || oid == 1266);
            }
        } else {
            Encoding encoding = this.connection.getEncoding();

            try {
                return this.trimString(columnIndex, encoding.decode(this.this_row[columnIndex - 1]));
            } catch (IOException var5) {
                throw new KSQLException(GT.tr("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", new Object[0]), KSQLState.DATA_ERROR, var5);
            }
        }
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getBoolean columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return false;
        } else {
            int col = columnIndex - 1;
            if (16 != this.fields[col].getOID()) {
                return this.isBinary(columnIndex) ? BooleanTypeUtil.castToBoolean(this.readDoubleValue(this.this_row[col], this.fields[col].getOID(), "BOOLEAN")) : BooleanTypeUtil.castToBoolean(this.getString(columnIndex));
            } else {
                byte[] v = this.this_row[col];
                return 1 == v.length && 116 == v[0];
            }
        }
    }

    public byte getByte(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getByte columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0;
        } else if (this.isBinary(columnIndex)) {
            int col = columnIndex - 1;
            int oid = this.fields[col].getOID();
            return oid == 9 ? ByteConverter.int1(this.this_row[col], 0) : (byte)((int)this.readLongValue(this.this_row[col], this.fields[col].getOID(), -128L, 127L, "byte"));
        } else {
            String s = this.getString(columnIndex);
            if (s != null) {
                s = s.trim();
                if (s.isEmpty()) {
                    return 0;
                } else {
                    try {
                        return Byte.parseByte(s);
                    } catch (NumberFormatException var9) {
                        try {
                            BigDecimal n = new BigDecimal(s);
                            BigInteger i = n.toBigInteger();
                            int gt = i.compareTo(BYTEMAX);
                            int lt = i.compareTo(BYTEMIN);
                            if (gt <= 0 && lt >= 0) {
                                return i.byteValue();
                            } else {
                                throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"byte", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                            }
                        } catch (NumberFormatException var8) {
                            throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"byte", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                        }
                    }
                }
            } else {
                return 0;
            }
        }
    }

    public short getShort(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getShort columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0;
        } else if (this.isBinary(columnIndex)) {
            int col = columnIndex - 1;
            int oid = this.fields[col].getOID();
            return oid == 21 ? ByteConverter.int2(this.this_row[col], 0) : (short)((int)this.readLongValue(this.this_row[col], oid, -32768L, 32767L, "short"));
        } else {
            String s = this.getFixedString(columnIndex);
            if (s != null) {
                s = s.trim();

                try {
                    return Short.parseShort(s);
                } catch (NumberFormatException var9) {
                    try {
                        BigDecimal n = new BigDecimal(s);
                        BigInteger i = n.toBigInteger();
                        int gt = i.compareTo(SHORTMAX);
                        int lt = i.compareTo(SHORTMIN);
                        if (gt <= 0 && lt >= 0) {
                            return i.shortValue();
                        } else {
                            throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"short", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                        }
                    } catch (NumberFormatException var8) {
                        throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"short", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                    }
                }
            } else {
                return 0;
            }
        }
    }

    public int getInt(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getInt columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0;
        } else if (this.isBinary(columnIndex)) {
            int col = columnIndex - 1;
            int oid = this.fields[col].getOID();
            return oid == 23 ? ByteConverter.int4(this.this_row[col], 0) : (int)this.readLongValue(this.this_row[col], oid, -2147483648L, 2147483647L, "INT");
        } else {
            Encoding encoding = this.connection.getEncoding();
            if (encoding.hasAsciiNumbers()) {
                try {
                    return this.getFastInt(columnIndex);
                } catch (NumberFormatException var4) {
                    ;
                }
            }

            return toInt(this.getFixedString(columnIndex));
        }
    }

    public long getLong(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getLong columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0L;
        } else if (this.isBinary(columnIndex)) {
            int col = columnIndex - 1;
            int oid = this.fields[col].getOID();
            return oid == 20 ? ByteConverter.int8(this.this_row[col], 0) : this.readLongValue(this.this_row[col], oid, -9223372036854775808L, 9223372036854775807L, "long");
        } else {
            Encoding encoding = this.connection.getEncoding();
            if (encoding.hasAsciiNumbers()) {
                try {
                    return this.getFastLong(columnIndex);
                } catch (NumberFormatException var4) {
                    ;
                }
            }

            return toLong(this.getFixedString(columnIndex));
        }
    }

    private long getFastLong(int columnIndex) throws SQLException, NumberFormatException {
        byte[] bytes = this.this_row[columnIndex - 1];
        if (bytes.length == 0) {
            throw FAST_NUMBER_FAILED;
        } else {
            long val = 0L;
            int start;
            boolean neg;
            if (bytes[0] == 45) {
                neg = true;
                start = 1;
                if (bytes.length == 1 || bytes.length > 19) {
                    throw FAST_NUMBER_FAILED;
                }
            } else {
                start = 0;
                neg = false;
                if (bytes.length > 18) {
                    throw FAST_NUMBER_FAILED;
                }
            }

            while(start < bytes.length) {
                byte b = bytes[start++];
                if (b < 48 || b > 57) {
                    throw FAST_NUMBER_FAILED;
                }

                val *= 10L;
                val += (long)(b - 48);
            }

            if (neg) {
                val = -val;
            }

            return val;
        }
    }

    private int getFastInt(int columnIndex) throws SQLException, NumberFormatException {
        byte[] bytes = this.this_row[columnIndex - 1];
        if (bytes.length == 0) {
            throw FAST_NUMBER_FAILED;
        } else {
            int val = 0;
            int start;
            boolean neg;
            if (bytes[0] == 45) {
                neg = true;
                start = 1;
                if (bytes.length == 1 || bytes.length > 10) {
                    throw FAST_NUMBER_FAILED;
                }
            } else {
                start = 0;
                neg = false;
                if (bytes.length > 9) {
                    throw FAST_NUMBER_FAILED;
                }
            }

            while(start < bytes.length) {
                byte b = bytes[start++];
                if (b < 48 || b > 57) {
                    throw FAST_NUMBER_FAILED;
                }

                val *= 10;
                val += b - 48;
            }

            if (neg) {
                val = -val;
            }

            return val;
        }
    }

    private BigDecimal getFastBigDecimal(int columnIndex) throws SQLException, NumberFormatException {
        byte[] bytes = this.this_row[columnIndex - 1];
        if (bytes.length == 0) {
            throw FAST_NUMBER_FAILED;
        } else {
            int scale = 0;
            long val = 0L;
            int start;
            boolean neg;
            if (bytes[0] == 45) {
                neg = true;
                start = 1;
                if (bytes.length == 1 || bytes.length > 19) {
                    throw FAST_NUMBER_FAILED;
                }
            } else {
                start = 0;
                neg = false;
                if (bytes.length > 18) {
                    throw FAST_NUMBER_FAILED;
                }
            }

            int periodsSeen = 0;

            while(true) {
                while(start < bytes.length) {
                    byte b = bytes[start++];
                    if (b >= 48 && b <= 57) {
                        val *= 10L;
                        val += (long)(b - 48);
                    } else {
                        if (b != 46) {
                            throw FAST_NUMBER_FAILED;
                        }

                        scale = bytes.length - start;
                        ++periodsSeen;
                    }
                }

                int numNonSignChars = neg ? bytes.length - 1 : bytes.length;
                if (periodsSeen <= 1 && periodsSeen != numNonSignChars) {
                    if (neg) {
                        val = -val;
                    }

                    return BigDecimal.valueOf(val, scale);
                }

                throw FAST_NUMBER_FAILED;
            }
        }
    }

    public float getFloat(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getFloat columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0.0F;
        } else if (this.isBinary(columnIndex)) {
            int col = columnIndex - 1;
            int oid = this.fields[col].getOID();
            return oid == 700 ? ByteConverter.float4(this.this_row[col], 0) : (float)this.readDoubleValue(this.this_row[col], oid, "FLOAT");
        } else {
            return toFloat(this.getFixedString(columnIndex));
        }
    }

    public double getDouble(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getDouble columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return 0.0D;
        } else if (this.isBinary(columnIndex)) {
            int col = columnIndex - 1;
            int oid = this.fields[col].getOID();
            return oid == 701 ? ByteConverter.float8(this.this_row[col], 0) : this.readDoubleValue(this.this_row[col], oid, "double");
        } else {
            return toDouble(this.getFixedString(columnIndex));
        }
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        LOGGER.log(Level.FINEST, "  getBigDecimal columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        } else {
            if (this.isBinary(columnIndex)) {
                int sqlType = this.getSQLType(columnIndex);
                if (sqlType != 2 && sqlType != 3) {
                    Object obj = this.internalGetObject(columnIndex, this.fields[columnIndex - 1]);
                    if (obj == null) {
                        return null;
                    }

                    if (!(obj instanceof Long) && !(obj instanceof Integer) && !(obj instanceof Byte)) {
                        return this.toBigDecimal(this.trimMoney(String.valueOf(obj)), scale);
                    }

                    BigDecimal res = BigDecimal.valueOf(((Number)obj).longValue());
                    res = this.scaleBigDecimal(res, scale);
                    return res;
                }
            }

            Encoding encoding = this.connection.getEncoding();
            if (encoding.hasAsciiNumbers()) {
                try {
                    BigDecimal res = this.getFastBigDecimal(columnIndex);
                    res = this.scaleBigDecimal(res, scale);
                    return res;
                } catch (NumberFormatException var6) {
                    ;
                }
            }

            return this.toBigDecimal(this.getFixedString(columnIndex), scale);
        }
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getBytes columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        } else if (this.isBinary(columnIndex)) {
            return this.this_row[columnIndex - 1];
        } else {
            return this.fields[columnIndex - 1].getOID() != 17 && this.fields[columnIndex - 1].getOID() != 11659 ? this.trimBytes(columnIndex, this.this_row[columnIndex - 1]) : this.trimBytes(columnIndex, KBbytea.toBytes(this.this_row[columnIndex - 1]));
        }
    }

    public Date getDate(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getDate columnIndex: {0}", columnIndex);
        return this.getDate(columnIndex, (Calendar)null);
    }

    public Time getTime(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getTime columnIndex: {0}", columnIndex);
        return this.getTime(columnIndex, (Calendar)null);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getTimestamp columnIndex: {0}", columnIndex);
        return this.getTimestamp(columnIndex, (Calendar)null);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getAsciiStream columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        } else {
            try {
                return new ByteArrayInputStream(this.getString(columnIndex).getBytes("ASCII"));
            } catch (UnsupportedEncodingException var3) {
                throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[]{"ASCII"}), KSQLState.UNEXPECTED_ERROR, var3);
            }
        }
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getUnicodeStream columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        } else {
            try {
                return new ByteArrayInputStream(this.getString(columnIndex).getBytes("UTF-8"));
            } catch (UnsupportedEncodingException var3) {
                throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[]{"UTF-8"}), KSQLState.UNEXPECTED_ERROR, var3);
            }
        }
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getBinaryStream columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        } else {
            byte[] b = this.getBytes(columnIndex);
            return b != null ? new ByteArrayInputStream(b) : null;
        }
    }

    public String getString(String columnName) throws SQLException {
        return this.getString(this.findColumn(columnName));
    }

    public boolean getBoolean(String columnName) throws SQLException {
        return this.getBoolean(this.findColumn(columnName));
    }

    public byte getByte(String columnName) throws SQLException {
        return this.getByte(this.findColumn(columnName));
    }

    public short getShort(String columnName) throws SQLException {
        return this.getShort(this.findColumn(columnName));
    }

    public int getInt(String columnName) throws SQLException {
        return this.getInt(this.findColumn(columnName));
    }

    public long getLong(String columnName) throws SQLException {
        return this.getLong(this.findColumn(columnName));
    }

    public float getFloat(String columnName) throws SQLException {
        return this.getFloat(this.findColumn(columnName));
    }

    public double getDouble(String columnName) throws SQLException {
        return this.getDouble(this.findColumn(columnName));
    }

    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        return this.getBigDecimal(this.findColumn(columnName), scale);
    }

    public byte[] getBytes(String columnName) throws SQLException {
        return this.getBytes(this.findColumn(columnName));
    }

    public Date getDate(String columnName) throws SQLException {
        return this.getDate(this.findColumn(columnName), (Calendar)null);
    }

    public Time getTime(String columnName) throws SQLException {
        return this.getTime(this.findColumn(columnName), (Calendar)null);
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        return this.getTimestamp(this.findColumn(columnName), (Calendar)null);
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        return this.getAsciiStream(this.findColumn(columnName));
    }

    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return this.getUnicodeStream(this.findColumn(columnName));
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        return this.getBinaryStream(this.findColumn(columnName));
    }

    public SQLWarning getWarnings() throws SQLException {
        this.checkClosed();
        return this.warnings;
    }

    public void clearWarnings() throws SQLException {
        this.checkClosed();
        this.warnings = null;
    }

    protected void addWarning(SQLWarning warnings) {
        if (this.warnings != null) {
            this.warnings.setNextWarning(warnings);
        } else {
            this.warnings = warnings;
        }

    }

    public String getCursorName() throws SQLException {
        this.checkClosed();
        return null;
    }

    public Object getObject(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getObject columnIndex: {0}", columnIndex);
        this.checkResultSet(columnIndex);
        if (this.wasNullFlag) {
            return null;
        } else {
            Field field = this.fields[columnIndex - 1];
            if (field == null) {
                this.wasNullFlag = true;
                return null;
            } else {
                Object result = this.internalGetObject(columnIndex, field);
                if (result != null) {
                    return result;
                } else {
                    return this.isBinary(columnIndex) ? this.connection.getObject(this.getKBType(columnIndex), (String)null, this.this_row[columnIndex - 1]) : this.connection.getObject(this.getKBType(columnIndex), this.getString(columnIndex), (byte[])null);
                }
            }
        }
    }

    public Object getObject(String columnName) throws SQLException {
        return this.getObject(this.findColumn(columnName));
    }

    public int findColumn(String columnName) throws SQLException {
        this.checkClosed();
        int col = this.findColumnIndex(columnName);
        if (col == 0) {
            throw new KSQLException(GT.tr("The column name {0} was not found in this ResultSet.", new Object[]{columnName}), KSQLState.UNDEFINED_COLUMN);
        } else {
            return col;
        }
    }

    public static Map<String, Integer> createColumnNameIndexMap(Field[] fields, boolean isSanitiserDisabled) {
        Map<String, Integer> columnNameIndexMap = new HashMap(fields.length * 2);

        for(int i = fields.length - 1; i >= 0; --i) {
            String columnLabel = fields[i].getColumnLabel();
            if (isSanitiserDisabled) {
                columnNameIndexMap.put(columnLabel, i + 1);
            } else {
                columnNameIndexMap.put(columnLabel.toUpperCase(Locale.US), i + 1);
            }
        }

        return columnNameIndexMap;
    }

    private int findColumnIndex(String columnName) {
        if (this.columnNameIndexMap == null) {
            if (this.originalQuery != null) {
                this.columnNameIndexMap = this.originalQuery.getResultSetColumnNameIndexMap();
            }

            if (this.columnNameIndexMap == null) {
                this.columnNameIndexMap = createColumnNameIndexMap(this.fields, this.connection.isColumnSanitiserDisabled());
            }
        }

        Integer index = (Integer)this.columnNameIndexMap.get(columnName);
        if (index != null) {
            return index;
        } else {
            index = (Integer)this.columnNameIndexMap.get(columnName.toLowerCase(Locale.US));
            if (index != null) {
                this.columnNameIndexMap.put(columnName, index);
                return index;
            } else {
                index = (Integer)this.columnNameIndexMap.get(columnName.toUpperCase(Locale.US));
                if (index != null) {
                    this.columnNameIndexMap.put(columnName, index);
                    return index;
                } else {
                    return 0;
                }
            }
        }
    }

    public int getColumnOID(int field) {
        return this.fields[field - 1].getOID();
    }

    public String getFixedString(int col) throws SQLException {
        return this.trimMoney(this.getString(col));
    }

    private String trimMoney(String s) {
        if (s == null) {
            return null;
        } else if (s.length() < 2) {
            return s;
        } else {
            char ch = s.charAt(0);
            if (ch > '-') {
                return s;
            } else {
                if (ch == '(') {
                    s = "-" + KBtokenizer.removePara(s).substring(1);
                } else if (ch == '$') {
                    s = s.substring(1);
                } else if (ch == '-' && s.charAt(1) == '$') {
                    s = "-" + s.substring(2);
                }

                return s;
            }
        }
    }

    protected String getKBType(int column) throws SQLException {
        Field field = this.fields[column - 1];
        this.initSqlType(field);
        return field.getKBType();
    }

    protected int getSQLType(int column) throws SQLException {
        Field field = this.fields[column - 1];
        this.initSqlType(field);
        return field.getSQLType();
    }

    private void initSqlType(Field field) throws SQLException {
        if (!field.isTypeInitialized()) {
            TypeInfo typeInfo = this.connection.getTypeInfo();
            int oid = field.getOID();
            String pgType = typeInfo.getKBType(oid);
            int sqlType = typeInfo.getSQLType(pgType);
            field.setSQLType(sqlType);
            field.setKBType(pgType);
        }

    }

    private void checkUpdateable() throws SQLException {
        this.checkClosed();
        if (!this.isUpdateable()) {
            throw new KSQLException(GT.tr("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            if (this.updateValues == null) {
                this.updateValues = new HashMap((int)((double)this.fields.length / 0.75D), 0.75F);
            }

        }
    }

    protected void checkClosed() throws SQLException {
        if (this.rows == null) {
            throw new KSQLException(GT.tr("This ResultSet is closed.", new Object[0]), KSQLState.OBJECT_NOT_IN_STATE);
        }
    }

    protected boolean isResultSetClosed() {
        return this.rows == null;
    }

    protected void checkColumnIndex(int column) throws SQLException {
        if (column < 1 || column > this.fields.length) {
            throw new KSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[]{column, this.fields.length}), KSQLState.INVALID_PARAMETER_VALUE);
        }
    }

    protected void checkResultSet(int column) throws SQLException {
        this.checkClosed();
        if (this.this_row == null) {
            throw new KSQLException(GT.tr("ResultSet not positioned properly, perhaps you need to call next.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            this.checkColumnIndex(column);
            this.wasNullFlag = this.this_row[column - 1] == null;
        }
    }

    protected boolean isBinary(int column) {
        return this.fields[column - 1].getFormat() == 1;
    }

    public static int toInt(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException var7) {
                try {
                    BigDecimal n = new BigDecimal(s);
                    BigInteger i = n.toBigInteger();
                    int gt = i.compareTo(INTMAX);
                    int lt = i.compareTo(INTMIN);
                    if (gt <= 0 && lt >= 0) {
                        return i.intValue();
                    } else {
                        throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"INT", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                    }
                } catch (NumberFormatException var6) {
                    throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"INT", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                }
            }
        } else {
            return 0;
        }
    }

    public static long toLong(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Long.parseLong(s);
            } catch (NumberFormatException var7) {
                try {
                    BigDecimal n = new BigDecimal(s);
                    BigInteger i = n.toBigInteger();
                    int gt = i.compareTo(LONGMAX);
                    int lt = i.compareTo(LONGMIN);
                    if (gt <= 0 && lt >= 0) {
                        return i.longValue();
                    } else {
                        throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"long", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                    }
                } catch (NumberFormatException var6) {
                    throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"long", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
                }
            }
        } else {
            return 0L;
        }
    }

    public static BigDecimal toBigDecimal(String s) throws SQLException {
        if (s == null) {
            return null;
        } else {
            try {
                s = s.trim();
                return new BigDecimal(s);
            } catch (NumberFormatException var2) {
                throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"BigDecimal", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
        }
    }

    public BigDecimal toBigDecimal(String s, int scale) throws SQLException {
        if (s == null) {
            return null;
        } else {
            BigDecimal val = toBigDecimal(s);
            return this.scaleBigDecimal(val, scale);
        }
    }

    private BigDecimal scaleBigDecimal(BigDecimal val, int scale) throws KSQLException {
        if (scale == -1) {
            return val;
        } else {
            try {
                return val.setScale(scale);
            } catch (ArithmeticException var4) {
                throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"BigDecimal", val}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
        }
    }

    public static float toFloat(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Float.parseFloat(s);
            } catch (NumberFormatException var2) {
                throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"FLOAT", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
        } else {
            return 0.0F;
        }
    }

    public static double toDouble(String s) throws SQLException {
        if (s != null) {
            try {
                s = s.trim();
                return Double.parseDouble(s);
            } catch (NumberFormatException var2) {
                throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{"double", s}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
            }
        } else {
            return 0.0D;
        }
    }

    private void initRowBuffer() {
        this.this_row = (byte[][])((byte[][])this.rows.get(this.current_row));
        if (this.resultsetconcurrency == 1008) {
            this.rowBuffer = new byte[this.this_row.length][];
            System.arraycopy(this.this_row, 0, this.rowBuffer, 0, this.this_row.length);
        } else {
            this.rowBuffer = (byte[][])((byte[][])null);
        }

    }

    private boolean isColumnTrimmable(int columnIndex) throws SQLException {
        switch(this.getSQLType(columnIndex)) {
            case -4:
            case -3:
            case -2:
            case -1:
            case 1:
            case 12:
                return true;
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            default:
                return false;
        }
    }

    private byte[] trimBytes(int p_columnIndex, byte[] p_bytes) throws SQLException {
        if (this.maxFieldSize > 0 && p_bytes.length > this.maxFieldSize && this.isColumnTrimmable(p_columnIndex)) {
            byte[] l_bytes = new byte[this.maxFieldSize];
            System.arraycopy(p_bytes, 0, l_bytes, 0, this.maxFieldSize);
            return l_bytes;
        } else {
            return p_bytes;
        }
    }

    private String trimString(int p_columnIndex, String p_string) throws SQLException {
        return this.maxFieldSize > 0 && p_string.length() > this.maxFieldSize && this.isColumnTrimmable(p_columnIndex) ? p_string.substring(0, this.maxFieldSize) : p_string;
    }

    private double readDoubleValue(byte[] bytes, int oid, String targetType) throws KSQLException {
        switch(oid) {
            case 9:
                return (double)ByteConverter.int1(bytes, 0);
            case 20:
                return (double)ByteConverter.int8(bytes, 0);
            case 21:
                return (double)ByteConverter.int2(bytes, 0);
            case 23:
                return (double)ByteConverter.int4(bytes, 0);
            case 700:
                return (double)ByteConverter.float4(bytes, 0);
            case 701:
                return ByteConverter.float8(bytes, 0);
            default:
                throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[]{Oid.toString(oid), targetType}), KSQLState.DATA_TYPE_MISMATCH);
        }
    }

    private long readLongValue(byte[] bytes, int oid, long minVal, long maxVal, String targetType) throws KSQLException {
        long val;
        switch(oid) {
            case 9:
                val = (long)ByteConverter.int1(bytes, 0);
                break;
            case 20:
                val = ByteConverter.int8(bytes, 0);
                break;
            case 21:
                val = (long)ByteConverter.int2(bytes, 0);
                break;
            case 23:
                val = (long)ByteConverter.int4(bytes, 0);
                break;
            case 700:
                val = (long)ByteConverter.float4(bytes, 0);
                break;
            case 701:
                val = (long)ByteConverter.float8(bytes, 0);
                break;
            default:
                throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[]{Oid.toString(oid), targetType}), KSQLState.DATA_TYPE_MISMATCH);
        }

        if (val >= minVal && val <= maxVal) {
            return val;
        } else {
            throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[]{targetType, val}), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
        }
    }

    protected void updateValue(int columnIndex, Object value) throws SQLException {
        this.checkUpdateable();
        if (!this.onInsertRow && (this.isBeforeFirst() || this.isAfterLast() || this.rows.isEmpty())) {
            throw new KSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
        } else {
            this.checkColumnIndex(columnIndex);
            this.doingUpdates = !this.onInsertRow;
            if (value == null) {
                this.updateNull(columnIndex);
            } else {
                KBResultSetMetaData md = (KBResultSetMetaData)this.getMetaData();
                this.updateValues.put(md.getBaseColumnName(columnIndex), value);
            }

        }
    }

    protected Object getUUID(String data) throws SQLException {
        try {
            UUID uuid = UUID.fromString(data);
            return uuid;
        } catch (IllegalArgumentException var3) {
            throw new KSQLException(GT.tr("Invalid UUID data.", new Object[0]), KSQLState.INVALID_PARAMETER_VALUE, var3);
        }
    }

    protected Object getUUID(byte[] data) throws SQLException {
        return new UUID(ByteConverter.int8(data, 0), ByteConverter.int8(data, 8));
    }

    void addRows(List<byte[][]> tuples) {
        this.rows.addAll(tuples);
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateRef(int,Ref)");
    }

    public void updateRef(String columnName, Ref x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateRef(String,Ref)");
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(int,Blob)");
    }

    public void updateBlob(String columnName, Blob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(String,Blob)");
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(int,Clob)");
    }

    public void updateClob(String columnName, Clob x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(String,Clob)");
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        this.updateObject(columnIndex, x);
    }

    public void updateArray(String columnName, Array x) throws SQLException {
        this.updateArray(this.findColumn(columnName), x);
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        if (type == null) {
            throw new SQLException("type is null");
        } else {
            int sqlType = this.getSQLType(columnIndex);
            if (type == BigDecimal.class) {
                if (sqlType != 2 && sqlType != 3) {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                } else {
                    return type.cast(this.getBigDecimal(columnIndex));
                }
            } else if (type == String.class) {
                if (sqlType != 1 && sqlType != 12) {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                } else {
                    return type.cast(this.getString(columnIndex));
                }
            } else if (type == Boolean.class) {
                if (sqlType != 16 && sqlType != -7) {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                } else {
                    boolean booleanValue = this.getBoolean(columnIndex);
                    return this.wasNull() ? null : type.cast(booleanValue);
                }
            } else if (type == Integer.class) {
                if (sqlType != -6 && sqlType != 5 && sqlType != 4) {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                } else {
                    int intValue = this.getInt(columnIndex);
                    return this.wasNull() ? null : type.cast(intValue);
                }
            } else if (type == Long.class) {
                if (sqlType == -5) {
                    long longValue = this.getLong(columnIndex);
                    return this.wasNull() ? null : type.cast(longValue);
                } else {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                }
            } else if (type == Float.class) {
                if (sqlType == 7) {
                    float floatValue = this.getFloat(columnIndex);
                    return this.wasNull() ? null : type.cast(floatValue);
                } else {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                }
            } else if (type == Double.class) {
                if (sqlType != 6 && sqlType != 8) {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                } else {
                    double doubleValue = this.getDouble(columnIndex);
                    return this.wasNull() ? null : type.cast(doubleValue);
                }
            } else if (type == Date.class) {
                if (sqlType == 91) {
                    return type.cast(this.getDate(columnIndex));
                } else {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                }
            } else if (type == Time.class) {
                if (sqlType == 92) {
                    return type.cast(this.getTime(columnIndex));
                } else {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                }
            } else if (type == Timestamp.class) {
                if (sqlType != 93 && sqlType != 2014) {
                    throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                } else {
                    return type.cast(this.getTimestamp(columnIndex));
                }
            } else {
                Timestamp timestampValue;
                if (type == Calendar.class) {
                    if (sqlType != 93 && sqlType != 2014) {
                        throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                    } else {
                        timestampValue = this.getTimestamp(columnIndex);
                        Calendar calendar = Calendar.getInstance(this.getDefaultCalendar().getTimeZone());
                        calendar.setTimeInMillis(timestampValue.getTime());
                        return type.cast(calendar);
                    }
                } else if (type == Blob.class) {
                    if (sqlType != 2004 && sqlType != -2) {
                        throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                    } else {
                        return type.cast(this.getBlob(columnIndex));
                    }
                } else if (type == Clob.class) {
                    if (sqlType != 2005 && sqlType != 12) {
                        throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                    } else {
                        return type.cast(this.getClob(columnIndex));
                    }
                } else if (type == NClob.class) {
                    if (sqlType == 2011) {
                        return type.cast(this.getNClob(columnIndex));
                    } else {
                        throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                    }
                } else if (type == Array.class) {
                    if (sqlType == 2003) {
                        return type.cast(this.getArray(columnIndex));
                    } else {
                        throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                    }
                } else if (type == SQLXML.class) {
                    if (sqlType == 2009) {
                        return type.cast(this.getSQLXML(columnIndex));
                    } else {
                        throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                    }
                } else if (type == UUID.class) {
                    return type.cast(this.getObject(columnIndex));
                } else {
                    Object object;
                    if (type == InetAddress.class) {
                        object = this.getObject(columnIndex);
                        if (object == null) {
                            return null;
                        } else {
                            try {
                                return type.cast(InetAddress.getByName(((KBobject)object).getValue()));
                            } catch (UnknownHostException var7) {
                                throw new SQLException("could not create inet address from string '" + object + "'");
                            }
                        }
                    } else if (type == LocalDate.class) {
                        if (sqlType == 91) {
                            Date dateValue = this.getDate(columnIndex);
                            return this.wasNull() ? null : type.cast(dateValue.toLocalDate());
                        } else {
                            throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                        }
                    } else if (type == LocalTime.class) {
                        if (sqlType == 92) {
                            return type.cast(this.getLocalTime(columnIndex));
                        } else {
                            throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                        }
                    } else if (type == LocalDateTime.class) {
                        if (sqlType == 93) {
                            return type.cast(this.getLocalDateTime(columnIndex));
                        } else {
                            throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                        }
                    } else if (type == OffsetDateTime.class) {
                        if (sqlType != 2014 && sqlType != 93) {
                            throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
                        } else {
                            timestampValue = this.getTimestamp(columnIndex);
                            if (this.wasNull()) {
                                return null;
                            } else {
                                OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(timestampValue.toInstant(), ZoneOffset.UTC);
                                return type.cast(offsetDateTime);
                            }
                        }
                    } else if (KBobject.class.isAssignableFrom(type)) {
                        if (this.isBinary(columnIndex)) {
                            object = this.connection.getObject(this.getKBType(columnIndex), (String)null, this.this_row[columnIndex - 1]);
                        } else {
                            object = this.connection.getObject(this.getKBType(columnIndex), this.getString(columnIndex), (byte[])null);
                        }

                        return type.cast(object);
                    } else {
                        throw new SQLException("unsupported conversion to " + type);
                    }
                }
            }
        }
    }

    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return this.getObject(this.findColumn(columnLabel), type);
    }

    public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(s, map);
    }

    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return this.getObjectImpl(i, map);
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateObject");
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateObject");
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateObject");
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateObject");
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getRowId columnIndex: {0}", columnIndex);
        throw Driver.notImplemented(this.getClass(), "getRowId(int)");
    }

    public RowId getRowId(String columnName) throws SQLException {
        return this.getRowId(this.findColumn(columnName));
    }

    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateRowId(int, RowId)");
    }

    public void updateRowId(String columnName, RowId x) throws SQLException {
        this.updateRowId(this.findColumn(columnName), x);
    }

    public int getHoldability() throws SQLException {
        throw Driver.notImplemented(this.getClass(), "getHoldability()");
    }

    public boolean isClosed() throws SQLException {
        return this.rows == null;
    }

    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNString(int, String)");
    }

    public void updateNString(String columnName, String nString) throws SQLException {
        this.updateNString(this.findColumn(columnName), nString);
    }

    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNClob(int, NClob)");
    }

    public void updateNClob(String columnName, NClob nClob) throws SQLException {
        this.updateNClob(this.findColumn(columnName), nClob);
    }

    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNClob(int, Reader)");
    }

    public void updateNClob(String columnName, Reader reader) throws SQLException {
        this.updateNClob(this.findColumn(columnName), reader);
    }

    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNClob(int, Reader, long)");
    }

    public void updateNClob(String columnName, Reader reader, long length) throws SQLException {
        this.updateNClob(this.findColumn(columnName), reader, length);
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getNClob columnIndex: {0}", columnIndex);
        throw Driver.notImplemented(this.getClass(), "getNClob(int)");
    }

    public NClob getNClob(String columnName) throws SQLException {
        return this.getNClob(this.findColumn(columnName));
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(int, InputStream, long)");
    }

    public void updateBlob(String columnName, InputStream inputStream, long length) throws SQLException {
        this.updateBlob(this.findColumn(columnName), inputStream, length);
    }

    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBlob(int, InputStream)");
    }

    public void updateBlob(String columnName, InputStream inputStream) throws SQLException {
        this.updateBlob(this.findColumn(columnName), inputStream);
    }

    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(int, Reader, long)");
    }

    public void updateClob(String columnName, Reader reader, long length) throws SQLException {
        this.updateClob(this.findColumn(columnName), reader, length);
    }

    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateClob(int, Reader)");
    }

    public void updateClob(String columnName, Reader reader) throws SQLException {
        this.updateClob(this.findColumn(columnName), reader);
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getSQLXML columnIndex: {0}", columnIndex);
        String data = this.getString(columnIndex);
        return data == null ? null : new KbSQLXML(this.connection, data);
    }

    public SQLXML getSQLXML(String columnName) throws SQLException {
        return this.getSQLXML(this.findColumn(columnName));
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        this.updateValue(columnIndex, xmlObject);
    }

    public void updateSQLXML(String columnName, SQLXML xmlObject) throws SQLException {
        this.updateSQLXML(this.findColumn(columnName), xmlObject);
    }

    public String getNString(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getNString columnIndex: {0}", columnIndex);
        throw Driver.notImplemented(this.getClass(), "getNString(int)");
    }

    public String getNString(String columnName) throws SQLException {
        return this.getNString(this.findColumn(columnName));
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        LOGGER.log(Level.FINEST, "  getNCharacterStream columnIndex: {0}", columnIndex);
        throw Driver.notImplemented(this.getClass(), "getNCharacterStream(int)");
    }

    public Reader getNCharacterStream(String columnName) throws SQLException {
        return this.getNCharacterStream(this.findColumn(columnName));
    }

    public void updateNCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader, int)");
    }

    public void updateNCharacterStream(String columnName, Reader x, int length) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnName), x, length);
    }

    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader)");
    }

    public void updateNCharacterStream(String columnName, Reader x) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnName), x);
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateNCharacterStream(int, Reader, long)");
    }

    public void updateNCharacterStream(String columnName, Reader x, long length) throws SQLException {
        this.updateNCharacterStream(this.findColumn(columnName), x, length);
    }

    public void updateCharacterStream(int columnIndex, Reader reader, long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateCharaceterStream(int, Reader, long)");
    }

    public void updateCharacterStream(String columnName, Reader reader, long length) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnName), reader, length);
    }

    public void updateCharacterStream(int columnIndex, Reader reader) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateCharaceterStream(int, Reader)");
    }

    public void updateCharacterStream(String columnName, Reader reader) throws SQLException {
        this.updateCharacterStream(this.findColumn(columnName), reader);
    }

    public void updateBinaryStream(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBinaryStream(int, InputStream, long)");
    }

    public void updateBinaryStream(String columnName, InputStream inputStream, long length) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnName), inputStream, length);
    }

    public void updateBinaryStream(int columnIndex, InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateBinaryStream(int, InputStream)");
    }

    public void updateBinaryStream(String columnName, InputStream inputStream) throws SQLException {
        this.updateBinaryStream(this.findColumn(columnName), inputStream);
    }

    public void updateAsciiStream(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateAsciiStream(int, InputStream, long)");
    }

    public void updateAsciiStream(String columnName, InputStream inputStream, long length) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnName), inputStream, length);
    }

    public void updateAsciiStream(int columnIndex, InputStream inputStream) throws SQLException {
        throw Driver.notImplemented(this.getClass(), "updateAsciiStream(int, InputStream)");
    }

    public void updateAsciiStream(String columnName, InputStream inputStream) throws SQLException {
        this.updateAsciiStream(this.findColumn(columnName), inputStream);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isAssignableFrom(this.getClass());
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(this.getClass())) {
            return iface.cast(this);
        } else {
            throw new SQLException("Cannot unwrap to " + iface.getName());
        }
    }

    private Calendar getDefaultCalendar() {
        TimestampUtils timestampUtils = this.connection.getTimestampUtils();
        if (timestampUtils.hasFastDefaultTimeZone()) {
            return timestampUtils.getSharedCalendar((TimeZone)null);
        } else {
            Calendar sharedCalendar = timestampUtils.getSharedCalendar(this.defaultTimeZone);
            if (this.defaultTimeZone == null) {
                this.defaultTimeZone = sharedCalendar.getTimeZone();
            }

            return sharedCalendar;
        }
    }

    public int getTupleCount() {
        return this.rows.size();
    }

    public class CursorResultHandler extends ResultHandlerBase {
        public CursorResultHandler() {
        }

        public void handleResultRows(Query fromQuery, Field[] fields, List<byte[][]> tuples, ResultCursor cursor) {
            KbResultSet.this.rows = tuples;
            KbResultSet.this.cursor = cursor;
        }

        public void handleCommandStatus(String status, int updateCount, long insertOID) {
            this.handleError(new KSQLException(GT.tr("Unexpected command status: {0}.", new Object[]{status}), KSQLState.PROTOCOL_VIOLATION));
        }

        public void handleCompletion() throws SQLException {
            SQLWarning warning = this.getWarning();
            if (warning != null) {
                KbResultSet.this.addWarning(warning);
            }

            super.handleCompletion();
        }
    }

    private class PrimaryKey {
        int index;
        String name;

        PrimaryKey(int index, String name) {
            this.index = index;
            this.name = name;
        }

        Object getValue() throws SQLException {
            return KbResultSet.this.getObject(this.index);
        }
    }

    static class NullObject extends KBobject {
        NullObject(String type) {
            this.setType(type);
        }

        public String getValue() {
            return null;
        }
    }
}
