package com.android.pixapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.pixapp.R
import com.android.pixapp.databinding.ActivityPictureDetailsBinding
import com.android.pixapp.viewmodels.PictureDetailsViewModel

/**
 * The responsibility of this activity is to display the picture details
 */
class PictureDetailsActivity: AppCompatActivity(){

    private lateinit var binding: ActivityPictureDetailsBinding

    private val viewModel: PictureDetailsViewModel by lazy {
        val activity = requireNotNull(this)
        ViewModelProvider(this, PictureDetailsViewModel.Factory(activity.application,
            intent.getIntExtra("PICTURE_ID", 0)))
            .get(PictureDetailsViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_details)

        // Setting the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

    }
}