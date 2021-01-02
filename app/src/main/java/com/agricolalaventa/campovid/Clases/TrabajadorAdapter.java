package com.agricolalaventa.campovid.Clases;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class TrabajadorAdapter extends ArrayAdapter<Trabajador> {

    //storing all the names in the list
    private List<Trabajador> trabajadores;

    //context object
    private Context context;

    //constructor
    public TrabajadorAdapter(Context context, int resource, List<Trabajador> trabajadores) {
        super(context, resource, trabajadores);
        this.context = context;
        this.trabajadores = trabajadores;
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.names, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        ImageView imageViewStatus = (ImageView) listViewItem.findViewById(R.id.imageViewStatus);

        //getting the current name
        Trabajador trabajador = trabajadores.get(position);

        //setting the name to textview
        //textViewName.setText(name.getCodbarra());

        if (trabajador.getStatus() == 0)
            imageViewStatus.setBackgroundResource(R.drawable.stopwatch);
        else
            imageViewStatus.setBackgroundResource(R.drawable.success);

        return listViewItem;
    }*/
}

