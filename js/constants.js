// Constantes do aplicativo baseadas no código Android
const StudyConstants = {
    EXAM_DATE: '2026-12-06',
    PREP_START_DATE: '2026-09-14',
    NO_NEW_CONTENT_AFTER: '2026-11-08',
    TOTAL_DAYS: 84,

    SECURITY_QUESTIONS: 40,
    PORTUGUESE_QUESTIONS: 10,
    MATH_QUESTIONS: 10,
    TOTAL_QUESTIONS: 60,

    MIN_SECURITY_PASS: 20,
    MIN_GENERAL_PASS: 10,

    GOAL_SECURITY: 32,
    GOAL_PORTUGUESE: 7,
    GOAL_MATH: 7,

    Phase: {
        FOUNDATION: 'Fundação',
        SWEEP: 'Varredura',
        FINAL: 'Reta Final',
        PHASE1_END: '2026-10-11',
        PHASE2_END: '2026-11-08'
    },

    BLOCKS: [
        { code: 'B1', name: 'Prevenção e Controle de Riscos (NR-10, 11, 12, 13, 18, 20, 33, 35, trabalhos a quente, CTB)', weight: 5, plannedWeeks: '2,3' },
        { code: 'B2', name: 'Higiene Ocupacional (ruído, calor, vibração, radiação, gases, vapores, aerodispersoides, FDS, PPR, PCA, Fundacentro)', weight: 5, plannedWeeks: '3' },
        { code: 'B3', name: 'Proteção contra Incêndio (sistemas fixos e portáteis, detecção e alarme, brigada, plano de emergência)', weight: 4, plannedWeeks: '6,7' },
        { code: 'B4', name: 'Legislação e Normas Técnicas (PNSST, CF/88, CLT, NR-01 a NR-38, eSocial, OIT, PPP, insalubridade e periculosidade)', weight: 5, plannedWeeks: '1,4' },
        { code: 'B5', name: 'Acidente do Trabalho (conceito, equiparações, CAT, investigação, custos, TF e TG)', weight: 4, plannedWeeks: '4' },
        { code: 'B6', name: 'Análise e Gerenciamento de Riscos (APR, HAZOP, inspeção, FMEA, Árvore de Falhas)', weight: 5, plannedWeeks: '5' },
        { code: 'B7', name: 'Gestão de SMS (SESMT, CIPA, GRO/PGR, capacitação, Diretrizes OIT, Resoluções ANP 43/2007 e 5/2014)', weight: 4, plannedWeeks: '5,6' },
        { code: 'B8', name: 'Ações de Saúde (PCMSO, doenças ocupacionais, Suporte Básico à Vida)', weight: 3, plannedWeeks: '7' },
        { code: 'B9', name: 'Ergonomia NR-17 (conforto ambiental, organização do trabalho, mobiliário, AET)', weight: 3, plannedWeeks: '7' },
        { code: 'B10', name: 'Planejamento e Resposta a Emergências (P2R2, CONAMA 398/2008, Sistema de Comando de Incidentes)', weight: 4, plannedWeeks: '8' },
        { code: 'B11', name: 'Plano Nacional de Contingência (Decreto 8.127/2013)', weight: 2, plannedWeeks: '8' },
        { code: 'BT', name: 'Bloco Transversal — Cálculos (TF, TG, HHT, dimensionamento CIPA/SESMT, carga de incêndio)', weight: 5, plannedWeeks: 'Transversal' },
        { code: 'PT', name: 'Língua Portuguesa', weight: 0, plannedWeeks: 'Todas as terças' },
        { code: 'MT', name: 'Matemática', weight: 0, plannedWeeks: 'Todas as quintas' },
        { code: 'SIM', name: 'Simulado', weight: 0, plannedWeeks: 'Sábados/Domingos' },
        { code: 'REV', name: 'Revisão', weight: 0, plannedWeeks: 'Sextas/Domingos' },
        { code: 'DIAG', name: 'Diagnóstico', weight: 0, plannedWeeks: 'Semana 1' }
    ],

    FORMULAS: [
        { title: 'HHT', formula: 'HHT = nº de trabalhadores × horas trabalhadas', note: '' },
        { title: 'Taxa de Frequência', formula: 'TF = (nº de acidentes × 1.000.000) / HHT', note: '' },
        { title: 'Taxa de Gravidade', formula: 'TG = ((dias perdidos + dias debitados) × 1.000.000) / HHT', note: '' },
        { title: 'Limite de Ruído', formula: '85 dB(A) para jornada de 8 horas', note: 'NR-15, Anexo 1' },
        { title: 'Dose de Ruído', formula: 'Dose = (tempo exposto / tempo máximo permitido) × 100', note: '' },
        { title: 'Insalubridade', formula: '20% / 30% / 40% — sobre o salário mínimo', note: '' },
        { title: 'Periculosidade', formula: '30% do salário contratual', note: 'NR-16' },
        { title: 'Trabalho em Altura', formula: 'Acima de 2,00 m', note: 'NR-35' },
        { title: 'HAZOP — palavras-guia', formula: 'Nenhum, Mais, Menos, Parte de, Além de, Reverso, Outro que', note: '' }
    ]
};

// Utilitários
const Utils = {
    formatDate(date) {
        return new Intl.DateTimeFormat('pt-BR').format(date);
    },

    formatTime(seconds) {
        const hours = Math.floor(seconds / 3600);
        const minutes = Math.floor((seconds % 3600) / 60);
        const secs = seconds % 60;
        return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
    },

    formatDuration(seconds) {
        const hours = Math.floor(seconds / 3600);
        const minutes = Math.floor((seconds % 3600) / 60);
        if (hours > 0) {
            return `${hours}h ${minutes}m`;
        }
        return `${minutes}m`;
    },

    daysBetween(date1, date2) {
        const oneDay = 24 * 60 * 60 * 1000;
        return Math.round((date2 - date1) / oneDay);
    },

    getCurrentPhase() {
        const today = new Date();
        const phase1End = new Date(StudyConstants.Phase.PHASE1_END);
        const phase2End = new Date(StudyConstants.Phase.PHASE2_END);

        if (today <= phase1End) {
            return StudyConstants.Phase.FOUNDATION;
        } else if (today <= phase2End) {
            return StudyConstants.Phase.SWEEP;
        } else {
            return StudyConstants.Phase.FINAL;
        }
    },

    isNewContentBlocked() {
        const today = new Date();
        const cutoffDate = new Date(StudyConstants.NO_NEW_CONTENT_AFTER);
        return today > cutoffDate;
    }
};
