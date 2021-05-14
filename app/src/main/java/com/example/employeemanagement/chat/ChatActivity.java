package com.example.employeemanagement.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.ClockInOutActivity;
import com.example.employeemanagement.activity.employee.EditProfileActivity;
import com.example.employeemanagement.chat.model.Consersation;
import com.example.employeemanagement.chat.model.MessageModel;
import com.example.employeemanagement.model.Attendance;
import com.example.employeemanagement.model.Employee;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerChat;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private ListMessageModelAdapter adapter;
    private String roomId,senderId,receiverId;
    private Consersation consersation;
    private ImageButton btnSend;
    private EditText editWriteMessageModel;
    TextView txtHeading;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    public static HashMap<String, Bitmap> bitmapAvataFriend;
    public Bitmap bitmapAvataUser;
    ImageView imgBack;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intentData = getIntent();
         count = getCount();
         Log.e("Message Count","Is "+count);
        Log.e("Count Value","Is 2"+count);
        senderId = Constants.employee.getUserId();
        receiverId = Constants.selectedEmployee.getUserId();
        Constants.selectedEmployee.setMessageCount(0);
        mDatabase.child("User").child(Constants.selectedEmployee.getUserId()).setValue(Constants.selectedEmployee,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
            }
        });
        Log.e("SenderId","Is"+senderId+" Receiver Id="+receiverId);
        if(senderId != null && receiverId != null)
        {
            if(Integer.parseInt(senderId) < Integer.parseInt(receiverId))
            {
                roomId = senderId+"_"+receiverId;
            }
            else {
                roomId = receiverId+"_"+senderId;
            }
        }

//        idFriend = intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);
//        roomId = intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        String nameFriend = Constants.selectedEmployee.getFirst_name()+" "+Constants.selectedEmployee.getLast_name();

        consersation = new Consersation();
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtHeading = (TextView) findViewById(R.id.txtHeading);
        btnSend.setOnClickListener(this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.selectedEmployee.setMessageCount(0);
                mDatabase.child("User").child(Constants.selectedEmployee.getUserId()).setValue(Constants.selectedEmployee,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                    }
                });

                finish();
            }
        });
        if(nameFriend != null)
        {
            txtHeading.setText(nameFriend);
        }
//        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
//        if (!base64AvataUser.equals(StaticConfig.STR_DEFAULT_BASE64)) {
//            byte[] decodedString = Base64.decode(base64AvataUser, Base64.DEFAULT);
//            bitmapAvataUser = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        } else {
//            bitmapAvataUser = null;
//        }

        editWriteMessageModel = (EditText) findViewById(R.id.editWriteMessage);
        if (senderId != null && receiverId != null) {
//            getSupportActionBar().setTitle(nameFriend);
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerChat = (RecyclerView) findViewById(R.id.recyclerChat);
            recyclerChat.setLayoutManager(linearLayoutManager);
            adapter = new ListMessageModelAdapter(this, consersation, bitmapAvataFriend, bitmapAvataUser);
            FirebaseDatabase.getInstance().getReference().child("message/" + roomId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {
                        HashMap mapMessageModel = (HashMap) dataSnapshot.getValue();
                        MessageModel newMessageModel = new MessageModel();
                        newMessageModel.idSender = (String) mapMessageModel.get("idSender");
                        newMessageModel.idReceiver = (String) mapMessageModel.get("idReceiver");
                        newMessageModel.text = (String) mapMessageModel.get("text");
                        newMessageModel.timestamp = (long) mapMessageModel.get("timestamp");

                        consersation.getListMessageData().add(newMessageModel);
                        adapter.notifyDataSetChanged();
                        linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            recyclerChat.setAdapter(adapter);
        }
    }
    int Newcount = 0;
    private int getCount() {

        mDatabase.child("User").child(Constants.employee.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //ArrayList<Employee> employeeArrayList = new ArrayList<>();
                        Employee tempattendance = snapshot.getValue(Employee.class);
                        Newcount = tempattendance.getMessageCount();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Value ","three"+Newcount);
                    }
                });
        return  Newcount;
    }

    private void updateMessageCount() {
        count++;
        Constants.employee.setMessageCount(count);
        mDatabase.child("User").child(Constants.employee.getUserId()).setValue(Constants.employee,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
            }
        });
    }


    @Override
    public void onBackPressed() {
        Constants.selectedEmployee.setMessageCount(0);
        mDatabase.child("User").child(Constants.selectedEmployee.getUserId()).setValue(Constants.selectedEmployee,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
            }
        });
        this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSend) {

            mDatabase.child("User").child(Constants.employee.getUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //ArrayList<Employee> employeeArrayList = new ArrayList<>();
                            Employee tempattendance = snapshot.getValue(Employee.class);
                            count = tempattendance.getMessageCount();
                            Log.e("Count Value","Is"+count);
                            String content = editWriteMessageModel.getText().toString().trim();
                            if (content.length() > 0) {
                                editWriteMessageModel.setText("");
                                MessageModel newMessageModel = new MessageModel();
                                newMessageModel.text = content;
                                newMessageModel.idSender = Constants.employee.getUserId();
                                newMessageModel.idReceiver = Constants.selectedEmployee.getUserId();
                                newMessageModel.timestamp = System.currentTimeMillis();
                                updateMessageCount();
                                FirebaseDatabase.getInstance().getReference().child("message/" + roomId).push().setValue(newMessageModel);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Value ","three"+Newcount);
                        }
                    });
           // count = getCount();

        }
    }
}

class ListMessageModelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Consersation consersation;
    private HashMap<String, Bitmap> bitmapAvata;
    private HashMap<String, DatabaseReference> bitmapAvataDB;
    private Bitmap bitmapAvataUser;

    public ListMessageModelAdapter(Context context, Consersation consersation, HashMap<String, Bitmap> bitmapAvata, Bitmap bitmapAvataUser) {
        this.context = context;
        this.consersation = consersation;
        this.bitmapAvata = bitmapAvata;
        this.bitmapAvataUser = bitmapAvataUser;
        bitmapAvataDB = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ChatActivity.VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageModelFriendHolder(view);
        } else if (viewType == ChatActivity.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageModelUserHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemMessageModelFriendHolder) {
            ((ItemMessageModelFriendHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
            GlideApp.with(context)
                    .load(Constants.selectedEmployee.getProfile_pic())
                    .placeholder(R.drawable.default_avata)
                    .error(R.drawable.default_avata)
                    .into( ((ItemMessageModelFriendHolder) holder).avata);
            } else if (holder instanceof ItemMessageModelUserHolder) {
            GlideApp.with(context)
                    .load(Constants.employee.getProfile_pic())
                    .placeholder(R.drawable.default_avata)
                    .error(R.drawable.default_avata)
                    .into( ((ItemMessageModelUserHolder) holder).avata);
            ((ItemMessageModelUserHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
            if (bitmapAvataUser != null) {
                ((ItemMessageModelUserHolder) holder).avata.setImageBitmap(bitmapAvataUser);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consersation.getListMessageData().get(position).idSender.equals(Constants.employee.getUserId()) ? ChatActivity.VIEW_TYPE_USER_MESSAGE : ChatActivity.VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return consersation.getListMessageData().size();
    }
}

class ItemMessageModelUserHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageModelUserHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentUser);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView2);
    }
}

class ItemMessageModelFriendHolder extends RecyclerView.ViewHolder {
    public TextView txtContent;
    public CircleImageView avata;

    public ItemMessageModelFriendHolder(View itemView) {
        super(itemView);
        txtContent = (TextView) itemView.findViewById(R.id.textContentFriend);
        avata = (CircleImageView) itemView.findViewById(R.id.imageView3);
    }
}
