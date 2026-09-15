// Gerenciamento do Timer de Estudo
const Timer = {
    interval: null,
    currentSession: null,
    elapsedSeconds: 0,

    start(blockCode) {
        if (this.interval) {
            console.warn('Timer já está rodando');
            return;
        }

        const block = StudyConstants.BLOCKS.find(b => b.code === blockCode);
        if (!block) {
            console.error('Bloco inválido:', blockCode);
            return;
        }

        this.currentSession = {
            blockCode: blockCode,
            blockName: block.name,
            startTime: new Date().toISOString(),
            startTimestamp: Date.now()
        };

        this.elapsedSeconds = 0;
        this.updateDisplay();

        this.interval = setInterval(() => {
            this.elapsedSeconds++;
            this.updateDisplay();
        }, 1000);

        UI.showTimerRunning();
        this.showNotification(`Timer iniciado: ${block.code}`);
    },

    async stop() {
        if (!this.interval) {
            console.warn('Timer não está rodando');
            return;
        }

        clearInterval(this.interval);
        this.interval = null;

        const session = {
            ...this.currentSession,
            endTime: new Date().toISOString(),
            durationSeconds: this.elapsedSeconds,
            date: new Date().toISOString().split('T')[0]
        };

        await DB.addSession(session);

        this.showNotification(`Sessão salva: ${Utils.formatDuration(this.elapsedSeconds)}`);

        this.currentSession = null;
        this.elapsedSeconds = 0;
        this.updateDisplay();

        UI.showTimerStopped();
        await Stats.refresh();
    },

    updateDisplay() {
        const displayEl = document.getElementById('timer-display');
        if (displayEl) {
            displayEl.textContent = Utils.formatTime(this.elapsedSeconds);

            if (this.interval) {
                displayEl.classList.add('pulse');
            } else {
                displayEl.classList.remove('pulse');
            }
        }
    },

    showNotification(message) {
        const banner = document.createElement('div');
        banner.className = 'notification-banner';
        banner.textContent = message;
        document.body.appendChild(banner);
        banner.style.display = 'block';

        setTimeout(() => {
            banner.style.display = 'none';
            banner.remove();
        }, 3000);
    },

    isRunning() {
        return this.interval !== null;
    }
};
