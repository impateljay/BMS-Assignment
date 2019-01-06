package com.jay.bmsassignment.base

interface BasePresenter<in T : BaseView> {
    fun onAttach(view: T)
    fun onDetach()
}