// Gerenciamento de Interface
const UI = {
    currentPage: 'home',

    init() {
        this.setupNavigation();
        this.setupTimerControls();
        this.setupSettings();
        this.populateBlockSelect();
        this.renderFormulas();
        this.updateCountdown();
        this.checkContentAlert();

        // Atualizar countdown a cada hora
        setInterval(() => this.updateCountdown(), 3600000);
    },

    setupNavigation() {
        const navButtons = document.querySelectorAll('.nav-btn');
        navButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const targetPage = btn.dataset.page;
                this.showPage(targetPage);
            });
        });
    },

    showPage(pageName) {
        // Esconder todas as páginas
        document.querySelectorAll('.page').forEach(page => {
            page.classList.remove('active');
        });

        // Mostrar página selecionada
        document.getElementById(`${pageName}-page`).classList.add('active');

        // Atualizar botões da navegação
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.classList.remove('active');
            if (btn.dataset.page === pageName) {
                btn.classList.add('active');
            }
        });

        this.currentPage = pageName;

        // Atualizar stats quando abrir a página
        if (pageName === 'stats') {
            Stats.refresh();
        }
    },

    populateBlockSelect() {
        const select = document.getElementById('block-select');
        StudyConstants.BLOCKS.forEach(block => {
            const option = document.createElement('option');
            option.value = block.code;
            option.textContent = `${block.code} - ${block.name}`;
            select.appendChild(option);
        });
    },

    setupTimerControls() {
        const blockSelect = document.getElementById('block-select');
        const startBtn = document.getElementById('start-btn');
        const stopBtn = document.getElementById('stop-btn');

        startBtn.addEventListener('click', () => {
            const selectedBlock = blockSelect.value;
            if (!selectedBlock) {
                alert('Selecione um bloco primeiro!');
                return;
            }
            Timer.start(selectedBlock);
        });

        stopBtn.addEventListener('click', () => {
            if (confirm('Deseja finalizar a sessão de estudo?')) {
                Timer.stop();
            }
        });
    },

    showTimerRunning() {
        const blockSelect = document.getElementById('block-select');
        const startBtn = document.getElementById('start-btn');
        const stopBtn = document.getElementById('stop-btn');
        const sessionInfo = document.getElementById('session-info');
        const currentBlockName = document.getElementById('current-block-name');
        const sessionStartTime = document.getElementById('session-start-time');

        blockSelect.disabled = true;
        startBtn.classList.add('d-none');
        stopBtn.classList.remove('d-none');
        sessionInfo.classList.remove('d-none');

        const block = StudyConstants.BLOCKS.find(b => b.code === Timer.currentSession.blockCode);
        currentBlockName.textContent = `${block.code} - ${block.name}`;

        const startDate = new Date(Timer.currentSession.startTime);
        sessionStartTime.textContent = startDate.toLocaleTimeString('pt-BR');
    },

    showTimerStopped() {
        const blockSelect = document.getElementById('block-select');
        const startBtn = document.getElementById('start-btn');
        const stopBtn = document.getElementById('stop-btn');
        const sessionInfo = document.getElementById('session-info');

        blockSelect.disabled = false;
        blockSelect.value = '';
        startBtn.classList.remove('d-none');
        stopBtn.classList.add('d-none');
        sessionInfo.classList.add('d-none');
    },

    updateCountdown() {
        const examDate = new Date(StudyConstants.EXAM_DATE);
        const today = new Date();
        const daysRemaining = Utils.daysBetween(today, examDate);

        document.getElementById('countdown-days').textContent = daysRemaining;
        document.getElementById('exam-date-display').textContent =
            `Prova: ${Utils.formatDate(examDate)}`;

        // Atualizar fase atual
        document.getElementById('current-phase').textContent = Utils.getCurrentPhase();
    },

    checkContentAlert() {
        if (Utils.isNewContentBlocked()) {
            document.getElementById('content-alert').classList.remove('d-none');
        }
    },

    renderFormulas() {
        const container = document.getElementById('formulas-container');
        container.innerHTML = StudyConstants.FORMULAS.map(formula => `
            <div class="card formula-card">
                <div class="card-body">
                    <div class="formula-title">${formula.title}</div>
                    <div class="formula-text">${formula.formula}</div>
                    ${formula.note ? `<div class="formula-note">${formula.note}</div>` : ''}
                </div>
            </div>
        `).join('');
    },

    setupSettings() {
        const darkModeToggle = document.getElementById('dark-mode-toggle');
        const clearDataBtn = document.getElementById('clear-data-btn');

        // Carregar preferência de tema
        DB.getSetting('darkMode').then(darkMode => {
            if (darkMode) {
                document.documentElement.setAttribute('data-theme', 'dark');
                darkModeToggle.checked = true;
            }
        });

        darkModeToggle.addEventListener('change', (e) => {
            if (e.target.checked) {
                document.documentElement.setAttribute('data-theme', 'dark');
                DB.setSetting('darkMode', true);
            } else {
                document.documentElement.removeAttribute('data-theme');
                DB.setSetting('darkMode', false);
            }
        });

        clearDataBtn.addEventListener('click', async () => {
            if (confirm('Tem certeza? Isso apagará todas as suas sessões de estudo!')) {
                await DB.clearAllData();
                alert('Dados limpos com sucesso!');
                await Stats.refresh();
            }
        });
    }
};
