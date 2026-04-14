// src/App.js

import React from 'react';
import { AuthProvider, useAuth } from './AuthContext';
import LoginPage from './components/LoginPage';
import SellerPage from './components/SellerPage';
import DeliveryPage from './components/DeliveryPage';
import Header from './components/Header'; // Import Header
import Footer from './components/Footer'; // Import Footer

// Main App component that handles routing based on authentication
const AppContent = () => {
    const { isAuthenticated, userType, loading } = useAuth();

    if (loading) {
        return (
            <div className="d-flex justify-content-center align-items-center vh-100 bg-light"> {/* Added vh-100 */}
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
                <p className="ms-2">Loading authentication state...</p>
            </div>
        );
    }

    if (!isAuthenticated) {
        return <LoginPage />;
    } else {
        if (userType === 'SELLER') {
            return <SellerPage />;
        } else if (userType === 'DELIVERY_BOY') {
            return <DeliveryPage />;
        } else {
            // Fallback for unknown user types, or if somehow isAuthenticated is true but type is missing
            return (
                <div className="container mt-5 text-center">
                    <div className="alert alert-warning">Unknown user type. Please log in again.</div>
                    <button className="btn btn-danger" onClick={() => localStorage.clear() || window.location.reload()}>
                        Reset & Go to Login
                    </button>
                </div>
            );
        }
    }
};

// Wrapper for the entire application to provide AuthContext
function App() {
    return (
        <AuthProvider>
            <div className="d-flex flex-column min-vh-100"> {/* Added d-flex flex-column min-vh-100 for sticky footer */}
                <Header />
                <main className="flex-grow-1"> {/* main tag with flex-grow-1 ensures content pushes footer down */}
                    <AppContent />
                </main>
                <Footer />
            </div>
        </AuthProvider>
    );
}

export default App;
