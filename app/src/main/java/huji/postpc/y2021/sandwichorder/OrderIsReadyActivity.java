package huji.postpc.y2021.sandwichorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class OrderIsReadyActivity extends AppCompatActivity {
    OrderManager orderManager;
    SharedPreferences sp;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_is_ready);
        Context context = OrderIsReadyActivity.this;
        sp = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        orderManager = new OrderManager(sp);
        order = orderManager.retrieveOrderLocally();

    }

    public void backToMain(View view) {
        order.setStatus(Order.DONE);
        orderManager.updateOrder(order);
        orderManager.markOrderDoneLocally();
        Intent intent = new Intent(OrderIsReadyActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}