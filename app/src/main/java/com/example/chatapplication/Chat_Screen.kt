package com.example.chatapplication

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chat_Screen : AppCompatActivity() {

    private lateinit var recylerView : RecyclerView
    private lateinit var message_Box : EditText
    private lateinit var message_sent_btn :ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_chat_screen)


        val name = intent.getStringExtra("name")
        supportActionBar?.title = name
        val receiverUid = intent.getStringExtra("uid")


        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        recylerView = findViewById(R.id.chat_recyclerView)
        message_Box = findViewById(R.id.messageBox)
        message_sent_btn = findViewById(R.id.btn_msg_send)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        //logic for adding data to recycler View
        mDbRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapShot in snapshot.children){
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.adapter = messageAdapter

        //logic to add message to database
        message_sent_btn.setOnClickListener {

            val message = message_Box.text.toString()
            val messageObject = Message(message,senderUid)

            mDbRef.child("Chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {

                    mDbRef.child("Chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            message_Box.setText("")
        }



    }
}