import styles from "../styling/footer.module.css";
import React from 'react';

export default function Footer() {
    return (
        <div className={styles.footerContainer}>
            <div className={styles.footer}>
                <a href="/en/">
                    <img
                        src="/sortify-logo-footer.png"
                        alt="sortify logo footer version"
                        width={"250"}
                        className={styles.logoImageFooter}
                    />
                </a>
                <p className={styles.footerText}>
                    &copy; 2025 Sortify. All rights reserved.
                </p>
            </div>
        </div>
    );
}
