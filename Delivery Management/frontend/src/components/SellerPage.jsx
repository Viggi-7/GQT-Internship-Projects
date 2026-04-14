// src/components/SellerPage.js

import React, { useState, useEffect } from 'react';
import { useAuth } from '../AuthContext';
import { getSellerPackets, createShippingPacket } from '../api';

const SellerPage = () => {
    const { authToken, userName, logout } = useAuth();
    const [packets, setPackets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [activeTab, setActiveTab] = useState('myPackets'); // 'myPackets', 'newPacket'

    // New Packet Form State
    const [productName, setProductName] = useState('');
    const [productDescription, setProductDescription] = useState('');
    const [destinationAddress, setDestinationAddress] = useState('');
    const [deliveryBoyId, setDeliveryBoyId] = useState('');
    const [newPacketMessage, setNewPacketMessage] = useState('');

    const fetchPackets = async () => {
        setLoading(true);
        setError('');
        try {
            const data = await getSellerPackets(authToken);
            setPackets(data);
        } catch (err) {
            setError(err.message || 'Failed to fetch packets.');
            setPackets([]); // Clear packets on error
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (authToken) {
            fetchPackets();
        }
    }, [authToken]); // Refetch when token changes (e.g., after login)

    const handleCreatePacket = async (e) => {
        e.preventDefault();
        setNewPacketMessage('');
        setError('');
        try {
            const requestBody = {
                productName,
                productDescription,
                destinationAddress,
                deliveryBoyId: deliveryBoyId ? parseInt(deliveryBoyId) : null // Ensure it's a number or null
            };
            const newPacket = await createShippingPacket(requestBody, authToken);
            setNewPacketMessage(`Packet "${newPacket.productName}" created successfully!`);
            // Clear form fields
            setProductName('');
            setProductDescription('');
            setDestinationAddress('');
            setDeliveryBoyId('');
            // Optionally, refresh the packet list
            fetchPackets();
        } catch (err) {
            setError(err.message || 'Failed to create packet.');
        }
    };

    const openGoogleMaps = (latitude, longitude) => {
        if (latitude && longitude) {
            window.open(`https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`, '_blank');
        } else {
            // Replaced alert with a more user-friendly modal or toast in a real app
            alert('Location data not available for this packet.');
        }
    };

    return (
        <div className="container mt-4">
            {/* Header section (retained for contextual info) - Header component covers main app header */}
            <div className="d-flex justify-content-between align-items-center mb-4 p-3 bg-white rounded shadow-sm custom-panel"> {/* Added custom-panel */}
                <h2 className="mb-0 text-brown fw-bold">Welcome, {userName}!</h2> {/* Changed text color */}
                {/* Logout button moved to Header component for global access */}
            </div>

            <ul className="nav nav-tabs mb-3" id="sellerTabs" role="tablist">
                <li className="nav-item" role="presentation">
                    <button
                        className={`nav-link ${activeTab === 'myPackets' ? 'active' : ''}`}
                        onClick={() => setActiveTab('myPackets')}
                        type="button"
                    >
                        My Packets
                    </button>
                </li>
                <li className="nav-item" role="presentation">
                    <button
                        className={`nav-link ${activeTab === 'newPacket' ? 'active' : ''}`}
                        onClick={() => setActiveTab('newPacket')}
                        type="button"
                    >
                        Create New Packet
                    </button>
                </li>
            </ul>

            <div className="tab-content glass-effect p-3" id="sellerTabContent"> {/* Applied glass-effect and padding */}
                {activeTab === 'myPackets' && (
                    <div className="tab-pane fade show active">
                        <h3 className="mb-3 text-brown">My Current Shipments</h3> {/* Changed text color */}
                        {loading ? (
                            <div className="text-center py-5">
                                <div className="spinner-border text-primary" role="status">
                                    <span className="visually-hidden">Loading...</span>
                                </div>
                                <p className="mt-2 text-brown">Loading packets...</p> {/* Changed text color */}
                            </div>
                        ) : error ? (
                            <div className="alert alert-danger">{error}</div>
                        ) : packets.length === 0 ? (
                            <div className="alert alert-info">No packets created yet.</div>
                        ) : (
                            <div className="table-responsive">
                                <table className="table table-striped table-hover align-middle">
                                    <thead className="table-dark-blue"> {/* New class for table header */}
                                        <tr>
                                            <th scope="col">ID</th>
                                            <th scope="col">Product Name</th>
                                            <th scope="col">Destination</th>
                                            <th scope="col">Status</th>
                                            <th scope="col">Progress</th>
                                            <th scope="col">Last Updated</th>
                                            <th scope="col">Delivery Boy</th>
                                            <th scope="col">Location</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {packets.map((packet) => (
                                            <tr key={packet.id}>
                                                <td>{packet.id}</td>
                                                <td>{packet.productName}</td>
                                                <td>{packet.destinationAddress}</td>
                                                <td><span className={`badge ${
                                                    packet.status === 'DELIVERED' ? 'bg-success' :
                                                    packet.status === 'CANCELLED' ? 'bg-danger' :
                                                    ['OUT_FOR_DELIVERY_METRO', 'REACHED_DISTRICT_HUB', 'OUT_FOR_DELIVERY_TOWN'].includes(packet.status) ? 'bg-primary' :
                                                    'bg-secondary'
                                                }`}>{packet.status.replace(/_/g, ' ')}</span></td>
                                                <td>
                                                    <div className="progress">
                                                        <div
                                                            className="progress-bar"
                                                            role="progressbar"
                                                            style={{ width: `${packet.progressPercentage}%` }}
                                                            aria-valuenow={packet.progressPercentage}
                                                            aria-valuemin="0"
                                                            aria-valuemax="100"
                                                        >
                                                            {packet.progressPercentage}%
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>{new Date(packet.updatedAt).toLocaleString()}</td>
                                                <td>{packet.deliveryBoyName}</td>
                                                <td>
                                                    <button
                                                        className="btn btn-sm btn-outline-primary"
                                                        onClick={() => openGoogleMaps(packet.currentLatitude, packet.currentLongitude)}
                                                        disabled={!packet.currentLatitude || !packet.currentLongitude}
                                                    >
                                                        View on Map
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                )}

                {activeTab === 'newPacket' && (
                    <div className="tab-pane fade show active">
                        <h3 className="mb-3 text-brown">Create New Shipping Packet</h3> {/* Changed text color */}
                        <div className="card p-4 shadow-sm glass-effect"> {/* Applied glass-effect */}
                            <form onSubmit={handleCreatePacket}>
                                <div className="mb-3">
                                    <label htmlFor="productName" className="form-label">Product Name</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="productName"
                                        value={productName}
                                        onChange={(e) => setProductName(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="productDescription" className="form-label">Product Description (Optional)</label>
                                    <textarea
                                        className="form-control"
                                        id="productDescription"
                                        rows="3"
                                        value={productDescription}
                                        onChange={(e) => setProductDescription(e.target.value)}
                                    ></textarea>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="destinationAddress" className="form-label">Destination Address</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="destinationAddress"
                                        value={destinationAddress}
                                        onChange={(e) => setDestinationAddress(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="deliveryBoyId" className="form-label">Assign Delivery Boy ID (Optional)</label>
                                    <input
                                        type="number"
                                        className="form-control"
                                        id="deliveryBoyId"
                                        value={deliveryBoyId}
                                        onChange={(e) => setDeliveryBoyId(e.target.value)}
                                        placeholder="Enter Delivery Boy User ID"
                                    />
                                    <small className="form-text text-white">Leave blank for unassigned packets.</small> {/* Adjusted text color */}
                                </div>
                                <button type="submit" className="btn btn-primary mt-3">Create Packet</button>
                            </form>
                            {newPacketMessage && <div className="alert alert-success mt-3">{newPacketMessage}</div>}
                            {error && <div className="alert alert-danger mt-3">{error}</div>}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default SellerPage;
