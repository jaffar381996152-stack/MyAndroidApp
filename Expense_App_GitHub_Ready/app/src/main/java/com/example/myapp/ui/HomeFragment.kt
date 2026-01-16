package com.example.myapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapp.databinding.FragmentHomeBinding
import com.example.myapp.viewmodel.ExpenseViewModel
import com.example.myapp.viewmodel.ExpenseViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe data
        lifecycleScope.launch {
            viewModel.roommates.collect { roommates ->
                binding.roommatesText.text = "Roommates: ${roommates.joinToString { it.name }}"
            }
        }

        lifecycleScope.launch {
            viewModel.expenses.collect { expenses ->
                binding.expensesText.text = "Expenses: ${expenses.size}"
            }
        }

        binding.addRoommateButton.setOnClickListener {
            val editText = EditText(requireContext()).apply { hint = "Roommate Name" }
            AlertDialog.Builder(requireContext())
                .setTitle("Add Roommate")
                .setView(editText)
                .setPositiveButton("Add") { _, _ ->
                    val name = editText.text.toString()
                    if (name.isNotEmpty()) {
                        viewModel.addRoommate(name)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.addExpenseButton.setOnClickListener {
            val roommates = viewModel.roommates.value
            if (roommates.isEmpty()) {
                Toast.makeText(requireContext(), "Add roommates first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val spinner = Spinner(requireContext())
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roommates.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            val priceEdit = EditText(requireContext()).apply {
                hint = "Price"
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
            val itemEdit = EditText(requireContext()).apply { hint = "Item Name" }
            val layout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                addView(spinner)
                addView(priceEdit)
                addView(itemEdit)
            }
            AlertDialog.Builder(requireContext())
                .setTitle("Add Expense")
                .setView(layout)
                .setPositiveButton("Add") { _, _ ->
                    val selectedIndex = spinner.selectedItemPosition
                    val buyerId = roommates[selectedIndex].id
                    val price = priceEdit.text.toString().toDoubleOrNull()
                    val item = itemEdit.text.toString()
                    if (price != null && item.isNotEmpty()) {
                        viewModel.addExpense(buyerId, price, item)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.calculateButton.setOnClickListener {
            viewModel.calculateAndRollover()
        }

        binding.historyButton.setOnClickListener {
            val summaries = viewModel.summaries.value
            val historyText = summaries.joinToString("\n") { "Month: ${it.month}/${it.year}, Total: ${it.totalExpenses}, Dues: ${it.dues}" }
            AlertDialog.Builder(requireContext())
                .setTitle("History")
                .setMessage(historyText.ifEmpty { "No history" })
                .setPositiveButton("OK", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}