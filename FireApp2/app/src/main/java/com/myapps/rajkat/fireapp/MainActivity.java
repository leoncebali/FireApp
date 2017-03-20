package com.myapps.rajkat.fireapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private String rec = "";
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase firedb = FirebaseDatabase.getInstance();

        final DatabaseReference dbRef = firedb.getReference().getRoot();

        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.edit);
        Button b = (Button) findViewById(R.id.click);
//        if (!editText.getText().toString().isEmpty()){
//            rec = editText.getText().toString();
//        }
        rec = !editText.getText().toString().isEmpty()? editText.getText().toString() : "";
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(editText.getText().toString(), "");
                dbRef.updateChildren(map);
                editText.setText("");
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set set = new HashSet();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
//                    set.add(dataSnapshot.getKey());
                }
                list.clear();
                list.addAll(set);

                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, ""+ list.size(), Toast.LENGTH_SHORT).show();
//                String value = dataSnapshot.getValue(String.class);
//                list.add(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }
}
