package com.example.locationfinder.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.locationfinder.BR
import com.example.locationfinder.R
import com.example.locationfinder.databinding.FragmentScheduleBinding
import com.example.locationfinder.ui.base.BaseFragment

class ScheduleFragment :
    BaseFragment<FragmentScheduleBinding, ScheduleViewModel>(),
    ScheduleNavigator {

    private val vm: ScheduleViewModel by viewModels()
    override fun getLayoutId() = R.layout.fragment_schedule
    override fun getViewModel(): ScheduleViewModel = vm
    override fun getBindingVariable(): Int = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().go.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleFragment_to_makeNewPostFragment)
        }

    }
}