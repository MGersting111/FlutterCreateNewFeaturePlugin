package com.example.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths



class NewFeatureAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val virtualFile = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        val basePath = virtualFile?.path ?: return

        val featureName = Messages.showInputDialog(
            e.project,
            "Gib den Namen des neuen Features ein:",
            "Neues Feature erstellen",
            Messages.getQuestionIcon()
        ) ?: return

        val featurePath = "$basePath/$featureName"

        val folders = listOf(
            "data",
            "data/datasources",
            "data/repositories",
            "data/models",
            "domain",
            "domain/repositories",
            "domain/entities",
            "domain/usecases",
            "domain/mapper",
            "presentation",
            "presentation/widgets",
            "presentation/bloc"
        )
        folders.forEach {
            File("$featurePath/$it").mkdirs()
        }

        val formattedFeatureName = featureName.lowercase()

        createDartFile("$featurePath/data/datasources", "${formattedFeatureName}_datasource.dart")
        createDartFile("$featurePath/data/repositories", "local_${formattedFeatureName}_repository.dart")
        createDartFile("$featurePath/domain/repositories", "${formattedFeatureName}_repository.dart")

        VirtualFileManager.getInstance().asyncRefresh {
            println("Ordnerstruktur wurde aktualisiert.")
        }
    }

    private fun createDartFile(directoryPath: String, fileName: String) {
        try {
            val filePath = Paths.get(directoryPath, fileName)
            if (!Files.exists(filePath)) {
                Files.createFile(filePath)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}


