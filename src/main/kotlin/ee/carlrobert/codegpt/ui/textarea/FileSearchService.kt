package ee.carlrobert.codegpt.ui.textarea

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.readText
import ee.carlrobert.codegpt.CodeGPTKeys
import ee.carlrobert.codegpt.ReferencedFile
import ee.carlrobert.codegpt.actions.IncludeFilesInContextNotifier
import java.io.File

@Service
class FileSearchService private constructor(val project: Project) {

    companion object {
        private val logger = thisLogger()
    }

    fun addFileToSession(file: VirtualFile) {
        addFilesToSession(listOf(file))
    }

    fun addFilesToSession(files: List<VirtualFile>) {
        val filesIncluded =
            project.getUserData(CodeGPTKeys.SELECTED_FILES).orEmpty().toMutableList()
        files.forEach { file ->
            try {
                filesIncluded.add(ReferencedFile(file.name, file.path, file.readText()))
            } catch (e: Exception) {
                logger.error("Failed to add file to session", e)
            }
        }
        updateFilesInSession(filesIncluded)
    }

    fun removeFilesFromSession() = updateFilesInSession(mutableListOf())

    private fun updateFilesInSession(files: MutableList<ReferencedFile>) {
        project.putUserData(CodeGPTKeys.SELECTED_FILES, files)
        project.messageBus
            .syncPublisher(IncludeFilesInContextNotifier.FILES_INCLUDED_IN_CONTEXT_TOPIC)
            .filesIncluded(files)
    }
}