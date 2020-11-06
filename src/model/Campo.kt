package model

enum class CampoEvent {
    ABERTURA,
    MARCACAO,
    DESMARCACAO,
    EXPLOSAO,
    REINICIALIZACAO
}

data class Campo(val linha: Int, val coluna: Int) {
    private val vizinhos = ArrayList<Campo>()
    private val callbacks = ArrayList<(Campo, CampoEvent) -> Unit>()

    var marcado: Boolean = false
    var aberto: Boolean = false
    var minado: Boolean = false

    //Somente leitura
    val desmarcado: Boolean
        get() = !marcado
    val fechado: Boolean
        get() = !aberto
    val seguro: Boolean
        get() = !minado
    val ojetivoAlcancado: Boolean
        get() = seguro && aberto || minado && marcado
    val qtdVizinhosMinados: Int
        get() = vizinhos.filter { it.minado }.size
    val vizinhancaSegura: Boolean
        get() = vizinhos.map { it.seguro }.reduce { result, seguro -> result && seguro }

    fun addVizinho(vizinho: Campo) {
        vizinhos.add(vizinho) //criar uma validação depois
    }

    fun onEvent(callback: (Campo, CampoEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun abrir() {
        if (fechado) {
            aberto = true
            if (minado) {
                callbacks.forEach { it(this, CampoEvent.EXPLOSAO) }
            } else {
                callbacks.forEach { it(this, CampoEvent.ABERTURA) }
                vizinhos.filter { it.fechado && it.seguro && vizinhancaSegura }.forEach { it.abrir() }
            }
        }
    }
    fun revelar() {
        if (fechado) {
            aberto = true
            if (minado) {
                callbacks.forEach { it(this, CampoEvent.EXPLOSAO) }
            } else {
                callbacks.forEach { it(this, CampoEvent.ABERTURA) }
            }
        }
    }

    fun alterarMarcacao() {
        if (fechado) {
            marcado = !marcado
            val event = if (marcado) CampoEvent.MARCACAO else CampoEvent.DESMARCACAO
            callbacks.forEach { it(this, event) }
        }
    }

    fun minar() {
        minado = true
    }

    fun reiniciar() {
        aberto = false
        minado = false
        marcado = false
        callbacks.forEach { it(this, CampoEvent.REINICIALIZACAO) }
    }
}