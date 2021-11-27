package com.ezam.playground

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezam.playground.databinding.FragmentNestedScrollBinding
import com.ezam.playground.databinding.ItemLipsumBinding


class NestedScrollFragment : Fragment() {

    var mBinding : FragmentNestedScrollBinding? = null
    val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nested_scroll, container, false).also { mBinding = FragmentNestedScrollBinding.bind(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.listLipsum.apply {
            adapter = LipsumAdapter()
            layoutManager = LinearLayoutManager( requireContext(), RecyclerView.VERTICAL, false )
        }
    }
}

class LipsumViewHolder( binding: ItemLipsumBinding ) : RecyclerView.ViewHolder( binding.root ) {

}

class LipsumAdapter(
    private val itemCount : Int = 30
) : RecyclerView.Adapter<LipsumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LipsumViewHolder {
        val inflater = LayoutInflater.from( parent.context )
        val binding = ItemLipsumBinding.inflate( inflater, parent, false )
        return LipsumViewHolder( binding )
    }

    override fun onBindViewHolder(holder: LipsumViewHolder, position: Int) {
    }

    override fun getItemCount() = itemCount
}