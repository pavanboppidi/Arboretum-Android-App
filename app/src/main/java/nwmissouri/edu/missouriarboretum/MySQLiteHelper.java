package nwmissouri.edu.missouriarboretum;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by S518637 on 10/30/2014.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "arboretum";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_ARB_TABLE1 = "CREATE TABLE commemrativetable ( " +
                "comid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "treeid TEXT FORIEGN KEY AUTOINCREMENT, " +
                "donorid TEXT FORIEGN KEY AUTOINCREMENT, " +
                "type INTEGER NOT NULL, " +
                "cfname TEXT NOT NULL, " +
                "clname TEXT NOT NULL, " +
                "company TEXT NOT NULL";

        String CREATE_ARB_TABLE2 = "CREATE TABLE donationtable (" +
                "donationid INTEGER PRIMARY KEY NOT NULL," +
                "date timestamp NOT NULL," +
                "donation double NOT NULL";


        String CREATE_ARB_TABLE3 = "CREATE TABLE donortable (" +
                "donorid INTEGER PRIMARY KEY NOT NULL," +
                "dfname TEXT NOT NULL," +
                "dlname TEXT NOT NULL, " +
                "companyname TEXT NOT NULL ";


        String CREATE_ARB_TABLE4 = "CREATE TABLE images (" +
                "treeid TEXT PRIMARY KEY NOT NULL," +
                "imageid TEXT PRIMARY KEY NOT NULL,";


        String CREATE_ARB_TABLE5 = "CREATE TABLE trails (" +
                "walkname TEXT PRIMARY KEY NOT NULL," +
                "type int(1) NOT NULL,";

        String CREATE_ARB_TABLE6 = "CREATE TABLE treedonor (" +
                "treeid TEXT PRIMARY KEY NOT NULL, " +
                "donorid INTEGER FORIEGN KEY NOT NULL, " +
                "date timestamp NOT NULL," +
                "donation double NOT NULL,";

        String CREATE_ARB_TABLE7 = "CREATE TABLE treetable(" +
                "treeid varchar(10)PRIMARY KEY NOT NULL," +
                "trailid varchar(10) NOT NULL," +
                "cname varchar(50) NOT NULL," +
                "sname varchar(50) NOT NULL," +
                "desc text NOT NULL," +
                "latitude double NOT NULL," +
                "longitude double NOT NULL," +
                "status int(2) NOT NULL," +
                "year int(4) NOT NULL," +
                "note text NOT NULL," +
                "audio varchar(50) NOT NULL," +
                "walkname varchar(50)FORIEGN KEY NOT NULL,";


        String CREATE_ARB_TABLE8 = "CREATE TABLE treevia (" +
                "id int(11) PRIMARY KEY NOT NULL," +
                "question text NOT NULL," +
                "answer text NOT NULL,";


        // create ARBORETUM tableS
        db.execSQL(CREATE_ARB_TABLE1);
        db.execSQL(CREATE_ARB_TABLE2);
        db.execSQL(CREATE_ARB_TABLE3);
        db.execSQL(CREATE_ARB_TABLE4);
        db.execSQL(CREATE_ARB_TABLE5);
        db.execSQL(CREATE_ARB_TABLE6);
        db.execSQL(CREATE_ARB_TABLE7);
        db.execSQL(CREATE_ARB_TABLE8);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS commemrativetable");

        db.execSQL("DROP TABLE IF EXISTS donationtable");
        db.execSQL("DROP TABLE IF EXISTS donartable");
        db.execSQL("DROP TABLE IF EXISTS images");
        db.execSQL("DROP TABLE IF EXISTS trails");
        db.execSQL("DROP TABLE IF EXISTS treedonar");
        db.execSQL("DROP TABLE IF EXISTS treetable");
        db.execSQL("DROP TABLE IF EXISTS treevia");


        // create fresh books table
        this.onCreate(db);
    }
}

