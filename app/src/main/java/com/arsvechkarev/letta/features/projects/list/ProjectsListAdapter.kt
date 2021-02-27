package com.arsvechkarev.letta.features.projects.list

import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.letta.core.model.LoadingMoreProjects
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.core.recycler.CallbackType
import com.arsvechkarev.letta.core.recycler.DifferentiableItem
import com.arsvechkarev.letta.core.recycler.MultiSelectionAdapter
import com.arsvechkarev.letta.extensions.childViewAs
import com.arsvechkarev.letta.features.projects.list.ProjectItemAdapterDelegate.Companion.Checkmark
import com.arsvechkarev.letta.features.projects.list.ProjectItemAdapterDelegate.Companion.Image
import com.arsvechkarev.letta.features.projects.list.ProjectItemAdapterDelegate.ProjectViewHolder
import com.arsvechkarev.letta.views.Checkmark
import com.arsvechkarev.letta.views.ClickableSquareImage

class ProjectsListAdapter(
  onProjectClick: (Project) -> Unit,
  onReadyToLoadFurtherData: (() -> Unit)? = null,
  onLongClick: () -> Unit,
  onProjectSelected: (Project) -> Unit,
  onProjectUnselected: (Project) -> Unit,
) : MultiSelectionAdapter<DifferentiableItem>(onReadyToLoadFurtherData) {
  
  init {
    setHasStableIds(true)
    val projectAdapterDelegate = ProjectItemAdapterDelegate(
      isInSelectionModeLambda,
      onProjectClick,
      onProjectSelected,
      onProjectUnselected,
      onLongClick
    )
    @Suppress("UNCHECKED_CAST")
    projectAdapterDelegate.setSelectedPositions(selectedItems as ArrayList<Project>)
    addDelegates(projectAdapterDelegate, LoadingItemDelegate())
  }
  
  fun addLoadingItem(item: LoadingMoreProjects) {
    if (data.last() != item) addItemToTheEnd(item)
  }
  
  fun deleteItems(list: Collection<Project>) {
    val newList = ArrayList(data)
    newList.removeAll(list)
    submitList(newList, CallbackType.TWO_LISTS)
  }
  
  override fun readyToStartLoadingData(position: Int): Boolean {
    return position == data.size - 9
  }
  
  override fun onSwitchingToSelectionMode(layoutManager: LinearLayoutManager) {
    val start = layoutManager.findFirstVisibleItemPosition()
    val end = layoutManager.findLastVisibleItemPosition()
    for (i in start..end) {
      val holder = recyclerView?.findViewHolderForAdapterPosition(i) ?: continue
      if (holder !is ProjectViewHolder) continue
      val child = holder.itemView
      val checkmark = child.childViewAs<Checkmark>(Checkmark)
      val image = child.childViewAs<ClickableSquareImage>(Image)
      checkmark.drawBorder = true
      image.scaleOnTouch = false
    }
    notifyItemRangeChanged(end + 1, itemCount - end + 1)
    notifyItemRangeChanged(0, start)
  }
  
  override fun onSwitchingBackFromSelectionMode(layoutManager: LinearLayoutManager) {
    val start = layoutManager.findFirstVisibleItemPosition()
    val end = layoutManager.findLastVisibleItemPosition()
    for (i in start..end) {
      val holder = recyclerView?.findViewHolderForAdapterPosition(i) ?: continue
      if (holder !is ProjectViewHolder) continue
      val child = holder.itemView
      val checkmark = child.childViewAs<Checkmark>(Checkmark)
      val image = child.childViewAs<ClickableSquareImage>(Image)
      checkmark.drawBorder = false
      checkmark.isChecked = false
      image.scaleOnTouch = true
    }
    notifyItemRangeChanged(end + 1, itemCount - end + 1)
    notifyItemRangeChanged(0, start)
  }
}