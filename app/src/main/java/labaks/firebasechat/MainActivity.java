package labaks.firebasechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Message> messageAdapter;
    private FirebaseListAdapter<Item> itemAdapter;
    RelativeLayout activity_main;
    Button button;
    boolean chat = true;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.editText);
                FirebaseDatabase.getInstance().getReference().child("chat")
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                        .push()
                        .setValue(new Message(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        } else {
            displayChat();
        }
    }

    private void displayChat() {

        ListView listMessage = (ListView) findViewById(R.id.listView);
        messageAdapter = new FirebaseListAdapter<Message>(this,
                Message.class,
                R.layout.message,
                FirebaseDatabase.getInstance().getReference().child("chat")) {
            @Override
            protected void populateView(View v, Message model, int position) {

                TextView textMessage, author, timeMessage;
                textMessage = (TextView) v.findViewById(R.id.tvMessage);
                author = (TextView) v.findViewById(R.id.tvUser);
                timeMessage = (TextView) v.findViewById(R.id.tvTime);

                textMessage.setText(model.getTextMessage());
                author.setText(model.getAuthor());
                timeMessage.setText(DateFormat.format("dd-MM-yyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessage.setAdapter(messageAdapter);
        chat = true;
    }

    private void displayItems() {

        ListView listItem = (ListView) findViewById(R.id.listView);
        itemAdapter = new FirebaseListAdapter<Item>(this,
                Item.class,
                R.layout.item,
                FirebaseDatabase.getInstance().getReference().child("items")) {
            @Override
            protected void populateView(View v, final Item model, final int position) {
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                TextView tv_name, tv_number_of_raters;
                RatingBar rb_userRatingBar;
                final TextView tv_totalRate;
                tv_name = (TextView) v.findViewById(R.id.tvItemName);
                rb_userRatingBar = (RatingBar) v.findViewById(R.id.rbItemRate);
                tv_totalRate = (TextView) v.findViewById(R.id.tvTotalRate);
                tv_number_of_raters = (TextView) v.findViewById(R.id.tvNumberOfRaters);

                tv_name.setText(model.getName());
                if (model.getUsersRate().get(userId) != null) {
                    rb_userRatingBar.setRating(model.getUsersRate().get(userId));
                } else {
                    rb_userRatingBar.setRating(0);
                }
                tv_totalRate.setText(Float.toString(model.getTotalRate()));
                tv_number_of_raters.setText(Integer.toString(model.getUsersRate().size()));
                rb_userRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        FirebaseDatabase.getInstance().getReference().child("items")
                                .child(format("Item%d", position))
                                .child("usersRate")
                                .child(userId)
                                .setValue(v);
                        FirebaseDatabase.getInstance().getReference().child("items")
                                .child(format("Item%d", position))
                                .child("totalRate")
                                .setValue(model.getTotalRate());
                    }
                });

            }
        };
        listItem.setAdapter(itemAdapter);
        chat = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "Вход выполнен", Snackbar.LENGTH_SHORT).show();
                displayChat();
            } else {
                Snackbar.make(activity_main, "Вход не выполнен", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_signout) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Snackbar.make(activity_main, "Выход выполнен", Snackbar.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
        if (item.getItemId() == R.id.menu_switch_list) {
            if (!chat) {
                displayChat();
                item.setIcon(R.drawable.star);
            } else {
                displayItems();
                item.setIcon(R.drawable.chat);
            }
        }
        return true;
    }
}
