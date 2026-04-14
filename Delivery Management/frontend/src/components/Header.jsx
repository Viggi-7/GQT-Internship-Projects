// src/components/Header.js

import React from 'react';
import { useAuth } from '../AuthContext'; // Import useAuth to potentially show user info or logout in header

const Header = () => {
    const { isAuthenticated, userName, userType, logout } = useAuth();

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark glass-effect custom-header">
            <div className="container-fluid">
                <a className="navbar-brand fw-bold fs-4 text-white" href="#">Trace360</a>
                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav"
                    aria-controls="navbarNav"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ms-auto"> {/* ms-auto pushes items to the right */}
                        {isAuthenticated && (
                            <li className="nav-item d-flex align-items-center me-3">
                                <span className="navbar-text text-white-50">
                                    Logged in as: <strong className="text-white">{userName} ({userType})</strong>
                                </span>
                            </li>
                        )}
                        {isAuthenticated && (
                            <li className="nav-item">
                                <button onClick={logout} className="btn btn-sm btn-outline-light">Logout</button>
                            </li>
                        )}
                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default Header;
