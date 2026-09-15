# Transpetro Prep - PWA

Aplicativo web progressivo (PWA) para preparação do concurso Transpetro.

## 📋 Estrutura do Projeto

```
transpetro-prep-web/
├── index.html              # Página principal
├── manifest.json           # Configuração PWA
├── sw.js                   # Service Worker (offline)
├── css/
│   └── style.css          # Estilos completos
├── js/
│   ├── constants.js       # Constantes e dados do concurso
│   ├── db.js              # IndexedDB (armazenamento local)
│   ├── timer.js           # Lógica do timer
│   ├── stats.js           # Estatísticas
│   ├── ui.js              # Interface do usuário
│   └── app.js             # Inicialização
└── icons/
    ├── icon-192x192.svg   # Ícone pequeno
    └── icon-512x512.svg   # Ícone grande
```

## 🚀 Como Fazer Upload

### Opção 1: Netlify (Recomendado - Mais Fácil)

1. Acesse https://netlify.com
2. Faça login ou crie uma conta gratuita
3. Arraste a pasta `transpetro-prep-web` na área de upload
4. Configure o domínio customizado:
   - Vá em "Domain settings"
   - Clique em "Add custom domain"
   - Digite: `transpetro.ganexus.com.br`
   - Siga as instruções para configurar o DNS

### Opção 2: Vercel

1. Instale o Vercel CLI (se ainda não tiver):
   ```bash
   npm install -g vercel
   ```

2. Na pasta do projeto:
   ```bash
   cd transpetro-prep-web
   vercel --prod
   ```

3. Configure o domínio:
   ```bash
   vercel domains add transpetro.ganexus.com.br
   ```

### Opção 3: Hosting Tradicional (cPanel/FTP)

1. Conecte via FTP no servidor da Ganexus
2. Navegue até a pasta do subdomínio `transpetro.ganexus.com.br`
3. Faça upload de TODOS os arquivos da pasta `transpetro-prep-web`
4. Certifique-se de que o `index.html` está na raiz

### Opção 4: GitHub Pages

1. Crie um repositório no GitHub
2. Faça upload dos arquivos
3. Vá em Settings > Pages
4. Selecione a branch `main` como source
5. Configure o domínio customizado

## ⚙️ Configuração do DNS

Para usar `transpetro.ganexus.com.br`, adicione no DNS da Ganexus:

**Para Netlify/Vercel:**
```
CNAME: transpetro → [endereço-fornecido-pela-plataforma]
```

**Para servidor próprio:**
```
A: transpetro → [IP do servidor]
```

## 📱 Funcionalidades

✅ **Timer de Estudo**
- 17 blocos de estudo (B1-B11, PT, MT, etc.)
- Persistência entre recarregamentos
- Notificações de início/fim

✅ **Estatísticas**
- Total de sessões e horas
- Estatísticas por bloco
- Média por dia
- Último estudo

✅ **Fórmulas de Referência**
- 9 fórmulas importantes
- Sempre acessíveis

✅ **Offline First**
- Funciona sem internet após primeira visita
- Dados salvos localmente (IndexedDB)
- Service Worker ativo

✅ **Instalável**
- Botão de instalação automático
- Funciona como app nativo
- Ícone na tela inicial

✅ **Modo Escuro**
- Tema claro/escuro
- Preferência salva

## 🎨 Personalização dos Ícones

Os ícones atuais são temporários. Para substituir:

1. Crie dois arquivos PNG ou SVG:
   - `icons/icon-192x192.png` (192x192 pixels)
   - `icons/icon-512x512.png` (512x512 pixels)

2. Use o logo da Transpetro ou Ganexus
3. Se usar PNG, atualize o `manifest.json`:
   ```json
   "type": "image/png"
   ```

## 🔧 Requisitos Técnicos

- ✅ HTML5, CSS3, JavaScript vanilla
- ✅ Não precisa de servidor backend
- ✅ Não precisa de Node.js ou build
- ✅ Funciona em qualquer hospedagem estática
- ✅ Compatível com todos navegadores modernos

## 📊 Dados Importantes

- **Data da Prova:** 06/12/2026
- **Bloqueio de Novo Conteúdo:** 08/11/2026
- **17 Blocos de Estudo:** B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, PT, MT, RA, CE, PF, FC
- **9 Fórmulas:** Porcentagem, média, regra de três, juros, área círculo, velocidade, densidade, proporção, probabilidade

## 🐛 Solução de Problemas

### App não instala
- Verifique se está em HTTPS (obrigatório para PWA)
- Netlify/Vercel fornecem HTTPS automático

### Dados não salvam
- Verifique se o navegador permite IndexedDB
- Limpe cache e recarregue

### Service Worker não funciona
- Verifique o console do navegador (F12)
- HTTPS é obrigatório
- Recarregue com Ctrl+Shift+R

## 📞 Suporte

Desenvolvido por **Ganexus**
- Tecnologia que faz negócios crescerem

## 📝 Versão

**v1.0.0** - Janeiro 2025
- Primeira versão completa do PWA
