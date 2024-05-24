package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context,val messageList : ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        val ITEM_RECEIVE = 1
        val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            //inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return receiveViewHolder(view)
        }else{
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return sentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        if (holder.javaClass == sentViewHolder::class.java){

            val viewHolder = holder as sentViewHolder

            holder.sentMessage.text = currentMessage.message
        }else{
            val viewHolder = holder as receiveViewHolder

            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
         val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    class sentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)

    }

    class receiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)

    }
}