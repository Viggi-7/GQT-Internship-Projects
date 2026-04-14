// src/components/Footer.js

import React from 'react';

const Footer = () => {
    return (
        <footer className="footer mt-auto py-3 bg-dark text-white-50 glass-effect custom-footer">
            <div className="container text-center">
                <span>&copy; {new Date().getFullYear()} Trace360. All rights reserved.</span>
            </div>
        </footer>
    );
};

export default Footer;
