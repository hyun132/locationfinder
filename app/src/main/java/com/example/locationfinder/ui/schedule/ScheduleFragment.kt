package com.example.locationfinder.ui.schedule

import androidx.fragment.app.viewModels
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


}