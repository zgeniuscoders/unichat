package cd.zgeniuscoders.unichat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cd.zgeniuscoders.unichat.databinding.ItemUserChatBinding
import cd.zgeniuscoders.unichat.models.ChatList
import cd.zgeniuscoders.unichat.models.Post
import cd.zgeniuscoders.unichat.models.User
import com.bumptech.glide.Glide

class UserContactAdapter(private val context: Context, private val users: ArrayList<User>) :
    RecyclerView.Adapter<UserContactAdapter.ContactUserViewHolder>(), Filterable {

    private var usersFilteredList: List<User> = ArrayList()

    init {
        usersFilteredList = users
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                usersFilteredList = if (charSearch.isEmpty()) {
                    users
                } else {
                    val result = ArrayList<User>()
                    for (user in users) {
                        if (user.username.lowercase()
                                .contains(charSearch.lowercase()) || user.number.lowercase()
                                .contains(charSearch.lowercase())
                        ) {
                            result.add(user)
                        }
                    }
                    result
                }
                val fresult = FilterResults()
                fresult.values = usersFilteredList
                return fresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFilteredList = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }

        }
    }

    inner class ContactUserViewHolder(binding: ItemUserChatBinding) : ViewHolder(binding.root) {
        val binding = ItemUserChatBinding.bind(binding.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactUserViewHolder {
        val binding = ItemUserChatBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContactUserViewHolder(binding)
    }

    override fun getItemCount() = users.size
    override fun onBindViewHolder(holder: ContactUserViewHolder, position: Int) {
        val user = usersFilteredList[position]
        holder.binding.username.text = user.username
        Glide.with(context).load(user.profile).into(holder.binding.userProfile)
    }

}