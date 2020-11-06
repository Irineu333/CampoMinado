package view

import model.Tabuleiro
import java.awt.GridLayout
import javax.swing.JPanel

class PainelTabuleiro(tauleiro : Tabuleiro) : JPanel() {

    init {
        layout = GridLayout(tauleiro.qtdLinhas, tauleiro.qtdColunas)
        tauleiro.forEach {  campo ->
            val button = CampoButton(campo)
            add(button)
        }
    }
}