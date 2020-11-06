package e.riper.projekt2

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_leaderboards.*
import kotlinx.android.synthetic.main.recyclerview_highscores_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class LeaderboardsModel(application: Application) : AndroidViewModel(application) {

    // Jest LiveData bo ładuje się asynchronicznie
    // Automatyczne powiadomienie recyclerView jak sie dane naładują
    private val data: MutableLiveData<List<HighScore>> by lazy {
        MutableLiveData<List<HighScore>>().also {
            loadData()
        }
    }

    fun getData(): LiveData<List<HighScore>> {
        return data
    }

    private fun loadData(){
        viewModelScope.launch(Dispatchers.Main) {
            data.value = AppDatabase.getInstance(getApplication<Application>()).highScoresDao().getAllDescending().asList()
        }
    }
}

class LeaderboardsAdapter(private var data: List<HighScore>, private val context: Context) : RecyclerView.Adapter<LeaderboardsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.highscore_item_name
        val textScore: TextView = itemView.highscore_item_score
        val textDate: TextView = itemView.highscore_item_date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_highscores_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textName.text = data[position].playerName
        viewHolder.textScore.text = data[position].score.toString()
        val date = Date(data[position].unixTime * 1000)
        viewHolder.textDate.text = DateFormat.getDateInstance(DateFormat.LONG).format(date)
    }

    override fun getItemCount() = data.size

    fun updateData(newData: List<HighScore>) {
        data = newData
        notifyDataSetChanged()
    }
}

class LeaderboardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboards)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val model: LeaderboardsModel by viewModels()
        recyclerview_highscores.layoutManager = LinearLayoutManager(this)
        recyclerview_highscores.setHasFixedSize(true)
        val adapter = LeaderboardsAdapter(listOf(), this)
        recyclerview_highscores.adapter = adapter

        model.getData().observe(this, {
            adapter.updateData(it)
        })
    }

    companion object {
        fun start(context: Context): Boolean {
            val starter = Intent(context, LeaderboardsActivity::class.java)
            context.startActivity(starter)
            return true
        }
    }
}
