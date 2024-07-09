# Importa le librerie necessarie
from flask import Flask, request, jsonify
import requests

# Crea un'applicazione Flask
app = Flask(__name__) 
# WARNING: This is a development server. Do not use it in a production deployment. Use a production WSGI server instead.
# SOLUZIONE : server WSGI dedicato come Gunicorn o uWSGI in combinazione con un web server come Nginx o Apache in produzione

# Definisce la route per la pagina principale
@app.route('/', methods=['GET'])
def index():
    # Invia il file HTML al client
    return open('index.html').read()

# Funzione per mappare il target agli indici degli URL
def target_to_index(target):
    if target == "node":
        return 0
    elif target == "java":
        return 1
    else:
        return None  # Gestione di altri casi se necessario

# Definisce la route per inviare messaggi
@app.route('/send', methods=['POST'])
def send_message():
    # Estrae il messaggio e il target dal corpo della richiesta
    message = request.form['message']
    target = request.form['target']
    
    # Definisce gli URL di destinazione per i diversi target
    urls = [
        'http://localhost:5001/node',  # URL del server Python (questo server)
        'http://localhost:5003/java'     # URL del server Java
    ]
    
    # Invio del messaggio ai server specificati
    if target == 'all':
        for url in urls:
            requests.post(url, json={'from':'python' , 'message': message})
    else:
        index = target_to_index(target)
        if index is not None:
            requests.post(urls[index], json={'from':'python' , 'message': message})
    
    # Risponde al client che il messaggio è stato inviato
    return 'Messaggio inviato'

# Definisce la route per ricevere messaggi
@app.route('/python', methods=['POST'])
def receive_message():
    message = request.json['message']
    fromS = request.json['from']
    print(f"Messaggio ricevuto: {message} da {fromS}")
    return jsonify({'status': 'Messaggio ricevuto'})

# Avvia il server

if __name__ == '__main__':
    app.run(port = 5002)
    print(f"http://localhost:5002")
