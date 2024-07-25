const express = require('express');
const bodyParser = require('body-parser');
const axios = require('axios');

const app = express();
const port = 5001;
const chatMessages = [];

// Middleware per gestire il parsing del corpo delle richieste
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

/**
 * @swagger
 * /:
 *   get:
 *     summary: Serve the HTML file
 *     responses:
 *       200:
 *         description: HTML file served
 */
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

/**
 * @swagger
 * /send:
 *   post:
 *     summary: Send messages to specified target
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               message:
 *                 type: string
 *               target:
 *                 type: string
 *     responses:
 *       200:
 *         description: Message sent
 *       500:
 *         description: Error while sending message
 */
app.post('/send', async (req, res) => {
    const message = req.body.message;
    const target = req.body.target;

    // URL di destinazione per i diversi target
    const urls = [
        'http://127.0.0.1:5002/python',  // URL del server Python
        // 'http://localhost:5002/python', DX non funziona mentre in Java funziona
        'http://localhost:5003/java'     // URL del server Java
    ];

    try {
        if (target === 'all') {
            // Invio del messaggio a tutti i server
            await Promise.all(urls.map(url => axios.post(url, { from: "node", message: message })));
            chatMessages.push(message);
        } else {
            const index = targetToIndex(target);
            if (index !== null) {
                // Invio del messaggio al server specificato
                await axios.post(urls[index], { from: "node", message: message });
                chatMessages.push({ from: "node", message: message });
            } else {
                res.send('Sei una brutta persona');
                return;
            }
        }
        res.send('Messaggio inviato');
    } catch (error) {
        console.error('Errore durante l\'invio del messaggio:', error);
        res.status(500).send('Errore durante l\'invio del messaggio');
    }
});

/**
 * @swagger
 * /node:
 *   post:
 *     summary: Receive messages
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               message:
 *                 type: string
 *               from:
 *                 type: string
 *     responses:
 *       200:
 *         description: Message received
 */
app.post('/node', (req, res) => {
    const data = req.body;
    console.log(`Messaggio ricevuto: ${data.message} from ${data.from}`);
    chatMessages.push(data);
    res.json({ status: 'Messaggio ricevuto' });
});

/**
 * @swagger
 * /chat:
 *   get:
 *     summary: Get all chat messages
 *     responses:
 *       200:
 *         description: A JSON array of chat messages
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 */
app.get('/chat', (req, res) => {
    res.json(chatMessages);
});

// Swagger setup
const swaggerJsdoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');

const options = {
    definition: {
        openapi: '3.0.0',
        info: {
            title: 'Chat API',
            version: '1.0.0',
        },
    },
    apis: [__filename], // Percorso del file corrente
};

const swaggerSpec = swaggerJsdoc(options);

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));

// Avvia il server
app.listen(port, () => {
    console.log(`http://localhost:${port}/api-docs/`);
});
