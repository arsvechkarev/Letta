package com.arsvechkarev.letta.media

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.letta.CommonInjector
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.editing.EditFragment
import com.arsvechkarev.letta.media.common.ImagesViewModel
import kotlinx.android.synthetic.main.fragment_images_list.recyclerPhotos
import java.util.concurrent.atomic.AtomicBoolean

class ImagesListFragment : Fragment(R.layout.fragment_images_list) {
  
  private val loadingFlag = AtomicBoolean()
  
  private lateinit var adapter: ImagesListAdapter
  private lateinit var imagesViewModel: ImagesViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    imagesViewModel = MediaInjector.provideImagesViewModel(this)
    adapter = ImagesListAdapter(
      imagesViewModel, CommonInjector.getImagesLoader(this),
      ::onPhotoClicked, ::startPostponeTransition
    )
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    recyclerPhotos.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
    recyclerPhotos.adapter = adapter
    adapter.submitList(imagesViewModel.loadDataIfNecessary())
    
    prepareTransitions()
    postponeEnterTransition()
    scrollToPosition()
  }
  
  
  /**
   * Scrolls the recycler view to show the last viewed item in the grid. This is important when
   * navigating back from the grid.
   */
  private fun scrollToPosition() {
    recyclerPhotos.addOnLayoutChangeListener(object : OnLayoutChangeListener {
      override fun onLayoutChange(
        v: View, left: Int, top: Int, right: Int, bottom: Int,
        oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
      ) {
        recyclerPhotos.removeOnLayoutChangeListener(this)
        val layoutManager = recyclerPhotos.layoutManager!!
        val viewAtPosition = layoutManager.findViewByPosition(imagesViewModel.currentPosition)
        if (viewAtPosition == null || layoutManager
            .isViewPartiallyVisible(viewAtPosition, false, true)
        ) {
          recyclerPhotos.post { layoutManager.scrollToPosition(imagesViewModel.currentPosition) }
        }
      }
    })
  }
  
  private fun onPhotoClicked(clickedView: View, position: Int) {
    // Update the position.
    imagesViewModel.currentPosition = position
    
    val editFragment = EditFragment.create(imagesViewModel.data[position].url)
    requireActivity().supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, editFragment)
      .addToBackStack(null)
      .commit()
    
    // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
    // instead of fading out with the rest to prevent an overlapping animation of fade and move).
    //    (exitTransition as TransitionSet).excludeTarget(clickedView, true)
    //
    //    requireActivity().supportFragmentManager
    //        .beginTransaction()
    //        .addSharedElement(clickedView, clickedView.transitionName)
    //        .replace(R.id.fragmentContainer, GalleryPagerFragment(), GalleryPagerFragment::class.java.simpleName)
    //        .addToBackStack(null)
    //        .commit()
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
    if (imagesViewModel.currentPosition != position || loadingFlag.getAndSet(true)) return
    startPostponedEnterTransition()
  }
}