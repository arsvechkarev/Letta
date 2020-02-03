package com.arsvechkarev.letta.media

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arsvechkarev.letta.R
import kotlinx.android.synthetic.main.fragment_images_list.recyclerPhotos

class ImagesListFragment : Fragment(R.layout.fragment_images_list) {
  
  private val imagesLoader = MediaDiInjector.getImageLoader(this)
  private val adapter = ImagesListAdapter(imagesLoader)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerPhotos.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
    recyclerPhotos.adapter = adapter
    // TODO (2/3/2020): Transfer to background thread
    val data = getImagesList(requireActivity().contentResolver)
    println(data)
    adapter.submitList(data)
  }
  
}