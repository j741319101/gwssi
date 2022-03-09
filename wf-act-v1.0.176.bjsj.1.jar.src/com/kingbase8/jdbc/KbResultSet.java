/*      */ package com.kingbase8.jdbc;
/*      */ import com.kingbase8.Driver;
/*      */ import com.kingbase8.core.Field;
/*      */ import com.kingbase8.util.ByteConverter;
/*      */ import com.kingbase8.util.GT;
/*      */ import com.kingbase8.util.KSQLException;
/*      */ import com.kingbase8.util.KSQLState;
/*      */ import com.kingbase8.util.LOGGER;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.sql.Date;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.logging.Level;
/*      */ 
/*      */ public class KbResultSet implements ResultSet, KBRefCursorResultSet {
/*      */   private boolean updateable = false;
/*   24 */   private HashMap<String, Object> updateValues = null; private boolean doingUpdates = false;
/*      */   private boolean usingOID = false;
/*      */   private List<PrimaryKey> primaryKeys;
/*      */   private boolean singleTable = false;
/*   28 */   private String onlyTable = "";
/*   29 */   private String tableName = null;
/*   30 */   private PreparedStatement updateStatement = null;
/*   31 */   private PreparedStatement insertStatement = null;
/*   32 */   private PreparedStatement deleteStatement = null;
/*   33 */   private PreparedStatement selectStatement = null;
/*      */   private final int resultsettype;
/*      */   private final int resultsetconcurrency;
/*   36 */   private int fetchdirection = 1002;
/*      */   private TimeZone defaultTimeZone;
/*      */   protected final BaseConnection connection;
/*      */   protected final BaseStatement statement;
/*      */   protected final Field[] fields;
/*      */   protected final Query originalQuery;
/*      */   protected final int maxRows;
/*      */   protected final int maxFieldSize;
/*      */   protected List<byte[][]> rows;
/*   45 */   protected int current_row = -1;
/*      */   protected int row_offset;
/*      */   protected byte[][] this_row;
/*   48 */   protected SQLWarning warnings = null;
/*      */   protected boolean wasNullFlag = false;
/*      */   protected boolean onInsertRow = false;
/*   51 */   private byte[][] rowBuffer = (byte[][])null;
/*      */   protected int fetchSize;
/*      */   protected ResultCursor cursor;
/*      */   private Map<String, Integer> columnNameIndexMap;
/*      */   private ResultSetMetaData rsMetaData;
/*      */   private String refCursorName;
/*   57 */   private static final BigInteger BYTEMAX = new BigInteger(Byte.toString(127));
/*   58 */   private static final BigInteger BYTEMIN = new BigInteger(Byte.toString(-128));
/*   59 */   private static final BigInteger SHORTMAX = new BigInteger(Short.toString('翿'));
/*   60 */   private static final BigInteger SHORTMIN = new BigInteger(Short.toString(-32768));
/*   61 */   private static final NumberFormatException FAST_NUMBER_FAILED = new NumberFormatException() {
/*      */       public synchronized Throwable fillInStackTrace() {
/*   63 */         return this;
/*      */       }
/*      */     };
/*   66 */   private static final BigInteger INTMAX = new BigInteger(Integer.toString(2147483647));
/*   67 */   private static final BigInteger INTMIN = new BigInteger(Integer.toString(-2147483648));
/*   68 */   private static final BigInteger LONGMAX = new BigInteger(Long.toString(Long.MAX_VALUE));
/*   69 */   private static final BigInteger LONGMIN = new BigInteger(Long.toString(Long.MIN_VALUE));
/*      */   
/*      */   protected ResultSetMetaData createMetaData() throws SQLException {
/*   72 */     return (ResultSetMetaData)new KbResultSetMetaData(this.connection, this.fields);
/*      */   }
/*      */   
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/*   76 */     checkClosed();
/*   77 */     if (this.rsMetaData == null) {
/*   78 */       this.rsMetaData = createMetaData();
/*      */     }
/*      */     
/*   81 */     return this.rsMetaData;
/*      */   }
/*      */   
/*      */   KbResultSet(Query originalQuery, BaseStatement statement, Field[] fields, List<byte[][]> tuples, ResultCursor cursor, int maxRows, int maxFieldSize, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
/*   85 */     if (tuples == null)
/*   86 */       throw new NullPointerException("tuples must be non-null"); 
/*   87 */     if (fields == null) {
/*   88 */       throw new NullPointerException("fields must be non-null");
/*      */     }
/*   90 */     this.originalQuery = originalQuery;
/*   91 */     this.connection = (BaseConnection)statement.getConnection();
/*   92 */     this.statement = statement;
/*   93 */     this.fields = fields;
/*   94 */     this.rows = tuples;
/*   95 */     this.cursor = cursor;
/*   96 */     this.maxRows = maxRows;
/*   97 */     this.maxFieldSize = maxFieldSize;
/*   98 */     this.resultsettype = rsType;
/*   99 */     this.resultsetconcurrency = rsConcurrency;
/*      */   }
/*      */ 
/*      */   
/*      */   public URL getURL(int columnIndex) throws SQLException {
/*  104 */     LOGGER.log(Level.FINEST, "  getURL columnIndex: {0}", Integer.valueOf(columnIndex));
/*  105 */     checkClosed();
/*  106 */     throw Driver.notImplemented(getClass(), "getURL(int)");
/*      */   }
/*      */   
/*      */   public URL getURL(String columnName) throws SQLException {
/*  110 */     return getURL(findColumn(columnName));
/*      */   }
/*      */   
/*      */   protected Object internalGetObject(int columnIndex, Field field) throws SQLException {
/*  114 */     switch (getSQLType(columnIndex)) {
/*      */       case -7:
/*  116 */         return (field.getMod() != 1 && field.getMod() != -1) ? getString(columnIndex) : Boolean.valueOf(getBoolean(columnIndex));
/*      */       case -6:
/*  118 */         return Byte.valueOf(getByte(columnIndex));
/*      */       case -5:
/*  120 */         return Long.valueOf(getLong(columnIndex));
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/*  124 */         return getBytes(columnIndex);
/*      */       case -1:
/*      */       case 1:
/*      */       case 12:
/*  128 */         return getString(columnIndex);
/*      */       case 2:
/*      */       case 3:
/*  131 */         return getBigDecimal(columnIndex, (field.getMod() == -1) ? -1 : (field.getMod() - 4 & 0xFFFF));
/*      */       case 4:
/*      */       case 5:
/*  134 */         return Integer.valueOf(getInt(columnIndex));
/*      */       case 6:
/*      */       case 8:
/*  137 */         return Double.valueOf(getDouble(columnIndex));
/*      */       case 7:
/*  139 */         return Float.valueOf(getFloat(columnIndex));
/*      */       case 16:
/*  141 */         return Boolean.valueOf(getBoolean(columnIndex));
/*      */       case 91:
/*  143 */         return getDate(columnIndex);
/*      */       case 92:
/*  145 */         return getTime(columnIndex);
/*      */       case 93:
/*  147 */         return getTimestamp(columnIndex, (Calendar)null);
/*      */       case 2003:
/*  149 */         return getArray(columnIndex);
/*      */       case 2004:
/*  151 */         return getBlob(columnIndex);
/*      */       case 2005:
/*  153 */         return getClob(columnIndex);
/*      */       case 2009:
/*  155 */         return getSQLXML(columnIndex);
/*      */     } 
/*  157 */     String type = getKBType(columnIndex);
/*  158 */     if (type.equalsIgnoreCase("unknown"))
/*  159 */       return getString(columnIndex); 
/*  160 */     if (type.equalsIgnoreCase("UUID")) {
/*  161 */       if (isBinary(columnIndex)) {
/*  162 */         return getUUID(this.this_row[columnIndex - 1]);
/*      */       }
/*  164 */       return getUUID(getString(columnIndex));
/*      */     } 
/*  166 */     if (type.equalsIgnoreCase("REFCURSOR")) {
/*  167 */       String cursorName = getString(columnIndex);
/*  168 */       StringBuilder sb = new StringBuilder("FETCH ALL IN ");
/*  169 */       Utils.escapeIdentifier(sb, cursorName);
/*  170 */       ResultSet rs = this.connection.execSQLQuery(sb.toString(), this.resultsettype, 1007);
/*  171 */       sb.setLength(0);
/*  172 */       sb.append("CLOSE ");
/*  173 */       Utils.escapeIdentifier(sb, cursorName);
/*  174 */       this.connection.execSQLUpdate(sb.toString());
/*  175 */       ((KbResultSet)rs).setRefCursor(cursorName);
/*  176 */       return rs;
/*  177 */     }  if ("HSTORE".equals(type)) {
/*  178 */       if (isBinary(columnIndex)) {
/*  179 */         return HStoreConverter.fromBytes(this.this_row[columnIndex - 1], this.connection.getEncoding());
/*      */       }
/*  181 */       return HStoreConverter.fromString(getString(columnIndex));
/*      */     } 
/*      */     
/*  184 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkScrollable() throws SQLException {
/*  190 */     checkClosed();
/*  191 */     if (this.resultsettype == 1003)
/*  192 */       throw new KSQLException(GT.tr("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/*      */   }
/*      */   
/*      */   public boolean absolute(int index) throws SQLException {
/*      */     int internalIndex;
/*  197 */     checkScrollable();
/*  198 */     if (index == 0) {
/*  199 */       beforeFirst();
/*  200 */       return false;
/*      */     } 
/*  202 */     int rows_size = this.rows.size();
/*      */     
/*  204 */     if (index < 0) {
/*  205 */       if (index < -rows_size) {
/*  206 */         beforeFirst();
/*  207 */         return false;
/*      */       } 
/*      */       
/*  210 */       internalIndex = rows_size + index;
/*      */     } else {
/*  212 */       if (index > rows_size) {
/*  213 */         afterLast();
/*  214 */         return false;
/*      */       } 
/*      */       
/*  217 */       internalIndex = index - 1;
/*      */     } 
/*      */     
/*  220 */     this.current_row = internalIndex;
/*  221 */     initRowBuffer();
/*  222 */     this.onInsertRow = false;
/*  223 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void afterLast() throws SQLException {
/*  228 */     checkScrollable();
/*  229 */     int rows_size = this.rows.size();
/*  230 */     if (rows_size > 0) {
/*  231 */       this.current_row = rows_size;
/*      */     }
/*      */     
/*  234 */     this.onInsertRow = false;
/*  235 */     this.this_row = (byte[][])null;
/*  236 */     this.rowBuffer = (byte[][])null;
/*      */   }
/*      */   
/*      */   public void beforeFirst() throws SQLException {
/*  240 */     checkScrollable();
/*  241 */     if (!this.rows.isEmpty()) {
/*  242 */       this.current_row = -1;
/*      */     }
/*      */     
/*  245 */     this.onInsertRow = false;
/*  246 */     this.this_row = (byte[][])null;
/*  247 */     this.rowBuffer = (byte[][])null;
/*      */   }
/*      */   
/*      */   public boolean first() throws SQLException {
/*  251 */     checkScrollable();
/*  252 */     if (this.rows.size() <= 0) {
/*  253 */       return false;
/*      */     }
/*  255 */     this.current_row = 0;
/*  256 */     initRowBuffer();
/*  257 */     this.onInsertRow = false;
/*  258 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Array getArray(String colName) throws SQLException {
/*  263 */     return getArray(findColumn(colName));
/*      */   }
/*      */   
/*      */   protected Array makeArray(int oid, byte[] value) throws SQLException {
/*  267 */     return (Array)new KbArray(this.connection, oid, value);
/*      */   }
/*      */   
/*      */   protected Array makeArray(int oid, String value) throws SQLException {
/*  271 */     return (Array)new KbArray(this.connection, oid, value);
/*      */   }
/*      */   
/*      */   public Array getArray(int i) throws SQLException {
/*  275 */     checkResultSet(i);
/*  276 */     if (this.wasNullFlag) {
/*  277 */       return null;
/*      */     }
/*  279 */     int oid = this.fields[i - 1].getOID();
/*  280 */     return isBinary(i) ? makeArray(oid, this.this_row[i - 1]) : makeArray(oid, getFixedString(i));
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
/*  285 */     return getBigDecimal(columnIndex, -1);
/*      */   }
/*      */   
/*      */   public BigDecimal getBigDecimal(String columnName) throws SQLException {
/*  289 */     return getBigDecimal(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public Blob getBlob(String columnName) throws SQLException {
/*  293 */     return getBlob(findColumn(columnName));
/*      */   }
/*      */   
/*      */   protected Blob makeBlob(byte[] buffer, KbResultSet rs, int columnIndex) throws SQLException {
/*  297 */     return (Blob)new KbBlob(buffer, rs, columnIndex);
/*      */   }
/*      */   
/*      */   public Blob getBlob(int i) throws SQLException {
/*  301 */     checkResultSet(i);
/*  302 */     return makeBlob(getBytes(i), this, i);
/*      */   }
/*      */   
/*      */   public Reader getCharacterStream(String columnName) throws SQLException {
/*  306 */     return getCharacterStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public Reader getCharacterStream(int i) throws SQLException {
/*  310 */     checkResultSet(i);
/*  311 */     return this.wasNullFlag ? null : new CharArrayReader(getString(i).toCharArray());
/*      */   }
/*      */   
/*      */   public Clob getClob(String columnName) throws SQLException {
/*  315 */     return getClob(findColumn(columnName));
/*      */   }
/*      */   
/*      */   protected Clob makeClob(char[] buffer, KbResultSet rs, int columnIndex) throws SQLException {
/*  319 */     return (Clob)new KbClob(buffer, rs, columnIndex);
/*      */   }
/*      */   
/*      */   public Clob getClob(int i) throws SQLException {
/*  323 */     checkResultSet(i);
/*  324 */     return (getString(i) == null) ? makeClob((char[])null, this, i) : makeClob(getString(i).toCharArray(), this, i);
/*      */   }
/*      */   
/*      */   public int getConcurrency() throws SQLException {
/*  328 */     checkClosed();
/*  329 */     return this.resultsetconcurrency;
/*      */   }
/*      */   
/*      */   public Date getDate(int i, Calendar cal) throws SQLException {
/*  333 */     checkResultSet(i);
/*  334 */     if (this.wasNullFlag) {
/*  335 */       return null;
/*      */     }
/*  337 */     if (cal == null) {
/*  338 */       cal = getDefaultCalendar();
/*      */     }
/*      */     
/*  341 */     if (isBinary(i)) {
/*  342 */       int col = i - 1;
/*  343 */       int oid = this.fields[col].getOID();
/*  344 */       TimeZone tz = cal.getTimeZone();
/*  345 */       if (oid == 1082)
/*  346 */         return this.connection.getTimestampUtils().toDateBin(tz, this.this_row[col]); 
/*  347 */       if (oid != 1114 && oid != 1184) {
/*  348 */         throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "DATE" }), KSQLState.DATA_TYPE_MISMATCH);
/*      */       }
/*  350 */       Timestamp timestamp = getTimestamp(i, cal);
/*  351 */       return this.connection.getTimestampUtils().convertToDate(timestamp.getTime(), tz);
/*      */     } 
/*      */     
/*  354 */     return this.connection.getTimestampUtils().toDate(cal, getString(i));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(int i, Calendar cal) throws SQLException {
/*  360 */     checkResultSet(i);
/*  361 */     if (this.wasNullFlag) {
/*  362 */       return null;
/*      */     }
/*  364 */     if (cal == null) {
/*  365 */       cal = getDefaultCalendar();
/*      */     }
/*      */     
/*  368 */     if (isBinary(i)) {
/*  369 */       int col = i - 1;
/*  370 */       int oid = this.fields[col].getOID();
/*  371 */       TimeZone tz = cal.getTimeZone();
/*  372 */       if (oid != 1083 && oid != 1266) {
/*  373 */         if (oid != 1114 && oid != 1184) {
/*  374 */           throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "TIME" }), KSQLState.DATA_TYPE_MISMATCH);
/*      */         }
/*  376 */         Timestamp timestamp = getTimestamp(i, cal);
/*  377 */         return this.connection.getTimestampUtils().convertToTime(timestamp.getTime(), tz);
/*      */       } 
/*      */       
/*  380 */       return this.connection.getTimestampUtils().toTimeBin(tz, this.this_row[col]);
/*      */     } 
/*      */     
/*  383 */     String string = getString(i);
/*  384 */     return this.connection.getTimestampUtils().toTime(cal, string);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private LocalTime getLocalTime(int i) throws SQLException {
/*  390 */     checkResultSet(i);
/*  391 */     if (this.wasNullFlag)
/*  392 */       return null; 
/*  393 */     if (isBinary(i)) {
/*  394 */       int col = i - 1;
/*  395 */       int oid = this.fields[col].getOID();
/*  396 */       if (oid == 1083) {
/*  397 */         return this.connection.getTimestampUtils().toLocalTimeBin(this.this_row[col]);
/*      */       }
/*  399 */       throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "TIME" }), KSQLState.DATA_TYPE_MISMATCH);
/*      */     } 
/*      */     
/*  402 */     String string = getString(i);
/*  403 */     return this.connection.getTimestampUtils().toLocalTime(string);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int i, Calendar cal) throws SQLException {
/*  408 */     checkResultSet(i);
/*  409 */     if (this.wasNullFlag) {
/*  410 */       return null;
/*      */     }
/*  412 */     if (cal == null) {
/*  413 */       cal = getDefaultCalendar();
/*      */     }
/*      */     
/*  416 */     int col = i - 1;
/*  417 */     int oid = this.fields[col].getOID();
/*  418 */     if (!isBinary(i)) {
/*  419 */       String string = getString(i);
/*  420 */       return (oid != 1083 && oid != 1266) ? this.connection.getTimestampUtils().toTimestamp(cal, string) : new Timestamp(this.connection.getTimestampUtils().toTime(cal, string).getTime());
/*  421 */     }  if (oid != 1184 && oid != 1114) {
/*      */       long millis;
/*  423 */       if (oid != 1083 && oid != 1266) {
/*  424 */         if (oid != 1082) {
/*  425 */           throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "TIMESTAMP" }), KSQLState.DATA_TYPE_MISMATCH);
/*      */         }
/*      */         
/*  428 */         millis = getDate(i, cal).getTime();
/*      */       } else {
/*  430 */         millis = getTime(i, cal).getTime();
/*      */       } 
/*      */       
/*  433 */       return new Timestamp(millis);
/*      */     } 
/*  435 */     boolean hasTimeZone = (oid == 1184);
/*  436 */     TimeZone tz = cal.getTimeZone();
/*  437 */     return this.connection.getTimestampUtils().toTimestampBin(tz, this.this_row[col], hasTimeZone);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private LocalDateTime getLocalDateTime(int i) throws SQLException {
/*  443 */     checkResultSet(i);
/*  444 */     if (this.wasNullFlag) {
/*  445 */       return null;
/*      */     }
/*  447 */     int col = i - 1;
/*  448 */     int oid = this.fields[col].getOID();
/*  449 */     if (oid != 1114)
/*  450 */       throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), "TIMESTAMP" }), KSQLState.DATA_TYPE_MISMATCH); 
/*  451 */     if (isBinary(i)) {
/*  452 */       TimeZone timeZone = getDefaultCalendar().getTimeZone();
/*  453 */       return this.connection.getTimestampUtils().toLocalDateTimeBin(timeZone, this.this_row[col]);
/*      */     } 
/*  455 */     String string = getString(i);
/*  456 */     return this.connection.getTimestampUtils().toLocalDateTime(string);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(String c, Calendar cal) throws SQLException {
/*  462 */     return getDate(findColumn(c), cal);
/*      */   }
/*      */   
/*      */   public Time getTime(String c, Calendar cal) throws SQLException {
/*  466 */     return getTime(findColumn(c), cal);
/*      */   }
/*      */   
/*      */   public Timestamp getTimestamp(String c, Calendar cal) throws SQLException {
/*  470 */     return getTimestamp(findColumn(c), cal);
/*      */   }
/*      */   
/*      */   public int getFetchDirection() throws SQLException {
/*  474 */     checkClosed();
/*  475 */     return this.fetchdirection;
/*      */   }
/*      */   
/*      */   public Object getObjectImpl(String columnName, Map<String, Class<?>> map) throws SQLException {
/*  479 */     return getObjectImpl(findColumn(columnName), map);
/*      */   }
/*      */   
/*      */   public Object getObjectImpl(int i, Map<String, Class<?>> map) throws SQLException {
/*  483 */     checkClosed();
/*  484 */     if (map != null && !map.isEmpty()) {
/*  485 */       throw Driver.notImplemented(getClass(), "getObjectImpl(int,Map)");
/*      */     }
/*  487 */     return getObject(i);
/*      */   }
/*      */ 
/*      */   
/*      */   public Ref getRef(String columnName) throws SQLException {
/*  492 */     return getRef(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public Ref getRef(int i) throws SQLException {
/*  496 */     checkClosed();
/*  497 */     throw Driver.notImplemented(getClass(), "getRef(int)");
/*      */   }
/*      */   
/*      */   public int getRow() throws SQLException {
/*  501 */     checkClosed();
/*  502 */     if (this.onInsertRow) {
/*  503 */       return 0;
/*      */     }
/*  505 */     int rows_size = this.rows.size();
/*  506 */     return (this.current_row >= 0 && this.current_row < rows_size) ? (this.row_offset + this.current_row + 1) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public Statement getStatement() throws SQLException {
/*  511 */     checkClosed();
/*  512 */     return (Statement)this.statement;
/*      */   }
/*      */   
/*      */   public int getType() throws SQLException {
/*  516 */     checkClosed();
/*  517 */     return this.resultsettype;
/*      */   }
/*      */   
/*      */   public boolean isAfterLast() throws SQLException {
/*  521 */     checkClosed();
/*  522 */     if (this.onInsertRow) {
/*  523 */       return false;
/*      */     }
/*  525 */     int rows_size = this.rows.size();
/*  526 */     if (this.row_offset + rows_size == 0) {
/*  527 */       return false;
/*      */     }
/*  529 */     return (this.current_row >= rows_size);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBeforeFirst() throws SQLException {
/*  535 */     checkClosed();
/*  536 */     if (this.onInsertRow) {
/*  537 */       return false;
/*      */     }
/*  539 */     return (this.row_offset + this.current_row < 0 && !this.rows.isEmpty());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFirst() throws SQLException {
/*  544 */     checkClosed();
/*  545 */     if (this.onInsertRow) {
/*  546 */       return false;
/*      */     }
/*  548 */     int rows_size = this.rows.size();
/*  549 */     if (this.row_offset + rows_size == 0) {
/*  550 */       return false;
/*      */     }
/*  552 */     return (this.row_offset + this.current_row == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLast() throws SQLException {
/*  558 */     checkClosed();
/*  559 */     if (this.onInsertRow) {
/*  560 */       return false;
/*      */     }
/*  562 */     int rows_size = this.rows.size();
/*  563 */     if (rows_size == 0)
/*  564 */       return false; 
/*  565 */     if (this.current_row != rows_size - 1)
/*  566 */       return false; 
/*  567 */     if (this.cursor == null)
/*  568 */       return true; 
/*  569 */     if (this.maxRows > 0 && this.row_offset + this.current_row == this.maxRows) {
/*  570 */       return true;
/*      */     }
/*  572 */     this.row_offset += rows_size - 1;
/*  573 */     int fetchRows = this.fetchSize;
/*  574 */     if (this.maxRows != 0 && (fetchRows == 0 || this.row_offset + fetchRows > this.maxRows)) {
/*  575 */       fetchRows = this.maxRows - this.row_offset;
/*      */     }
/*      */     
/*  578 */     this.connection.getQueryExecutor().fetch(this.cursor, (ResultHandler)new CursorResultHandler(), fetchRows);
/*  579 */     this.rows.add(0, this.this_row);
/*  580 */     this.current_row = 0;
/*  581 */     return (this.rows.size() == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean last() throws SQLException {
/*  587 */     checkScrollable();
/*  588 */     int rows_size = this.rows.size();
/*  589 */     if (rows_size <= 0) {
/*  590 */       return false;
/*      */     }
/*  592 */     this.current_row = rows_size - 1;
/*  593 */     initRowBuffer();
/*  594 */     this.onInsertRow = false;
/*  595 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean previous() throws SQLException {
/*  600 */     checkScrollable();
/*  601 */     if (this.onInsertRow)
/*  602 */       throw new KSQLException(GT.tr("Can''t use relative move methods while on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/*  603 */     if (this.current_row - 1 < 0) {
/*  604 */       this.current_row = -1;
/*  605 */       this.this_row = (byte[][])null;
/*  606 */       this.rowBuffer = (byte[][])null;
/*  607 */       return false;
/*      */     } 
/*  609 */     this.current_row--;
/*  610 */     initRowBuffer();
/*  611 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean relative(int rows) throws SQLException {
/*  616 */     checkScrollable();
/*  617 */     if (this.onInsertRow) {
/*  618 */       throw new KSQLException(GT.tr("Can''t use relative move methods while on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     }
/*  620 */     return absolute(this.current_row + 1 + rows);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFetchDirection(int direction) throws SQLException {
/*  625 */     checkClosed();
/*  626 */     switch (direction) {
/*      */       case 1001:
/*      */       case 1002:
/*  629 */         checkScrollable();
/*      */       case 1000:
/*  631 */         this.fetchdirection = direction;
/*      */         return;
/*      */     } 
/*  634 */     throw new KSQLException(GT.tr("Invalid fetch direction constant: {0}.", new Object[] { Integer.valueOf(direction) }), KSQLState.INVALID_PARAMETER_VALUE);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void cancelRowUpdates() throws SQLException {
/*  639 */     checkClosed();
/*  640 */     if (this.onInsertRow) {
/*  641 */       throw new KSQLException(GT.tr("Cannot call cancelRowUpdates() when on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     }
/*  643 */     if (this.doingUpdates) {
/*  644 */       this.doingUpdates = false;
/*  645 */       clearRowBuffer(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void deleteRow() throws SQLException {
/*  652 */     checkUpdateable();
/*  653 */     if (this.onInsertRow)
/*  654 */       throw new KSQLException(GT.tr("Cannot call deleteRow() when on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/*  655 */     if (isBeforeFirst())
/*  656 */       throw new KSQLException(GT.tr("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/*  657 */     if (isAfterLast())
/*  658 */       throw new KSQLException(GT.tr("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/*  659 */     if (this.rows.isEmpty()) {
/*  660 */       throw new KSQLException(GT.tr("There are no rows in this ResultSet.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     }
/*  662 */     int numKeys = this.primaryKeys.size();
/*  663 */     if (this.deleteStatement == null) {
/*  664 */       StringBuilder deleteSQL = (new StringBuilder("DELETE FROM ")).append(this.onlyTable).append(this.tableName).append(" where ");
/*      */       
/*  666 */       for (int j = 0; j < numKeys; j++) {
/*  667 */         Utils.escapeIdentifier(deleteSQL, ((PrimaryKey)this.primaryKeys.get(j)).name);
/*  668 */         deleteSQL.append(" = ?");
/*  669 */         if (j < numKeys - 1) {
/*  670 */           deleteSQL.append(" and ");
/*      */         }
/*      */       } 
/*      */       
/*  674 */       this.deleteStatement = this.connection.prepareStatement(deleteSQL.toString());
/*      */     } 
/*      */     
/*  677 */     this.deleteStatement.clearParameters();
/*      */     
/*  679 */     for (int i = 0; i < numKeys; i++) {
/*  680 */       this.deleteStatement.setObject(i + 1, ((PrimaryKey)this.primaryKeys.get(i)).getValue());
/*      */     }
/*      */     
/*  683 */     this.deleteStatement.executeUpdate();
/*  684 */     this.rows.remove(this.current_row);
/*  685 */     this.current_row--;
/*  686 */     moveToCurrentRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void insertRow() throws SQLException {
/*  691 */     checkUpdateable();
/*  692 */     if (!this.onInsertRow)
/*  693 */       throw new KSQLException(GT.tr("Not on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/*  694 */     if (this.updateValues.isEmpty()) {
/*  695 */       throw new KSQLException(GT.tr("You must specify at least one column value to insert a row.", new Object[0]), KSQLState.INVALID_PARAMETER_VALUE);
/*      */     }
/*  697 */     StringBuilder insertSQL = (new StringBuilder("INSERT INTO ")).append(this.tableName).append(" (");
/*  698 */     StringBuilder paramSQL = new StringBuilder(") values (");
/*  699 */     Iterator<String> columnNames = this.updateValues.keySet().iterator();
/*  700 */     int numColumns = this.updateValues.size();
/*      */     
/*  702 */     for (int i = 0; columnNames.hasNext(); i++) {
/*  703 */       String columnName = columnNames.next();
/*  704 */       Utils.escapeIdentifier(insertSQL, columnName);
/*  705 */       if (i < numColumns - 1) {
/*  706 */         insertSQL.append(", ");
/*  707 */         paramSQL.append("?,");
/*      */       } else {
/*  709 */         paramSQL.append("?)");
/*      */       } 
/*      */     } 
/*      */     
/*  713 */     insertSQL.append(paramSQL.toString());
/*  714 */     this.insertStatement = this.connection.prepareStatement(insertSQL.toString());
/*  715 */     Iterator<Object> values = this.updateValues.values().iterator();
/*      */     
/*  717 */     for (int j = 1; values.hasNext(); j++) {
/*  718 */       this.insertStatement.setObject(j, values.next());
/*      */     }
/*      */     
/*  721 */     this.insertStatement.executeUpdate();
/*  722 */     if (this.usingOID) {
/*  723 */       long insertedOID = ((KbStatement)this.insertStatement).getLastOID();
/*  724 */       this.updateValues.put("OID", Long.valueOf(insertedOID));
/*      */     } 
/*      */     
/*  727 */     updateRowBuffer();
/*  728 */     this.rows.add(this.rowBuffer);
/*  729 */     this.this_row = this.rowBuffer;
/*  730 */     clearRowBuffer(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void moveToCurrentRow() throws SQLException {
/*  735 */     checkUpdateable();
/*  736 */     if (this.current_row >= 0 && this.current_row < this.rows.size()) {
/*  737 */       initRowBuffer();
/*      */     } else {
/*  739 */       this.this_row = (byte[][])null;
/*  740 */       this.rowBuffer = (byte[][])null;
/*      */     } 
/*      */     
/*  743 */     this.onInsertRow = false;
/*  744 */     this.doingUpdates = false;
/*      */   }
/*      */   
/*      */   public synchronized void moveToInsertRow() throws SQLException {
/*  748 */     checkUpdateable();
/*  749 */     if (this.insertStatement != null) {
/*  750 */       this.insertStatement = null;
/*      */     }
/*      */     
/*  753 */     clearRowBuffer(false);
/*  754 */     this.onInsertRow = true;
/*  755 */     this.doingUpdates = false;
/*      */   }
/*      */   
/*      */   private synchronized void clearRowBuffer(boolean copyCurrentRow) throws SQLException {
/*  759 */     this.rowBuffer = new byte[this.fields.length][];
/*  760 */     if (copyCurrentRow) {
/*  761 */       System.arraycopy(this.this_row, 0, this.rowBuffer, 0, this.this_row.length);
/*      */     }
/*      */     
/*  764 */     this.updateValues.clear();
/*      */   }
/*      */   
/*      */   public boolean rowDeleted() throws SQLException {
/*  768 */     checkClosed();
/*  769 */     return false;
/*      */   }
/*      */   
/*      */   public boolean rowInserted() throws SQLException {
/*  773 */     checkClosed();
/*  774 */     return false;
/*      */   }
/*      */   
/*      */   public boolean rowUpdated() throws SQLException {
/*  778 */     checkClosed();
/*  779 */     return false;
/*      */   }
/*      */   
/*      */   public synchronized void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
/*  783 */     if (x == null) {
/*  784 */       updateNull(columnIndex);
/*      */     } else {
/*      */       try {
/*  787 */         InputStreamReader reader = new InputStreamReader(x, "ASCII");
/*  788 */         char[] data = new char[length];
/*  789 */         int numRead = 0;
/*      */         
/*      */         do {
/*  792 */           int n = reader.read(data, numRead, length - numRead);
/*  793 */           if (n == -1) {
/*      */             break;
/*      */           }
/*      */           
/*  797 */           numRead += n;
/*  798 */         } while (numRead != length);
/*      */         
/*  800 */         updateString(columnIndex, new String(data, 0, numRead));
/*  801 */       } catch (UnsupportedEncodingException var8) {
/*  802 */         throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[] { "ASCII" }), KSQLState.UNEXPECTED_ERROR, var8);
/*  803 */       } catch (IOException var9) {
/*  804 */         throw new KSQLException(GT.tr("Provided InputStream failed.", new Object[0]), (KSQLState)null, var9);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public synchronized void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
/*  810 */     updateValue(columnIndex, x);
/*      */   }
/*      */   
/*      */   public synchronized void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
/*  814 */     if (x == null) {
/*  815 */       updateNull(columnIndex);
/*      */     } else {
/*  817 */       byte[] data = new byte[length];
/*  818 */       int numRead = 0;
/*      */       
/*      */       try {
/*      */         do {
/*  822 */           int n = x.read(data, numRead, length - numRead);
/*  823 */           if (n == -1) {
/*      */             break;
/*      */           }
/*      */           
/*  827 */           numRead += n;
/*  828 */         } while (numRead != length);
/*  829 */       } catch (IOException var7) {
/*  830 */         throw new KSQLException(GT.tr("Provided InputStream failed.", new Object[0]), (KSQLState)null, var7);
/*      */       } 
/*      */       
/*  833 */       if (numRead == length) {
/*  834 */         updateBytes(columnIndex, data);
/*      */       } else {
/*  836 */         byte[] data2 = new byte[numRead];
/*  837 */         System.arraycopy(data, 0, data2, 0, numRead);
/*  838 */         updateBytes(columnIndex, data2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBoolean(int columnIndex, boolean x) throws SQLException {
/*  845 */     updateValue(columnIndex, Boolean.valueOf(x));
/*      */   }
/*      */   
/*      */   public synchronized void updateByte(int columnIndex, byte x) throws SQLException {
/*  849 */     updateValue(columnIndex, Byte.valueOf(x));
/*      */   }
/*      */   
/*      */   public synchronized void updateBytes(int columnIndex, byte[] x) throws SQLException {
/*  853 */     updateValue(columnIndex, x);
/*      */   }
/*      */   
/*      */   public synchronized void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
/*  857 */     if (x == null) {
/*  858 */       updateNull(columnIndex);
/*      */     } else {
/*      */       try {
/*  861 */         char[] data = new char[length];
/*  862 */         int numRead = 0;
/*      */         
/*      */         do {
/*  865 */           int n = x.read(data, numRead, length - numRead);
/*  866 */           if (n == -1) {
/*      */             break;
/*      */           }
/*      */           
/*  870 */           numRead += n;
/*  871 */         } while (numRead != length);
/*      */         
/*  873 */         updateString(columnIndex, new String(data, 0, numRead));
/*  874 */       } catch (IOException var7) {
/*  875 */         throw new KSQLException(GT.tr("Provided Reader failed.", new Object[0]), (KSQLState)null, var7);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public synchronized void updateDate(int columnIndex, Date x) throws SQLException {
/*  881 */     updateValue(columnIndex, x);
/*      */   }
/*      */   
/*      */   public synchronized void updateDouble(int columnIndex, double x) throws SQLException {
/*  885 */     updateValue(columnIndex, Double.valueOf(x));
/*      */   }
/*      */   
/*      */   public synchronized void updateFloat(int columnIndex, float x) throws SQLException {
/*  889 */     updateValue(columnIndex, Float.valueOf(x));
/*      */   }
/*      */   
/*      */   public synchronized void updateInt(int columnIndex, int x) throws SQLException {
/*  893 */     updateValue(columnIndex, Integer.valueOf(x));
/*      */   }
/*      */   
/*      */   public synchronized void updateLong(int columnIndex, long x) throws SQLException {
/*  897 */     updateValue(columnIndex, Long.valueOf(x));
/*      */   }
/*      */   
/*      */   public synchronized void updateNull(int columnIndex) throws SQLException {
/*  901 */     checkColumnIndex(columnIndex);
/*  902 */     String columnTypeName = getKBType(columnIndex);
/*  903 */     updateValue(columnIndex, new NullObject(columnTypeName));
/*      */   }
/*      */   
/*      */   public synchronized void updateObject(int columnIndex, Object x) throws SQLException {
/*  907 */     updateValue(columnIndex, x);
/*      */   }
/*      */   
/*      */   public synchronized void updateObject(int columnIndex, Object x, int scale) throws SQLException {
/*  911 */     updateObject(columnIndex, x);
/*      */   }
/*      */   
/*      */   public void refreshRow() throws SQLException {
/*  915 */     checkUpdateable();
/*  916 */     if (this.onInsertRow)
/*  917 */       throw new KSQLException(GT.tr("Can''t refresh the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/*  918 */     if (!isBeforeFirst() && !isAfterLast() && !this.rows.isEmpty()) {
/*  919 */       StringBuilder selectSQL = new StringBuilder("select ");
/*  920 */       ResultSetMetaData rsmd = getMetaData();
/*  921 */       KBResultSetMetaData pgmd = (KBResultSetMetaData)rsmd;
/*      */       
/*      */       int numKeys;
/*  924 */       for (numKeys = 1; numKeys <= rsmd.getColumnCount(); numKeys++) {
/*  925 */         if (numKeys > 1) {
/*  926 */           selectSQL.append(", ");
/*      */         }
/*      */         
/*  929 */         selectSQL.append(pgmd.getBaseColumnName(numKeys));
/*      */       } 
/*      */       
/*  932 */       selectSQL.append(" from ").append(this.onlyTable).append(this.tableName).append(" where ");
/*  933 */       numKeys = this.primaryKeys.size();
/*      */       
/*  935 */       for (int i = 0; i < numKeys; i++) {
/*  936 */         PrimaryKey primaryKey = this.primaryKeys.get(i);
/*  937 */         selectSQL.append(primaryKey.name).append("= ?");
/*  938 */         if (i < numKeys - 1) {
/*  939 */           selectSQL.append(" and ");
/*      */         }
/*      */       } 
/*      */       
/*  943 */       String sqlText = selectSQL.toString();
/*  944 */       if (LOGGER.isLoggable(Level.FINE)) {
/*  945 */         LOGGER.log(Level.FINE, "selecting {0}", sqlText);
/*      */       }
/*      */       
/*  948 */       this.selectStatement = this.connection.prepareStatement(sqlText, 1004, 1008);
/*  949 */       int j = 0;
/*      */       
/*  951 */       for (int k = 1; j < numKeys; k++) {
/*  952 */         this.selectStatement.setObject(k, ((PrimaryKey)this.primaryKeys.get(j)).getValue());
/*  953 */         j++;
/*      */       } 
/*      */       
/*  956 */       KbResultSet rs = (KbResultSet)this.selectStatement.executeQuery();
/*  957 */       if (rs.next()) {
/*  958 */         this.rowBuffer = rs.this_row;
/*      */       }
/*      */       
/*  961 */       this.rows.set(this.current_row, this.rowBuffer);
/*  962 */       this.this_row = this.rowBuffer;
/*  963 */       LOGGER.log(Level.FINE, "done updates");
/*  964 */       rs.close();
/*  965 */       this.selectStatement.close();
/*  966 */       this.selectStatement = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public synchronized void insertLob(Object obj) throws SQLException {
/*  971 */     init();
/*  972 */     StringBuilder updateSQL = new StringBuilder("UPDATE " + this.onlyTable + this.tableName + " SET  ");
/*  973 */     KBResultSetMetaData md = (KBResultSetMetaData)getMetaData();
/*  974 */     String column = null;
/*  975 */     if (obj instanceof KbClob) {
/*  976 */       column = md.getBaseColumnName(((KbClob)obj).getColumnIndex());
/*  977 */     } else if (obj instanceof KbBlob) {
/*  978 */       column = md.getBaseColumnName(((KbBlob)obj).getColumnIndex());
/*      */     } 
/*      */     
/*  981 */     Utils.escapeIdentifier(updateSQL, column);
/*  982 */     updateSQL.append(" = ?");
/*  983 */     updateSQL.append(" WHERE ");
/*  984 */     int numKeys = this.primaryKeys.size();
/*      */     
/*  986 */     for (int i = 0; i < numKeys; i++) {
/*  987 */       PrimaryKey primaryKey = this.primaryKeys.get(i);
/*  988 */       Utils.escapeIdentifier(updateSQL, primaryKey.name);
/*  989 */       updateSQL.append(" = ?");
/*  990 */       if (i < numKeys - 1) {
/*  991 */         updateSQL.append(" and ");
/*      */       }
/*      */     } 
/*      */     
/*  995 */     updateSQL.append(" returning CTID,XMIN");
/*  996 */     String sqlText = updateSQL.toString();
/*  997 */     if (LOGGER.isLoggable(Level.FINE)) {
/*  998 */       LOGGER.log(Level.FINE, "updating {0}", sqlText);
/*      */     }
/*      */     
/* 1001 */     this.updateStatement = this.connection.prepareStatement(sqlText);
/* 1002 */     int k = 1;
/* 1003 */     this.updateStatement.setObject(k, obj);
/*      */     
/* 1005 */     for (int j = 0; j < numKeys; k++) {
/* 1006 */       this.updateStatement.setObject(k + 1, ((PrimaryKey)this.primaryKeys.get(j)).getValue());
/* 1007 */       j++;
/*      */     } 
/*      */     
/* 1010 */     this.updateStatement.execute();
/* 1011 */     ResultSet rs = this.updateStatement.getResultSet();
/* 1012 */     if (rs.next()) {
/* 1013 */       this.this_row[this.this_row.length - 2] = rs.getBytes(1);
/* 1014 */       this.this_row[this.this_row.length - 1] = rs.getBytes(2);
/*      */     } 
/*      */     
/* 1017 */     this.updateStatement.close();
/* 1018 */     this.updateStatement = null;
/*      */   }
/*      */   
/*      */   void init() throws SQLException {
/* 1022 */     checkClosed();
/* 1023 */     if (!this.originalQuery.hasForUpdate()) {
/* 1024 */       throw new KSQLException(GT.tr("The query \"{0}\" is not locked.", new Object[] { this.originalQuery.toString((ParameterList)null) }), (KSQLState)null);
/*      */     }
/* 1026 */     parseQuery();
/* 1027 */     if (!this.singleTable) {
/* 1028 */       LOGGER.log(Level.FINE, "not a single table");
/*      */     } else {
/* 1030 */       LOGGER.log(Level.FINE, "getting primary keys");
/* 1031 */       this.primaryKeys = new ArrayList<>();
/* 1032 */       int ctidIndex = findColumnIndex("CTID");
/* 1033 */       int xminIndex = findColumnIndex("XMIN");
/* 1034 */       boolean existInternalRID = false;
/* 1035 */       if (ctidIndex > 0 && xminIndex > 0) {
/* 1036 */         existInternalRID = true;
/*      */       }
/*      */       
/* 1039 */       int i = 0;
/* 1040 */       if (existInternalRID) {
/* 1041 */         i++;
/* 1042 */         this.primaryKeys.add(new PrimaryKey(ctidIndex, "CTID"));
/* 1043 */         i++;
/* 1044 */         this.primaryKeys.add(new PrimaryKey(xminIndex, "XMIN"));
/*      */       } 
/*      */       
/* 1047 */       LOGGER.log(Level.FINE, "no of keys={0}", Integer.valueOf(i));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateRow() throws SQLException {
/* 1053 */     checkUpdateable();
/* 1054 */     if (this.onInsertRow)
/* 1055 */       throw new KSQLException(GT.tr("Cannot call updateRow() when on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/* 1056 */     if (!isBeforeFirst() && !isAfterLast() && !this.rows.isEmpty()) {
/* 1057 */       if (this.doingUpdates) {
/* 1058 */         StringBuilder updateSQL = new StringBuilder("UPDATE " + this.onlyTable + this.tableName + " SET  ");
/* 1059 */         int numColumns = this.updateValues.size();
/* 1060 */         Iterator<String> columns = this.updateValues.keySet().iterator();
/*      */         
/*      */         int numKeys;
/*      */         
/* 1064 */         for (numKeys = 0; columns.hasNext(); numKeys++) {
/* 1065 */           String str = columns.next();
/* 1066 */           Utils.escapeIdentifier(updateSQL, str);
/* 1067 */           updateSQL.append(" = ?");
/* 1068 */           if (numKeys < numColumns - 1) {
/* 1069 */             updateSQL.append(", ");
/*      */           }
/*      */         } 
/*      */         
/* 1073 */         updateSQL.append(" WHERE ");
/* 1074 */         numKeys = this.primaryKeys.size();
/*      */         int i;
/* 1076 */         for (i = 0; i < numKeys; i++) {
/* 1077 */           PrimaryKey primaryKey = this.primaryKeys.get(i);
/* 1078 */           Utils.escapeIdentifier(updateSQL, primaryKey.name);
/* 1079 */           updateSQL.append(" = ?");
/* 1080 */           if (i < numKeys - 1) {
/* 1081 */             updateSQL.append(" and ");
/*      */           }
/*      */         } 
/*      */         
/* 1085 */         String sqlText = updateSQL.toString();
/* 1086 */         if (LOGGER.isLoggable(Level.FINE)) {
/* 1087 */           LOGGER.log(Level.FINE, "updating {0}", sqlText);
/*      */         }
/*      */         
/* 1090 */         this.updateStatement = this.connection.prepareStatement(sqlText);
/* 1091 */         i = 0;
/*      */         
/* 1093 */         for (Iterator iterator = this.updateValues.values().iterator(); iterator.hasNext(); i++) {
/* 1094 */           Object o = iterator.next();
/* 1095 */           this.updateStatement.setObject(i + 1, o);
/*      */         } 
/*      */         
/* 1098 */         for (int j = 0; j < numKeys; i++) {
/* 1099 */           this.updateStatement.setObject(i + 1, ((PrimaryKey)this.primaryKeys.get(j)).getValue());
/* 1100 */           j++;
/*      */         } 
/*      */         
/* 1103 */         this.updateStatement.executeUpdate();
/* 1104 */         this.updateStatement.close();
/* 1105 */         this.updateStatement = null;
/* 1106 */         updateRowBuffer();
/* 1107 */         LOGGER.log(Level.FINE, "copying data");
/* 1108 */         System.arraycopy(this.rowBuffer, 0, this.this_row, 0, this.rowBuffer.length);
/* 1109 */         this.rows.set(this.current_row, this.rowBuffer);
/* 1110 */         LOGGER.log(Level.FINE, "done updates");
/* 1111 */         this.updateValues.clear();
/* 1112 */         this.doingUpdates = false;
/*      */       } 
/*      */     } else {
/* 1115 */       throw new KSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     } 
/*      */   }
/*      */   
/*      */   public synchronized void updateShort(int columnIndex, short x) throws SQLException {
/* 1120 */     updateValue(columnIndex, Short.valueOf(x));
/*      */   }
/*      */   
/*      */   public synchronized void updateString(int columnIndex, String x) throws SQLException {
/* 1124 */     updateValue(columnIndex, x);
/*      */   }
/*      */   
/*      */   public synchronized void updateTime(int columnIndex, Time x) throws SQLException {
/* 1128 */     updateValue(columnIndex, x);
/*      */   }
/*      */   
/*      */   public synchronized void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
/* 1132 */     updateValue(columnIndex, x);
/*      */   }
/*      */   
/*      */   public synchronized void updateNull(String columnName) throws SQLException {
/* 1136 */     updateNull(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public synchronized void updateBoolean(String columnName, boolean x) throws SQLException {
/* 1140 */     updateBoolean(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateByte(String columnName, byte x) throws SQLException {
/* 1144 */     updateByte(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateShort(String columnName, short x) throws SQLException {
/* 1148 */     updateShort(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateInt(String columnName, int x) throws SQLException {
/* 1152 */     updateInt(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateLong(String columnName, long x) throws SQLException {
/* 1156 */     updateLong(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateFloat(String columnName, float x) throws SQLException {
/* 1160 */     updateFloat(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateDouble(String columnName, double x) throws SQLException {
/* 1164 */     updateDouble(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
/* 1168 */     updateBigDecimal(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateString(String columnName, String x) throws SQLException {
/* 1172 */     updateString(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateBytes(String columnName, byte[] x) throws SQLException {
/* 1176 */     updateBytes(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateDate(String columnName, Date x) throws SQLException {
/* 1180 */     updateDate(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateTime(String columnName, Time x) throws SQLException {
/* 1184 */     updateTime(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateTimestamp(String columnName, Timestamp x) throws SQLException {
/* 1188 */     updateTimestamp(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
/* 1192 */     updateAsciiStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */   public synchronized void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
/* 1196 */     updateBinaryStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */   public synchronized void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
/* 1200 */     updateCharacterStream(findColumn(columnName), reader, length);
/*      */   }
/*      */   
/*      */   public synchronized void updateObject(String columnName, Object x, int scale) throws SQLException {
/* 1204 */     updateObject(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public synchronized void updateObject(String columnName, Object x) throws SQLException {
/* 1208 */     updateObject(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   boolean isUpdateable() throws SQLException {
/* 1212 */     checkClosed();
/* 1213 */     if (this.resultsetconcurrency == 1007)
/* 1214 */       throw new KSQLException(GT.tr("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", new Object[0]), KSQLState.INVALID_CURSOR_STATE); 
/* 1215 */     if (this.updateable) {
/* 1216 */       return true;
/*      */     }
/* 1218 */     LOGGER.log(Level.FINE, "checking if rs is updateable");
/* 1219 */     parseQuery();
/* 1220 */     if (!this.singleTable) {
/* 1221 */       LOGGER.log(Level.FINE, "not a single table");
/* 1222 */       return false;
/*      */     } 
/* 1224 */     LOGGER.log(Level.FINE, "getting primary keys");
/* 1225 */     this.primaryKeys = new ArrayList<>();
/* 1226 */     this.usingOID = false;
/* 1227 */     int oidIndex = findColumnIndex("OID");
/* 1228 */     boolean existInternalRID = false;
/* 1229 */     int ctidIndex = findColumnIndex("CTID");
/* 1230 */     int xminIndex = findColumnIndex("XMIN");
/* 1231 */     if (ctidIndex > 0 && xminIndex > 0) {
/* 1232 */       existInternalRID = true;
/*      */     }
/*      */     
/* 1235 */     int i = 0;
/* 1236 */     int numPKcolumns = 0;
/* 1237 */     if (oidIndex > 0) {
/* 1238 */       i++;
/* 1239 */       numPKcolumns++;
/* 1240 */       this.primaryKeys.add(new PrimaryKey(oidIndex, "OID"));
/* 1241 */       this.usingOID = true;
/* 1242 */     } else if (existInternalRID) {
/* 1243 */       i++;
/* 1244 */       numPKcolumns++;
/* 1245 */       this.primaryKeys.add(new PrimaryKey(ctidIndex, "CTID"));
/* 1246 */       i++;
/* 1247 */       numPKcolumns++;
/* 1248 */       this.primaryKeys.add(new PrimaryKey(xminIndex, "XMIN"));
/*      */     } else {
/* 1250 */       String[] s = quotelessTableName(this.tableName);
/* 1251 */       String quotelessTableName = s[0];
/* 1252 */       String quotelessSchemaName = s[1];
/* 1253 */       ResultSet rs = this.connection.getMetaData().getPrimaryKeys("", quotelessSchemaName, quotelessTableName);
/*      */       
/* 1255 */       while (rs.next()) {
/* 1256 */         numPKcolumns++;
/* 1257 */         String columnName = rs.getString(4);
/* 1258 */         int index = findColumnIndex(columnName);
/* 1259 */         if (index > 0) {
/* 1260 */           i++;
/* 1261 */           this.primaryKeys.add(new PrimaryKey(index, columnName));
/*      */         } 
/*      */       } 
/*      */       
/* 1265 */       rs.close();
/*      */     } 
/*      */     
/* 1268 */     LOGGER.log(Level.FINE, "no of keys={0}", Integer.valueOf(i));
/* 1269 */     if (i < 1) {
/* 1270 */       throw new KSQLException(GT.tr("No primary key found for table {0}.", new Object[] { this.tableName }), KSQLState.DATA_ERROR);
/*      */     }
/* 1272 */     this.updateable = (i == numPKcolumns);
/* 1273 */     LOGGER.log(Level.FINE, "checking primary key {0}", Boolean.valueOf(this.updateable));
/* 1274 */     return this.updateable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] quotelessTableName(String fullname) {
/* 1281 */     String[] parts = { null, "" };
/* 1282 */     StringBuilder acc = new StringBuilder();
/* 1283 */     boolean betweenQuotes = false;
/*      */     
/* 1285 */     for (int i = 0; i < fullname.length(); i++) {
/* 1286 */       char c = fullname.charAt(i);
/* 1287 */       switch (c) {
/*      */         case '"':
/* 1289 */           if (i < fullname.length() - 1 && fullname.charAt(i + 1) == '"') {
/* 1290 */             i++;
/* 1291 */             acc.append(c);
/*      */             
/*      */             break;
/*      */           } 
/* 1295 */           betweenQuotes = !betweenQuotes;
/*      */           break;
/*      */         case '.':
/* 1298 */           if (betweenQuotes) {
/* 1299 */             acc.append(c); break;
/*      */           } 
/* 1301 */           parts[1] = acc.toString();
/* 1302 */           acc = new StringBuilder();
/*      */           break;
/*      */         
/*      */         default:
/* 1306 */           acc.append(betweenQuotes ? c : Character.toUpperCase(c));
/*      */           break;
/*      */       } 
/*      */     } 
/* 1310 */     parts[0] = acc.toString();
/* 1311 */     return parts;
/*      */   }
/*      */   
/*      */   private void parseQuery() {
/* 1315 */     String l_sql = this.originalQuery.toString((ParameterList)null);
/* 1316 */     StringTokenizer st = new StringTokenizer(l_sql, " \r\t\n");
/* 1317 */     boolean tableFound = false;
/* 1318 */     boolean tablesChecked = false;
/* 1319 */     String name = "";
/* 1320 */     this.singleTable = true;
/*      */     
/* 1322 */     while (!tableFound && !tablesChecked && st.hasMoreTokens()) {
/* 1323 */       name = st.nextToken();
/* 1324 */       if ("from".equalsIgnoreCase(name)) {
/* 1325 */         this.tableName = st.nextToken();
/* 1326 */         if ("only".equalsIgnoreCase(this.tableName)) {
/* 1327 */           this.tableName = st.nextToken();
/* 1328 */           this.onlyTable = "ONLY ";
/*      */         } 
/*      */         
/* 1331 */         tableFound = true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateRowBuffer() throws SQLException {
/* 1338 */     Iterator<Map.Entry<String, Object>> var1 = this.updateValues.entrySet().iterator();
/*      */     
/* 1340 */     while (var1.hasNext()) {
/* 1341 */       Map.Entry<String, Object> entry = var1.next();
/* 1342 */       int columnIndex = findColumn(entry.getKey()) - 1;
/* 1343 */       Object valueObject = entry.getValue();
/* 1344 */       if (valueObject instanceof KBobject) {
/* 1345 */         String value = ((KBobject)valueObject).getValue();
/* 1346 */         this.rowBuffer[columnIndex] = (value == null) ? null : this.connection.encodeString(value); continue;
/*      */       } 
/* 1348 */       switch (getSQLType(columnIndex + 1)) {
/*      */         case -4:
/*      */         case -3:
/*      */         case -2:
/* 1352 */           if (isBinary(columnIndex + 1)) {
/* 1353 */             this.rowBuffer[columnIndex] = (byte[])valueObject; continue;
/*      */           } 
/*      */           try {
/* 1356 */             this.rowBuffer[columnIndex] = KBbytea.toKBString((byte[])valueObject).getBytes("ISO-8859-1");
/* 1357 */           } catch (UnsupportedEncodingException var6) {
/* 1358 */             throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[] { "ISO-8859-1" }), KSQLState.UNEXPECTED_ERROR, var6);
/*      */           } 
/*      */           continue;
/*      */         case 0:
/*      */           continue;
/*      */         case 91:
/* 1364 */           this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(getDefaultCalendar(), (Date)valueObject));
/*      */           continue;
/*      */         case 92:
/* 1367 */           this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(getDefaultCalendar(), (Time)valueObject));
/*      */           continue;
/*      */         case 93:
/* 1370 */           this.rowBuffer[columnIndex] = this.connection.encodeString(this.connection.getTimestampUtils().toString(getDefaultCalendar(), (Timestamp)valueObject));
/*      */           continue;
/*      */       } 
/* 1373 */       this.rowBuffer[columnIndex] = this.connection.encodeString(String.valueOf(valueObject));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BaseStatement getKBStatement() {
/* 1381 */     return this.statement;
/*      */   }
/*      */   
/*      */   public String getRefCursor() {
/* 1385 */     return this.refCursorName;
/*      */   }
/*      */   
/*      */   private void setRefCursor(String refCursorName) {
/* 1389 */     this.refCursorName = refCursorName;
/*      */   }
/*      */   
/*      */   public void setFetchSize(int rows) throws SQLException {
/* 1393 */     checkClosed();
/* 1394 */     if (rows < 0) {
/* 1395 */       throw new KSQLException(GT.tr("Fetch size must be a value greater to or equal to 0.", new Object[0]), KSQLState.INVALID_PARAMETER_VALUE);
/*      */     }
/* 1397 */     this.fetchSize = rows;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFetchSize() throws SQLException {
/* 1402 */     checkClosed();
/* 1403 */     return this.fetchSize;
/*      */   }
/*      */   
/*      */   public boolean next() throws SQLException {
/* 1407 */     checkClosed();
/* 1408 */     if (this.onInsertRow) {
/* 1409 */       throw new KSQLException(GT.tr("Can''t use relative move methods while on the insert row.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     }
/* 1411 */     if (this.current_row + 1 < this.rows.size()) {
/* 1412 */       this.current_row++;
/*      */     } else {
/* 1414 */       if (this.cursor == null || (this.maxRows > 0 && this.row_offset + this.rows.size() >= this.maxRows)) {
/* 1415 */         this.current_row = this.rows.size();
/* 1416 */         this.this_row = (byte[][])null;
/* 1417 */         this.rowBuffer = (byte[][])null;
/* 1418 */         return false;
/*      */       } 
/*      */       
/* 1421 */       this.row_offset += this.rows.size();
/* 1422 */       int fetchRows = this.fetchSize;
/* 1423 */       if (this.maxRows != 0 && (fetchRows == 0 || this.row_offset + fetchRows > this.maxRows)) {
/* 1424 */         fetchRows = this.maxRows - this.row_offset;
/*      */       }
/*      */       
/* 1427 */       this.connection.getQueryExecutor().fetch(this.cursor, (ResultHandler)new CursorResultHandler(), fetchRows);
/* 1428 */       this.current_row = 0;
/* 1429 */       if (this.rows.isEmpty()) {
/* 1430 */         this.this_row = (byte[][])null;
/* 1431 */         this.rowBuffer = (byte[][])null;
/* 1432 */         return false;
/*      */       } 
/*      */     } 
/*      */     
/* 1436 */     initRowBuffer();
/* 1437 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws SQLException {
/*      */     try {
/* 1443 */       this.rows = null;
/* 1444 */       if (this.cursor != null) {
/* 1445 */         this.cursor.close();
/* 1446 */         this.cursor = null;
/*      */       } 
/*      */     } finally {
/* 1449 */       ((KbStatement)this.statement).checkCompletion();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean wasNull() throws SQLException {
/* 1455 */     checkClosed();
/* 1456 */     return this.wasNullFlag;
/*      */   }
/*      */   
/*      */   public String getString(int columnIndex) throws SQLException {
/* 1460 */     LOGGER.log(Level.FINEST, "  getString columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1461 */     checkResultSet(columnIndex);
/* 1462 */     if (this.wasNullFlag)
/* 1463 */       return null; 
/* 1464 */     if (isBinary(columnIndex) && getSQLType(columnIndex) != 12 && getSQLType(columnIndex) != -7 && getSQLType(columnIndex) != 2005) {
/* 1465 */       Field field = this.fields[columnIndex - 1];
/* 1466 */       Object obj = internalGetObject(columnIndex, field);
/* 1467 */       if (obj == null)
/* 1468 */         return null; 
/* 1469 */       if (!(obj instanceof Date)) {
/* 1470 */         return "HSTORE".equals(getKBType(columnIndex)) ? HStoreConverter.toString((Map)obj) : trimString(columnIndex, obj.toString());
/*      */       }
/* 1472 */       int oid = field.getOID();
/* 1473 */       return this.connection.getTimestampUtils().timeToString((Date)obj, (oid == 1184 || oid == 1266));
/*      */     } 
/*      */     
/* 1476 */     Encoding encoding = this.connection.getEncoding();
/*      */     
/*      */     try {
/* 1479 */       return trimString(columnIndex, encoding.decode(this.this_row[columnIndex - 1]));
/* 1480 */     } catch (IOException var5) {
/* 1481 */       throw new KSQLException(GT.tr("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", new Object[0]), KSQLState.DATA_ERROR, var5);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int columnIndex) throws SQLException {
/* 1487 */     LOGGER.log(Level.FINEST, "  getBoolean columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1488 */     checkResultSet(columnIndex);
/* 1489 */     if (this.wasNullFlag) {
/* 1490 */       return false;
/*      */     }
/* 1492 */     int col = columnIndex - 1;
/* 1493 */     if (16 != this.fields[col].getOID()) {
/* 1494 */       return isBinary(columnIndex) ? BooleanTypeUtil.castToBoolean(Double.valueOf(readDoubleValue(this.this_row[col], this.fields[col].getOID(), "BOOLEAN"))) : BooleanTypeUtil.castToBoolean(getString(columnIndex));
/*      */     }
/* 1496 */     byte[] v = this.this_row[col];
/* 1497 */     return (1 == v.length && 116 == v[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(int columnIndex) throws SQLException {
/* 1503 */     LOGGER.log(Level.FINEST, "  getByte columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1504 */     checkResultSet(columnIndex);
/* 1505 */     if (this.wasNullFlag)
/* 1506 */       return 0; 
/* 1507 */     if (isBinary(columnIndex)) {
/* 1508 */       int col = columnIndex - 1;
/* 1509 */       int oid = this.fields[col].getOID();
/* 1510 */       return (oid == 9) ? ByteConverter.int1(this.this_row[col], 0) : (byte)(int)readLongValue(this.this_row[col], this.fields[col].getOID(), -128L, 127L, "byte");
/*      */     } 
/* 1512 */     String s = getString(columnIndex);
/* 1513 */     if (s != null) {
/* 1514 */       s = s.trim();
/* 1515 */       if (s.isEmpty()) {
/* 1516 */         return 0;
/*      */       }
/*      */       try {
/* 1519 */         return Byte.parseByte(s);
/* 1520 */       } catch (NumberFormatException var9) {
/*      */         try {
/* 1522 */           BigDecimal n = new BigDecimal(s);
/* 1523 */           BigInteger i = n.toBigInteger();
/* 1524 */           int gt = i.compareTo(BYTEMAX);
/* 1525 */           int lt = i.compareTo(BYTEMIN);
/* 1526 */           if (gt <= 0 && lt >= 0) {
/* 1527 */             return i.byteValue();
/*      */           }
/* 1529 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "byte", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         }
/* 1531 */         catch (NumberFormatException var8) {
/* 1532 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "byte", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1537 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(int columnIndex) throws SQLException {
/* 1543 */     LOGGER.log(Level.FINEST, "  getShort columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1544 */     checkResultSet(columnIndex);
/* 1545 */     if (this.wasNullFlag)
/* 1546 */       return 0; 
/* 1547 */     if (isBinary(columnIndex)) {
/* 1548 */       int col = columnIndex - 1;
/* 1549 */       int oid = this.fields[col].getOID();
/* 1550 */       return (oid == 21) ? ByteConverter.int2(this.this_row[col], 0) : (short)(int)readLongValue(this.this_row[col], oid, -32768L, 32767L, "short");
/*      */     } 
/* 1552 */     String s = getFixedString(columnIndex);
/* 1553 */     if (s != null) {
/* 1554 */       s = s.trim();
/*      */       
/*      */       try {
/* 1557 */         return Short.parseShort(s);
/* 1558 */       } catch (NumberFormatException var9) {
/*      */         try {
/* 1560 */           BigDecimal n = new BigDecimal(s);
/* 1561 */           BigInteger i = n.toBigInteger();
/* 1562 */           int gt = i.compareTo(SHORTMAX);
/* 1563 */           int lt = i.compareTo(SHORTMIN);
/* 1564 */           if (gt <= 0 && lt >= 0) {
/* 1565 */             return i.shortValue();
/*      */           }
/* 1567 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "short", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         }
/* 1569 */         catch (NumberFormatException var8) {
/* 1570 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "short", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1574 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(int columnIndex) throws SQLException {
/* 1580 */     LOGGER.log(Level.FINEST, "  getInt columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1581 */     checkResultSet(columnIndex);
/* 1582 */     if (this.wasNullFlag)
/* 1583 */       return 0; 
/* 1584 */     if (isBinary(columnIndex)) {
/* 1585 */       int col = columnIndex - 1;
/* 1586 */       int oid = this.fields[col].getOID();
/* 1587 */       return (oid == 23) ? ByteConverter.int4(this.this_row[col], 0) : (int)readLongValue(this.this_row[col], oid, -2147483648L, 2147483647L, "INT");
/*      */     } 
/* 1589 */     Encoding encoding = this.connection.getEncoding();
/* 1590 */     if (encoding.hasAsciiNumbers()) {
/*      */       try {
/* 1592 */         return getFastInt(columnIndex);
/* 1593 */       } catch (NumberFormatException numberFormatException) {}
/*      */     }
/*      */ 
/*      */     
/* 1597 */     return toInt(getFixedString(columnIndex));
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int columnIndex) throws SQLException {
/* 1602 */     LOGGER.log(Level.FINEST, "  getLong columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1603 */     checkResultSet(columnIndex);
/* 1604 */     if (this.wasNullFlag)
/* 1605 */       return 0L; 
/* 1606 */     if (isBinary(columnIndex)) {
/* 1607 */       int col = columnIndex - 1;
/* 1608 */       int oid = this.fields[col].getOID();
/* 1609 */       return (oid == 20) ? ByteConverter.int8(this.this_row[col], 0) : readLongValue(this.this_row[col], oid, Long.MIN_VALUE, Long.MAX_VALUE, "long");
/*      */     } 
/* 1611 */     Encoding encoding = this.connection.getEncoding();
/* 1612 */     if (encoding.hasAsciiNumbers()) {
/*      */       try {
/* 1614 */         return getFastLong(columnIndex);
/* 1615 */       } catch (NumberFormatException numberFormatException) {}
/*      */     }
/*      */ 
/*      */     
/* 1619 */     return toLong(getFixedString(columnIndex));
/*      */   }
/*      */   private long getFastLong(int columnIndex) throws SQLException, NumberFormatException {
/*      */     int start;
/*      */     boolean neg;
/* 1624 */     byte[] bytes = this.this_row[columnIndex - 1];
/* 1625 */     if (bytes.length == 0) {
/* 1626 */       throw FAST_NUMBER_FAILED;
/*      */     }
/* 1628 */     long val = 0L;
/*      */ 
/*      */     
/* 1631 */     if (bytes[0] == 45) {
/* 1632 */       neg = true;
/* 1633 */       start = 1;
/* 1634 */       if (bytes.length == 1 || bytes.length > 19) {
/* 1635 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */     } else {
/* 1638 */       start = 0;
/* 1639 */       neg = false;
/* 1640 */       if (bytes.length > 18) {
/* 1641 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */     } 
/*      */     
/* 1645 */     while (start < bytes.length) {
/* 1646 */       byte b = bytes[start++];
/* 1647 */       if (b < 48 || b > 57) {
/* 1648 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */       
/* 1651 */       val *= 10L;
/* 1652 */       val += (b - 48);
/*      */     } 
/*      */     
/* 1655 */     if (neg) {
/* 1656 */       val = -val;
/*      */     }
/*      */     
/* 1659 */     return val;
/*      */   }
/*      */   private int getFastInt(int columnIndex) throws SQLException, NumberFormatException {
/*      */     int start;
/*      */     boolean neg;
/* 1664 */     byte[] bytes = this.this_row[columnIndex - 1];
/* 1665 */     if (bytes.length == 0) {
/* 1666 */       throw FAST_NUMBER_FAILED;
/*      */     }
/* 1668 */     int val = 0;
/*      */ 
/*      */     
/* 1671 */     if (bytes[0] == 45) {
/* 1672 */       neg = true;
/* 1673 */       start = 1;
/* 1674 */       if (bytes.length == 1 || bytes.length > 10) {
/* 1675 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */     } else {
/* 1678 */       start = 0;
/* 1679 */       neg = false;
/* 1680 */       if (bytes.length > 9) {
/* 1681 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */     } 
/*      */     
/* 1685 */     while (start < bytes.length) {
/* 1686 */       byte b = bytes[start++];
/* 1687 */       if (b < 48 || b > 57) {
/* 1688 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */       
/* 1691 */       val *= 10;
/* 1692 */       val += b - 48;
/*      */     } 
/*      */     
/* 1695 */     if (neg) {
/* 1696 */       val = -val;
/*      */     }
/*      */     
/* 1699 */     return val;
/*      */   }
/*      */   private BigDecimal getFastBigDecimal(int columnIndex) throws SQLException, NumberFormatException {
/*      */     int start;
/*      */     boolean neg;
/* 1704 */     byte[] bytes = this.this_row[columnIndex - 1];
/* 1705 */     if (bytes.length == 0) {
/* 1706 */       throw FAST_NUMBER_FAILED;
/*      */     }
/* 1708 */     int scale = 0;
/* 1709 */     long val = 0L;
/*      */ 
/*      */     
/* 1712 */     if (bytes[0] == 45) {
/* 1713 */       neg = true;
/* 1714 */       start = 1;
/* 1715 */       if (bytes.length == 1 || bytes.length > 19) {
/* 1716 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */     } else {
/* 1719 */       start = 0;
/* 1720 */       neg = false;
/* 1721 */       if (bytes.length > 18) {
/* 1722 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */     } 
/*      */     
/* 1726 */     int periodsSeen = 0;
/*      */ 
/*      */     
/* 1729 */     while (start < bytes.length) {
/* 1730 */       byte b = bytes[start++];
/* 1731 */       if (b >= 48 && b <= 57) {
/* 1732 */         val *= 10L;
/* 1733 */         val += (b - 48); continue;
/*      */       } 
/* 1735 */       if (b != 46) {
/* 1736 */         throw FAST_NUMBER_FAILED;
/*      */       }
/*      */       
/* 1739 */       scale = bytes.length - start;
/* 1740 */       periodsSeen++;
/*      */     } 
/*      */ 
/*      */     
/* 1744 */     int numNonSignChars = neg ? (bytes.length - 1) : bytes.length;
/* 1745 */     if (periodsSeen <= 1 && periodsSeen != numNonSignChars) {
/* 1746 */       if (neg) {
/* 1747 */         val = -val;
/*      */       }
/*      */       
/* 1750 */       return BigDecimal.valueOf(val, scale);
/*      */     } 
/*      */     
/* 1753 */     throw FAST_NUMBER_FAILED;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(int columnIndex) throws SQLException {
/* 1759 */     LOGGER.log(Level.FINEST, "  getFloat columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1760 */     checkResultSet(columnIndex);
/* 1761 */     if (this.wasNullFlag)
/* 1762 */       return 0.0F; 
/* 1763 */     if (isBinary(columnIndex)) {
/* 1764 */       int col = columnIndex - 1;
/* 1765 */       int oid = this.fields[col].getOID();
/* 1766 */       return (oid == 700) ? ByteConverter.float4(this.this_row[col], 0) : (float)readDoubleValue(this.this_row[col], oid, "FLOAT");
/*      */     } 
/* 1768 */     return toFloat(getFixedString(columnIndex));
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int columnIndex) throws SQLException {
/* 1773 */     LOGGER.log(Level.FINEST, "  getDouble columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1774 */     checkResultSet(columnIndex);
/* 1775 */     if (this.wasNullFlag)
/* 1776 */       return 0.0D; 
/* 1777 */     if (isBinary(columnIndex)) {
/* 1778 */       int col = columnIndex - 1;
/* 1779 */       int oid = this.fields[col].getOID();
/* 1780 */       return (oid == 701) ? ByteConverter.float8(this.this_row[col], 0) : readDoubleValue(this.this_row[col], oid, "double");
/*      */     } 
/* 1782 */     return toDouble(getFixedString(columnIndex));
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
/* 1787 */     LOGGER.log(Level.FINEST, "  getBigDecimal columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1788 */     checkResultSet(columnIndex);
/* 1789 */     if (this.wasNullFlag) {
/* 1790 */       return null;
/*      */     }
/* 1792 */     if (isBinary(columnIndex)) {
/* 1793 */       int sqlType = getSQLType(columnIndex);
/* 1794 */       if (sqlType != 2 && sqlType != 3) {
/* 1795 */         Object obj = internalGetObject(columnIndex, this.fields[columnIndex - 1]);
/* 1796 */         if (obj == null) {
/* 1797 */           return null;
/*      */         }
/*      */         
/* 1800 */         if (!(obj instanceof Long) && !(obj instanceof Integer) && !(obj instanceof Byte)) {
/* 1801 */           return toBigDecimal(trimMoney(String.valueOf(obj)), scale);
/*      */         }
/*      */         
/* 1804 */         BigDecimal res = BigDecimal.valueOf(((Number)obj).longValue());
/* 1805 */         res = scaleBigDecimal(res, scale);
/* 1806 */         return res;
/*      */       } 
/*      */     } 
/*      */     
/* 1810 */     Encoding encoding = this.connection.getEncoding();
/* 1811 */     if (encoding.hasAsciiNumbers()) {
/*      */       try {
/* 1813 */         BigDecimal res = getFastBigDecimal(columnIndex);
/* 1814 */         res = scaleBigDecimal(res, scale);
/* 1815 */         return res;
/* 1816 */       } catch (NumberFormatException numberFormatException) {}
/*      */     }
/*      */ 
/*      */     
/* 1820 */     return toBigDecimal(getFixedString(columnIndex), scale);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getBytes(int columnIndex) throws SQLException {
/* 1825 */     LOGGER.log(Level.FINEST, "  getBytes columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1826 */     checkResultSet(columnIndex);
/* 1827 */     if (this.wasNullFlag)
/* 1828 */       return null; 
/* 1829 */     if (isBinary(columnIndex)) {
/* 1830 */       return this.this_row[columnIndex - 1];
/*      */     }
/* 1832 */     return (this.fields[columnIndex - 1].getOID() != 17 && this.fields[columnIndex - 1].getOID() != 11659) ? trimBytes(columnIndex, this.this_row[columnIndex - 1]) : trimBytes(columnIndex, KBbytea.toBytes(this.this_row[columnIndex - 1]));
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(int columnIndex) throws SQLException {
/* 1837 */     LOGGER.log(Level.FINEST, "  getDate columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1838 */     return getDate(columnIndex, (Calendar)null);
/*      */   }
/*      */   
/*      */   public Time getTime(int columnIndex) throws SQLException {
/* 1842 */     LOGGER.log(Level.FINEST, "  getTime columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1843 */     return getTime(columnIndex, (Calendar)null);
/*      */   }
/*      */   
/*      */   public Timestamp getTimestamp(int columnIndex) throws SQLException {
/* 1847 */     LOGGER.log(Level.FINEST, "  getTimestamp columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1848 */     return getTimestamp(columnIndex, (Calendar)null);
/*      */   }
/*      */   
/*      */   public InputStream getAsciiStream(int columnIndex) throws SQLException {
/* 1852 */     LOGGER.log(Level.FINEST, "  getAsciiStream columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1853 */     checkResultSet(columnIndex);
/* 1854 */     if (this.wasNullFlag) {
/* 1855 */       return null;
/*      */     }
/*      */     try {
/* 1858 */       return new ByteArrayInputStream(getString(columnIndex).getBytes("ASCII"));
/* 1859 */     } catch (UnsupportedEncodingException var3) {
/* 1860 */       throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[] { "ASCII" }), KSQLState.UNEXPECTED_ERROR, var3);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getUnicodeStream(int columnIndex) throws SQLException {
/* 1866 */     LOGGER.log(Level.FINEST, "  getUnicodeStream columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1867 */     checkResultSet(columnIndex);
/* 1868 */     if (this.wasNullFlag) {
/* 1869 */       return null;
/*      */     }
/*      */     try {
/* 1872 */       return new ByteArrayInputStream(getString(columnIndex).getBytes("UTF-8"));
/* 1873 */     } catch (UnsupportedEncodingException var3) {
/* 1874 */       throw new KSQLException(GT.tr("The JVM claims not to support the encoding: {0}", new Object[] { "UTF-8" }), KSQLState.UNEXPECTED_ERROR, var3);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getBinaryStream(int columnIndex) throws SQLException {
/* 1880 */     LOGGER.log(Level.FINEST, "  getBinaryStream columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1881 */     checkResultSet(columnIndex);
/* 1882 */     if (this.wasNullFlag) {
/* 1883 */       return null;
/*      */     }
/* 1885 */     byte[] b = getBytes(columnIndex);
/* 1886 */     return (b != null) ? new ByteArrayInputStream(b) : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getString(String columnName) throws SQLException {
/* 1891 */     return getString(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public boolean getBoolean(String columnName) throws SQLException {
/* 1895 */     return getBoolean(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public byte getByte(String columnName) throws SQLException {
/* 1899 */     return getByte(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public short getShort(String columnName) throws SQLException {
/* 1903 */     return getShort(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public int getInt(String columnName) throws SQLException {
/* 1907 */     return getInt(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public long getLong(String columnName) throws SQLException {
/* 1911 */     return getLong(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public float getFloat(String columnName) throws SQLException {
/* 1915 */     return getFloat(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public double getDouble(String columnName) throws SQLException {
/* 1919 */     return getDouble(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
/* 1923 */     return getBigDecimal(findColumn(columnName), scale);
/*      */   }
/*      */   
/*      */   public byte[] getBytes(String columnName) throws SQLException {
/* 1927 */     return getBytes(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public Date getDate(String columnName) throws SQLException {
/* 1931 */     return getDate(findColumn(columnName), (Calendar)null);
/*      */   }
/*      */   
/*      */   public Time getTime(String columnName) throws SQLException {
/* 1935 */     return getTime(findColumn(columnName), (Calendar)null);
/*      */   }
/*      */   
/*      */   public Timestamp getTimestamp(String columnName) throws SQLException {
/* 1939 */     return getTimestamp(findColumn(columnName), (Calendar)null);
/*      */   }
/*      */   
/*      */   public InputStream getAsciiStream(String columnName) throws SQLException {
/* 1943 */     return getAsciiStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public InputStream getUnicodeStream(String columnName) throws SQLException {
/* 1947 */     return getUnicodeStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public InputStream getBinaryStream(String columnName) throws SQLException {
/* 1951 */     return getBinaryStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public SQLWarning getWarnings() throws SQLException {
/* 1955 */     checkClosed();
/* 1956 */     return this.warnings;
/*      */   }
/*      */   
/*      */   public void clearWarnings() throws SQLException {
/* 1960 */     checkClosed();
/* 1961 */     this.warnings = null;
/*      */   }
/*      */   
/*      */   protected void addWarning(SQLWarning warnings) {
/* 1965 */     if (this.warnings != null) {
/* 1966 */       this.warnings.setNextWarning(warnings);
/*      */     } else {
/* 1968 */       this.warnings = warnings;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCursorName() throws SQLException {
/* 1974 */     checkClosed();
/* 1975 */     return null;
/*      */   }
/*      */   
/*      */   public Object getObject(int columnIndex) throws SQLException {
/* 1979 */     LOGGER.log(Level.FINEST, "  getObject columnIndex: {0}", Integer.valueOf(columnIndex));
/* 1980 */     checkResultSet(columnIndex);
/* 1981 */     if (this.wasNullFlag) {
/* 1982 */       return null;
/*      */     }
/* 1984 */     Field field = this.fields[columnIndex - 1];
/* 1985 */     if (field == null) {
/* 1986 */       this.wasNullFlag = true;
/* 1987 */       return null;
/*      */     } 
/* 1989 */     Object result = internalGetObject(columnIndex, field);
/* 1990 */     if (result != null) {
/* 1991 */       return result;
/*      */     }
/* 1993 */     return isBinary(columnIndex) ? this.connection.getObject(getKBType(columnIndex), (String)null, this.this_row[columnIndex - 1]) : this.connection.getObject(getKBType(columnIndex), getString(columnIndex), (byte[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(String columnName) throws SQLException {
/* 2000 */     return getObject(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public int findColumn(String columnName) throws SQLException {
/* 2004 */     checkClosed();
/* 2005 */     int col = findColumnIndex(columnName);
/* 2006 */     if (col == 0) {
/* 2007 */       throw new KSQLException(GT.tr("The column name {0} was not found in this ResultSet.", new Object[] { columnName }), KSQLState.UNDEFINED_COLUMN);
/*      */     }
/* 2009 */     return col;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, Integer> createColumnNameIndexMap(Field[] fields, boolean isSanitiserDisabled) {
/* 2014 */     Map<String, Integer> columnNameIndexMap = new HashMap<>(fields.length * 2);
/*      */     
/* 2016 */     for (int i = fields.length - 1; i >= 0; i--) {
/* 2017 */       String columnLabel = fields[i].getColumnLabel();
/* 2018 */       if (isSanitiserDisabled) {
/* 2019 */         columnNameIndexMap.put(columnLabel, Integer.valueOf(i + 1));
/*      */       } else {
/* 2021 */         columnNameIndexMap.put(columnLabel.toUpperCase(Locale.US), Integer.valueOf(i + 1));
/*      */       } 
/*      */     } 
/*      */     
/* 2025 */     return columnNameIndexMap;
/*      */   }
/*      */   
/*      */   private int findColumnIndex(String columnName) {
/* 2029 */     if (this.columnNameIndexMap == null) {
/* 2030 */       if (this.originalQuery != null) {
/* 2031 */         this.columnNameIndexMap = this.originalQuery.getResultSetColumnNameIndexMap();
/*      */       }
/*      */       
/* 2034 */       if (this.columnNameIndexMap == null) {
/* 2035 */         this.columnNameIndexMap = createColumnNameIndexMap(this.fields, this.connection.isColumnSanitiserDisabled());
/*      */       }
/*      */     } 
/*      */     
/* 2039 */     Integer index = this.columnNameIndexMap.get(columnName);
/* 2040 */     if (index != null) {
/* 2041 */       return index.intValue();
/*      */     }
/* 2043 */     index = this.columnNameIndexMap.get(columnName.toLowerCase(Locale.US));
/* 2044 */     if (index != null) {
/* 2045 */       this.columnNameIndexMap.put(columnName, index);
/* 2046 */       return index.intValue();
/*      */     } 
/* 2048 */     index = this.columnNameIndexMap.get(columnName.toUpperCase(Locale.US));
/* 2049 */     if (index != null) {
/* 2050 */       this.columnNameIndexMap.put(columnName, index);
/* 2051 */       return index.intValue();
/*      */     } 
/* 2053 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getColumnOID(int field) {
/* 2060 */     return this.fields[field - 1].getOID();
/*      */   }
/*      */   
/*      */   public String getFixedString(int col) throws SQLException {
/* 2064 */     return trimMoney(getString(col));
/*      */   }
/*      */   
/*      */   private String trimMoney(String s) {
/* 2068 */     if (s == null)
/* 2069 */       return null; 
/* 2070 */     if (s.length() < 2) {
/* 2071 */       return s;
/*      */     }
/* 2073 */     char ch = s.charAt(0);
/* 2074 */     if (ch > '-') {
/* 2075 */       return s;
/*      */     }
/* 2077 */     if (ch == '(') {
/* 2078 */       s = "-" + KBtokenizer.removePara(s).substring(1);
/* 2079 */     } else if (ch == '$') {
/* 2080 */       s = s.substring(1);
/* 2081 */     } else if (ch == '-' && s.charAt(1) == '$') {
/* 2082 */       s = "-" + s.substring(2);
/*      */     } 
/*      */     
/* 2085 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getKBType(int column) throws SQLException {
/* 2091 */     Field field = this.fields[column - 1];
/* 2092 */     initSqlType(field);
/* 2093 */     return field.getKBType();
/*      */   }
/*      */   
/*      */   protected int getSQLType(int column) throws SQLException {
/* 2097 */     Field field = this.fields[column - 1];
/* 2098 */     initSqlType(field);
/* 2099 */     return field.getSQLType();
/*      */   }
/*      */   
/*      */   private void initSqlType(Field field) throws SQLException {
/* 2103 */     if (!field.isTypeInitialized()) {
/* 2104 */       TypeInfo typeInfo = this.connection.getTypeInfo();
/* 2105 */       int oid = field.getOID();
/* 2106 */       String pgType = typeInfo.getKBType(oid);
/* 2107 */       int sqlType = typeInfo.getSQLType(pgType);
/* 2108 */       field.setSQLType(sqlType);
/* 2109 */       field.setKBType(pgType);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkUpdateable() throws SQLException {
/* 2114 */     checkClosed();
/* 2115 */     if (!isUpdateable()) {
/* 2116 */       throw new KSQLException(GT.tr("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     }
/* 2118 */     if (this.updateValues == null) {
/* 2119 */       this.updateValues = new HashMap<>((int)(this.fields.length / 0.75D), 0.75F);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkClosed() throws SQLException {
/* 2126 */     if (this.rows == null) {
/* 2127 */       throw new KSQLException(GT.tr("This ResultSet is closed.", new Object[0]), KSQLState.OBJECT_NOT_IN_STATE);
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean isResultSetClosed() {
/* 2132 */     return (this.rows == null);
/*      */   }
/*      */   
/*      */   protected void checkColumnIndex(int column) throws SQLException {
/* 2136 */     if (column < 1 || column > this.fields.length) {
/* 2137 */       throw new KSQLException(GT.tr("The column index is out of range: {0}, number of columns: {1}.", new Object[] { Integer.valueOf(column), Integer.valueOf(this.fields.length) }), KSQLState.INVALID_PARAMETER_VALUE);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void checkResultSet(int column) throws SQLException {
/* 2142 */     checkClosed();
/* 2143 */     if (this.this_row == null) {
/* 2144 */       throw new KSQLException(GT.tr("ResultSet not positioned properly, perhaps you need to call next.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     }
/* 2146 */     checkColumnIndex(column);
/* 2147 */     this.wasNullFlag = (this.this_row[column - 1] == null);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isBinary(int column) {
/* 2152 */     return (this.fields[column - 1].getFormat() == 1);
/*      */   }
/*      */   
/*      */   public static int toInt(String s) throws SQLException {
/* 2156 */     if (s != null) {
/*      */       try {
/* 2158 */         s = s.trim();
/* 2159 */         return Integer.parseInt(s);
/* 2160 */       } catch (NumberFormatException var7) {
/*      */         try {
/* 2162 */           BigDecimal n = new BigDecimal(s);
/* 2163 */           BigInteger i = n.toBigInteger();
/* 2164 */           int gt = i.compareTo(INTMAX);
/* 2165 */           int lt = i.compareTo(INTMIN);
/* 2166 */           if (gt <= 0 && lt >= 0) {
/* 2167 */             return i.intValue();
/*      */           }
/* 2169 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "INT", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         }
/* 2171 */         catch (NumberFormatException var6) {
/* 2172 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "INT", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         } 
/*      */       } 
/*      */     }
/* 2176 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long toLong(String s) throws SQLException {
/* 2181 */     if (s != null) {
/*      */       try {
/* 2183 */         s = s.trim();
/* 2184 */         return Long.parseLong(s);
/* 2185 */       } catch (NumberFormatException var7) {
/*      */         try {
/* 2187 */           BigDecimal n = new BigDecimal(s);
/* 2188 */           BigInteger i = n.toBigInteger();
/* 2189 */           int gt = i.compareTo(LONGMAX);
/* 2190 */           int lt = i.compareTo(LONGMIN);
/* 2191 */           if (gt <= 0 && lt >= 0) {
/* 2192 */             return i.longValue();
/*      */           }
/* 2194 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "long", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         }
/* 2196 */         catch (NumberFormatException var6) {
/* 2197 */           throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "long", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */         } 
/*      */       } 
/*      */     }
/* 2201 */     return 0L;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal toBigDecimal(String s) throws SQLException {
/* 2206 */     if (s == null) {
/* 2207 */       return null;
/*      */     }
/*      */     try {
/* 2210 */       s = s.trim();
/* 2211 */       return new BigDecimal(s);
/* 2212 */     } catch (NumberFormatException var2) {
/* 2213 */       throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "BigDecimal", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal toBigDecimal(String s, int scale) throws SQLException {
/* 2219 */     if (s == null) {
/* 2220 */       return null;
/*      */     }
/* 2222 */     BigDecimal val = toBigDecimal(s);
/* 2223 */     return scaleBigDecimal(val, scale);
/*      */   }
/*      */ 
/*      */   
/*      */   private BigDecimal scaleBigDecimal(BigDecimal val, int scale) throws KSQLException {
/* 2228 */     if (scale == -1) {
/* 2229 */       return val;
/*      */     }
/*      */     try {
/* 2232 */       return val.setScale(scale);
/* 2233 */     } catch (ArithmeticException var4) {
/* 2234 */       throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "BigDecimal", val }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static float toFloat(String s) throws SQLException {
/* 2240 */     if (s != null) {
/*      */       try {
/* 2242 */         s = s.trim();
/* 2243 */         return Float.parseFloat(s);
/* 2244 */       } catch (NumberFormatException var2) {
/* 2245 */         throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "FLOAT", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */       } 
/*      */     }
/* 2248 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static double toDouble(String s) throws SQLException {
/* 2253 */     if (s != null) {
/*      */       try {
/* 2255 */         s = s.trim();
/* 2256 */         return Double.parseDouble(s);
/* 2257 */       } catch (NumberFormatException var2) {
/* 2258 */         throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { "double", s }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */       } 
/*      */     }
/* 2261 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   private void initRowBuffer() {
/* 2266 */     this.this_row = this.rows.get(this.current_row);
/* 2267 */     if (this.resultsetconcurrency == 1008) {
/* 2268 */       this.rowBuffer = new byte[this.this_row.length][];
/* 2269 */       System.arraycopy(this.this_row, 0, this.rowBuffer, 0, this.this_row.length);
/*      */     } else {
/* 2271 */       this.rowBuffer = (byte[][])null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isColumnTrimmable(int columnIndex) throws SQLException {
/* 2277 */     switch (getSQLType(columnIndex)) {
/*      */       case -4:
/*      */       case -3:
/*      */       case -2:
/*      */       case -1:
/*      */       case 1:
/*      */       case 12:
/* 2284 */         return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2297 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private byte[] trimBytes(int p_columnIndex, byte[] p_bytes) throws SQLException {
/* 2302 */     if (this.maxFieldSize > 0 && p_bytes.length > this.maxFieldSize && isColumnTrimmable(p_columnIndex)) {
/* 2303 */       byte[] l_bytes = new byte[this.maxFieldSize];
/* 2304 */       System.arraycopy(p_bytes, 0, l_bytes, 0, this.maxFieldSize);
/* 2305 */       return l_bytes;
/*      */     } 
/* 2307 */     return p_bytes;
/*      */   }
/*      */ 
/*      */   
/*      */   private String trimString(int p_columnIndex, String p_string) throws SQLException {
/* 2312 */     return (this.maxFieldSize > 0 && p_string.length() > this.maxFieldSize && isColumnTrimmable(p_columnIndex)) ? p_string.substring(0, this.maxFieldSize) : p_string;
/*      */   }
/*      */   
/*      */   private double readDoubleValue(byte[] bytes, int oid, String targetType) throws KSQLException {
/* 2316 */     switch (oid) {
/*      */       case 9:
/* 2318 */         return ByteConverter.int1(bytes, 0);
/*      */       case 20:
/* 2320 */         return ByteConverter.int8(bytes, 0);
/*      */       case 21:
/* 2322 */         return ByteConverter.int2(bytes, 0);
/*      */       case 23:
/* 2324 */         return ByteConverter.int4(bytes, 0);
/*      */       case 700:
/* 2326 */         return ByteConverter.float4(bytes, 0);
/*      */       case 701:
/* 2328 */         return ByteConverter.float8(bytes, 0);
/*      */     } 
/* 2330 */     throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), targetType }), KSQLState.DATA_TYPE_MISMATCH);
/*      */   }
/*      */ 
/*      */   
/*      */   private long readLongValue(byte[] bytes, int oid, long minVal, long maxVal, String targetType) throws KSQLException {
/*      */     long val;
/* 2336 */     switch (oid) {
/*      */       case 9:
/* 2338 */         val = ByteConverter.int1(bytes, 0);
/*      */         break;
/*      */       case 20:
/* 2341 */         val = ByteConverter.int8(bytes, 0);
/*      */         break;
/*      */       case 21:
/* 2344 */         val = ByteConverter.int2(bytes, 0);
/*      */         break;
/*      */       case 23:
/* 2347 */         val = ByteConverter.int4(bytes, 0);
/*      */         break;
/*      */       case 700:
/* 2350 */         val = (long)ByteConverter.float4(bytes, 0);
/*      */         break;
/*      */       case 701:
/* 2353 */         val = (long)ByteConverter.float8(bytes, 0);
/*      */         break;
/*      */       default:
/* 2356 */         throw new KSQLException(GT.tr("Cannot convert the column of type {0} to requested type {1}.", new Object[] { Oid.toString(oid), targetType }), KSQLState.DATA_TYPE_MISMATCH);
/*      */     } 
/*      */     
/* 2359 */     if (val >= minVal && val <= maxVal) {
/* 2360 */       return val;
/*      */     }
/* 2362 */     throw new KSQLException(GT.tr("Bad value for type {0} : {1}", new Object[] { targetType, Long.valueOf(val) }), KSQLState.NUMERIC_VALUE_OUT_OF_RANGE);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateValue(int columnIndex, Object value) throws SQLException {
/* 2367 */     checkUpdateable();
/* 2368 */     if (!this.onInsertRow && (isBeforeFirst() || isAfterLast() || this.rows.isEmpty())) {
/* 2369 */       throw new KSQLException(GT.tr("Cannot update the ResultSet because it is either before the start or after the end of the results.", new Object[0]), KSQLState.INVALID_CURSOR_STATE);
/*      */     }
/* 2371 */     checkColumnIndex(columnIndex);
/* 2372 */     this.doingUpdates = !this.onInsertRow;
/* 2373 */     if (value == null) {
/* 2374 */       updateNull(columnIndex);
/*      */     } else {
/* 2376 */       KBResultSetMetaData md = (KBResultSetMetaData)getMetaData();
/* 2377 */       this.updateValues.put(md.getBaseColumnName(columnIndex), value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object getUUID(String data) throws SQLException {
/*      */     try {
/* 2385 */       UUID uuid = UUID.fromString(data);
/* 2386 */       return uuid;
/* 2387 */     } catch (IllegalArgumentException var4) {
/* 2388 */       throw new KSQLException(GT.tr("Invalid UUID data.", new Object[0]), KSQLState.INVALID_PARAMETER_VALUE, var4);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Object getUUID(byte[] data) throws SQLException {
/* 2393 */     return new UUID(ByteConverter.int8(data, 0), ByteConverter.int8(data, 8));
/*      */   }
/*      */   
/*      */   void addRows(List<byte[][]> tuples) {
/* 2397 */     this.rows.addAll(tuples);
/*      */   }
/*      */   
/*      */   public void updateRef(int columnIndex, Ref x) throws SQLException {
/* 2401 */     throw Driver.notImplemented(getClass(), "updateRef(int,Ref)");
/*      */   }
/*      */   
/*      */   public void updateRef(String columnName, Ref x) throws SQLException {
/* 2405 */     throw Driver.notImplemented(getClass(), "updateRef(String,Ref)");
/*      */   }
/*      */   
/*      */   public void updateBlob(int columnIndex, Blob x) throws SQLException {
/* 2409 */     throw Driver.notImplemented(getClass(), "updateBlob(int,Blob)");
/*      */   }
/*      */   
/*      */   public void updateBlob(String columnName, Blob x) throws SQLException {
/* 2413 */     throw Driver.notImplemented(getClass(), "updateBlob(String,Blob)");
/*      */   }
/*      */   
/*      */   public void updateClob(int columnIndex, Clob x) throws SQLException {
/* 2417 */     throw Driver.notImplemented(getClass(), "updateClob(int,Clob)");
/*      */   }
/*      */   
/*      */   public void updateClob(String columnName, Clob x) throws SQLException {
/* 2421 */     throw Driver.notImplemented(getClass(), "updateClob(String,Clob)");
/*      */   }
/*      */   
/*      */   public void updateArray(int columnIndex, Array x) throws SQLException {
/* 2425 */     updateObject(columnIndex, x);
/*      */   }
/*      */   
/*      */   public void updateArray(String columnName, Array x) throws SQLException {
/* 2429 */     updateArray(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
/* 2433 */     if (type == null) {
/* 2434 */       throw new SQLException("type is null");
/*      */     }
/* 2436 */     int sqlType = getSQLType(columnIndex);
/* 2437 */     if (type == BigDecimal.class) {
/* 2438 */       if (sqlType != 2 && sqlType != 3) {
/* 2439 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2441 */       return type.cast(getBigDecimal(columnIndex));
/*      */     } 
/* 2443 */     if (type == String.class) {
/* 2444 */       if (sqlType != 1 && sqlType != 12) {
/* 2445 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2447 */       return type.cast(getString(columnIndex));
/*      */     } 
/* 2449 */     if (type == Boolean.class) {
/* 2450 */       if (sqlType != 16 && sqlType != -7) {
/* 2451 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2453 */       boolean booleanValue = getBoolean(columnIndex);
/* 2454 */       return wasNull() ? null : type.cast(Boolean.valueOf(booleanValue));
/*      */     } 
/* 2456 */     if (type == Integer.class) {
/* 2457 */       if (sqlType != -6 && sqlType != 5 && sqlType != 4) {
/* 2458 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2460 */       int intValue = getInt(columnIndex);
/* 2461 */       return wasNull() ? null : type.cast(Integer.valueOf(intValue));
/*      */     } 
/* 2463 */     if (type == Long.class) {
/* 2464 */       if (sqlType == -5) {
/* 2465 */         long longValue = getLong(columnIndex);
/* 2466 */         return wasNull() ? null : type.cast(Long.valueOf(longValue));
/*      */       } 
/* 2468 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2470 */     if (type == Float.class) {
/* 2471 */       if (sqlType == 7) {
/* 2472 */         float floatValue = getFloat(columnIndex);
/* 2473 */         return wasNull() ? null : type.cast(Float.valueOf(floatValue));
/*      */       } 
/* 2475 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2477 */     if (type == Double.class) {
/* 2478 */       if (sqlType != 6 && sqlType != 8) {
/* 2479 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2481 */       double doubleValue = getDouble(columnIndex);
/* 2482 */       return wasNull() ? null : type.cast(Double.valueOf(doubleValue));
/*      */     } 
/* 2484 */     if (type == Date.class) {
/* 2485 */       if (sqlType == 91) {
/* 2486 */         return type.cast(getDate(columnIndex));
/*      */       }
/* 2488 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2490 */     if (type == Time.class) {
/* 2491 */       if (sqlType == 92) {
/* 2492 */         return type.cast(getTime(columnIndex));
/*      */       }
/* 2494 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2496 */     if (type == Timestamp.class) {
/* 2497 */       if (sqlType != 93 && sqlType != 2014) {
/* 2498 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2500 */       return type.cast(getTimestamp(columnIndex));
/*      */     } 
/*      */ 
/*      */     
/* 2504 */     if (type == Calendar.class) {
/* 2505 */       if (sqlType != 93 && sqlType != 2014) {
/* 2506 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2508 */       Timestamp timestampValue = getTimestamp(columnIndex);
/* 2509 */       Calendar calendar = Calendar.getInstance(getDefaultCalendar().getTimeZone());
/* 2510 */       calendar.setTimeInMillis(timestampValue.getTime());
/* 2511 */       return type.cast(calendar);
/*      */     } 
/* 2513 */     if (type == Blob.class) {
/* 2514 */       if (sqlType != 2004 && sqlType != -2) {
/* 2515 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2517 */       return type.cast(getBlob(columnIndex));
/*      */     } 
/* 2519 */     if (type == Clob.class) {
/* 2520 */       if (sqlType != 2005 && sqlType != 12) {
/* 2521 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2523 */       return type.cast(getClob(columnIndex));
/*      */     } 
/* 2525 */     if (type == NClob.class) {
/* 2526 */       if (sqlType == 2011) {
/* 2527 */         return type.cast(getNClob(columnIndex));
/*      */       }
/* 2529 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2531 */     if (type == Array.class) {
/* 2532 */       if (sqlType == 2003) {
/* 2533 */         return type.cast(getArray(columnIndex));
/*      */       }
/* 2535 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2537 */     if (type == SQLXML.class) {
/* 2538 */       if (sqlType == 2009) {
/* 2539 */         return type.cast(getSQLXML(columnIndex));
/*      */       }
/* 2541 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2543 */     if (type == UUID.class) {
/* 2544 */       return type.cast(getObject(columnIndex));
/*      */     }
/*      */     
/* 2547 */     if (type == InetAddress.class) {
/* 2548 */       Object object = getObject(columnIndex);
/* 2549 */       if (object == null) {
/* 2550 */         return null;
/*      */       }
/*      */       try {
/* 2553 */         return type.cast(InetAddress.getByName(((KBobject)object).getValue()));
/* 2554 */       } catch (UnknownHostException var6) {
/* 2555 */         throw new SQLException("could not create inet address from string '" + object + "'");
/*      */       } 
/*      */     } 
/* 2558 */     if (type == LocalDate.class) {
/* 2559 */       if (sqlType == 91) {
/* 2560 */         Date dateValue = getDate(columnIndex);
/* 2561 */         return wasNull() ? null : type.cast(dateValue.toLocalDate());
/*      */       } 
/* 2563 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2565 */     if (type == LocalTime.class) {
/* 2566 */       if (sqlType == 92) {
/* 2567 */         return type.cast(getLocalTime(columnIndex));
/*      */       }
/* 2569 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2571 */     if (type == LocalDateTime.class) {
/* 2572 */       if (sqlType == 93) {
/* 2573 */         return type.cast(getLocalDateTime(columnIndex));
/*      */       }
/* 2575 */       throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */     } 
/* 2577 */     if (type == OffsetDateTime.class) {
/* 2578 */       if (sqlType != 2014 && sqlType != 93) {
/* 2579 */         throw new SQLException("conversion to " + type + " from " + sqlType + " not supported");
/*      */       }
/* 2581 */       Timestamp timestampValue = getTimestamp(columnIndex);
/* 2582 */       if (wasNull()) {
/* 2583 */         return null;
/*      */       }
/* 2585 */       OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(timestampValue.toInstant(), ZoneOffset.UTC);
/* 2586 */       return type.cast(offsetDateTime);
/*      */     } 
/*      */     
/* 2589 */     if (KBobject.class.isAssignableFrom(type)) {
/* 2590 */       Object object; if (isBinary(columnIndex)) {
/* 2591 */         object = this.connection.getObject(getKBType(columnIndex), (String)null, this.this_row[columnIndex - 1]);
/*      */       } else {
/* 2593 */         object = this.connection.getObject(getKBType(columnIndex), getString(columnIndex), (byte[])null);
/*      */       } 
/*      */       
/* 2596 */       return type.cast(object);
/*      */     } 
/* 2598 */     throw new SQLException("unsupported conversion to " + type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
/* 2606 */     return getObject(findColumn(columnLabel), type);
/*      */   }
/*      */   
/*      */   public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
/* 2610 */     return getObjectImpl(s, map);
/*      */   }
/*      */   
/*      */   public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
/* 2614 */     return getObjectImpl(i, map);
/*      */   }
/*      */   
/*      */   public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
/* 2618 */     throw Driver.notImplemented(getClass(), "updateObject");
/*      */   }
/*      */   
/*      */   public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
/* 2622 */     throw Driver.notImplemented(getClass(), "updateObject");
/*      */   }
/*      */   
/*      */   public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
/* 2626 */     throw Driver.notImplemented(getClass(), "updateObject");
/*      */   }
/*      */   
/*      */   public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
/* 2630 */     throw Driver.notImplemented(getClass(), "updateObject");
/*      */   }
/*      */   
/*      */   public RowId getRowId(int columnIndex) throws SQLException {
/* 2634 */     LOGGER.log(Level.FINEST, "  getRowId columnIndex: {0}", Integer.valueOf(columnIndex));
/* 2635 */     throw Driver.notImplemented(getClass(), "getRowId(int)");
/*      */   }
/*      */   
/*      */   public RowId getRowId(String columnName) throws SQLException {
/* 2639 */     return getRowId(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public void updateRowId(int columnIndex, RowId x) throws SQLException {
/* 2643 */     throw Driver.notImplemented(getClass(), "updateRowId(int, RowId)");
/*      */   }
/*      */   
/*      */   public void updateRowId(String columnName, RowId x) throws SQLException {
/* 2647 */     updateRowId(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public int getHoldability() throws SQLException {
/* 2651 */     throw Driver.notImplemented(getClass(), "getHoldability()");
/*      */   }
/*      */   
/*      */   public boolean isClosed() throws SQLException {
/* 2655 */     return (this.rows == null);
/*      */   }
/*      */   
/*      */   public void updateNString(int columnIndex, String nString) throws SQLException {
/* 2659 */     throw Driver.notImplemented(getClass(), "updateNString(int, String)");
/*      */   }
/*      */   
/*      */   public void updateNString(String columnName, String nString) throws SQLException {
/* 2663 */     updateNString(findColumn(columnName), nString);
/*      */   }
/*      */   
/*      */   public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
/* 2667 */     throw Driver.notImplemented(getClass(), "updateNClob(int, NClob)");
/*      */   }
/*      */   
/*      */   public void updateNClob(String columnName, NClob nClob) throws SQLException {
/* 2671 */     updateNClob(findColumn(columnName), nClob);
/*      */   }
/*      */   
/*      */   public void updateNClob(int columnIndex, Reader reader) throws SQLException {
/* 2675 */     throw Driver.notImplemented(getClass(), "updateNClob(int, Reader)");
/*      */   }
/*      */   
/*      */   public void updateNClob(String columnName, Reader reader) throws SQLException {
/* 2679 */     updateNClob(findColumn(columnName), reader);
/*      */   }
/*      */   
/*      */   public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
/* 2683 */     throw Driver.notImplemented(getClass(), "updateNClob(int, Reader, long)");
/*      */   }
/*      */   
/*      */   public void updateNClob(String columnName, Reader reader, long length) throws SQLException {
/* 2687 */     updateNClob(findColumn(columnName), reader, length);
/*      */   }
/*      */   
/*      */   public NClob getNClob(int columnIndex) throws SQLException {
/* 2691 */     LOGGER.log(Level.FINEST, "  getNClob columnIndex: {0}", Integer.valueOf(columnIndex));
/* 2692 */     throw Driver.notImplemented(getClass(), "getNClob(int)");
/*      */   }
/*      */   
/*      */   public NClob getNClob(String columnName) throws SQLException {
/* 2696 */     return getNClob(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
/* 2700 */     throw Driver.notImplemented(getClass(), "updateBlob(int, InputStream, long)");
/*      */   }
/*      */   
/*      */   public void updateBlob(String columnName, InputStream inputStream, long length) throws SQLException {
/* 2704 */     updateBlob(findColumn(columnName), inputStream, length);
/*      */   }
/*      */   
/*      */   public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
/* 2708 */     throw Driver.notImplemented(getClass(), "updateBlob(int, InputStream)");
/*      */   }
/*      */   
/*      */   public void updateBlob(String columnName, InputStream inputStream) throws SQLException {
/* 2712 */     updateBlob(findColumn(columnName), inputStream);
/*      */   }
/*      */   
/*      */   public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
/* 2716 */     throw Driver.notImplemented(getClass(), "updateClob(int, Reader, long)");
/*      */   }
/*      */   
/*      */   public void updateClob(String columnName, Reader reader, long length) throws SQLException {
/* 2720 */     updateClob(findColumn(columnName), reader, length);
/*      */   }
/*      */   
/*      */   public void updateClob(int columnIndex, Reader reader) throws SQLException {
/* 2724 */     throw Driver.notImplemented(getClass(), "updateClob(int, Reader)");
/*      */   }
/*      */   
/*      */   public void updateClob(String columnName, Reader reader) throws SQLException {
/* 2728 */     updateClob(findColumn(columnName), reader);
/*      */   }
/*      */   
/*      */   public SQLXML getSQLXML(int columnIndex) throws SQLException {
/* 2732 */     LOGGER.log(Level.FINEST, "  getSQLXML columnIndex: {0}", Integer.valueOf(columnIndex));
/* 2733 */     String data = getString(columnIndex);
/* 2734 */     return (data == null) ? null : (SQLXML)new KbSQLXML(this.connection, data);
/*      */   }
/*      */   
/*      */   public SQLXML getSQLXML(String columnName) throws SQLException {
/* 2738 */     return getSQLXML(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
/* 2742 */     updateValue(columnIndex, xmlObject);
/*      */   }
/*      */   
/*      */   public void updateSQLXML(String columnName, SQLXML xmlObject) throws SQLException {
/* 2746 */     updateSQLXML(findColumn(columnName), xmlObject);
/*      */   }
/*      */   
/*      */   public String getNString(int columnIndex) throws SQLException {
/* 2750 */     LOGGER.log(Level.FINEST, "  getNString columnIndex: {0}", Integer.valueOf(columnIndex));
/* 2751 */     throw Driver.notImplemented(getClass(), "getNString(int)");
/*      */   }
/*      */   
/*      */   public String getNString(String columnName) throws SQLException {
/* 2755 */     return getNString(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public Reader getNCharacterStream(int columnIndex) throws SQLException {
/* 2759 */     LOGGER.log(Level.FINEST, "  getNCharacterStream columnIndex: {0}", Integer.valueOf(columnIndex));
/* 2760 */     throw Driver.notImplemented(getClass(), "getNCharacterStream(int)");
/*      */   }
/*      */   
/*      */   public Reader getNCharacterStream(String columnName) throws SQLException {
/* 2764 */     return getNCharacterStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   public void updateNCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
/* 2768 */     throw Driver.notImplemented(getClass(), "updateNCharacterStream(int, Reader, int)");
/*      */   }
/*      */   
/*      */   public void updateNCharacterStream(String columnName, Reader x, int length) throws SQLException {
/* 2772 */     updateNCharacterStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */   public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
/* 2776 */     throw Driver.notImplemented(getClass(), "updateNCharacterStream(int, Reader)");
/*      */   }
/*      */   
/*      */   public void updateNCharacterStream(String columnName, Reader x) throws SQLException {
/* 2780 */     updateNCharacterStream(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */   public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
/* 2784 */     throw Driver.notImplemented(getClass(), "updateNCharacterStream(int, Reader, long)");
/*      */   }
/*      */   
/*      */   public void updateNCharacterStream(String columnName, Reader x, long length) throws SQLException {
/* 2788 */     updateNCharacterStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */   public void updateCharacterStream(int columnIndex, Reader reader, long length) throws SQLException {
/* 2792 */     throw Driver.notImplemented(getClass(), "updateCharaceterStream(int, Reader, long)");
/*      */   }
/*      */   
/*      */   public void updateCharacterStream(String columnName, Reader reader, long length) throws SQLException {
/* 2796 */     updateCharacterStream(findColumn(columnName), reader, length);
/*      */   }
/*      */   
/*      */   public void updateCharacterStream(int columnIndex, Reader reader) throws SQLException {
/* 2800 */     throw Driver.notImplemented(getClass(), "updateCharaceterStream(int, Reader)");
/*      */   }
/*      */   
/*      */   public void updateCharacterStream(String columnName, Reader reader) throws SQLException {
/* 2804 */     updateCharacterStream(findColumn(columnName), reader);
/*      */   }
/*      */   
/*      */   public void updateBinaryStream(int columnIndex, InputStream inputStream, long length) throws SQLException {
/* 2808 */     throw Driver.notImplemented(getClass(), "updateBinaryStream(int, InputStream, long)");
/*      */   }
/*      */   
/*      */   public void updateBinaryStream(String columnName, InputStream inputStream, long length) throws SQLException {
/* 2812 */     updateBinaryStream(findColumn(columnName), inputStream, length);
/*      */   }
/*      */   
/*      */   public void updateBinaryStream(int columnIndex, InputStream inputStream) throws SQLException {
/* 2816 */     throw Driver.notImplemented(getClass(), "updateBinaryStream(int, InputStream)");
/*      */   }
/*      */   
/*      */   public void updateBinaryStream(String columnName, InputStream inputStream) throws SQLException {
/* 2820 */     updateBinaryStream(findColumn(columnName), inputStream);
/*      */   }
/*      */   
/*      */   public void updateAsciiStream(int columnIndex, InputStream inputStream, long length) throws SQLException {
/* 2824 */     throw Driver.notImplemented(getClass(), "updateAsciiStream(int, InputStream, long)");
/*      */   }
/*      */   
/*      */   public void updateAsciiStream(String columnName, InputStream inputStream, long length) throws SQLException {
/* 2828 */     updateAsciiStream(findColumn(columnName), inputStream, length);
/*      */   }
/*      */   
/*      */   public void updateAsciiStream(int columnIndex, InputStream inputStream) throws SQLException {
/* 2832 */     throw Driver.notImplemented(getClass(), "updateAsciiStream(int, InputStream)");
/*      */   }
/*      */   
/*      */   public void updateAsciiStream(String columnName, InputStream inputStream) throws SQLException {
/* 2836 */     updateAsciiStream(findColumn(columnName), inputStream);
/*      */   }
/*      */   
/*      */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 2840 */     return iface.isAssignableFrom(getClass());
/*      */   }
/*      */   
/*      */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 2844 */     if (iface.isAssignableFrom(getClass())) {
/* 2845 */       return iface.cast(this);
/*      */     }
/* 2847 */     throw new SQLException("Cannot unwrap to " + iface.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   private Calendar getDefaultCalendar() {
/* 2852 */     TimestampUtils timestampUtils = this.connection.getTimestampUtils();
/* 2853 */     if (timestampUtils.hasFastDefaultTimeZone()) {
/* 2854 */       return timestampUtils.getSharedCalendar((TimeZone)null);
/*      */     }
/* 2856 */     Calendar sharedCalendar = timestampUtils.getSharedCalendar(this.defaultTimeZone);
/* 2857 */     if (this.defaultTimeZone == null) {
/* 2858 */       this.defaultTimeZone = sharedCalendar.getTimeZone();
/*      */     }
/*      */     
/* 2861 */     return sharedCalendar;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTupleCount() {
/* 2866 */     return this.rows.size();
/*      */   }
/*      */   
/*      */   static class NullObject extends KBobject {
/*      */     NullObject(String type) {
/* 2871 */       setType(type);
/*      */     }
/*      */     
/*      */     public String getValue() {
/* 2875 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private class PrimaryKey {
/*      */     int index;
/*      */     String name;
/*      */     
/*      */     PrimaryKey(int index, String name) {
/* 2884 */       this.index = index;
/* 2885 */       this.name = name;
/*      */     }
/*      */     
/*      */     Object getValue() throws SQLException {
/* 2889 */       return KbResultSet.this.getObject(this.index);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class CursorResultHandler
/*      */     extends ResultHandlerBase
/*      */   {
/*      */     public void handleResultRows(Query fromQuery, Field[] fields, List<byte[][]> tuples, ResultCursor cursor) {
/* 2898 */       KbResultSet.this.rows = tuples;
/* 2899 */       KbResultSet.this.cursor = cursor;
/*      */     }
/*      */     
/*      */     public void handleCommandStatus(String status, int updateCount, long insertOID) {
/* 2903 */       handleError((SQLException)new KSQLException(GT.tr("Unexpected command status: {0}.", new Object[] { status }), KSQLState.PROTOCOL_VIOLATION));
/*      */     }
/*      */     
/*      */     public void handleCompletion() throws SQLException {
/* 2907 */       SQLWarning warning = getWarning();
/* 2908 */       if (warning != null) {
/* 2909 */         KbResultSet.this.addWarning(warning);
/*      */       }
/*      */       
/* 2912 */       super.handleCompletion();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/com/kingbase8/jdbc/KbResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */