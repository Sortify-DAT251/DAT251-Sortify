'use client'
import React, { useState } from 'react';
import styles from './header.module.css'; // Import the CSS module styles

export default function DropdownMenu() {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen); // Toggle the menu visibility
    };

    return (
        <div className={styles.menuWrapper}>
            {/* The button that toggles the dropdown */}
            <a
                href="#"
                className={styles.button}
                onClick={toggleMenu} // Toggle the dropdown menu on click
            >
                Menu
            </a>

            {/* Dropdown menu items */}
            <div className={`${styles.dropdownMenu} ${isOpen ? styles.show : ''}`}>
                <a href="/en/" className={styles.menuItem}>Home</a>
                <a href="/en/stats" className={styles.menuItem}>Stats</a>
                <a href="https://docs.astro.build/en/develop-and-build/" className={styles.menuItem}>Documentation</a>
            </div>
        </div>
    );
}
