package view

import model.Campo
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class MouseClickListener (
    private val campo: Campo,
    private val onLeftClick: (Campo) -> Unit,
    private val onRightClick: (Campo) -> Unit
) : MouseListener {
    override fun mouseClicked(event: MouseEvent?) {
        when(event?.button) {
            MouseEvent.BUTTON1 -> onLeftClick(campo)
            MouseEvent.BUTTON3 -> onRightClick(campo)
        }
    }

    override fun mousePressed(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}

}