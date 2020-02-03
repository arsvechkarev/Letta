package com.arsvechkarev.letta.photoslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arsvechkarev.letta.R
import kotlinx.android.synthetic.main.fragment_photos.recyclerPhotos

class PhotosFragment : Fragment(R.layout.fragment_photos) {
  
  private val adapter = PhotosAdapter(Injector.getImageLoader(this))
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerPhotos.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
    recyclerPhotos.adapter = adapter
    val data = getImagesList(requireActivity().contentResolver)
    println("hellao")
    println(data)
    data.forEach {
      println("res = ${it.url}")
    }
    adapter.submitList(data)
  }
  
}