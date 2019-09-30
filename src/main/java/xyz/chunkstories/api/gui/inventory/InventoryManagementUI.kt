//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.inventory

import xyz.chunkstories.api.gui.Gui
import xyz.chunkstories.api.gui.GuiDrawer
import xyz.chunkstories.api.gui.GuiElement
import xyz.chunkstories.api.gui.Layer
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.input.Mouse

class InventoryManagementUI(gui: Gui, parentLayer: Layer?) : Layer(gui, parentLayer) {
    val subwindows = mutableListOf<InventoryManagementUIPanel>()

    override fun render(drawer: GuiDrawer) {
        width = gui.viewportWidth
        height = gui.viewportHeight

        val spacing = 32
        var totalWidth = -spacing
        for (window in subwindows) {
            totalWidth += window.width + spacing
        }

        var posx = width / 2 - totalWidth / 2

        subwindows.forEach { window ->
            window.positionX = posx
            posx += window.width + spacing
            window.positionY = height / 2 - window.height / 2
            window.render(drawer)
        }
    }

    override fun handleInput(input: Input): Boolean {
        if (input is Mouse.MouseButton) {
            for (window in subwindows) {
                if (window.handleClick(input))
                    return true
            }

            if (draggingFrom != null) {
                transfer(draggingFrom!!, null, draggingAmount, gui.client.ingame?.player)
                draggingFrom = null
            }
        }

        if (input.name == "exit") {
            gui.popTopLayer()
        }

        return super.handleInput(input)
    }

    companion object {
        var draggingFrom: InventorySlot? = null
        var draggingAmount = 0
    }
}

open class InventoryManagementUIPanel(layer: Layer, width: Int, height: Int) : GuiElement(layer, width, height) {
    val slots = mutableListOf<InventorySlotUI>()

    inner class InventorySlotUI(val underlying: InventorySlot, val x: Int, val y: Int) {
        val width = 20
        val height = 20

        fun render(drawer: GuiDrawer) {
            if (isMouseOver())
                drawer.drawBox(this@InventoryManagementUIPanel.positionX + x, this@InventoryManagementUIPanel.positionY + y, 20, 20, "textures/gui/inventory/slot_hover.png")
            else
                drawer.drawBox(this@InventoryManagementUIPanel.positionX + x, this@InventoryManagementUIPanel.positionY + y, 20, 20, "textures/gui/inventory/slot.png")
        }

        fun isMouseOver() =
                layer.gui.mouse.cursorX > this@InventoryManagementUIPanel.positionX + x && layer.gui.mouse.cursorX <= this@InventoryManagementUIPanel.positionX + x + width &&
                        layer.gui.mouse.cursorY > this@InventoryManagementUIPanel.positionY + y && layer.gui.mouse.cursorY <= this@InventoryManagementUIPanel.positionY + y + height


        fun handleClick(mouseButton: Mouse.MouseButton) {
            if (InventoryManagementUI.draggingFrom != null) {
                if (InventoryManagementUI.draggingFrom != underlying) {
                    val amountToTransfer = if (mouseButton.name == "mouse.right") 1 else InventoryManagementUI.draggingAmount

                    transfer(InventoryManagementUI.draggingFrom!!, underlying, amountToTransfer, layer.gui.client.ingame?.player)

                    InventoryManagementUI.draggingAmount -= amountToTransfer
                } else {
                    if(mouseButton.name == "mouse.left") {
                        InventoryManagementUI.draggingFrom = null
                    }
                    else if (mouseButton.name == "mouse.right" && InventoryManagementUI.draggingAmount < InventoryManagementUI.draggingFrom!!.visibleContents?.second ?: 0) {
                        InventoryManagementUI.draggingAmount++
                        return
                    }
                }

                if (InventoryManagementUI.draggingAmount < 0) {
                    throw RuntimeException("InventoryManagementUI.draggingAmount < 0")
                } else if (InventoryManagementUI.draggingAmount == 0)
                    InventoryManagementUI.draggingFrom = null
            } else {
                val (item, amount) = underlying.visibleContents ?: return
                if(amount <= 0)
                    return
                InventoryManagementUI.draggingFrom = underlying

                if (mouseButton.name == "mouse.left")
                    InventoryManagementUI.draggingAmount = amount
                else
                    InventoryManagementUI.draggingAmount = 1
            }
        }
    }

    override fun render(drawer: GuiDrawer) {
        drawer.drawBoxWithCorners(positionX, positionY, width, height, 8, "textures/gui/scalableButton.png")
        slots.forEach { it.render(drawer) }

        for (slot in slots) {
            var (item, amount) = slot.underlying.visibleContents ?: continue
            if (InventoryManagementUI.draggingFrom == slot.underlying)
                amount -= InventoryManagementUI.draggingAmount
            if (amount <= 0)
                continue

            drawer.drawBox(this@InventoryManagementUIPanel.positionX + slot.x + 2, this@InventoryManagementUIPanel.positionY + slot.y + 2, 16, 16, item.getTextureName())
        }

        for (slot in slots) {
            var (item, amount) = slot.underlying.visibleContents ?: continue
            if (InventoryManagementUI.draggingFrom == slot.underlying)
                amount -= InventoryManagementUI.draggingAmount
            if (amount <= 1)
                continue

            drawer.drawStringWithShadow(layer.gui.fonts.defaultFont(), this@InventoryManagementUIPanel.positionX + slot.x + 20 * item.definition.slotsWidth - 8, this@InventoryManagementUIPanel.positionY + slot.y - 2, "$amount")
        }

        if (InventoryManagementUI.draggingFrom != null) {
            val (item, amount) = InventoryManagementUI.draggingFrom?.visibleContents ?: return

            val mx = drawer.gui.mouse.cursorX.toInt()
            val my = drawer.gui.mouse.cursorY.toInt()
            drawer.drawBox(mx - 8, my - 8, 16, 16, item.getTextureName())
            drawer.drawStringWithShadow(layer.gui.fonts.defaultFont(), mx + 20 * item.definition.slotsWidth - 18, my - 18, "${InventoryManagementUI.draggingAmount}")
        }
    }

    open fun handleClick(mouseButton: Mouse.MouseButton): Boolean {
        for (slot in slots) {
            if (slot.isMouseOver()) {
                slot.handleClick(mouseButton)
                return true
            }
        }
        return false
    }
}