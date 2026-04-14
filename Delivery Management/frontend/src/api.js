// src/api.js

// Base URL for your Spring Boot backend.
// Make sure this matches the port your backend is running on (e.g., 8083 as per your logs).
const API_BASE_URL = 'http://localhost:8083/api';

/**
 * Helper function for making API requests.
 * @param {string} url The full URL of the endpoint.
 * @param {string} method HTTP method (GET, POST, PATCH).
 * @param {object} [body=null] Request body for POST/PATCH requests.
 * @param {string} [token=null] JWT token for authenticated requests.
 * @returns {Promise<object>} Parsed JSON response.
 * @throws {Error} If the network request fails or the server returns an error status.
 */
const callApi = async (url, method, body = null, token = null) => {
    const headers = {
        'Content-Type': 'application/json',
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
        method: method,
        headers: headers,
    };

    if (body) {
        config.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(url, config);

        if (!response.ok) {
            let errorData = {};
            try {
                // Try to parse error response as JSON if available
                errorData = await response.json();
            } catch (e) {
                // If not JSON, use response text
                errorData = { message: response.statusText || 'Unknown error' };
            }
            throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('API call failed:', error);
        throw error; // Re-throw to be handled by the calling component
    }
};

// --- Authentication Endpoints ---

export const login = (email, password) => {
    return callApi(`${API_BASE_URL}/auth/login`, 'POST', { email, password });
};

export const registerSeller = (name, email, password) => {
    return callApi(`${API_BASE_URL}/auth/register-seller`, 'POST', { name, email, password });
};

export const registerDeliveryBoy = (name, email, password, phoneNumber) => {
    return callApi(`${API_BASE_URL}/auth/register-delivery`, 'POST', { name, email, password, phoneNumber });
};

// --- Seller Endpoints ---

export const createShippingPacket = (requestBody, token) => {
    return callApi(`${API_BASE_URL}/seller/packets`, 'POST', requestBody, token);
};

export const getSellerPackets = (token) => {
    return callApi(`${API_BASE_URL}/seller/packets`, 'GET', null, token);
};

// --- Delivery Boy Endpoints ---

export const getDeliveryBoyPackets = (token) => {
    return callApi(`${API_BASE_URL}/delivery/packets`, 'GET', null, token);
};

export const updatePacketLocation = (packetId, requestBody, token) => {
    return callApi(`${API_BASE_URL}/delivery/packets/${packetId}/location`, 'PATCH', requestBody, token);
};
