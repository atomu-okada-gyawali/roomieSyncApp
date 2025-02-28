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
import com.example.roomiesync.adapter.ChoreAdapter
import com.example.roomiesync.databinding.FragmentChoreBinding
import com.example.roomiesync.repository.ChoreRepositoryImpl
import com.example.roomiesync.ui.activity.AddChoreActivity
import com.example.roomiesync.viewmodel.ChoreViewModel

class ExpenseFragment : Fragment() {
    lateinit var binding: FragmentChoreBinding
    lateinit var choreViewModel: ChoreViewModel
    lateinit var adapter: ChoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = ChoreRepositoryImpl()
        choreViewModel = ChoreViewModel(repo)
        adapter = ChoreAdapter(requireContext(), ArrayList())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        choreViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if(loading){
                binding.progressBar2.visibility = View.VISIBLE
            }else{
                binding.progressBar2.visibility = View.GONE
            }

        }

        choreViewModel.getAllChores.observe(viewLifecycleOwner) {
            it?.let { adapter.updateData(it) }
        }
        choreViewModel.getAllChores()
        choreViewModel.getAllChores.observe(viewLifecycleOwner){ it ->

            it?.let{
                adapter.updateData(it)
            }

        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), AddChoreActivity::class.java)
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
                val projectId = adapter.getchoreId(viewHolder.adapterPosition)
                choreViewModel.deleteChore(projectId) { success, message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView) // Corrected position
    }
}
