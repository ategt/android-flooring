package com.example.ateg.flooringmaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ateg.flooringmaster.annotations.ApplicationContext;
import com.example.ateg.flooringmaster.annotations.DatabaseInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ATeg on 10/13/2017.
 */

public class AddressClientSqliteImpl extends SQLiteOpenHelper implements AddressDao, AddressClient {

    private static final String TAG = "Address Client SQLite";

    private static final String ADDRESS_TABLE_NAME = "addresses";
    private static final String ADDRESS_COLUMN_ADDRESS_ID = "id";
    private static final String ADDRESS_COLUMN_ADDRESS_FIRST_NAME = "first_name";
    private static final String ADDRESS_COLUMN_ADDRESS_LAST_NAME = "last_name";
    private static final String ADDRESS_COLUMN_ADDRESS_COMPANY = "company";
    private static final String ADDRESS_COLUMN_ADDRESS_STREET_NUMBER = "street_number";
    private static final String ADDRESS_COLUMN_ADDRESS_STREET_NAME = "street_name";
    private static final String ADDRESS_COLUMN_ADDRESS_CITY = "city";
    private static final String ADDRESS_COLUMN_ADDRESS_STATE = "state";
    private static final String ADDRESS_COLUMN_ADDRESS_ZIP = "zip";

    private static final String SQL_INSERT_ADDRESS = "INSERT INTO addresses (first_name, last_name, company, street_number, street_name, city, state, zip) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) RETURNING id";
    private static final String SQL_UPDATE_ADDRESS = "UPDATE addresses SET first_name=?, last_name=?, company=?, street_number=?, street_name=?, city=?, state=?, zip=? WHERE KEY_ROWID=? RETURNING *";
    private static final String SQL_DELETE_ADDRESS = "DELETE FROM addresses WHERE KEY_ROWID = ? RETURNING *";
    private static final String SQL_GET_ADDRESS = "SELECT *, ROWID AS id, ROWID AS id, 1 AS rank FROM addresses WHERE ROWID = ?";
    private static final String SQL_GET_ADDRESS_BY_COMPANY = "SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE company = ?";
    private static final String SQL_GET_ADDRESS_LIST = "SELECT *, ROWID AS id, 1 AS rank FROM addresses";
    private static final String SQL_GET_ADDRESS_COUNT = "SELECT COUNT(*) FROM addresses";

    private static final String SQL_CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS addresses (first_name varchar(45), last_name varchar(45), company varchar(45), street_number varchar(45), street_name varchar(45), city varchar(45), state varchar(45), zip varchar(45))";
    private static final String SQL_DROP_ADDRESS_TABLE = "DROP TABLE IF EXISTS addresses";

    private static final String SQL_ADDRESS_ID_WHERE_CLAUSE = "ROWID = ?";

    private static final String SQL_SORT_ADDRESSES_BY_LAST_NAME_PARTIAL = " ORDER BY rank ASC, LOWER(last_name) ASC, LOWER(first_name) ASC, LOWER(company) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_LAST_NAME_INVERSE_PARTIAL = " ORDER BY rank ASC, LOWER(last_name) DESC, LOWER(first_name) DESC, LOWER(company) DESC, id DESC";
    private static final String SQL_SORT_ADDRESSES_BY_FIRST_NAME_PARTIAL = " ORDER BY rank ASC, LOWER(first_name) ASC, LOWER(last_name) ASC, LOWER(company) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_FIRST_NAME_INVERSE_PARTIAL = " ORDER BY rank ASC, LOWER(first_name) DESC, LOWER(last_name) DESC, LOWER(company) DESC, id DESC";
    private static final String SQL_SORT_ADDRESSES_BY_COMPANY_PARTIAL = " ORDER BY rank ASC, LOWER(company) ASC, LOWER(last_name) ASC, LOWER(first_name) ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_COMPANY_INVERSE_PARTIAL = " ORDER BY rank ASC, LOWER(company) DESC, LOWER(last_name) DESC, LOWER(first_name) DESC, id DESC";
    private static final String SQL_SORT_ADDRESSES_BY_ID_PARTIAL = " ORDER BY rank ASC, id ASC";
    private static final String SQL_SORT_ADDRESSES_BY_ID_INVERSE_PARTIAL = " ORDER BY rank ASC, id DESC";

    private static final String SQL_SEARCH_ADDRESS_BY_FIRST_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         first_name = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(first_name) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(first_name) LIKE (SELECT LOWER(n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(first_name) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_LAST_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         last_name = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(last_name) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(last_name) LIKE (SELECT LOWER(n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(last_name) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' ) LIKE (SELECT '% ' || n || ' %' FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' ) LIKE (SELECT LOWER('% ' || n || ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name ) LIKE (SELECT LOWER('% ' || n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name ) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_COMPANYz = "WITH inputQuery(n) AS (SELECT ?), "
            + "mainQuery AS ( "
            + "SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "company = (SELECT n FROM inputQuery) "
            + "UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "LOWER(company) = (SELECT LOWER(n) FROM inputQuery) "
            + "UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "LOWER(company) LIKE (SELECT LOWER(n || '%') FROM inputQuery) "
            + "UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "LOWER(company) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery) "
            + ") "
            + " SELECT t1.* FROM mainQuery t1 "
            + " JOIN ( "
            + " SELECT id, MIN(rank) min_rank "
            + " FROM mainQuery "
            + " GROUP BY id "
            + " ) t2 "
            + " ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_COMPANY = "WITH inputQuery(n) AS (SELECT ?), mainQuery AS ( SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE company = (SELECT n FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE LOWER(company) = (SELECT LOWER(n) FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER(n || '%') FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery) )  SELECT t1.* FROM mainQuery t1 JOIN ( SELECT id, MIN(rank) min_rank FROM mainQuery GROUP BY id ) t2 ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' ') LIKE (SELECT '% ' || n || ' %' FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' ') LIKE (SELECT LOWER('% ' || n || ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' || company) LIKE (SELECT LOWER('% ' || n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' || company) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_CITY = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         city = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(city) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(city) LIKE (SELECT LOWER(n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(city) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STATE = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         state = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(state) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(state) LIKE (SELECT LOWER(n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(state) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_ZIP = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         zip = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(zip) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(zip) LIKE (SELECT LOWER(n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(zip) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NUMBER = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         street_number = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(street_number) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(street_number) LIKE (SELECT LOWER(n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(street_number) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STREET_NAME = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         street_name = (SELECT n FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(street_name) = (SELECT LOWER(n) FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(street_name) LIKE (SELECT LOWER(n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(street_name) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_STREET = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         ' ' || street_number || ' ' || street_name LIKE (SELECT '% ' || n || ' %' FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || street_number || ' ' || street_name) LIKE (SELECT LOWER('% ' || n || ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || street_number || ' ' || street_name) LIKE (SELECT LOWER('% ' || n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || street_number || ' ' || street_name) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + ") "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_ALL = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "     SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "         ' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip LIKE (SELECT '% ' || n || ' %' FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip) LIKE (SELECT LOWER('% ' || n || ' %') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip) LIKE (SELECT LOWER('% ' || n || '%') FROM inputQuery)"
            + "     UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE "
            + "         LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)"
            + "      ) "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_SEARCH_ADDRESS_BY_ANY = "WITH inputQuery(n) AS (SELECT ?),"
            + " mainQuery AS ("
            + "      SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE "
            + "       	' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ' LIKE (SELECT '% ' || n || ' %' FROM inputQuery) "
            + "      UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE  "
            + "       	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE (SELECT LOWER('% ' || n || ' %') FROM inputQuery) "
            + "      UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE  "
            + "       	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE (SELECT LOWER('% ' || n || '%') FROM inputQuery) "
            + "      UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE  "
            + "       	LOWER(' ' || first_name || ' ' || last_name || ' ' || company || ' '  || street_number || ' ' || street_name || ' ' || city || ' ' || state || ' ' || zip || ' ') LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery) "
            + " ) "
            + "SELECT t1.* FROM mainQuery t1"
            + " JOIN ("
            + "     SELECT id, MIN(rank) min_rank"
            + "     FROM mainQuery"
            + "     GROUP BY id"
            + " ) t2 "
            + "ON t1.id = t2.id AND t1.rank = t2.min_rank";

    private static final String SQL_ADDRESS_NAME_COMPLETION_QUERY = "WITH inputQuery(n) AS (SELECT ?), " +
            " nameOrCompany(col) AS ( " +
            " SELECT (' ' || first_name || ' ' || last_name || ' ') col FROM addresses  " +
            " UNION SELECT ' ' || company || ' ' col FROM addresses ), " +
            " " +
            "  fullQuery AS ( " +
            "  " +
            " SELECT col, 1 rank FROM nameOrCompany WHERE col LIKE (SELECT '% ' || n || ' %' FROM inputQuery)   " +
            "             UNION ALL SELECT col, 2 rank FROM nameOrCompany WHERE LOWER(col) LIKE (SELECT LOWER('% ' || n || ' %') FROM inputQuery)   " +
            "             UNION ALL SELECT col, 3 rank FROM nameOrCompany WHERE LOWER(col) LIKE (SELECT LOWER('% ' || n || '%') FROM inputQuery)   " +
            "             UNION ALL SELECT col, 4 rank FROM nameOrCompany WHERE LOWER(col) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery)   " +
            "   " +
            "  SELECT TRIM(t1.col) col FROM fullQuery t1 " +
            "         JOIN ( " +
            "            SELECT col, MIN(rank) min_rank " +
            "            FROM fullQuery " +
            "            GROUP BY col " +
            "         ) t2  " +
            "   ON t1.col = t2.col AND t1.rank = t2.min_rank " +
            "   ORDER BY t1.rank ASC, t1.col ASC" +
            "   LIMIT ?";

    public AddressClientSqliteImpl(@ApplicationContext Context context,
                                   @DatabaseInfo String dbName,
                                   @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreateStatements(db);
    }

    private void tableCreateStatements(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SQL_CREATE_ADDRESS_TABLE);
        } catch (SQLException ex) {
            Log.d(TAG, "Table creation problem.", ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_ADDRESS_TABLE);
        onCreate(db);
    }

    @Override
    public Address create(Address address) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ADDRESS_COLUMN_ADDRESS_FIRST_NAME, address.getFirstName());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_LAST_NAME, address.getLastName());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_COMPANY, address.getCompany());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_STREET_NUMBER, address.getStreetNumber());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_STREET_NAME, address.getStreetName());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_CITY, address.getCity());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_STATE, address.getState());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_ZIP, address.getZip());

            long id = db.insert(ADDRESS_TABLE_NAME, null, contentValues);

            if (id > Integer.MAX_VALUE) {
                Log.d(TAG, "Database ID returned is too large for Address Object to handle. ID: " + id);
                throw new RuntimeException("Database ID returned is too large for Address Object to handle.");
            } else {
                address.setId((int) id);
                return address;
            }
        } catch (Exception e) {
            Log.d(TAG, "Address creation error.", e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void update(Address address) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ADDRESS_COLUMN_ADDRESS_FIRST_NAME, address.getFirstName());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_LAST_NAME, address.getLastName());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_COMPANY, address.getCompany());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_STREET_NUMBER, address.getStreetNumber());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_STREET_NAME, address.getStreetName());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_CITY, address.getCity());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_STATE, address.getState());
            contentValues.put(ADDRESS_COLUMN_ADDRESS_ZIP, address.getZip());

            int linesUpdated = db.update(ADDRESS_TABLE_NAME, contentValues, SQL_ADDRESS_ID_WHERE_CLAUSE, new String[]{Integer.toString(address.getId())});

            if (linesUpdated != 1) {
                Log.d(TAG, "Database lines updated were outside of expectations. Lines Changed: " + linesUpdated);
                throw new RuntimeException("Database lines updated were outside of expectations. Lines Changed: " + linesUpdated);
            }
        } catch (Exception e) {
            Log.d(TAG, "Address creation error.", e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Address get(Integer id) throws Resources.NotFoundException, NullPointerException {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    SQL_GET_ADDRESS, new String[]{Integer.toString(id)}
            );

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                return rowMapper(cursor);
            } else {
                //throw new Resources.NotFoundException("Address with ID " + id + " does not exist");
                return null;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Address rowMapper(Cursor cursor) {

        try {
            Address address = new Address();

            address.setId(cursor.getInt(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_ID)));

            address.setFirstName(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_FIRST_NAME)));
            address.setLastName(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_LAST_NAME)));
            address.setCompany(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_COMPANY)));
            address.setStreetNumber(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_STREET_NUMBER)));
            address.setStreetName(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_STREET_NAME)));
            address.setCity(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_CITY)));
            address.setState(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_STATE)));
            address.setZip(cursor.getString(cursor.getColumnIndex(ADDRESS_COLUMN_ADDRESS_ZIP)));

            return address;
        } catch (Resources.NotFoundException | NullPointerException ex) {
            return null;
        }
    }

    @NonNull
    private String cursorTypeToString(int type) {
        String typeString;
        switch (type) {
            case Cursor.FIELD_TYPE_NULL:
                typeString = "Null";
                break;
            case Cursor.FIELD_TYPE_BLOB:
                typeString = "Blob";
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                typeString = "Float";
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                typeString = "Integer";
                break;
            case Cursor.FIELD_TYPE_STRING:
                typeString = "String";
                break;
            default:
                typeString = "UNKOWN";
        }
        return typeString;
    }

    @Override
    public Address get(String input) {
        List<Address> addressList = search(new AddressSearchRequest(input, AddressSearchByOptionEnum.ALL), new ResultProperties(AddressSortByEnum.SORT_BY_ID, 0, 1));

        if (addressList.isEmpty())
            return null;
        else
            return addressList.iterator().next();
    }

    @Override
    public List<Address> search(AddressSearchRequest addressSearchRequest, ResultSegment<AddressSortByEnum> addressResultSegment) {
        return search(addressSearchRequest.getSearchText(), addressSearchRequest.searchBy(), addressResultSegment);
    }

    private List<Address> search(AddressSearchRequest addressSearchRequest) {
        return search(addressSearchRequest, null);
    }

    private List<Address> search(String queryString,
                                 AddressSearchByOptionEnum searchOption,
                                 ResultSegment<AddressSortByEnum> addressResultSegment) {

        List<Address> addresses;
        String sqlSearchQuery;

        if (null == searchOption) {
            addresses = list(addressResultSegment);
        } else {
            sqlSearchQuery = determineSqlSearchQuery(searchOption);
            addresses = search(queryString, sqlSearchQuery, addressResultSegment);
        }
        return addresses;
    }

    private List<Address> search(String stringToSearchFor, String sqlQueryToUse, ResultSegment<AddressSortByEnum> addressResultSegment) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = sortAndPaginateQuery(sqlQueryToUse, addressResultSegment);

        try {
//
//            sqLiteDatabase.compileStatement("SELECT 'bill'");
//
//            sqLiteDatabase.compileStatement("WITH mainQuery AS ( SELECT ? ) SELECT * FROM mainQuery");
//
//            sqLiteDatabase.compileStatement("WITH mainQuery AS ( SELECT 'asdf' AS r ) SELECT * FROM mainQuery");
//
//            sqLiteDatabase.compileStatement("WITH inputQuery(n) AS (SELECT ?), mainQuery AS ( SELECT 'asdf' AS r ) SELECT * FROM mainQuery");
//
//            sqLiteDatabase.compileStatement("WITH inputQuery(n) AS (SELECT ?), mainQuery AS ( SELECT 'asdf' AS r ) SELECT t1.* FROM mainQuery t1");
//
//            sqLiteDatabase.compileStatement("WITH inputQuery(n) AS (SELECT ?), mainQuery AS ( SELECT 2 AS id, 1 AS rank ) SELECT t1.* FROM mainQuery t1 JOIN ( SELECT id, MIN(rank) min_rank FROM mainQuery GROUP BY id ) AS t2 ON t1.id = t2.id AND t1.rank = t2.min_rank");
//
//            sqLiteDatabase.compileStatement("WITH inputQuery(n) AS (SELECT ?), mainQuery AS ( SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE company = (SELECT n FROM inputQuery) )  SELECT t1.* FROM mainQuery t1 JOIN ( SELECT id, MIN(rank) min_rank FROM mainQuery GROUP BY id ) t2 ON t1.id = t2.id AND t1.rank = t2.min_rank");
//
//            sqLiteDatabase.compileStatement("WITH inputQuery(n) AS (SELECT ?), mainQuery AS ( SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE company = (SELECT n FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE LOWER(company) = (SELECT LOWER(n) FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER(n || '%') FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery) )  SELECT 'bill'");

            //sqLiteDatabase.compileStatement("WITH inputQuery(n) AS (SELECT ?), mainQuery AS ( SELECT *, ROWID AS id, 1 AS rank FROM addresses WHERE company = (SELECT n FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 2 AS rank FROM addresses WHERE LOWER(company) = (SELECT LOWER(n) FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 3 AS rank FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER(n || '%') FROM inputQuery) UNION ALL SELECT *, ROWID AS id, 4 AS rank FROM addresses WHERE LOWER(company) LIKE (SELECT LOWER('%' || n || '%') FROM inputQuery) )  SELECT t1.* FROM mainQuery t1 JOIN ( SELECT id, MIN(rank) min_rank FROM mainQuery GROUP BY id ) t2 ON t1.id = t2.id AND t1.rank = t2.min_rank");

            Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{stringToSearchFor});

            return mapToList(cursor);
        } catch (android.database.sqlite.SQLiteException ex) {
            System.out.print("\tQuery: " + query + "\n\tParam: " + stringToSearchFor);
            Log.e(TAG, "Query: " + query + "\nParam: " + stringToSearchFor, ex);
            throw ex;
        }
    }

    @NonNull
    @ClosesCursor
    private List<Address> mapToList(Cursor cursor) {
        List<Address> list = new LinkedList<>();

        if (cursor.moveToFirst()) {
            do {
                list.add(rowMapper(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();

        return list;
    }

    @Override
    public Address getByCompany(String company) {
        List<Address> addressList = search(new AddressSearchRequest(company, AddressSearchByOptionEnum.COMPANY));
        return addressList.isEmpty() ? null : addressList.iterator().next();
    }

    @Override
    public Address delete(Integer id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Address result = get(id);
        int rowsAffected = sqLiteDatabase.delete(ADDRESS_TABLE_NAME, SQL_ADDRESS_ID_WHERE_CLAUSE, new String[]{String.valueOf(id)});

        if (rowsAffected == 1)
            return result;
        else
            return null;
    }

    @Override
    public int size() {
        return (int) this.getReadableDatabase().compileStatement(SQL_GET_ADDRESS_COUNT).simpleQueryForLong();
    }

    @Override
    public int size(boolean block) {
        return size();
    }

    @Override
    public int size(AddressSearchRequest addressSearchRequest) {
        if (addressSearchRequest == null) {
            return size();
        }

        String sqlQuery = determineSqlSearchQuery(addressSearchRequest.searchBy());

        final String SQL_ADDRESS_SEARCH_COUNT = new StringBuffer().append("SELECT COUNT(*) AS result FROM (")
                .append(sqlQuery)
                .append(") AS countingQuery")
                .toString();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ADDRESS_SEARCH_COUNT, new String[]{addressSearchRequest.getSearchText()});

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("result"));
        } else {
            return 0;
        }
    }

    @Override
    public int size(boolean block, AddressSearchRequest addressSearchRequest) {
        return size(addressSearchRequest);
    }

    @Override
    public List<Address> getAddressesSortedByParameter(String sortBy) {
        return list(new AddressResultSegment(AddressSortByEnum.parse(sortBy), 0, Integer.MAX_VALUE));
    }

    @Override
    public List<String> getCompletionGuesses(String input, int limit) {
        if (input == null) {
            return null;
        }

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ADDRESS_NAME_COMPLETION_QUERY,
                new String[]{input, limit < 1 ? Integer.toString(Integer.MAX_VALUE) : String.valueOf(limit)});

        if (cursor.moveToFirst()) {

            List<String> results = new LinkedList<>();
            do {
                results.add(cursor.getString(0));
            } while (cursor.moveToNext());
            return results;
        } else
            return null;
    }

    @Override
    public List<Address> list() {
        return null;
    }

    @Override
    public List<Address> list(ResultSegment<AddressSortByEnum> resultProperties) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = sortAndPaginateQuery(SQL_GET_ADDRESS_LIST, resultProperties);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        return mapToList(cursor);
    }

    @Override
    public List<Address> searchByFirstName(String firstName) {
        return search(new AddressSearchRequest(firstName, AddressSearchByOptionEnum.FIRST_NAME));
    }

    @Override
    public List<Address> searchByLastName(String lastName) {
        return search(new AddressSearchRequest(lastName, AddressSearchByOptionEnum.LAST_NAME));
    }

    @Override
    public List<Address> searchByCity(String city) {
        return search(new AddressSearchRequest(city, AddressSearchByOptionEnum.CITY));
    }

    @Override
    public List<Address> searchByCompany(String company) {
        return search(new AddressSearchRequest(company, AddressSearchByOptionEnum.COMPANY));
    }

    @Override
    public List<Address> searchByState(String state) {
        return search(new AddressSearchRequest(state, AddressSearchByOptionEnum.STATE));
    }

    @Override
    public List<Address> searchByZip(String zip) {
        return search(new AddressSearchRequest(zip, AddressSearchByOptionEnum.ZIP));
    }


    private String determineSqlSearchQuery(AddressSearchByOptionEnum searchOption) {
        System.out.println("Using: " + searchOption);

        String sqlSearchQuery;
        switch (searchOption) {
            case LAST_NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_LAST_NAME;
                break;
            case FIRST_NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_FIRST_NAME;
                break;
            case COMPANY:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_COMPANY;
                break;
            case CITY:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_CITY;
                break;
            case STATE:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STATE;
                break;
            case STREET_NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET_NAME;
                break;
            case STREET_NUMBER:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET_NUMBER;
                break;
            case STREET:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_STREET;
                break;
            case ZIP:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ZIP;
                break;
            case NAME:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_NAME;
                break;
            case NAME_OR_COMPANY:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_NAME_OR_COMPANY;
                break;
            case ALL:
            case DEFAULT:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ALL;
                break;
            default:
                sqlSearchQuery = SQL_SEARCH_ADDRESS_BY_ANY;
                break;
        }
        return sqlSearchQuery;
    }

    private String paginateQuery(String query, Integer page, Integer resultsPerPage) {
        if (page == null || resultsPerPage == null) {
            return query;
        }

        if (page < 0 || resultsPerPage < 0) {
            return query;
        }

        if (query.contains(";")) {
            throw new UnsupportedOperationException("Pagination Method can not handle semi-colons(;)");
        }

        long offset = (long) resultsPerPage * (long) page;

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("SELECT * FROM (");
        stringBuffer.append(query);
        stringBuffer.append(") AS innerQuery LIMIT ");
        stringBuffer.append(resultsPerPage);
        stringBuffer.append(" OFFSET ");
        stringBuffer.append(offset);

        return stringBuffer.toString();
    }

    private String sortQuery(String query, AddressSortByEnum sortByEnum) {
        if (sortByEnum == null) {
            return query;
        }

        if (query.contains(";")) {
            throw new UnsupportedOperationException("Sorting Method can not handle semi-colons(;)");
        }

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("SELECT * FROM (");
        stringBuffer.append(query);
        stringBuffer.append(") AS preSortedQuery");

        switch (sortByEnum) {
            case SORT_BY_LAST_NAME:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_LAST_NAME_PARTIAL);
                break;
            case SORT_BY_LAST_NAME_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_LAST_NAME_INVERSE_PARTIAL);
                break;
            case SORT_BY_FIRST_NAME:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_FIRST_NAME_PARTIAL);
                break;
            case SORT_BY_FIRST_NAME_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_FIRST_NAME_INVERSE_PARTIAL);
                break;
            case SORT_BY_COMPANY:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_COMPANY_PARTIAL);
                break;
            case SORT_BY_COMPANY_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_COMPANY_INVERSE_PARTIAL);
                break;
            case SORT_BY_ID_INVERSE:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_ID_INVERSE_PARTIAL);
                break;
            case SORT_BY_ID:
            default:
                stringBuffer.append(SQL_SORT_ADDRESSES_BY_ID_PARTIAL);
                break;
        }

        return stringBuffer.toString();
    }

    private String sortAndPaginateQuery(String query, AddressSortByEnum sortByEnum, Integer page, Integer resultsPerPage) {
        return paginateQuery(sortQuery(query, sortByEnum), page, resultsPerPage);
    }

    private String sortAndPaginateQuery(String query, ResultSegment<AddressSortByEnum> addressResultSegment) {
        if (addressResultSegment == null) {
            return query;
        }
        return sortAndPaginateQuery(query, addressResultSegment.getSortByEnum(), addressResultSegment.getPageNumber(), addressResultSegment.getResultsPerPage());
    }
}