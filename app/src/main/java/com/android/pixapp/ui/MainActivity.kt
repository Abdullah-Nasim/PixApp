package com.android.pixapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.pixapp.R
import com.android.pixapp.databinding.ActivityMainBinding
import com.android.pixapp.databinding.MainRecyclerItemBinding
import com.android.pixapp.domain.PixAppPicture
import com.android.pixapp.viewmodels.MainViewModel

/**
 * The responsibility of this activity is to display the list of pictures
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this)
        ViewModelProvider(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java) }

    private var viewModelAdapter: MainRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Setting the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModelAdapter = MainRecyclerAdapter(PictureClick {
            val intent = Intent(this, PictureDetailsActivity::class.java)
            intent.putExtra("PICTURE_ID", it.id)
            startActivity(intent)
        }, this)

        viewModel.pictures.observe(this, Observer { pictures ->
            pictures?.apply {
                viewModelAdapter?.pictures = pictures
            }
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        viewModel.eventProgressShown.observe(this, Observer { showProgressBar ->
            if(showProgressBar)
                binding.loadingSpinner.visibility = View.VISIBLE
            else
                binding.loadingSpinner.visibility = View.GONE

        })

        viewModel.eventNetworkError.observe(this, Observer { isNetworkError ->
            if(isNetworkError)
                Toast.makeText(this,"Network Error", Toast.LENGTH_SHORT).show()
        })

        viewModel.eventOpenScreenLiveData.observe(this, Observer {
            if(it == "OPEN_LOGIN_SCREEN"){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_log_out -> {
            viewModel.logoutUser()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

class PictureClick(val block: (PixAppPicture) -> Unit) {
    /**
     * Called when a picture is clicked
     */
    fun onClick(picture: PixAppPicture) = block(picture)
}

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class MainRecyclerAdapter(private val callback: PictureClick, private val context: Context) : RecyclerView.Adapter<MainRecyclerViewHolder>() {

    /**
     * The pictures that Adapter will show
     */
    var pictures: List<PixAppPicture> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val withDataBinding: MainRecyclerItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MainRecyclerViewHolder.LAYOUT,
            parent,
            false)
        return MainRecyclerViewHolder(withDataBinding)
    }

    override fun getItemCount() = pictures.size

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.picture = pictures[position]
            it.uploadedByTv.text = context.getString(R.string.uploaded_by)
            it.pictureCallback = callback
        }
    }
}

class MainRecyclerViewHolder(val viewDataBinding: MainRecyclerItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.main_recycler_item
    }
}

