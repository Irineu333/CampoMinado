package model

import java.util.*
import kotlin.collections.ArrayList


enum class TabuleiroEvent { VITORIA, DERROTA }

class Tabuleiro(val qtdLinhas: Int, val qtdColunas: Int, private val qtdMinas: Int) {
    private val campos = ArrayList<ArrayList<Campo>>() //Matriz
    private val callbacks = ArrayList<(TabuleiroEvent) -> Unit>()

    init {
        gerarCampos()
        associarVizinhos()
        sortearMinas()
    }

    private fun gerarCampos() {
        //se for chamado mais de uma vez na mesma instancia vai dar ruim
        for (linha in 0 until qtdLinhas) {
            campos.add(ArrayList())
            for (coluna in 0 until qtdColunas) {
                val novoCampo = Campo(linha, coluna)
                novoCampo.onEvent(this::gameEndChecker)
                campos[linha].add(novoCampo)
            }
        }
    }

    private fun associarVizinhos() {
        forEach { associarVizinhos(it) }
    }

    private fun associarVizinhos(campoAtual: Campo) {
        val (linha, coluna) = campoAtual
        val linhas = arrayOf(linha - 1, linha, linha + 1)
        val colunas = arrayOf(coluna - 1, coluna, coluna + 1)

        linhas.forEach { l ->
            colunas.forEach { c ->
                val atual = campos.getOrNull(l)?.getOrNull(c) //volta nulo se não existir esse campo
                atual?.takeIf { campo -> campoAtual != campo }?.let { campo -> campoAtual.addVizinho(campo) }
            }
        }
    }

    private fun sortearMinas() {
        val gerador = Random()

        var linhaSorteada: Int
        var colunaSorteada: Int
        var qtdMinasAtual = 0
        while (qtdMinasAtual < this.qtdMinas) {
            linhaSorteada = gerador.nextInt(qtdLinhas)
            colunaSorteada = gerador.nextInt(qtdColunas)

            val campoSorteado = campos[linhaSorteada][colunaSorteada]
            if (campoSorteado.seguro) {
                campoSorteado.minar()
                qtdMinasAtual++
            }
        }
    }

    private fun objetivoAlcancado(): Boolean {
        var jogadorGanhou = true
        forEach { campo ->
            if (!campo.ojetivoAlcancado) {
                jogadorGanhou = false
                return@forEach //cometi esse pecado em nome da otimização
            }
        }
        return jogadorGanhou
    }

    private fun gameEndChecker(campo: Campo, event: CampoEvent) {
        if (event == CampoEvent.EXPLOSAO) {
            callbacks.forEach { it(TabuleiroEvent.DERROTA) }
        } else if (objetivoAlcancado()) {
            callbacks.forEach { it(TabuleiroEvent.VITORIA) }
        }
    }

    fun forEach(callback: (Campo) -> Unit) {
        campos.forEach { linha -> linha.forEach(callback) }
    }

    fun onEvent(callback: (TabuleiroEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun reiniciar() {
        forEach { it.reiniciar() }
        sortearMinas()
    }

    fun abrirTudo() {
        campos.onEach {
            it.forEach { campo ->
                campo.revelar()
            }
        }
    }
}