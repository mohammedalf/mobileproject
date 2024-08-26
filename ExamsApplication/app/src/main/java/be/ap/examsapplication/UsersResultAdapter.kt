package be.ap.examsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersResultAdapter (
    private val users: List<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UsersResultAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return UserViewHolder(view, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View, private val onUserClick: (User) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val userNameTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(user: User) {
            userNameTextView.text = "${user.firstName} ${user.lastName}"
            itemView.setOnClickListener {
                onUserClick(user)
            }
        }
    }
}