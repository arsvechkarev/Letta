package com.arsvechkarev.letta.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.arsvechkarev.letta.R
import com.arsvechkarev.letta.core.model.Project
import com.arsvechkarev.letta.extensions.projectsDirectory
import java.io.File

class AndroidSharer(private val context: Context) : Sharer {
  
  override fun share(projects: Collection<Project>) {
    val sharingIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
    val uris = ArrayList<Uri>()
    projects.forEach { project ->
      val uri = FileProvider.getUriForFile(context,
        context.packageName,
        File(context.projectsDirectory, project.filename)
      )
      uris.add(uri)
    }
    sharingIntent.type = "image/jpeg"
    sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
    context.startActivity(
      Intent.createChooser(sharingIntent, context.getString(R.string.text_share)))
  }
}