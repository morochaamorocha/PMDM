package com.example.rals.ejer1bd;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class RecorridoCursor extends Activity implements View.OnClickListener {

    private Button btnPrimero, btnSiguiente, btnUltimo, btnAnterior;
    private TextView lblNombre, lblApellido;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido_cursor);

        getActionBar().setTitle("Recorrido Cursor");
        btnPrimero = (Button)findViewById(R.id.btnPrimero);
        btnPrimero.setOnClickListener(this);

        btnSiguiente = (Button)findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(this);

        btnUltimo = (Button)findViewById(R.id.btnUltimo);
        btnUltimo.setOnClickListener(this);

        btnAnterior = (Button)findViewById(R.id.btnAnterior);
        btnAnterior.setOnClickListener(this);

        lblNombre = (TextView)findViewById(R.id.lblNombre1);

        lblApellido = (TextView)findViewById(R.id.lblApellido2);

        //Abrimos la BBDD
        AdaptadorBD bd = new AdaptadorBD(getApplicationContext());
        bd.abrir();

        //Obtenemos los datos
        cursor = bd.obtenerDatos();
        cursor.moveToFirst();

        //Asignamos los datos a las vistas
        actualizarVistas();
        btnPrimero.setEnabled(false);
        btnAnterior.setEnabled(false);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnPrimero:

                cursor.moveToFirst();
                actualizarVistas();
                btnPrimero.setEnabled(false);
                btnAnterior.setEnabled(false);
                btnUltimo.setEnabled(true);
                btnSiguiente.setEnabled(true);
                break;
            case R.id.btnSiguiente:

                if (cursor.isFirst() || !cursor.isLast()){
                    cursor.moveToNext();
                    actualizarVistas();
                    btnPrimero.setEnabled(true);
                    btnAnterior.setEnabled(true);
                }
                else if (cursor.isLast()){
                    btnUltimo.setEnabled(false);
                    btnSiguiente.setEnabled(false);
                }
                break;
            case R.id.btnAnterior:

                if (cursor.isLast() ||  !cursor.isFirst()){
                    cursor.moveToPrevious();
                    actualizarVistas();
                    btnPrimero.setEnabled(true);
                    btnAnterior.setEnabled(true);
                }
                else if (cursor.isFirst()){
                    btnPrimero.setEnabled(false);
                    btnAnterior.setEnabled(false);
                }
                break;
            case R.id.btnUltimo:

                cursor.moveToLast();
                actualizarVistas();
                btnUltimo.setEnabled(false);
                btnSiguiente.setEnabled(false);
                btnPrimero.setEnabled(true);
                btnAnterior.setEnabled(true);
                break;
        }
    }

    public void actualizarVistas() {
        lblNombre.setText(cursor.getString(0));
        lblApellido.setText(cursor.getString(1));
    }
}
