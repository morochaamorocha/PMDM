package com.example.rals.ejer1bd;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener{

    private EditText txtID, txtNombre, txtApellido;
    private ArrayList<Contacto> contactos;
    private ContactosAdapter adapter;
    private AdaptadorBD bd;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listaContactos = (ListView) findViewById(R.id.listaContactos);

        Button btnInsertar = (Button) findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(this);

        Button btnBuscarMayor = (Button) findViewById(R.id.btnBuscarMayor);
        btnBuscarMayor.setOnClickListener(this);

        Button btnBuscarMenor = (Button) findViewById(R.id.btnBuscarMenor);
        btnBuscarMenor.setOnClickListener(this);

        txtID = (EditText)findViewById(R.id.txtID);

        txtNombre = (EditText)findViewById(R.id.txtNombre);

        txtApellido = (EditText)findViewById(R.id.txtApellido);

        //Abrir BBDD
        bd = new AdaptadorBD(getApplicationContext());
        bd.abrir();

        //Obtenemos los datos
        cursor = bd.obtenerDatos();

        //Agregamos los datos existentes a la lista
        contactos = new ArrayList<>();
        if (cursor.moveToFirst()){
            contactos = getContactos(cursor);
        }
        else {
            Toast.makeText(getApplicationContext(), "No hay datos en la BBDD", Toast.LENGTH_SHORT).show();
        }
        adapter = new ContactosAdapter(contactos);
        listaContactos.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.recorrido_cursor) {
            bd.cerrar();
            startActivity(new Intent(getApplicationContext(), RecorridoCursor.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnInsertar:

                if (!txtNombre.getText().toString().equals("") && !txtApellido.getText().toString().equals("")){

                    //Recuperamos datos de los controles de texto
                    Contacto contacto = new Contacto(txtNombre.getText().toString(), txtApellido.getText().toString());

                    //Insertamos los datos en la BBDD
                    bd.insertar(contacto);
                    Toast.makeText(getApplicationContext(), "Inserción realizada con éxito", Toast.LENGTH_SHORT).show();

                    //Actualizamos la lista
                    cursor = bd.obtenerDatos();
                    contactos = getContactos(cursor);

                    actualizarLista();
                    limpiarControles();
                }
                break;
            case R.id.btnBuscarMayor:

                if (!txtID.getText().toString().equals("")){
                    //Obtenemos los datos según el id indicado en el control
                    cursor = bd.obtenerMayorQue(txtID.getText().toString());
                    contactos = getContactos(cursor);
                    actualizarLista();
                    limpiarControles();
                }
                break;
            case R.id.btnBuscarMenor:

                if (!txtID.getText().toString().equals("")){
                    //Obtenemos los datos según el id indicado en el control
                    cursor = bd.obtenerMenorQue(txtID.getText().toString());
                    contactos = getContactos(cursor);
                    actualizarLista();
                    limpiarControles();
                }
                break;

        }
    }

    public void limpiarControles(){
        txtID.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
    }

    public void actualizarLista() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.remove(contactos);
                adapter.add(contactos);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public ArrayList<Contacto> getContactos(Cursor c){
        ArrayList<Contacto> aux = new ArrayList<>();
        String nombre, apellido;
        contactos.clear();

        c.moveToFirst();

        while (!c.isAfterLast()){
            nombre = c.getString(0);
            apellido = c.getString(1);
            aux.add(new Contacto(nombre, apellido));
            c.moveToNext();
        }

        c.close();
        return aux;
    }

    public class ContactosAdapter extends ArrayAdapter{

        ViewHolder holder;
        public ContactosAdapter(List objects) {
            super(getApplicationContext(), R.layout.adaptador, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.adaptador, null);

                holder = new ViewHolder();
                holder.lblNombre = (TextView)convertView.findViewById(R.id.lblNombre1);
                holder.lblApellido = (TextView)convertView.findViewById(R.id.lblApellido);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.lblNombre.setText(contactos.get(position).getNombre());
            holder.lblApellido.setText(contactos.get(position).getApellido());

            return convertView;
        }
    }

    static class ViewHolder{
        TextView lblNombre, lblApellido;
    }
}
