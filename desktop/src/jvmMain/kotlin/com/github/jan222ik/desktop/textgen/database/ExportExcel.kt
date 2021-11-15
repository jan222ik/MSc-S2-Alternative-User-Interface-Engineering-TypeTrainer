package com.github.jan222ik.desktop.textgen.database

import com.github.jan222ik.desktop.textgen.error.CharEvaluation
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

object ExportExcelUtil {

    fun createWorkbook() : Workbook {
        return XSSFWorkbook()
    }

    fun save(file: File, workbook: Workbook) {
        val directory = file.parentFile
        println("directory = ${directory.absolutePath}")
        if (!directory.exists()) {
            directory.mkdir()
            if (!file.exists()) {
                file.parentFile.mkdir()
                file.createNewFile()
            }
        }
        val foS = FileOutputStream(file)
        workbook.write(foS)
    }

    fun run2Sheet(workbook: Workbook, localDateTime: LocalDateTime, exerciseEvaluation: ExerciseEvaluation) {
        val newSheet: Sheet = workbook.createSheet(
            "run-${localDateTime.toString().replace(":", "-")}"
        )
        val headerRow = newSheet.createRow(0)
        listOf(
            "timeRemaining",
            "expectedChar",
            "actual",
            "isError"
        ).forEachIndexed() { idx, it ->
            headerRow.createCell(idx).setCellValue(it)
        }
        var rowIdx = 0
        exerciseEvaluation.texts.forEach { textEvaluation ->
            textEvaluation.chars.forEach { charEvaluation ->
                rowIdx += 1
                val expectedChar = charEvaluation.getExpectedChar(textEvaluation.text)
                val row = newSheet.createRow(rowIdx)
                row
                    .createCell(0)
                    .setCellValue(charEvaluation.timeRemaining.toDouble())
                row
                    .createCell(1)
                    .setCellValue(expectedChar.toString())
                when (charEvaluation) {
                    is CharEvaluation.Correct -> {
                        row
                            .createCell(3)
                            .setCellValue(false)
                    }
                    is CharEvaluation.FingerError -> Unit
                    is CharEvaluation.TypingError -> {
                        row
                            .createCell(2)
                            .setCellValue(charEvaluation.actual.toString())
                        row
                            .createCell(3)
                            .setCellValue(true)
                    }
                }

            }

        }

    }

}

fun main() {
    val baseDir = "Z:\\Users\\jan22\\CodeProjects\\TypeTrainerMultiplatform\\desktop\\build\\tmp\\excel\\"
    val fileName = "test.xlsx"

    // Creating excel workbook
    val workbook = XSSFWorkbook()



    ExportExcelUtil.run2Sheet(workbook, LocalDateTime.now(), DEMO.demoData)

    ExportExcelUtil.save(File(baseDir + fileName), workbook)
}
