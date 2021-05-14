package huji.postpc.y2021.sandwichorder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import static android.content.ContentValues.TAG;

public class OrderManager extends Activity {
    FirebaseFirestore db;
    SharedPreferences sp;
    final static String ORDERS = "orders";
    final static String ID = "id";
    final static String NAME = "name";
    final static String ORDER = "order";
    OrderManager(SharedPreferences sp){
        db = FirebaseFirestore.getInstance();
        this.sp = sp;
    }

    FirebaseFirestore getDb(){return db;}

    public void saveLocally(Order order){
        Gson gson = new Gson();
        String orderString = gson.toJson(order);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ORDER, orderString);
        editor.apply();
    }

    public Order retrieveOrderLocally(){
        String orderString = sp.getString(ORDER, "");
        if(orderString.equals("")){
            Log.w(TAG, "no id found for this order");
            return null;
        }
        else {
            Gson gson = new Gson();
            return gson.fromJson(orderString, Order.class);
        }
    }

    public void markOrderDoneLocally(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ID, "");
        editor.putString(ORDER, "");
        editor.apply();
    }

    public DocumentReference getDoc(){
        String id = sp.getString(ID, "");
        if(id.equals("")){
            Log.w(TAG, "no id found for this order");
            return null;
        }
        return db.collection(ORDERS).document(id);
    }

    public void updateOrder(Order order){
        saveLocally(order);
        String id = sp.getString(ID, "");
        if (id.equals("")){
            return;
        }
        db.collection(ORDERS).document(id)
                .set(order)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    public void deleteOrder(){
        String id = sp.getString(ID, "");
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ID, "");
        editor.apply();
        db.collection(ORDERS).document(id)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

}


