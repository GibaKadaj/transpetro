package com.prep.transpetro.domain.model

object StudyConstants {

    const val EXAM_DATE = "2026-12-06"
    const val PREP_START_DATE = "2026-09-14"
    const val NO_NEW_CONTENT_AFTER = "2026-11-08"
    const val TOTAL_DAYS = 84

    const val SECURITY_QUESTIONS = 40
    const val PORTUGUESE_QUESTIONS = 10
    const val MATH_QUESTIONS = 10
    const val TOTAL_QUESTIONS = 60

    const val MIN_SECURITY_PASS = 20
    const val MIN_GENERAL_PASS = 10

    const val GOAL_SECURITY = 32
    const val GOAL_PORTUGUESE = 7
    const val GOAL_MATH = 7

    object Phase {
        const val FOUNDATION = "Fundação"
        const val SWEEP = "Varredura"
        const val FINAL = "Reta Final"
        const val PHASE1_END = "2026-10-11"
        const val PHASE2_END = "2026-11-08"
    }

    data class BlockInfo(
        val code: String,
        val name: String,
        val weight: Int,
        val plannedWeeks: String
    )

    val BLOCKS = listOf(
        BlockInfo("B1",  "Prevenção e Controle de Riscos (NR-10, 11, 12, 13, 18, 20, 33, 35, trabalhos a quente, CTB)", 5, "2,3"),
        BlockInfo("B2",  "Higiene Ocupacional (ruído, calor, vibração, radiação, gases, vapores, aerodispersoides, FDS, PPR, PCA, Fundacentro)", 5, "3"),
        BlockInfo("B3",  "Proteção contra Incêndio (sistemas fixos e portáteis, detecção e alarme, brigada, plano de emergência)", 4, "6,7"),
        BlockInfo("B4",  "Legislação e Normas Técnicas (PNSST, CF/88, CLT, NR-01 a NR-38, eSocial, OIT, PPP, insalubridade e periculosidade)", 5, "1,4"),
        BlockInfo("B5",  "Acidente do Trabalho (conceito, equiparações, CAT, investigação, custos, TF e TG)", 4, "4"),
        BlockInfo("B6",  "Análise e Gerenciamento de Riscos (APR, HAZOP, inspeção, FMEA, Árvore de Falhas)", 5, "5"),
        BlockInfo("B7",  "Gestão de SMS (SESMT, CIPA, GRO/PGR, capacitação, Diretrizes OIT, Resoluções ANP 43/2007 e 5/2014)", 4, "5,6"),
        BlockInfo("B8",  "Ações de Saúde (PCMSO, doenças ocupacionais, Suporte Básico à Vida)", 3, "7"),
        BlockInfo("B9",  "Ergonomia NR-17 (conforto ambiental, organização do trabalho, mobiliário, AET)", 3, "7"),
        BlockInfo("B10", "Planejamento e Resposta a Emergências (P2R2, CONAMA 398/2008, Sistema de Comando de Incidentes)", 4, "8"),
        BlockInfo("B11", "Plano Nacional de Contingência (Decreto 8.127/2013)", 2, "8"),
        BlockInfo("BT",  "Bloco Transversal — Cálculos (TF, TG, HHT, dimensionamento CIPA/SESMT, carga de incêndio)", 5, "Transversal"),
        BlockInfo("PT",  "Língua Portuguesa", 0, "Todas as terças"),
        BlockInfo("MT",  "Matemática", 0, "Todas as quintas"),
        BlockInfo("SIM", "Simulado", 0, "Sábados/Domingos"),
        BlockInfo("REV", "Revisão", 0, "Sextas/Domingos"),
        BlockInfo("DIAG","Diagnóstico", 0, "Semana 1")
    )

    val BLOCK_CODES_SELECTABLE = listOf("B1","B2","B3","B4","B5","B6","B7","B8","B9","B10","B11","BT","PT","MT","SIM","REV","DIAG")

    val ERROR_REASONS = listOf(
        "DESCONHECIMENTO",
        "DESATENCAO",
        "PEGADINHA",
        "CALCULO",
        "CHUTE"
    )

    val ERROR_REASON_LABELS = mapOf(
        "DESCONHECIMENTO" to "Desconhecimento",
        "DESATENCAO"      to "Desatenção",
        "PEGADINHA"       to "Pegadinha da banca",
        "CALCULO"         to "Erro de cálculo",
        "CHUTE"           to "Chute"
    )

    data class FormulaEntry(
        val title: String,
        val formula: String,
        val note: String = ""
    )

    val FORMULAS = listOf(
        FormulaEntry("HHT",              "HHT = nº de trabalhadores × horas trabalhadas"),
        FormulaEntry("Taxa de Frequência","TF = (nº de acidentes × 1.000.000) / HHT"),
        FormulaEntry("Taxa de Gravidade", "TG = ((dias perdidos + dias debitados) × 1.000.000) / HHT"),
        FormulaEntry("Limite de Ruído",   "85 dB(A) para jornada de 8 horas", "NR-15, Anexo 1"),
        FormulaEntry("Dose de Ruído",     "Dose = (tempo exposto / tempo máximo permitido) × 100"),
        FormulaEntry("Insalubridade",     "20% / 30% / 40% — sobre o salário mínimo"),
        FormulaEntry("Periculosidade",    "30% do salário contratual", "NR-16"),
        FormulaEntry("Trabalho em Altura","Acima de 2,00 m", "NR-35"),
        FormulaEntry("HAZOP — palavras-guia","Nenhum, Mais, Menos, Parte de, Além de, Reverso, Outro que")
    )

    val PLANNED_SIMULADOS = listOf(
        Triple("2026-09-14", 1, "DIAGNOSTICO"),
        Triple("2026-09-15", 1, "DIAGNOSTICO"),
        Triple("2026-11-01", 8, "PARCIAL"),
        Triple("2026-11-08", 8, "COMPLETO"),
        Triple("2026-11-14", 9, "PARCIAL"),
        Triple("2026-11-21", 10, "DIAGNOSTICO"),
        Triple("2026-11-22", 10, "COMPLETO"),
        Triple("2026-11-24", 10, "VELOCIDADE"),
        Triple("2026-11-28", 11, "COMPLETO")
    )

    val DEFAULT_REWARDS = listOf(
        Pair("5_nights_streak",     "5 noites seguidas de estudo"),
        Pair("week_complete",       "Semana completa (7 de 7 dias)"),
        Pair("simulado_above_goal", "Simulado acima de todas as metas"),
        Pair("nov22_general_above", "22/11 com Português e Matemática ≥ 7/10"),
        Pair("approval",            "Aprovação na Transpetro")
    )
}
