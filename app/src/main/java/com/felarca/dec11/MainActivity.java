package com.felarca.dec11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;

    DatabaseReference androidRef;
    ArrayList<AndroidVersion> list;
    EditText etID, etCode, etDate, etApi;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        androidRef = db.getReference("AndroidVer");
        etID = findViewById(R.id.et1);
        etCode = findViewById(R.id.et2);
        etDate = findViewById(R.id.et3);
        etApi = findViewById(R.id.et4);
        list = new ArrayList<AndroidVersion>();
    }

    public void addRecord(View v){
        String id = androidRef.push().getKey();
        //String id = etID.getText().toString();
        String cname = etCode.getText().toString();
        String date = etDate.getText().toString();
        String api = etApi.getText().toString();
        AndroidVersion  version = new AndroidVersion(id, cname, date, api);
       // androidRef.setValue(version);
        androidRef.child(id).setValue(version);
        clearData(v);
        Toast.makeText(this, "Data added!", Toast.LENGTH_SHORT).show();
    }

    public void editText(View v){
        String id = etID.getText().toString();
        String cname = etCode.getText().toString();
        String date = etDate.getText().toString();
        String api = etApi.getText().toString();
        AndroidVersion  version = new AndroidVersion(id, cname, date, api);
        androidRef.child(id).setValue(version);
        clearData(v);
        Toast.makeText(this, "Data Edited!", Toast.LENGTH_SHORT).show();
    }

    public void removeRecord(View v){
        String id = etID.getText().toString();
        androidRef.child(id).removeValue();
        clearData(v);
        Toast.makeText(this, "Data Deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        androidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                for(DataSnapshot androidObj: ds.getChildren()){
                    AndroidVersion av =androidObj.getValue(AndroidVersion.class);
                    list.add(av);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void moveFirst(View v){
        etID.setText(list.get(0).getId());
        etCode.setText(list.get(0).getcName());
        etDate.setText(list.get(0).getrDate());
        etApi.setText(list.get(0).getApiLevel());
        position = 0;

    }

    public void movePrevious(View v){
        if(position > 0) {
            position--;
            etID.setText((list.get(position).getId()));
            etCode.setText(list.get(position).getcName());
            etDate.setText(list.get(position).getrDate());
            etApi.setText(list.get(position).getApiLevel());
        }else{
            Toast.makeText(this, "No more previous", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveNext(View v) {
        if ((position < list.size() - 1) && position >= -1) {
            position++;
            etID.setText((list.get(position).getId()));
            etCode.setText(list.get(position).getcName());
            etDate.setText(list.get(position).getrDate());
            etApi.setText(list.get(position).getApiLevel());
        } else {
            Toast.makeText(this, "No more next", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveLast(View v){
        position = (list.size()-1);
        etID.setText((list.get(position).getId()));
        etCode.setText(list.get(position).getcName());
        etDate.setText(list.get(position).getrDate());
        etApi.setText(list.get(position).getApiLevel());
    }

    public void clearData(View v){
        position = 0;
        etID.setText("");
        etCode.setText("");
        etDate.setText("");
        etApi.setText("");
    }
}
