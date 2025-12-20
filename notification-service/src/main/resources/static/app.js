let stompClient = null;
let currentUserId = 1;
const API_BASE = 'http://localhost:8080/notifications';

// --- WebSocket Connection ---
function connect() {
    currentUserId = document.getElementById('userId').value;
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        updateStatus(true);

        // Subscribe to user specific topic
        stompClient.subscribe('/topic/user/' + currentUserId, function (notification) {
            showNotification(JSON.parse(notification.body));
        });

        // Load initial data
        getAllChat();
        getAllConsent();
        getAllOneWay();

    }, function (error) {
        console.error(error);
        updateStatus(false);
    });
}

function updateStatus(connected) {
    const dot = document.getElementById('status-dot');
    const text = document.getElementById('status-text');
    if (connected) {
        dot.className = 'dot connected';
        text.innerText = 'Connected as User ' + currentUserId;
    } else {
        dot.className = 'dot disconnected';
        text.innerText = 'Disconnected';
    }
}

function showNotification(notification) {
    showToast("New Notification: " + notification.message);
    // Auto refresh the active list
    const activeTab = document.querySelector('.tab-content.active').id;
    if (activeTab === 'chat') getAllChat();
    else if (activeTab === 'consent') getAllConsent();
    else if (activeTab === 'oneway') getAllOneWay();
}

function showToast(msg, isError = false) {
    const toast = document.getElementById('toast');
    toast.innerText = msg;
    toast.className = isError ? 'toast error visible' : 'toast visible';
    setTimeout(() => {
        toast.className = 'toast hidden';
    }, 3000);
}

// --- Tabs ---
function openTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    document.getElementById(tabName).classList.add('active');
    event.target.classList.add('active');
}

// --- API Helpers ---
async function fetchAPI(endpoint, method = 'GET', body = null) {
    const headers = {
        'Content-Type': 'application/json',
        // Simulate JWT structure: Header.Body.Signature. Body needs "role":"PATIENT" or "DOCTOR"
        'Authorization': 'Bearer header.' + btoa(JSON.stringify({ role: 'PATIENT', id: currentUserId })) + '.signature'
    };

    try {
        const options = { method, headers };
        if (body) options.body = JSON.stringify(body);

        const response = await fetch(API_BASE + endpoint, options);
        if (!response.ok) {
            const err = await response.json();
            throw err; // Expected error DTO
        }

        // Handle text responses (like "Notification sent") vs JSON
        const text = await response.text();
        try {
            return JSON.parse(text);
        } catch {
            return text;
        }
    } catch (error) {
        logError(error);
        showToast("Error: " + (error.message || "Request failed"), true);
        throw error;
    }
}

// --- Chat Actions ---
async function getAllChat() {
    try {
        const data = await fetchAPI('/getAllChatNotifications');
        renderList('chatList', data, 'deleteChatNotification');
    } catch (e) { }
}

async function sendChat() {
    const msg = document.getElementById('chatMsg').value;
    const recId = document.getElementById('chatRecId').value;
    try {
        await fetchAPI('/sendChatNotification', 'POST', {
            message: msg,
            recipientType: 'PATIENT',
            recipientId: recId,
            chatType: 'PRIVATE',
            chatId: 99
        });
        document.getElementById('chatMsg').value = '';
    } catch (e) { }
}

async function deleteAllChat() {
    try {
        await fetchAPI('/deleteAllChatNotifications', 'DELETE');
        getAllChat();
    } catch (e) { }
}

// --- Consent Actions ---
async function getAllConsent() {
    try {
        const data = await fetchAPI('/getAllConsentRequestNotifications');
        renderList('consentList', data, 'deleteConsentRequestNotification');
    } catch (e) { }
}

async function sendConsent() {
    const msg = document.getElementById('consentMsg').value;
    const recId = document.getElementById('consentRecId').value;
    try {
        await fetchAPI('/sendConsentRequestNotification', 'POST', {
            message: msg,
            recipientType: 'PATIENT',
            recipientId: recId,
            consentRequestId: 888
        });
        document.getElementById('consentMsg').value = '';
    } catch (e) { }
}

async function deleteAllConsent() {
    try {
        await fetchAPI('/deleteAllConsentRequestNotifications', 'DELETE');
        getAllConsent();
    } catch (e) { }
}

// --- OneWay Actions ---
async function getAllOneWay() {
    try {
        const data = await fetchAPI('/getAllOneWayNotifications');
        renderList('onewayList', data, 'deleteOneWayNotification');
    } catch (e) { }
}

async function sendOneWay() {
    const msg = document.getElementById('onewayMsg').value;
    const recId = document.getElementById('onewayRecId').value;
    try {
        await fetchAPI('/sendOneWayNotification', 'POST', {
            message: msg,
            recipientType: 'PATIENT',
            recipientId: recId
        });
        document.getElementById('onewayMsg').value = '';
    } catch (e) { }
}

async function deleteAllOneWay() {
    try {
        await fetchAPI('/deleteAllOneWayNotifications', 'DELETE');
        getAllOneWay();
    } catch (e) { }
}

// --- Render Helper ---
function renderList(elementId, data, deleteEndpoint) {
    const list = document.getElementById(elementId);
    list.innerHTML = '';
    if (!Array.isArray(data)) return;

    data.forEach(item => {
        const li = document.createElement('li');
        li.className = 'notification-item';
        li.innerHTML = `
            <span>${item.message} <small>(ID: ${item.id})</small></span>
            <button onclick="deleteItem('${deleteEndpoint}', ${item.id})" class="btn-danger" style="padding:5px 10px; font-size:0.8em">X</button>
        `;
        list.appendChild(li);
    });
}

async function deleteItem(endpoint, id) {
    try {
        await fetchAPI('/' + endpoint + '/' + id, 'DELETE');
        // Refresh appropriate list
        if (endpoint.includes('Chat')) getAllChat();
        if (endpoint.includes('Consent')) getAllConsent();
        if (endpoint.includes('OneWay')) getAllOneWay();
    } catch (e) { }
}

// --- Error Simulation ---
function logError(err) {
    const log = document.getElementById('errorLog');
    const time = new Date().toLocaleTimeString();
    log.innerHTML += `<div>[${time}] ${JSON.stringify(err)}</div>`;
}

async function triggerInvalidRecipient() {
    // Send to invalid ID 9999
    try {
        await fetchAPI('/sendChatNotification', 'POST', {
            message: "This should fail",
            recipientType: 'PATIENT',
            recipientId: 9999, // Our service mocks this check likely to fail or we force it?
            // Actually the service checks external service. Since we are simulating external service URLs to fail or pass?
            // The service code connects to "external-services.user-management.url".
            // Since those URLs don't exist, the restTemplate call might fail or return 404.
            // Let's see what the Exception handler catches.
            chatType: 'PRIVATE',
            chatId: 1
        });
    } catch (e) { }
}

async function triggerInvalidChat() {
    // We would need to tweak logic to force failure, but simply sending bad data might trigger validation exceptions
    // depending on how strict the Mock/Stubs are.
    // In our current setup, isChatValid calls a URL. If URL fails, it returns false -> InvalidChatException.
    try {
        await fetchAPI('/sendChatNotification', 'POST', {
            message: "Fail Chat",
            recipientType: 'PATIENT',
            recipientId: 1,
            chatType: 'INVALID_TYPE',
            chatId: -1
        });
    } catch (e) { }
}

async function triggerInvalidConsent() {
    try {
        await fetchAPI('/sendConsentRequestNotification', 'POST', {
            message: "Fail Consent",
            recipientType: 'PATIENT',
            recipientId: 1,
            consentRequestId: -500
        });
    } catch (e) { }
}

async function triggerUnauthorized() {
    // Send request without token implies 403 usually, but our client code adds token.
    // We can try sending a bad token manually using raw fetch if we wanted.
    const headers = { 'Content-Type': 'application/json' }; // No Auth
    try {
        const response = await fetch(API_BASE + '/getAllChatNotifications', { headers });
        if (!response.ok) throw await response.json();
    } catch (error) {
        logError(error);
        showToast("Unauthorized Error Triggered", true);
    }
}
