// Footer.jsx
import React from "react";
import "./assets/styles.css";

export function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">

        {/* Left Section */}
        <div className="footer-left">
          <h3 className="footer-title">ANGADIHUB</h3>
          <p className="footer-tagline">
            Your one-stop shop for all your needs
          </p>
        </div>

        {/* Links + Info Section */}
        <div className="footer-links">
          <div className="footer-column">
            <h4>About Us</h4>
            <p>
              SalesSavvy is a modern e-commerce platform delivering
              quality products with a seamless shopping experience.
            </p>
          </div>

          <div className="footer-column">
            <h4>Contact</h4>
            <p>Email: support@angadihub.com</p>
            <p>Phone: +91 98765 43210</p>
            <p>Bangalore, India</p>
          </div>

          <div className="footer-column">
            <h4>Legal</h4>
            <a href="#">Terms of Service</a>
            <a href="#">Privacy Policy</a>
          </div>
        </div>

      </div>

      <div className="footer-bottom">
        <p>© 2026 AngadiHub. All rights reserved.</p>
      </div>
    </footer>
  );
}