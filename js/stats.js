// Gerenciamento de Estatísticas
const Stats = {
    data: {
        sessions: [],
        totalTime: 0,
        sessionCount: 0,
        blockStats: {}
    },

    async load() {
        const sessions = await DB.getAllSessions();
        this.data.sessions = sessions;
        this.data.sessionCount = sessions.length;
        this.data.totalTime = sessions.reduce((sum, s) => sum + (s.durationSeconds || 0), 0);

        // Calcular estatísticas por bloco
        this.data.blockStats = {};
        sessions.forEach(session => {
            const code = session.blockCode;
            if (!this.data.blockStats[code]) {
                this.data.blockStats[code] = {
                    count: 0,
                    totalSeconds: 0,
                    blockName: session.blockName
                };
            }
            this.data.blockStats[code].count++;
            this.data.blockStats[code].totalSeconds += session.durationSeconds;
        });
    },

    async refresh() {
        await this.load();
        this.updateHomeDisplay();
        this.updateStatsPage();
    },

    updateHomeDisplay() {
        document.getElementById('total-sessions').textContent = this.data.sessionCount;
        document.getElementById('total-hours').textContent = Utils.formatDuration(this.data.totalTime);
    },

    updateStatsPage() {
        // Estatísticas gerais
        document.getElementById('stats-sessions').textContent = this.data.sessionCount;
        document.getElementById('stats-time').textContent = Utils.formatDuration(this.data.totalTime);

        // Média por dia
        const startDate = new Date(StudyConstants.PREP_START_DATE);
        const today = new Date();
        const daysSinceStart = Math.max(1, Utils.daysBetween(startDate, today));
        const avgPerDay = Math.floor(this.data.totalTime / daysSinceStart);
        document.getElementById('stats-avg').textContent = Utils.formatDuration(avgPerDay);

        // Último estudo
        if (this.data.sessions.length > 0) {
            const lastSession = this.data.sessions[this.data.sessions.length - 1];
            const lastDate = new Date(lastSession.startTime);
            document.getElementById('stats-last').textContent = Utils.formatDate(lastDate);
        } else {
            document.getElementById('stats-last').textContent = '--';
        }

        // Estatísticas por bloco
        this.renderBlockStats();
    },

    renderBlockStats() {
        const container = document.getElementById('block-stats-container');

        if (Object.keys(this.data.blockStats).length === 0) {
            container.innerHTML = '<small class="text-muted">Nenhuma sessão registrada ainda</small>';
            return;
        }

        // Ordenar por tempo total (decrescente)
        const sorted = Object.entries(this.data.blockStats)
            .sort((a, b) => b[1].totalSeconds - a[1].totalSeconds);

        container.innerHTML = sorted.map(([code, stats]) => `
            <div class="block-stat-item">
                <div>
                    <div class="block-stat-name">${code}</div>
                    <small class="text-muted">${stats.count} sessõ${stats.count > 1 ? 'es' : 'ão'}</small>
                </div>
                <div class="block-stat-time">${Utils.formatDuration(stats.totalSeconds)}</div>
            </div>
        `).join('');
    }
};
