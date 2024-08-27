package be.ap.examsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class UsersAdapter(
    private var users: List<User>,
    private val onUserClick: ((User, Boolean) -> Unit)? = null // Optional lambda for selection
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_with_checkbox, parent, false)
        return UserViewHolder(view, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(
        itemView: View,
        private val onUserClick: ((User, Boolean) -> Unit)? // Optional lambda for selection
    ) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val userCheckBox: CheckBox = itemView.findViewById(R.id.userCheckBox)

        fun bind(user: User) {
            userNameTextView.text = "${user.firstName} ${user.lastName}"
            userCheckBox.visibility = if (onUserClick != null) View.VISIBLE else View.GONE

            userCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onUserClick?.invoke(user, isChecked)
            }
        }
    }
}


