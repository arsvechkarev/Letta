package com.arsvechkarev.letta.media

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.media.common.ImagesViewModel
import com.arsvechkarev.letta.media.gallery.GalleryPagerFragment
import kotlinx.android.synthetic.main.fragment_images_list.recyclerPhotos
import java.util.concurrent.atomic.AtomicBoolean

class ImagesListFragment : Fragment(R.layout.fragment_images_list) {
  
  private val loadingFlag = AtomicBoolean()
  
  private val imagesLoader = MediaInjector.getImageLoader(this)
  private lateinit var adapter: ImagesListAdapter
  private lateinit var imagesViewModel: ImagesViewModel
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    imagesViewModel = MediaInjector.provideImagesViewModel(this)
    adapter = ImagesListAdapter(imagesViewModel, this, ::onPhotoClicked, ::startPostponeTransition)
    recyclerPhotos.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
    recyclerPhotos.adapter = adapter
    adapter.submitList(imagesViewModel.loadDataIfNecessary())
    
    prepareTransitions()
    postponeEnterTransition()
  }
  
  private fun onPhotoClicked(clickedView: View, position: Int) {
    // Update the position.
    imagesViewModel.currentPosition = position
    
    // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
    // instead of fading out with the rest to prevent an overlapping animation of fade and move).
    (exitTransition as TransitionSet).excludeTarget(clickedView, true)
    
    val transitioningView = clickedView.findViewById<ConstraintLayout>(R.id.itemImageRoot)
    this.childFragmentManager
      .beginTransaction()
      .setReorderingAllowed(true) // Optimize for shared element transition
      .addSharedElement(transitioningView, transitioningView.transitionName)
      .replace(R.id.recyclerPhotos, GalleryPagerFragment())
      .addToBackStack(null)
      .commit()
  }
  
  private fun prepareTransitions() {
    exitTransition =
      TransitionInflater.from(context).inflateTransition(R.transition.grid_exit_transition)
    // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
    setExitSharedElementCallback(
      object : SharedElementCallback() {
        override fun onMapSharedElements(
          names: List<String?>,
          sharedElements: MutableMap<String?, View?>
        ) { // Locate the ViewHolder for the clicked position.
          val selectedViewHolder =
            recyclerPhotos.findViewHolderForAdapterPosition(imagesViewModel.currentPosition)
          if (selectedViewHolder?.itemView == null) return
          // Map the first shared element name to the child ImageView.
          sharedElements[names[0]] = selectedViewHolder.itemView.findViewById(R.id.imageInRecycler)
        }
      })
  }
  
  private fun startPostponeTransition(position: Int) {
    if (imagesViewModel.currentPosition == position && !loadingFlag.getAndSet(true)) {
      startPostponedEnterTransition()
    }
  }
}