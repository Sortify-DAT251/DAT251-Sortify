import styles from "../component/footer.module.css";
import React from 'react';

export default function Footer() {
    return (
        <div className={styles.footerContainer}>
            <div className={styles.footer}>
                <a href="/en/" className={styles.logo}>
                    <img
                        src="/sortify-footer.png"
                        alt="sortify logo footer version"
                        width="200"
                        className={styles.logoImage}
                    />
                </a>
                <p className={styles.footerText}>
                    &copy; 2025 Sortify. All rights reserved.
                </p>
            </div>
        </div>
    );
}
