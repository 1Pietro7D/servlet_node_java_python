const express = require('express');
const bodyParser = require('body-parser');
const axios = require('axios');

const app = express();
const port = 5001;

// Middleware per gestire il parsing del corpo delle richieste
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// Serve il file HTML
app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
});

// Funzione per mappare il target agli indici degli URL
function targetToIndex(target) {
    if (target === "python") {
        return 0;
    } else if (target === "java") {
        return 1;
    } else {
        return null; // Gestione di altri casi se necessario
    }
}

// Route per inviare messaggi
app.post('/send', async (req, res) => {
    const message = req.body.message;
    const target = req.body.target;

    // URL di destinazione per i diversi target
    const urls = [
        'http://127.0.0.1:5002/python',  // URL del server Python
        // 'http://localhost:5002/python', DX non funziona
        'http://localhost:5003/java'     // URL del server Java
    ];

    try {
        if (target === 'all') {
            // Invio del messaggio a tutti i server
            await Promise.all(urls.map(url => axios.post(url, { from: "node", message: message })));
        } else {
            const index = targetToIndex(target);
            if (index !== null) {
                // Invio del messaggio al server specificato
                await axios.post(urls[index], { from: "node", message: message });
            } else {
                res.status(200).send('Sei una brutta persona');
            }
        }
        res.send('Messaggio inviato');
    } catch (error) {
        console.error('Errore durante l\'invio del messaggio:', error);
        res.status(500).send('Errore durante l\'invio del messaggio');
    }
});

// Route per ricevere messaggi
app.post('/node', (req, res) => {
    const data = req.body;
    console.log(`Messaggio ricevuto: ${data.message} from ${data.from}`);
    res.json({ status: 'Messaggio ricevuto' });
});

// Avvia il server
app.listen(port, () => {
    console.log(`http://localhost:${port}`);
});
