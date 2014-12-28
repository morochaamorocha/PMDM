package com.example.rals.ejer1bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rals1_000 on 27/12/2014.
 */
public class AdaptadorBD {

    //Constantes de la BBDD
    private static final String KEY_ROWID = "_id";
    private static final String NOMBRE = "nombre";
    private static final String APELLIDO = "apellido";
    private static final String DATABASE_TABLE = "contactos";
    private static final String DATABASE_NAME = "DBcontactos";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "(" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOMBRE + " TEXT, " + APELLIDO + " TEXT);";

    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private Context context;

    public AdaptadorBD(Context context) {
        this.context = context;
    }

    //Abrir la BBDD
    public AdaptadorBD abrir(){
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    //Cerrar la BBDD
    public void cerrar(){
        helper.close();
    }

    //Obtener todos los datos
    public Cursor obtenerDatos(){
        return database.rawQuery("SELECT " + NOMBRE + ", " + APELLIDO + " FROM " + DATABASE_TABLE + " ORDER BY " + APELLIDO + ", " + NOMBRE, null);
    }

    //Obtener mayores que
    public Cursor obtenerMayorQue(String id){
        String[] campos = {NOMBRE, APELLIDO};
        return database.query(DATABASE_TABLE, campos, KEY_ROWID + " >= " + id, null, null, null, APELLIDO);
    }

    //Obtener menores que
    public Cursor obtenerMenorQue(String id){
        String[] campos ={NOMBRE, APELLIDO};
        return database.query(DATABASE_TABLE, campos, KEY_ROWID + " <= " + id, null, null, null, APELLIDO);
    }

    //Insertar
    public String insertar(Contacto c){
        ContentValues values = new ContentValues();
        values.put(NOMBRE, c.getNombre());
        values.put(APELLIDO, c.getApellido());
        return String.valueOf(database.insert(DATABASE_TABLE, null, values));
    }


    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
