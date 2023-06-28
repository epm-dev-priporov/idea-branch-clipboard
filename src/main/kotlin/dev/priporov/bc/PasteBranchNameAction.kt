package dev.priporov.bc

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import git4idea.GitUtil
import git4idea.repo.GitRepository
import java.awt.Toolkit
import java.awt.datatransfer.*

class PasteBranchNameAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.getData(CommonDataKeys.PROJECT) ?: return
        val repositories: MutableCollection<GitRepository> = GitUtil.getRepositories(project)

        if (repositories.isEmpty()) {
            return
        }

        val currentBranch = repositories.first().currentBranch?.name ?: ""
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val clipboardOwner = ClipboardOwner { _, _ -> }
        clipboard.setContents(BranchNameTransferable(currentBranch), clipboardOwner)

        println(currentBranch)
    }

}

class BranchNameTransferable(private val data: String) : Transferable, ClipboardOwner {

    private val dataFlavors = arrayOf(DataFlavor.stringFlavor)

    override fun getTransferDataFlavors(): Array<DataFlavor> = dataFlavors

    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean = flavor == DataFlavor.stringFlavor

    override fun getTransferData(flavor: DataFlavor?): Any {
        if (!isDataFlavorSupported(flavor)) {
            throw UnsupportedFlavorException(flavor)
        }
        if (DataFlavor.stringFlavor == flavor) {
            return data
        }
        return data
    }

    override fun lostOwnership(clipboard: Clipboard?, contents: Transferable?) {}

}