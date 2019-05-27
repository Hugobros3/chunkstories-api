package xyz.chunkstories.api.gui.elements

import org.joml.Vector4f
import xyz.chunkstories.api.gui.ClickableGuiElement
import xyz.chunkstories.api.gui.GuiDrawer
import xyz.chunkstories.api.gui.GuiElement
import xyz.chunkstories.api.gui.Layer
import xyz.chunkstories.api.input.Mouse
import java.lang.Integer.min

open class Scroller<T : GuiElement>(layer: Layer, width: Int, height: Int, initialContents: Collection<T>) : GuiElement(layer, width, height), ClickableGuiElement {

    var elementsSpacing = 8
    var scrollIncrements = 8

    var scroll = 0
    val elements = initialContents.toMutableList()

    var draggingScrollBar = false

    override fun render(drawer: GuiDrawer) {
        if (scroll < 0)
            scroll = 0

        val elementHeight = elements.getOrNull(0)?.height ?: 0
        val totalElementsHeight = elementHeight * elements.size + if (elements.size > 1) (elements.size - 1) * elementsSpacing else 0
        var maxScroll = 0

        var h = totalElementsHeight
        while (h > height) {
            h -= scrollIncrements
            if (h >= height - (scrollIncrements - 1))
                maxScroll++
        }

        if (scroll > maxScroll)
            scroll = maxScroll

        //debug helper
        //drawer.drawBox(positionX, positionY, width, height, Vector4f(1.0f, 0f, 0f, 0.25f))

        drawer.drawBox(positionX, positionY + height - min(height, totalElementsHeight), width, min(height, totalElementsHeight), Vector4f(0.0f, 0f, 0f, 0.25f))

        // Make space for the scroll bar if needed
        val availableWidth = if (maxScroll > 0) width - 8 else width

        // Draw the elements in a scissor viewport, using an offset
        var yPosition = positionY + height + scroll * scrollIncrements

        if (scroll > 0 && scroll == maxScroll)
            yPosition = positionY + totalElementsHeight

        drawer.withScissor(positionX, positionY, width, height) {
            for (element in elements) {
                yPosition -= element.height

                element.setPosition(positionX, yPosition)
                element.width = availableWidth
                element.render(drawer)

                yPosition -= elementsSpacing
            }
        }

        if (maxScroll > 0) {
            //debug helper
            //drawer.drawBox(positionX, positionY, availableWidth, height, Vector4f(1.0f, 1f, 0f, 0.25f))

            val barSize = height * height / totalElementsHeight
            var offset = height * scroll * scrollIncrements / totalElementsHeight

            if (scroll > 0 && scroll == maxScroll)
                offset = height - barSize

            var hover = false

            val mx = drawer.gui.mouse.cursorX
            val my = drawer.gui.mouse.cursorY
            if ((mx >= positionX + availableWidth && mx < positionX + availableWidth + 8) || draggingScrollBar) {
                if ((my >= positionY && my < positionY + height) || draggingScrollBar) {
                    hover = true
                }
            }

            if (drawer.gui.mouse.mainButton.isPressed) {

                if (hover || draggingScrollBar) {
                    if (hover || draggingScrollBar) {
                        draggingScrollBar = drawer.gui.mouse.mainButton.isPressed
                        val relativeMy = positionY + height - my

                        scroll = ((relativeMy - barSize / 2) / (height - barSize) * maxScroll).toInt()
                    }
                }
            } else {
                draggingScrollBar = false
            }

            drawer.drawBox(positionX + availableWidth, positionY, 8, height, Vector4f(0.0f, 0f, 0f, 0.25f))
            drawer.drawBox(positionX + availableWidth, positionY + height - barSize - offset, 8, barSize, Vector4f(1.0f, 1f, 1f, if(draggingScrollBar) 1f else if(hover) 0.75f else 0.5f))
        }
    }

    fun handleScroll(mouseScroll: Mouse.MouseScroll) {
        scroll -= mouseScroll.amount()
    }

    override fun handleClick(mouseButton: Mouse.MouseButton): Boolean {
        val elementHeight = elements.getOrNull(0)?.height ?: 0
        val totalElementsHeight = elementHeight * elements.size + if (elements.size > 1) (elements.size - 1) * elementsSpacing else 0
        var maxScroll = 0

        var h = totalElementsHeight
        while (h > height) {
            h -= scrollIncrements
            if (h >= height - (scrollIncrements - 1))
                maxScroll++
        }

        if (scroll > maxScroll)
            scroll = maxScroll

        // Draw the elements in a scissor viewport, using an offset
        var yPosition = positionY + height + scroll * scrollIncrements

        if (scroll > 0 && scroll == maxScroll)
            yPosition = positionY + totalElementsHeight

        for (element in elements) {
            yPosition -= element.height

            //element.setPosition(positionX, yPosition)
            //element.width = width
            if (element.isMouseOver && element is ClickableGuiElement)
                return element.handleClick(mouseButton)

            yPosition -= elementsSpacing
        }

        return false
    }
}