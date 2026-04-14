// src/index.js

import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css'; // Keep your basic index.css if it has root styles
import './style.css'; // Import the new custom styles
import App from './App';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

reportWebVitals();
