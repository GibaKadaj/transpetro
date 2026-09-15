// Service Worker para PWA - Cache e Funcionalidade Offline
const CACHE_NAME = 'transpetro-prep-v1';

// Recursos locais: essenciais. Se algum faltar, a instalação falha e
// é melhor falhar alto do que ter um app que "instala" sem funcionar.
const urlsToCache = [
    './',
    './index.html',
    './css/style.css',
    './js/constants.js',
    './js/db.js',
    './js/timer.js',
    './js/stats.js',
    './js/ui.js',
    './js/app.js',
    './manifest.json',
    './icons/icon-192x192.svg',
    './icons/icon-512x512.svg'
];

// CDNs em lista separada: cache.addAll é tudo-ou-nada, então um CDN
// fora do ar derrubaria o Service Worker inteiro se estivesse junto.
const cdnUrls = [
    'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css',
    'https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.2/font/bootstrap-icons.css',
    'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'
];

// Instalação - cachear recursos
self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME).then(async cache => {
            await cache.addAll(urlsToCache);

            // Falha individual de CDN não aborta a instalação
            await Promise.all(
                cdnUrls.map(url =>
                    fetch(url, { mode: 'cors' })
                        .then(res => (res.ok ? cache.put(url, res) : null))
                        .catch(() => null)
                )
            );

            return self.skipWaiting();
        })
    );
});

// Ativação - limpar caches antigos
self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys()
            .then(cacheNames => Promise.all(
                cacheNames.map(cacheName => {
                    if (cacheName !== CACHE_NAME) {
                        return caches.delete(cacheName);
                    }
                })
            ))
            .then(() => self.clients.claim())
    );
});

// Fetch - apenas GET; POST/PUT não vão para o cache
self.addEventListener('fetch', event => {
    if (event.request.method !== 'GET') return;

    event.respondWith(
        caches.match(event.request)
            .then(response => {
                if (response) {
                    return response;
                }

                return fetch(event.request.clone()).then(response => {
                    // 'basic' = mesmo domínio; 'cors' = CDN. Ambos podem ir pro cache.
                    if (!response || response.status !== 200) {
                        return response;
                    }
                    if (response.type !== 'basic' && response.type !== 'cors') {
                        return response;
                    }

                    const responseToCache = response.clone();
                    caches.open(CACHE_NAME).then(cache => {
                        cache.put(event.request, responseToCache);
                    });

                    return response;
                });
            })
            .catch(() => {
                // Navegação offline cai no index; outros recursos apenas falham
                if (event.request.mode === 'navigate') {
                    return caches.match('./index.html');
                }
            })
    );
});
