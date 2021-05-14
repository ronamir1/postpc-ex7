package huji.postpc.y2021.sandwichorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

import static android.content.ContentValues.TAG;

public class OrderInTheMakingActivity extends AppCompatActivity {
    OrderManager orderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_in_the_making);
        Context context = OrderInTheMakingActivity.this;
        SharedPreferences sp = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        orderManager = new OrderManager(sp);
        DocumentReference doc = orderManager.getDoc();
        doc.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.getData());
                Order order = snapshot.toObject(Order.class);
                if (order.getStatus().equals(Order.READY)){
                    Intent intent = new Intent(OrderInTheMakingActivity.this, OrderIsReadyActivity.class);
                    startActivity(intent);
                }
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}