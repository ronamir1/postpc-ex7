package huji.postpc.y2021.sandwichorder;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

import static android.content.ContentValues.TAG;

public class EditOrderActivity extends AppCompatActivity {
    TextView nameEditText;
    EditText commentEditText;
    NumberPicker pickleNumberPicker;
    CheckBox hummusCheckBox;
    CheckBox tahiniCheckBox;
    OrderManager orderManager;
    Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
        Context context = EditOrderActivity.this;
        SharedPreferences sp = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        nameEditText = findViewById(R.id.nameEditText);
        pickleNumberPicker = findViewById(R.id.pickleNumberPicker);
        hummusCheckBox =findViewById(R.id.hummusCheckBox);
        tahiniCheckBox = findViewById(R.id.tahiniCheckBox);
        commentEditText = findViewById(R.id.commentEditText);
        pickleNumberPicker.setMinValue(0);
        pickleNumberPicker.setMaxValue(10);
        pickleNumberPicker.setWrapSelectorWheel(true);
        pickleNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            order.setPickles(i1);
            orderManager.updateOrder(order);
        });

        hummusCheckBox.setOnClickListener(view -> {
            order.setHummus(hummusCheckBox.isChecked());
            orderManager.updateOrder(order);
        });

        tahiniCheckBox.setOnClickListener(view -> {
            order.setTahini(tahiniCheckBox.isChecked());
            orderManager.updateOrder(order);
        });

        commentEditText.setCursorVisible(false);
        commentEditText.setOnClickListener(view -> commentEditText.setCursorVisible(true));

        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                order.setComment(editable.toString());
                orderManager.updateOrder(order);
            }
        });

        orderManager = new OrderManager(sp);
        order = orderManager.retrieveOrderLocally();
        DocumentReference docRef = orderManager.getDoc();
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.getData());
                order = snapshot.toObject(Order.class);
                if (order.getStatus().equals(Order.IN_PROGRESS)){
                    Intent intent = new Intent(EditOrderActivity.this, OrderInTheMakingActivity.class);
                    startActivity(intent);
                }
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
        tahiniCheckBox.setChecked(order.getTahini());
        nameEditText.setText(order.getName());
        pickleNumberPicker.setValue((int) order.getPickles());
        hummusCheckBox.setChecked(order.getHummus());
        commentEditText.setText(order.getComment());
    }


    public void cancelOnclick(View view) {
        orderManager.deleteOrder();
        Intent intent = new Intent(EditOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    public void hideKeyBoard(View view) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
        commentEditText.setCursorVisible(false);
    }
}