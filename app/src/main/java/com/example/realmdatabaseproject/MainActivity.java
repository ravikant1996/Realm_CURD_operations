package com.example.realmdatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button insert, update, read, delete;
    TextView showText;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insert = findViewById(R.id.insert);
        update = findViewById(R.id.update);
        read = findViewById(R.id.read);
        delete = findViewById(R.id.delete);
        showText = findViewById(R.id.showText);

        realm = Realm.getDefaultInstance();

        insert.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        read.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
            case R.id.insert:
                showInsertDialogBox();
                break;
            case R.id.update:
                updateData();
                break;
            case R.id.delete:
                deleteData();
                break;
            case R.id.read:
                showData();
                break;
        }
    }

    private void updateData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.delete_data, null);
        builder.setView(view);

        EditText id = view.findViewById(R.id.id);
        Button button = view.findViewById(R.id.delete);
        button.setText("update");

        AlertDialog alertDialog = builder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().toString().trim().equals("")) {
                    return;
                }
                alertDialog.dismiss();
                long _id = Long.parseLong(id.getText().toString().trim());
                final DataModel dataModel = realm.where(DataModel.class).equalTo("id", _id).findFirst();
                if (dataModel!=null) {
                    showInsertDialogBox(dataModel);
                }else {
                    Toast.makeText(MainActivity.this, "Not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showInsertDialogBox(DataModel dataModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.data_input_dialog, null);
        builder.setView(view);

        EditText name = view.findViewById(R.id.name);
        EditText age = view.findViewById(R.id.age);
        Spinner gender = view.findViewById(R.id.gender);
        Button submit = view.findViewById(R.id.submit);
        submit.setText("Update");
        name.setText(dataModel.getName());
        age.setText(dataModel.getAge() + "");
        name.setText(dataModel.getName());

        if (dataModel.getGender().equalsIgnoreCase("Male")) {
            gender.setSelection(0);
        } else if (dataModel.getGender().equalsIgnoreCase("Female")) {
            gender.setSelection(1);
        } else {
            gender.setSelection(2);
        }
        AlertDialog alertDialog = builder.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equals("") ||
                        age.getText().toString().trim().equals("")) {
                    return;
                }
                alertDialog.dismiss();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        dataModel.setAge(Integer.parseInt(age.getText().toString().trim()));
                        dataModel.setName(name.getText().toString().trim());
                        dataModel.setGender(gender.getSelectedItem().toString());
                        realm.copyToRealmOrUpdate(dataModel);
                        Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void deleteData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.delete_data, null);
        builder.setView(view);

        EditText id = view.findViewById(R.id.id);
        Button button = view.findViewById(R.id.delete);

        AlertDialog alertDialog = builder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().toString().trim().equals("")) {
                    return;
                }
                long _id = Long.parseLong(id.getText().toString().trim());
                final DataModel dataModel = realm.where(DataModel.class).equalTo("id", _id).findFirst();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        if (dataModel != null) {
                            alertDialog.dismiss();
                            dataModel.deleteFromRealm();
                            Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void showInsertDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.data_input_dialog, null);
        builder.setView(view);

        EditText name = view.findViewById(R.id.name);
        EditText age = view.findViewById(R.id.age);
        Spinner gender = view.findViewById(R.id.gender);
        Button submit = view.findViewById(R.id.submit);

        AlertDialog alertDialog = builder.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equals("") ||
                        age.getText().toString().trim().equals("")) {
                    return;
                }
                alertDialog.dismiss();
                DataModel dataModel = new DataModel();
                Number current_id = realm.where(DataModel.class).max("id");
                long nextId;
                if (current_id == null) {
                    nextId = 1;
                } else {
                    nextId = current_id.intValue() + 1;
                }
                dataModel.setId(nextId);
                dataModel.setAge(Integer.parseInt(age.getText().toString().trim()));
                dataModel.setName(name.getText().toString().trim());
                dataModel.setGender(gender.getSelectedItem().toString());
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(dataModel);
                        Toast.makeText(MainActivity.this, "inserted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showData() {
        List<DataModel> dataModels = realm.where(DataModel.class).findAll();
        showText.setText(dataModels + "");
    }
}