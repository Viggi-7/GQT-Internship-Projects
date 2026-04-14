// src/AuthContext.js

import React, { createContext, useState, useContext, useEffect } from 'react';

// Create the AuthContext
const AuthContext = createContext(null);

// Custom hook to use the AuthContext
export const useAuth = () => {
    return useContext(AuthContext);
};

// AuthProvider component to wrap your application
export const AuthProvider = ({ children }) => {
    // State to hold authentication information
    const [authToken, setAuthToken] = useState(localStorage.getItem('authToken'));
    const [userType, setUserType] = useState(localStorage.getItem('userType'));
    const [userId, setUserId] = useState(localStorage.getItem('userId'));
    const [userName, setUserName] = useState(localStorage.getItem('userName'));
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // When the component mounts, check localStorage for existing auth data
        const storedToken = localStorage.getItem('authToken');
        const storedUserType = localStorage.getItem('userType');
        const storedUserId = localStorage.getItem('userId');
        const storedUserName = localStorage.getItem('userName');

        if (storedToken && storedUserType && storedUserId && storedUserName) {
            setAuthToken(storedToken);
            setUserType(storedUserType);
            setUserId(storedUserId);
            setUserName(storedUserName);
        }
        setLoading(false);
    }, []);

    // Function to handle successful login/registration
    const handleLogin = (token, type, id, name) => {
        localStorage.setItem('authToken', token);
        localStorage.setItem('userType', type);
        localStorage.setItem('userId', id);
        localStorage.setItem('userName', name);
        setAuthToken(token);
        setUserType(type);
        setUserId(id);
        setUserName(name);
    };

    // Function to handle logout
    const handleLogout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userType');
        localStorage.removeItem('userId');
        localStorage.removeItem('userName');
        setAuthToken(null);
        setUserType(null);
        setUserId(null);
        setUserName(null);
    };

    // Value provided by the context to its consumers
    const value = {
        authToken,
        userType,
        userId,
        userName,
        isAuthenticated: !!authToken, // Boolean flag for authentication status
        loading,
        login: handleLogin,
        logout: handleLogout,
    };

    return (
        <AuthContext.Provider value={value}>
            {!loading && children} {/* Render children only after checking localStorage */}
        </AuthContext.Provider>
    );
};
