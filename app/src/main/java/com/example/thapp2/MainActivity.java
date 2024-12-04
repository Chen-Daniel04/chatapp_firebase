package com.example.thapp2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView chatRecyclerView;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Firebase and Views
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        // RecyclerView Setup
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, auth.getCurrentUser().getUid());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(
                this
        ));
        chatRecyclerView.setAdapter(chatAdapter);
        
        // Load exciting messages
        loadMessages();

        // Send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString();
                if (!messageText.isEmpty()) {
                    Message message = new Message(auth.getCurrentUser().getUid(), messageText, System.currentTimeMillis());
                    firestore.collection("messages").add(message);
                    messageInput.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendMessage(String messageText) {
        String senderId = auth.getCurrentUser().getUid();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("senderId", senderId);
        messageMap.put("message", messageText);
        messageMap.put("timestamp", System.currentTimeMillis());

        firestore.collection("chats").document("chatId").collection("messages")
                .add(messageMap)
                .addOnSuccessListener(
                        documentReference -> Toast
                                .makeText(MainActivity.this, "Message sent successfully", Toast.LENGTH_SHORT)
                                .show()
                )
                .addOnFailureListener(
                        e -> Toast
                                .makeText(MainActivity.this, "Failed to send message", Toast.LENGTH_SHORT)
                                .show()
                );
    }

    private void loadMessages() {
        firestore.collection("chats").document("chatId").collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(MainActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange docChange : snapshots.getDocumentChanges()) {
                            if (docChange.getType() == DocumentChange.Type.ADDED) {
                                Message message = docChange.getDocument().toObject(Message.class);
                                messageList.add(message);
                                chatAdapter.notifyItemInserted(messageList.size() - 1);
                                chatRecyclerView.scrollToPosition(messageList.size() - 1);
                            }
                        }
                    }
                });
    }
}

