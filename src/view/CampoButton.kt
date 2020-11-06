package view

import model.Campo
import model.CampoEvent
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities

private val COLOR_BACKGROUNG_NORMAL = Color(184, 184, 184)
private val COLOR_BACKGROUNG_MARCACAO = Color(8, 179, 247)
private val COLOR_BACKGROUNG_EXPLOSAO = Color(189, 66, 68)
private val COLOR_TEXT_VERDE = Color(0, 100, 0)
private val COLOR_TEXT_VERMELHO_ESCURO = Color(180, 0, 0);

class CampoButton(private val campo: Campo) : JButton() {
    init {
        font = font.deriveFont(Font.BOLD)
        background = COLOR_BACKGROUNG_NORMAL
        isOpaque = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(campo, { campo -> campo.abrir() }, { campo -> campo.alterarMarcacao() }))

        campo.onEvent(this::aplicarEstilo)
    }

    private fun aplicarEstilo(campo: Campo, event: CampoEvent) {
        when (event) {
            CampoEvent.EXPLOSAO -> aplicarEstiloExplodido()
            CampoEvent.ABERTURA -> aplicarEstiloAberto()
            CampoEvent.MARCACAO -> aplicarEstiloMarcado()
            else -> aplicarEstiloPadrao()
        }

        //atualizar view
        SwingUtilities.invokeLater {
            repaint()
            validate()
        }
    }

    private fun aplicarEstiloExplodido() {
        background = COLOR_BACKGROUNG_EXPLOSAO
        foreground = Color.BLACK
        text = "X"
    }

    private fun aplicarEstiloAberto() {
        background = COLOR_BACKGROUNG_NORMAL
        border = BorderFactory.createLineBorder(Color.GRAY)

        foreground = when(campo.qtdVizinhosMinados) {
            1 -> COLOR_TEXT_VERDE
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4 -> Color.RED
            5, 6 -> COLOR_TEXT_VERMELHO_ESCURO
            else -> Color.PINK
        }

        text = if (campo.qtdVizinhosMinados > 0) campo.qtdVizinhosMinados.toString() else ""
    }

    private fun aplicarEstiloMarcado() {
        background = COLOR_BACKGROUNG_MARCACAO
        foreground = Color.BLACK
        text = "M"
    }

    private fun aplicarEstiloPadrao() {
        background = COLOR_BACKGROUNG_NORMAL
        border = BorderFactory.createBevelBorder(0)
        text = ""
    }
}
