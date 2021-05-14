package huji.postpc.y2021.sandwichorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText commentEditText;
    NumberPicker pickleNumberPicker;
    CheckBox hummusCheckBox;
    CheckBox tahiniCheckBox;
    OrderManager orderManager;
    ConstraintLayout mainActivityLayout;
    ProgressDialog pd;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = MainActivity.this;
        if (sp == null){
            sp = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            orderManager = new OrderManager(sp);
        }

        pickleNumberPicker = findViewById(R.id.pickleNumberPicker);
        String id = sp.getString(OrderManager.ID, "");
        pd = new ProgressDialog(context);
        pd.setMessage("We Load Your Order");
        pd.setCancelable(false);
        pd.setProgress(0);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if(!id.equals("")){
            mainActivityLayout = findViewById(R.id.mainActivityLayout);
            mainActivityLayout.setVisibility(View.INVISIBLE);
            pd.show();
            DocumentReference doc = orderManager.getDoc();
            if (doc != null){
                doc.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Order order = document.toObject(Order.class);
                            orderManager.saveLocally(order);
                            moveToAnotherActivity(order);
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
            }
        }

        pickleNumberPicker.setMinValue(0);
        pickleNumberPicker.setMaxValue(10);
        pickleNumberPicker.setWrapSelectorWheel(true);
        nameEditText = findViewById(R.id.nameEditText);
        String name = sp.getString(OrderManager.NAME, "");
        if (!name.equals("")){
            nameEditText.setText(name);
        }

        nameEditText.setCursorVisible(false);
        nameEditText.setOnClickListener(view -> nameEditText.setCursorVisible(true));



        commentEditText = findViewById(R.id.commentEditText);
        commentEditText.setCursorVisible(false);
        commentEditText.setOnClickListener(view -> commentEditText.setCursorVisible(true));

        hummusCheckBox = findViewById(R.id.hummusCheckBox);
        tahiniCheckBox = findViewById(R.id.tahiniNumberPicker);
    }

    public void saveOnclick(View view) {
        String comment = commentEditText.getText().toString();
        String name = nameEditText.getText().toString();
        int pickles = pickleNumberPicker.getValue();
        boolean hummus = hummusCheckBox.isChecked();
        boolean tahini = tahiniCheckBox.isChecked();
        Order order = new Order(name, pickles, hummus, tahini, comment, Order.WAITING);
        orderManager.saveLocally(order);
        pd.setMessage("Let Us Process Your Order");
        pd.show();
        FirebaseFirestore db = orderManager.getDb();
        db.collection(OrderManager.ORDERS)
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(OrderManager.ID, documentReference.getId());
                    editor.putString(OrderManager.NAME, order.name);
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, EditOrderActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(MainActivity.this, "We failed to send your order please try again shortly.", Toast.LENGTH_LONG).show();
                });
    }

    public void moveToAnotherActivity(Order order){
        if (order != null){
            if (order.status.equals(Order.WAITING)){
                Intent intent = new Intent(this, EditOrderActivity.class);
                startActivity(intent);
            }
            else if (order.getStatus().equals(Order.IN_PROGRESS)){
                Intent intent = new Intent(MainActivity.this, OrderInTheMakingActivity.class);
                startActivity(intent);
            }
            else if (order.getStatus().equals(Order.READY)){
                Intent intent = new Intent(MainActivity.this, OrderIsReadyActivity.class);
                startActivity(intent);
            }
        }
    }

    public void hideKeyBoard(View view) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
        nameEditText.setCursorVisible(false);
        commentEditText.setCursorVisible(false);
    }
}