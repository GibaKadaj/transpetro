// Gerenciamento do banco de dados IndexedDB
const DB_NAME = 'TranspetroPrep';
const DB_VERSION = 1;
let db = null;

const DB = {
    async init() {
        return new Promise((resolve, reject) => {
            const request = indexedDB.open(DB_NAME, DB_VERSION);

            request.onerror = () => reject(request.error);
            request.onsuccess = () => {
                db = request.result;
                resolve(db);
            };

            request.onupgradeneeded = (event) => {
                const database = event.target.result;

                // Store para sessões de estudo
                if (!database.objectStoreNames.contains('sessions')) {
                    const sessionsStore = database.createObjectStore('sessions', {
                        keyPath: 'id',
                        autoIncrement: true
                    });
                    sessionsStore.createIndex('blockCode', 'blockCode', { unique: false });
                    sessionsStore.createIndex('date', 'date', { unique: false });
                }

                // Store para configurações
                if (!database.objectStoreNames.contains('settings')) {
                    database.createObjectStore('settings', { keyPath: 'key' });
                }
            };
        });
    },

    async addSession(sessionData) {
        const transaction = db.transaction(['sessions'], 'readwrite');
        const store = transaction.objectStore('sessions');
        return store.add(sessionData);
    },

    async getAllSessions() {
        const transaction = db.transaction(['sessions'], 'readonly');
        const store = transaction.objectStore('sessions');
        return new Promise((resolve, reject) => {
            const request = store.getAll();
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    },

    async getSessionsByBlock(blockCode) {
        const transaction = db.transaction(['sessions'], 'readonly');
        const store = transaction.objectStore('sessions');
        const index = store.index('blockCode');
        return new Promise((resolve, reject) => {
            const request = index.getAll(blockCode);
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    },

    async getSetting(key) {
        const transaction = db.transaction(['settings'], 'readonly');
        const store = transaction.objectStore('settings');
        return new Promise((resolve, reject) => {
            const request = store.get(key);
            request.onsuccess = () => resolve(request.result?.value);
            request.onerror = () => reject(request.error);
        });
    },

    async setSetting(key, value) {
        const transaction = db.transaction(['settings'], 'readwrite');
        const store = transaction.objectStore('settings');
        return store.put({ key, value });
    },

    async clearAllData() {
        const transaction = db.transaction(['sessions', 'settings'], 'readwrite');
        await transaction.objectStore('sessions').clear();
        await transaction.objectStore('settings').clear();
    }
};
