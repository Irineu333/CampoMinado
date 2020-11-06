package view

import model.Tabuleiro
import model.TabuleiroEvent
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

/**
 * Classe principal, responsável por iniciar a aplicação
 * @author Irineu A. Silva (Curso Fundamentos de Programação Moderna, Cod3r, Udermy)
 */

fun main(args: Array<String>) {
    TelaPrincipal()
}

class TelaPrincipal : JFrame() {
    private val tabuleiro = Tabuleiro(16, 20, 30)
    private val painelTabuleiro = PainelTabuleiro(tabuleiro)
    private var runing = true

    init {
        tabuleiro.onEvent(this::callBack)
        add(painelTabuleiro)

        setSize(500, 430)
        localizarTelaNoCentro()
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "NeoMined"
        isVisible = true
    }

    private fun callBack(event: TabuleiroEvent)
    {
        if (runing)
            mostrarResultado(event)
    }

    private fun mostrarResultado(event: TabuleiroEvent) {
        runing = false
        SwingUtilities.invokeLater {
            val msg = when (event) {
                TabuleiroEvent.VITORIA -> "Parabéns, você é pika!"
                TabuleiroEvent.DERROTA -> "Perdeu otário!"
            }
            tabuleiro.abrirTudo()

            JOptionPane.showMessageDialog(this, msg)

            runing = true
            tabuleiro.reiniciar()

            painelTabuleiro.repaint()
            painelTabuleiro.validate()
        }

    }

    private fun localizarTelaNoCentro() {
        setLocationRelativeTo(null)
    }
}