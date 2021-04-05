@file:Suppress("FunctionName")

package ui.exercise.result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import edu.umd.cs.treemap.MapItem
import edu.umd.cs.treemap.PivotBySplitSize
import edu.umd.cs.treemap.TreeModel
import textgen.error.CharEvaluation
import textgen.error.TextEvaluation

@Composable
fun TypingErrorTreeMap(modifier: Modifier = Modifier, list: List<TextEvaluation>) {
    val charMapped = list
        .map { textEval ->
            textEval.chars
                .filterIsInstance<CharEvaluation.TypingError>()
                .map { it.getExpectedChar(textEval.text) }
        }
        .flatten()
        .groupBy { it } // TODO change to it.actual
        .flatMap { listOf(it.key to it.value.size) }
        .sortedBy { it.second }

    println(charMapped)
    val sum = charMapped.map { it.second }.sum()
    Box(
        modifier = modifier
            .padding(all = 10.dp)
            .fillMaxSize()
            .background(Color.Cyan)
    ) {
        Layout(
            content = {
                for (charValue in charMapped) {
                    Box(
                        modifier = Modifier.border(width = 3.dp, color = Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {

                            Text(charValue.first.toString())
                            Text("${charValue.second}/$sum")
                        }
                    }
                }
            }
        ) { list, constraints ->

            var order = 1
            val root = MapItem(sum.toDouble(), order)
            val tree = TreeModel(root).apply {
                for (item in charMapped) {
                    val percentage = item.second//.div(sum.toFloat())
                    this.addChild(TreeModel(MapItem(percentage.toDouble(), order++)))
                }
            }
            tree.layout(PivotBySplitSize())
            tree.print()
            println("tree = ${tree.treeItems.contentDeepToString()}")


            val placeables = list.mapIndexed { index: Int, measurable: Measurable ->
                val bounds = tree.treeItems[index].bounds
                val const = Constraints(
                    maxHeight = (bounds.h * constraints.maxHeight).toInt(),
                    minHeight = (bounds.h * constraints.maxHeight).toInt(),
                    maxWidth = (bounds.w * constraints.maxWidth).toInt(),
                    minWidth = (bounds.w * constraints.maxWidth).toInt(),
                )
                measurable.measure(const)
            }
            layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                placeables.forEachIndexed { index, placeable ->
                    val bounds = tree.treeItems[index].bounds
                    placeable.place(
                        (bounds.x * constraints.maxWidth).toInt(),
                        (bounds.y * constraints.maxHeight).toInt()
                    )
                }
            }
        }
    }
}
