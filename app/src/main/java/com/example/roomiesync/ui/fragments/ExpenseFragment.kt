package com.example.roomiesync.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomiesync.adapter.ExpenseAdapter
import com.example.roomiesync.databinding.FragmentExpenseBinding
import com.example.roomiesync.repository.ExpenseRepositoryImpl
import com.example.roomiesync.ui.activity.AddExpenseActivity
import com.example.roomiesync.viewmodel.ExpenseViewModel

class ExpenseFragment : Fragment() {
    lateinit var binding: FragmentExpenseBinding
    lateinit var expenseViewModel: ExpenseViewModel
    lateinit var adapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = ExpenseRepositoryImpl()
        expenseViewModel = ExpenseViewModel(repo)
        adapter = ExpenseAdapter(requireContext(), ArrayList())

        binding.recyclerViewExpense.adapter = adapter
        binding.recyclerViewExpense.layoutManager = LinearLayoutManager(requireContext())

        expenseViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if(loading){
                binding.progressBar2.visibility = View.VISIBLE
            }else{
                binding.progressBar2.visibility = View.GONE
            }

        }

        expenseViewModel.getAllExpenses.observe(viewLifecycleOwner) {
            it?.let { adapter.updateData(it) }
        }
        expenseViewModel.getAllExpenses()
        expenseViewModel.getAllExpenses.observe(viewLifecycleOwner){ it ->

            it?.let{
                adapter.updateData(it)
            }

        }

        binding.floatingAddExpenseBtn.setOnClickListener {
            val intent = Intent(requireContext(), AddExpenseActivity::class.java)
            startActivity(intent)
        }

        // Fixing the ItemTouchHelper implementation
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val projectId = adapter.getexpenseId(viewHolder.adapterPosition)
                expenseViewModel.deleteExpense(projectId) { success, message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }).attachToRecyclerView(binding.recyclerViewExpense) // Corrected position
    }
}
