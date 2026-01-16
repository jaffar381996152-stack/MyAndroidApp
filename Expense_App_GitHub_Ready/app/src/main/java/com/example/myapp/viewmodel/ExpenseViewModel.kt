package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.Expense
import com.example.myapp.data.Roommate
import com.example.myapp.data.MonthSummary
import com.example.myapp.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {
    private val _roommates = MutableStateFlow<List<Roommate>>(emptyList())
    val roommates: StateFlow<List<Roommate>> = _roommates

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private val _summaries = MutableStateFlow<List<MonthSummary>>(emptyList())
    val summaries: StateFlow<List<MonthSummary>> = _summaries

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getAllRoommates().collect { _roommates.value = it }
            repository.getAllExpenses().collect { _expenses.value = it }
            repository.getAllSummaries().collect { _summaries.value = it }
        }
    }

    fun addRoommate(name: String) {
        viewModelScope.launch {
            repository.insertRoommate(Roommate(name = name))
            loadData()
        }
    }

    fun addExpense(buyerId: Long, price: Double, itemName: String) {
        viewModelScope.launch {
            repository.insertExpense(Expense(buyerId = buyerId, price = price, itemName = itemName))
            loadData()
        }
    }

    fun calculateAndRollover() {
        viewModelScope.launch {
            val expenses = _expenses.value
            val roommates = _roommates.value
            if (roommates.isNotEmpty()) {
                val total = expenses.sumOf { it.price }
                val perPerson = total / roommates.size
                val duesMap = mutableMapOf<Long, Double>()
                roommates.forEach { roommate ->
                    val spent = expenses.filter { it.buyerId == roommate.id }.sumOf { it.price }
                    duesMap[roommate.id] = perPerson - spent
                }
                // Assume current month
                val calendar = java.util.Calendar.getInstance()
                val month = calendar.get(java.util.Calendar.MONTH) + 1
                val year = calendar.get(java.util.Calendar.YEAR)
                val duesJson = com.google.gson.Gson().toJson(duesMap)
                repository.insertSummary(MonthSummary(month = month, year = year, totalExpenses = total, roommatesCount = roommates.size, dues = duesJson))
                repository.clearExpenses()
                loadData()
            }
        }
    }
}