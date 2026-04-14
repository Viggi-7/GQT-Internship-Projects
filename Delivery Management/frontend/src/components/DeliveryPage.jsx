// src/components/DeliveryPage.js

import React, { useState, useEffect } from 'react';
import { useAuth } from '../AuthContext';
import { getDeliveryBoyPackets, updatePacketLocation } from '../api';
import { PACKET_STATUSES } from '../util/constants';

const DeliveryPage = () => {
    const { authToken, userName, logout } = useAuth();
    const [packets, setPackets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [updateMessage, setUpdateMessage] = useState('');

    const [selectedPacketId, setSelectedPacketId] = useState('');
    const [currentLatitude, setCurrentLatitude] = useState('');
    const [currentLongitude, setCurrentLongitude] = useState('');
    const [newStatus, setNewStatus] = useState('');

    const fetchPackets = async () => {
        setLoading(true);
        setError('');
        try {
            const data = await getDeliveryBoyPackets(authToken);
            setPackets(data);
            // Pre-select first packet for update if available
            if (data.length > 0) {
                const firstPacket = data[0];
                setSelectedPacketId(firstPacket.id);
                setCurrentLatitude(firstPacket.currentLatitude || '');
                setCurrentLongitude(firstPacket.currentLongitude || '');
                setNewStatus(firstPacket.status);
            }
        } catch (err) {
            setError(err.message || 'Failed to fetch assigned packets.');
            setPackets([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (authToken) {
            fetchPackets();
        }
    }, [authToken]);

    // Handle selection change in dropdown to update form fields
    useEffect(() => {
        const selectedPacket = packets.find(p => p.id === selectedPacketId);
        if (selectedPacket) {
            setCurrentLatitude(selectedPacket.currentLatitude || '');
            setCurrentLongitude(selectedPacket.currentLongitude || '');
            setNewStatus(selectedPacket.status);
        } else {
             // If no packet is selected or packets become empty
            setCurrentLatitude('');
            setCurrentLongitude('');
            setNewStatus('');
        }
    }, [selectedPacketId, packets]);


    const handleUpdateLocation = async (e) => {
        e.preventDefault();
        setUpdateMessage('');
        setError('');

        if (!selectedPacketId) {
            setError('Please select a packet to update.');
            return;
        }

        try {
            const requestBody = {
                latitude: parseFloat(currentLatitude),
                longitude: parseFloat(currentLongitude),
                newStatus: newStatus
            };
            const updatedPacket = await updatePacketLocation(selectedPacketId, requestBody, authToken);
            setUpdateMessage(`Packet ID ${updatedPacket.id} updated successfully to ${updatedPacket.status.replace(/_/g, ' ')}!`);
            // Refresh packets after update
            fetchPackets();
        } catch (err) {
            setError(err.message || 'Failed to update packet location/status.');
        }
    };

    const getCurrentLocation = () => {
        setUpdateMessage('');
        setError('');
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    setCurrentLatitude(position.coords.latitude);
                    setCurrentLongitude(position.coords.longitude);
                    setUpdateMessage('Current device location fetched.');
                },
                (geoError) => {
                    setError(`Error getting location: ${geoError.message}. Please ensure location services are enabled.`);
                },
                { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 }
            );
        } else {
            setError('Geolocation is not supported by your browser.');
        }
    };

    const openGoogleMaps = (latitude, longitude) => {
        if (latitude && longitude) {
            window.open(`https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`, '_blank');
        } else {
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

            <div className="row">
                {/* Assigned Packets List */}
                <div className="col-md-7 mb-4 mb-md-0"> {/* Added mb-4 for mobile spacing */}
                    <h3 className="mb-3 text-brown">My Assigned Shipments</h3> {/* Added styling */}
                    {loading ? (
                        <div className="text-center py-5">
                            <div className="spinner-border text-primary" role="status">
                                <span className="visually-hidden">Loading...</span>
                            </div>
                            <p className="mt-2 text-brown">Loading assigned packets...</p> {/* Changed text color */}
                        </div>
                    ) : error ? (
                        <div className="alert alert-danger">{error}</div>
                    ) : packets.length === 0 ? (
                        <div className="alert alert-info">No packets currently assigned to you.</div>
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
                                                'bg-primary'
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
                                            <td>
                                                <button
                                                    className="btn btn-sm btn-outline-primary"
                                                    onClick={() => openGoogleMaps(packet.currentLatitude, packet.currentLongitude)}
                                                    disabled={!packet.currentLatitude && !packet.currentLongitude}
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

                {/* Update Packet Location/Status Form */}
                <div className="col-md-5">
                    <h3 className="mb-3 text-brown">Update Packet Status/Location</h3> {/* Added styling */}
                    <div className="card p-4 shadow-sm glass-effect"> {/* Applied glass-effect */}
                        <form onSubmit={handleUpdateLocation}>
                            <div className="mb-3">
                                <label htmlFor="selectPacket" className="form-label">Select Packet</label>
                                <select
                                    className="form-select"
                                    id="selectPacket"
                                    value={selectedPacketId}
                                    onChange={(e) => setSelectedPacketId(parseInt(e.target.value))}
                                    required
                                    disabled={packets.length === 0}
                                >
                                    <option value="" disabled>Choose a packet...</option>
                                    {packets.map(packet => (
                                        <option key={packet.id} value={packet.id}>
                                            {packet.id} - {packet.productName} ({packet.status.replace(/_/g, ' ')})
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="mb-3">
                                <label htmlFor="latitude" className="form-label">Latitude</label>
                                <input
                                    type="number"
                                    step="0.000001"
                                    className="form-control"
                                    id="latitude"
                                    value={currentLatitude}
                                    onChange={(e) => setCurrentLatitude(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="longitude" className="form-label">Longitude</label>
                                <input
                                    type="number"
                                    step="0.000001"
                                    className="form-control"
                                    id="longitude"
                                    value={currentLongitude}
                                    onChange={(e) => setCurrentLongitude(e.target.value)}
                                    required
                                />
                            </div>
                            <button type="button" className="btn btn-secondary btn-sm mb-3" onClick={getCurrentLocation}>
                                Get Current Device Location
                            </button>
                            <div className="mb-3">
                                <label htmlFor="newStatus" className="form-label">New Status</label>
                                <select
                                    className="form-select"
                                    id="newStatus"
                                    value={newStatus}
                                    onChange={(e) => setNewStatus(e.target.value)}
                                    required
                                >
                                    {PACKET_STATUSES.map(status => (
                                        <option key={status} value={status}>
                                            {status.replace(/_/g, ' ')}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <button type="submit" className="btn btn-primary w-100 mt-3">Update Packet</button>
                        </form>
                        {updateMessage && <div className="alert alert-success mt-3">{updateMessage}</div>}
                        {error && <div className="alert alert-danger mt-3">{error}</div>}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DeliveryPage;
