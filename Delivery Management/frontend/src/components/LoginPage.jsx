// src/components/LoginPage.js

import React, { useState } from 'react';
import { useAuth } from '../AuthContext';
import { login, registerSeller, registerDeliveryBoy } from '../api';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [error, setError] = useState('');
    const [activeTab, setActiveTab] = useState('login'); // 'login', 'registerSeller', 'registerDelivery'

    const { login: authLogin } = useAuth(); // Rename login from context to avoid conflict

    const handleSubmitLogin = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = await login(email, password);
            authLogin(data.token, data.userType, data.userId, data.name);
        } catch (err) {
            setError(err.message || 'Login failed. Please check your credentials.');
        }
    };

    const handleSubmitRegisterSeller = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = await registerSeller(name, email, password);
            authLogin(data.token, data.userType, data.userId, data.name);
        } catch (err) {
            setError(err.message || 'Seller registration failed. Please try again.');
        }
    };

    const handleSubmitRegisterDelivery = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = await registerDeliveryBoy(name, email, password, phoneNumber);
            authLogin(data.token, data.userType, data.userId, data.name);
        } catch (err) {
            setError(err.message || 'Delivery boy registration failed. Please try again.');
        }
    };

    return (
        <div className="container-full-height container mt-5"> {/* Added container-full-height */}
            <div className="card shadow-lg p-4 mx-auto" style={{ maxWidth: '500px' }}>
                <h2 className="card-title text-center mb-4 text-primary fw-bold">Delivery App</h2> {/* Added text-primary fw-bold */}

                <ul className="nav nav-tabs mb-3" id="myTab" role="tablist">
                    <li className="nav-item" role="presentation">
                        <button
                            className={`nav-link ${activeTab === 'login' ? 'active' : ''}`}
                            onClick={() => setActiveTab('login')}
                            type="button"
                        >
                            Login
                        </button>
                    </li>
                    <li className="nav-item" role="presentation">
                        <button
                            className={`nav-link ${activeTab === 'registerSeller' ? 'active' : ''}`}
                            onClick={() => setActiveTab('registerSeller')}
                            type="button"
                        >
                            Register Seller
                        </button>
                    </li>
                    <li className="nav-item" role="presentation">
                        <button
                            className={`nav-link ${activeTab === 'registerDelivery' ? 'active' : ''}`}
                            onClick={() => setActiveTab('registerDelivery')}
                            type="button"
                        >
                            Register Delivery Boy
                        </button>
                    </li>
                </ul>

                <div className="tab-content" id="myTabContent">
                    {/* Login Tab */}
                    {activeTab === 'login' && (
                        <div className="tab-pane fade show active">
                            <form onSubmit={handleSubmitLogin}>
                                <div className="mb-3">
                                    <label htmlFor="loginEmail" className="form-label">Email address</label>
                                    <input
                                        type="email"
                                        className="form-control"
                                        id="loginEmail"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="loginPassword" className="form-label">Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        id="loginPassword"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </div>
                                <button type="submit" className="btn btn-primary w-100 mt-3">Login</button> {/* Added mt-3 */}
                            </form>
                        </div>
                    )}

                    {/* Register Seller Tab */}
                    {activeTab === 'registerSeller' && (
                        <div className="tab-pane fade show active">
                            <form onSubmit={handleSubmitRegisterSeller}>
                                <div className="mb-3">
                                    <label htmlFor="sellerName" className="form-label">Name</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="sellerName"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="sellerEmail" className="form-label">Email address</label>
                                    <input
                                        type="email"
                                        className="form-control"
                                        id="sellerEmail"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="sellerPassword" className="form-label">Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        id="sellerPassword"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </div>
                                <button type="submit" className="btn btn-success w-100 mt-3">Register as Seller</button> {/* Added mt-3 */}
                            </form>
                        </div>
                    )}

                    {/* Register Delivery Boy Tab */}
                    {activeTab === 'registerDelivery' && (
                        <div className="tab-pane fade show active">
                            <form onSubmit={handleSubmitRegisterDelivery}>
                                <div className="mb-3">
                                    <label htmlFor="deliveryName" className="form-label">Name</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="deliveryName"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="deliveryEmail" className="form-label">Email address</label>
                                    <input
                                        type="email"
                                        className="form-control"
                                        id="deliveryEmail"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="deliveryPassword" className="form-label">Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        id="deliveryPassword"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="deliveryPhoneNumber" className="form-label">Phone Number</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="deliveryPhoneNumber"
                                        value={phoneNumber}
                                        onChange={(e) => setPhoneNumber(e.target.value)}
                                        required
                                    />
                                </div>
                                <button type="submit" className="btn btn-info w-100 mt-3">Register as Delivery Boy</button> {/* Added mt-3 */}
                            </form>
                        </div>
                    )}
                </div>

                {error && <div className="alert alert-danger mt-3">{error}</div>}
            </div>
        </div>
    );
};

export default LoginPage;
