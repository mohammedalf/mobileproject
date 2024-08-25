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

/*class UsersAdapter(
    private var users: List<User>,
    private val onUserClick: (User, Boolean) -> Unit
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
        private val onUserClick: (User, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val userCheckBox: CheckBox = itemView.findViewById(R.id.userCheckBox)

        fun bind(user: User) {
            userNameTextView.text = "${user.firstName} ${user.lastName}"

            userCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onUserClick(user, isChecked)
            }
        }
    }
}*/

/*class UsersAdapter(
    private var users: List<User>,
    private val isCheckboxEnabled: Boolean = false,
    private val onUserClick: (User, Boolean) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_with_checkbox, parent, false)
        return UserViewHolder(view, isCheckboxEnabled, onUserClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(
        itemView: View,
        private val isCheckboxEnabled: Boolean,
        private val onUserClick: (User, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val userCheckBox: CheckBox = itemView.findViewById(R.id.userCheckBox)

        fun bind(user: User) {
            userNameTextView.text = "${user.firstName} ${user.lastName}"
            userCheckBox.visibility = if (isCheckboxEnabled) View.VISIBLE else View.GONE

            userCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onUserClick(user, isChecked)
            }
        }
    }
}*/
/*class UsersAdapter(
    private var users: List<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, onUserClick)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(user: User, onUserClick: (User) -> Unit) {
            userNameTextView.text = "${user.firstName} ${user.lastName}"
            itemView.setOnClickListener { onUserClick(user) }
        }
    }
}*/
/*
class UsersAdapter(private val onUserClick: (User) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private var users: List<User> = listOf()

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, onUserClick)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(user: User, onUserClick: (User) -> Unit) {
            userNameTextView.text = "${user.firstName} ${user.lastName}"
            itemView.setOnClickListener {
                onUserClick(user)
            }
        }
    }
}
*/

/*
class UsersAdapter (private var users: MutableList<User>) :  RecyclerView.Adapter<UsersAdapter.UserViewHolder>(){
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(android.R.id.text1)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userNameTextView.text = "${user.firstName} ${user.lastName}"
    }

    override fun getItemCount(): Int = users.size

    fun setUsers(users: List<User>) {
        this.users = users.toMutableList()
        notifyDataSetChanged()
    }
}*/
